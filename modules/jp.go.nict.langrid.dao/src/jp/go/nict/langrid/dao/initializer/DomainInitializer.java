package jp.go.nict.langrid.dao.initializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jp.go.nict.langrid.commons.beanutils.ConverterForJsonRpc;
import jp.go.nict.langrid.commons.io.RegexFileNameFilter;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.ProtocolDao;
import jp.go.nict.langrid.dao.ResourceTypeDao;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ServiceInterfaceDefinition;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.util.LangridJSON;
import jp.go.nict.langrid.dao.util.LobUtil;

public class DomainInitializer {
	public static void init(DaoFactory f, DaoContext dc, String gridId, boolean dropAndCreate, String baseDir)
	throws DaoException, IOException, SQLException{
		dc.beginTransaction();
		try{
			ProtocolDao pDao = f.createProtocolDao();
			DomainDao dDao = f.createDomainDao();
			ResourceTypeDao rtDao = f.createResourceTypeDao();
			ServiceTypeDao stDao = f.createServiceTypeDao();
			String path = baseDir;
			if(dropAndCreate){
				logger.info("dropAndCreate specified, refreshing entries..");
				clearDomains(dDao, rtDao, stDao);
				clearProtocols(pDao);
			}
			initProtocols(new File(path + "/protocols"),
					f.createProtocolDao());
			initDomains(new File(path + "/domains"),
					dDao, rtDao, stDao);
			dc.commitTransaction();
		} catch(IOException e){
			dc.rollbackTransaction();
			throw e;
		} catch(DaoException e){
			dc.rollbackTransaction();
			throw e;
		} catch(SQLException e){
			dc.rollbackTransaction();
			throw e;
		}
	}

	private static void clearProtocols(ProtocolDao pdao) throws DaoException{
		int count = 0;
		for(Protocol p : pdao.listAllProtocols()){
			pdao.deleteProtocol(p.getProtocolId());
			count++;
		}
		logger.info(count + " protocols deleted.");
	}

	private static void clearDomains(DomainDao ddao, ResourceTypeDao rtDao, ServiceTypeDao stDao) throws DaoException{
		int count = 0;
		for(Domain d : ddao.listAllDomains()){
			String did = d.getDomainId();
			rtDao.deleteResourceType(did);
			rtDao.deleteResourceMetaAttribute(did);
			stDao.deleteServiceType(did);
			stDao.deleteServiceMetaAttribute(did);
			ddao.deleteDomain(did);
			count++;
		}
		logger.info(count + " domains deleted.");
	}

	private static void initProtocols(File protocolsDir, ProtocolDao pdao)
			throws IOException, FileNotFoundException, DaoException {
		if(!protocolsDir.exists()) return;
		int count = 0;
		for(File file : protocolsDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			InputStream is = new FileInputStream(file);
			try{
				Protocol p = json.parse(is, Protocol.class);
				if(pdao.isProtocolExist(p.getProtocolId())){
					continue;
				}
				pdao.addProtocol(p);
				count++;
			} finally{
				is.close();
			}
		}
		logger.info(count + " protocols initialized.");
	}

	private static void initDomains(
			File domainsDir, DomainDao ddao, ResourceTypeDao rtdao, ServiceTypeDao stdao)
	throws DaoException, FileNotFoundException, IOException, SQLException{
		if(!domainsDir.exists()) return;

		int count = 0;
		for(File file : domainsDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			InputStream is = new FileInputStream(file);
			try{
				Domain d = json.parse(is, Domain.class);
				String did = d.getDomainId();
				if(ddao.isDomainExist(did)){
					continue;
				}
				ddao.addDomain(d);
				initResourceMetaAttributes(domainsDir, rtdao, did);
				initResourceTypes(domainsDir, rtdao, did);
				initServiceMetaAttributes(domainsDir, stdao, did);
				initServiceTypes(domainsDir, stdao, did);
				count++;
			} finally{
				is.close();
			}
		}
		logger.info(count + " domains initialized.");
	}

	private static void initServiceTypes(File domainsDir, ServiceTypeDao std, String domainId)
	throws IOException, FileNotFoundException, DaoException, SQLException{
		File serviceTypeDir = new File(new File(domainsDir, domainId), "serviceTypes");
		if(!serviceTypeDir.exists()) return;
		int count = 0;
		for(File typeFile : serviceTypeDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			std.addServiceType(getServiceType(typeFile, domainId, std));
			count++;
		}
		logger.info(count + " service types added.");
	}

