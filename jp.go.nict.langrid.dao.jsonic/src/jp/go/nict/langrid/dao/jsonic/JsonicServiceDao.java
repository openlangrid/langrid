/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.dao.jsonic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.go.nict.langrid.commons.io.FileUtil;
import jp.go.nict.langrid.commons.io.RegexFileNameFilter;
import jp.go.nict.langrid.commons.util.ListUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceAlreadyExistsException;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.ServiceSearchResult;
import jp.go.nict.langrid.dao.ServiceStatRankingSearchResult;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceAttribute;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.entity.WebappService;
import jp.go.nict.langrid.dao.jsonic.searchsupport.ServiceSearchSupport;
import jp.go.nict.langrid.dao.util.LobUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicServiceDao implements ServiceDao {
	public JsonicServiceDao(JsonicDaoContext context){
		this.context = context;
	}

	@Override
	public void clear() throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearDetachedInvocations() throws DaoException {
		throw new UnsupportedOperationException();
	}

	Pattern serviceIdPattern = Pattern.compile("^[ebw]s_(.*)\\.json$");
	@Override
	public List<Service> listAllServices(String serviceGridId)
	throws DaoException {
		List<Service> services = ListUtil.newArrayList();
		final File servicesBaseDir = getBaseDir(serviceGridId);
		File[] files = servicesBaseDir.listFiles(new RegexFileNameFilter(serviceIdPattern.pattern()));
		if(files == null) return services;
		JSON j = createJSON(serviceGridId);
		try{
			for(File f : files){
				Service s = JsonicUtil.decode(f, j, getServiceClass(f.getName()));
				s.setGridId(serviceGridId);
				for(ServiceAttribute a : s.getAttributes()){
					a.setGridId(serviceGridId);
				}
				for(ServiceEndpoint e : s.getServiceEndpoints()){
					e.setGridId(serviceGridId);
				}
				services.add(s);
			}
			return services;
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	private static Class<? extends Service> getServiceClass(String fileName){
		if(fileName.startsWith("es_")){
			return ExternalService.class;
		} else if(fileName.startsWith("bs_")){
			return BPELService.class;
		} else{
			return WebappService.class;
		}
	}

	@Override
	public List<Service> listServicesOfUser(String userGridId, String userId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<BPELService> listParentServicesOf(String gridId, String serviceId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServiceSearchResult searchServices(int startIndex, int maxCount,
			String serviceGridId, boolean acrossGrids, MatchingCondition[] conditions, Order[] orders)
	throws DaoException {
		String[] names = getBaseDir(serviceGridId).list();
		List<Service> bulkList = ListUtil.newArrayList();

		for (String name : names) {
			Matcher m = serviceIdPattern.matcher(name);
			if (! m.matches()) {
				continue;
			}
			bulkList.add(getService(serviceGridId, m.group(1)));
		}

		Pair<Service[], Integer> p = 
				new ServiceSearchSupport(conditions, orders)
					.filter(bulkList.toArray(new Service[bulkList.size()]),
							startIndex, maxCount);

		ServiceSearchResult res = new ServiceSearchResult(p.getFirst(), p.getSecond(), true);

		return res;
	}

	@Override
	public ServiceStatRankingSearchResult searchServiceStatRanking(
			int startIndex, int maxCount, String serviceGridId, String nodeId,
			boolean acrossGrids, MatchingCondition[] conditions, int sinceDays,
			Order[] orders) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addService(Service service)
	throws DaoException, ServiceAlreadyExistsException {
		String prefix = null;
		if(service instanceof ExternalService){
			prefix = "es_";
		} else if(service instanceof WebappService){
			prefix = "ws_";
		} else if(service instanceof BPELService){
			prefix = "bs_";
		} else{
			throw new UnsupportedOperationException();
		}
		String gridId = service.getGridId();
		File baseDir = getBaseDir(gridId);
		File servicefile = new File(baseDir.getAbsolutePath() + "/" +
				prefix + service.getServiceId() + ".json");
		if(servicefile.exists()){
			throw new ServiceAlreadyExistsException(gridId,
					service.getServiceId());
		}
		FileUtil.assertDirectoryExists(servicefile.getParentFile());
		try {
			FileUtil.writeString(servicefile, JSON.encode(service, true), "UTF-8");
		} catch (IOException e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteService(String serviceGridId, String serviceId)
	throws ServiceNotFoundException, DaoException {
		File baseDir = getBaseDir(serviceGridId);
		File es = new File(baseDir, "es_" + serviceId + ".json");
		File bs = new File(baseDir, "bs_" + serviceId + ".json");
		File ws = new File(baseDir, "ws_" + serviceId + ".json");
		if(es.exists() && es.isFile()){
			es.delete();
		} else if(bs.exists() && bs.isFile()){
			bs.delete();
		} else if(ws.exists() && ws.isFile()){
			ws.delete();
		} else{
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public void deleteServicesOfGrid(String gridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteServicesOfUser(String userGridId, String userId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Service getService(String serviceGridId, String serviceId)
	throws ServiceNotFoundException, DaoException {
		File baseDir = getBaseDir(serviceGridId);
		File es = new File(baseDir, "es_" + serviceId + ".json");
		File bs = new File(baseDir, "bs_" + serviceId + ".json");
		File ws = new File(baseDir, "ws_" + serviceId + ".json");
		JSON j = createJSON(serviceGridId);
		try{
			final Service obj;
			if(es.exists() && es.isFile()){
				obj = JsonicUtil.decode(es, j, ExternalService.class);
			} else if(bs.exists() && bs.isFile()){
				obj = JsonicUtil.decode(bs, j, BPELService.class);
			} else if(ws.exists() && ws.isFile()){
				obj = JsonicUtil.decode(ws, j, WebappService.class);
			} else{
				throw new ServiceNotFoundException(serviceGridId, serviceId);
			}
			obj.setGridId(serviceGridId);
			obj.setServiceId(serviceId);
			return obj;
		} catch(FileNotFoundException e){
			throw new ServiceNotFoundException(serviceGridId, serviceId);
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isServiceExist(String serviceGridId, String serviceId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getServiceInstance(String serviceGridId, String serviceId)
	throws DaoException, ServiceNotFoundException {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getServiceWsdl(String serviceGridId, String serviceId)
	throws DaoException, ServiceNotFoundException {
		Service service = getService(serviceGridId, serviceId);
		final File typeFile;

		if ("Other".equals(service.getServiceTypeId())) {
			typeFile = new File(getServiceTypeDir(service.getServiceTypeDomainId()), serviceId + ".wsdl");
		} else {
			typeFile = new File(getServiceTypeDir(service.getServiceTypeDomainId()), service.getServiceTypeId() + ".wsdl");
		}

		try {
			return new FileInputStream(typeFile);
		} catch (FileNotFoundException e) {
			throw new DaoException(e);
		}
	}

	private class ServiceJSON extends JSON{
		public ServiceJSON(String gridId){
			baseDir = getBaseDir(gridId);
		}
		@Override
		protected <T> T postparse(Context context, Object value, Class<? extends T> cls, Type type)
		throws Exception {
			if(context.getKey().equals("instance")){
				return cls.cast(LobUtil.createBlob(
						new File(baseDir, value.toString())
						));
			} else{
				return super.postparse(context, value, cls, type);
			}
		};
		private File baseDir;
	};

	private JSON createJSON(String gridId){
		return new ServiceJSON(gridId);
	}

	private File getBaseDir(String gridId){
		return new File(new File(context.getBaseDir(), gridId), "services");
	}

	private File getServiceTypeDir(String domainId){
		return new File(new File(context.getBaseDir(), domainId), "serviceTypes");
	}

	private JsonicDaoContext context;
}
