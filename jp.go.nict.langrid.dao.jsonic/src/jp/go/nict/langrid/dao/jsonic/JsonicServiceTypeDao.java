package jp.go.nict.langrid.dao.jsonic;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jp.go.nict.langrid.commons.io.RegexFileNameFilter;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.ServiceTypeNotFoundException;
import jp.go.nict.langrid.dao.entity.ServiceInterfaceDefinition;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class JsonicServiceTypeDao implements ServiceTypeDao {

	public JsonicServiceTypeDao(JsonicDaoContext context) {
		this.context = context;
	}

	@Override
	public void clear() throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ServiceType> listAllServiceTypes(String domainId)
			throws DaoException {
		return getList(domainId);
	}

	@Override
	public void addServiceType(ServiceType serviceType) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteServiceType(String domainId, String serviceTypeId)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServiceType getServiceType(String domainId, String serviceTypeId)
			throws DaoException {
		List<ServiceType>  l = listAllServiceTypes(domainId);
		if (l == null) {
			throw new ServiceTypeNotFoundException(domainId, serviceTypeId);
		}

		for (ServiceType s : l) {
			if (s.getServiceTypeId().equals(serviceTypeId)) {
				return s;
			}
		}
		throw new ServiceTypeNotFoundException(domainId, serviceTypeId);
	}

	@Override
	public boolean isServiceTypeExist(String domainId, String serviceTypeId)
			throws DaoException {
		for (ServiceType s : listAllServiceTypes(domainId)) {
			if (s.getServiceTypeId().equals(serviceTypeId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<ServiceMetaAttribute> listAllServiceMetaAttributes()
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ServiceMetaAttribute> listAllServiceMetaAttributes(
			String domainId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addServiceMetaAttribute(ServiceMetaAttribute metaAttribute)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteServiceMetaAttribute(String domainId, String attributeName)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServiceMetaAttribute getServiceMetaAttribute(String domainId,
			String attributeName) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isServiceMetaAttributeExist(String domainId,
			String serviceMetaAttributeId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	private JsonicDaoContext context;

	private File getServiceTypeDir(String domainId) {
		return new File(context.getDomainBaseDir(domainId), "serviceTypes");
	}

	private Map<String, List<ServiceType>> serviceTypes = new HashMap<String, List<ServiceType>>();
	private List<ServiceType> getList(String domainId) throws DaoException {
		if (! this.serviceTypes.containsKey(domainId)) {
			List<ServiceType> types = new ArrayList<ServiceType>();
			this.serviceTypes.put(domainId, types);

			File[] files = getServiceTypeDir(domainId).listFiles(new RegexFileNameFilter("^.*\\.json$"));
			JSON parser = new ServiceTypeJSON(domainId);
			try {
				for (File f : files) {
					types.add(parser.parse(new FileInputStream(f), ServiceType.class));
				}
			} catch (IOException e) {
				throw new DaoException(e);
			}
		}
		return this.serviceTypes.get(domainId);
	}

	@Override
	public void deleteServiceType(String domainId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteServiceMetaAttribute(String domainId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	class ServiceTypeJSON extends JSON {
		private String domainId;
		public ServiceTypeJSON(String domainId) {
			this.domainId = domainId;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected <T> T postparse(Context context, Object value,
				Class<? extends T> cls, Type type) throws Exception {
			if (ServiceInterfaceDefinition.class.isAssignableFrom(cls) && value instanceof Map) {
				String protocolId = (String)((Map)value).get("protocolId");
				final String definition = (String)((Map)value).get("definition");
				final String domainId = this.domainId;

				ServiceInterfaceDefinition def = new ServiceInterfaceDefinition(null, protocolId) {
					private static final long serialVersionUID = 1L;
					public java.sql.Blob getDefinition() {
						File file = new File(getServiceTypeDir(domainId), definition);

						// generateWSDL method requires zip compressed wsdl.
						ByteArrayOutputStream buf = new ByteArrayOutputStream();
						ZipOutputStream zip = new ZipOutputStream(buf);
						try {
							InputStream in = new BufferedInputStream(new FileInputStream(file));
							try{
								ZipEntry ent = new ZipEntry(file.getName());
								ent.setMethod(ZipEntry.STORED);
								ent.setSize(file.length());
								CRC32 crc = new CRC32();
								in.mark((int)file.length());
								byte[] b = new byte[16384]; // 16k buff
								for (; in.available() > 0;) {
									int l = in.read(b);
									crc.update(b, 0, l);
								}
								in.reset();
								ent.setCrc(crc.getValue());
								zip.putNextEntry(ent);
								for (; in.available() > 0;) {
									int l = in.read(b);
									zip.write(b, 0, l);
								}
								zip.close();
							} finally{
								in.close();
							}
						} catch (IOException e1) {
							throw new RuntimeException(e1);
						} finally{
							try {
								zip.close();
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
						}

						final byte[] zipped = buf.toByteArray();
						return new java.sql.Blob() {
							@Override
							public long length() throws SQLException {
								return zipped.length;
							}
							@Override
							public byte[] getBytes(long pos, int length)
									throws SQLException {
								return Arrays.copyOfRange(zipped, (int)pos, length);
							}
							@Override
							public InputStream getBinaryStream()
									throws SQLException {
								return new ByteArrayInputStream(zipped);
							}
							@Override
							public InputStream getBinaryStream(long pos, long length) throws SQLException { 
								return new ByteArrayInputStream(getBytes(pos, (int)length));
							}
							@Override
							public long position(byte[] pattern, long start)
									throws SQLException { throw new UnsupportedOperationException(); }
							@Override
							public long position(Blob pattern, long start)
									throws SQLException { throw new UnsupportedOperationException(); }
							@Override
							public int setBytes(long pos, byte[] bytes)
									throws SQLException { throw new UnsupportedOperationException(); }
							@Override
							public int setBytes(long pos, byte[] bytes,
									int offset, int len) throws SQLException { throw new UnsupportedOperationException(); }
							@Override
							public OutputStream setBinaryStream(long pos)
									throws SQLException { throw new UnsupportedOperationException(); }
							@Override
							public void truncate(long len) throws SQLException { throw new UnsupportedOperationException(); }
							@Override
							public void free() throws SQLException { throw new UnsupportedOperationException(); }
						};
					};
				};
				return (T) def;
			}
			return super.postparse(context, value, cls, type);
		}
	}
}