	@SuppressWarnings("unchecked")
	public static ServiceType getServiceType(File stJson, String domainId, ServiceTypeDao stdao)
	throws DaoException, IOException, SQLException{
		Map<String, Object> stMap = null;
		{
			InputStream is = new FileInputStream(stJson);
			try{
				stMap = json.parse(is);
			} finally{
				is.close();
			}
		}
		ServiceType st = new ConverterForJsonRpc().convert(stMap, ServiceType.class);
		st.setDomainId(domainId);
		for(Map<String, Object> def : (List<Map<String, Object>>)stMap.get("interfaceDefinitions")){
			String pid = def.get("protocolId").toString();
			ServiceInterfaceDefinition d = new ServiceInterfaceDefinition(st, pid);
			byte[] content;
			File f = new File(stJson.getParentFile(), def.get("definition").toString());
			InputStream is = new FileInputStream(f);
			try{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos);
				zos.putNextEntry(new ZipEntry(f.getName()));
				StreamUtil.transfer(is, zos);
				zos.finish();
				content = baos.toByteArray();
			} finally{
				is.close();
			}
			d.setDefinition(LobUtil.createBlob(content));
			st.getInterfaceDefinitions().put(pid, d);
		}
		for(Map<String, Object> meta : (List<Map<String, Object>>)stMap.get("metaAttributes")){
			String aid = meta.get("attributeId").toString();
			st.getMetaAttributes().put(aid, stdao.getServiceMetaAttribute(domainId, aid));
		}
		return st;
	}

	private static void initServiceMetaAttributes(
		File domainsDir, ServiceTypeDao std, String domainId)
	throws IOException, FileNotFoundException, DaoException{
		File serviceMetaDir = new File(new File(domainsDir, domainId), "serviceMetaAttributes");
		if(!serviceMetaDir.exists()) return;

		int count = 0;
		for(File metaFile : serviceMetaDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			InputStream is = new FileInputStream(metaFile);
			try{
				ServiceMetaAttribute sma = json.parse(is, ServiceMetaAttribute.class);
				sma.setDomainId(domainId);
				std.addServiceMetaAttribute(sma);
				count++;
			} finally{
				is.close();
			}
		}
		logger.info(count + " service meta attributes added.");
	}

	private static void initResourceTypes(File domainsDir, ResourceTypeDao rtd, String domainId)
	throws IOException, FileNotFoundException, DaoException, SQLException{
		File resourceTypeDir = new File(new File(domainsDir, domainId), "resourceTypes");
		if(!resourceTypeDir.exists()) return;
		int count = 0;
		for(File typeFile : resourceTypeDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			rtd.addResourceType(getResourceType(typeFile, domainId, rtd));
			count++;
		}
		logger.info(count + " resource types added.");
	}

	@SuppressWarnings("unchecked")
	public static ResourceType getResourceType(File rtJson, String domainId, ResourceTypeDao rtdao)
	throws DaoException, IOException, SQLException{
		Map<String, Object> rtMap = null;
		InputStream is = new FileInputStream(rtJson);
		try{
			rtMap = json.parse(is);
		} finally{
			is.close();
		}
		ResourceType rt = new ConverterForJsonRpc().convert(rtMap, ResourceType.class);
		rt.setDomainId(domainId);
		for(Map<String, Object> meta : (List<Map<String, Object>>)rtMap.get("metaAttributes")){
			String aid = meta.get("attributeId").toString();
			rt.getMetaAttributes().put(aid, rtdao.getResourceMetaAttribute(domainId, aid));
		}
		return rt;
	}

	private static void initResourceMetaAttributes(File domainsDir, ResourceTypeDao rtd, String domainId)
	throws IOException, FileNotFoundException, DaoException
	{
		File resourceMetaDir = new File(new File(domainsDir, domainId), "resourceMetaAttributes");
		if(!resourceMetaDir.exists()) return;

		int count = 0;
		for(File metaFile : resourceMetaDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			InputStream is = new FileInputStream(metaFile);
			try{
				ResourceMetaAttribute rma = json.parse(is, ResourceMetaAttribute.class);
				rma.setDomainId(domainId);
				rtd.addResourceMetaAttribute(rma);
				count++;
			} finally{
				is.close();
			}
		}
		logger.info(count + " resource meta attributes added.");
	}

	private static LangridJSON json = new LangridJSON();
	private static Logger logger = Logger.getLogger(DomainInitializer.class.getName());
}
