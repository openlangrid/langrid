package jp.go.nict.langrid.management.web.model.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.dao.AccessLogSearchResult;
import jp.go.nict.langrid.dao.AccessRankingEntry;
import jp.go.nict.langrid.dao.AccessRankingEntrySearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceAlreadyExistsException;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.ServiceSearchResult;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.management.logic.AccessLogLogic;
import jp.go.nict.langrid.management.logic.AccessStatLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.logic.ServiceLogic;
import jp.go.nict.langrid.management.logic.ServiceLogicException;
import jp.go.nict.langrid.management.logic.ServiceNotActivatableException;
import jp.go.nict.langrid.management.web.model.ExecutionInformationStatisticsModel;
import jp.go.nict.langrid.management.web.model.IndividualExecutionInformationModel;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.ServiceInformationService;
import jp.go.nict.langrid.management.web.model.service.ServiceModelUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;

public class LangridServiceServiceImpl<T extends ServiceModel>
implements LangridServiceService<T>, ServiceInformationService
{
	public LangridServiceServiceImpl() throws ServiceManagerException {
	}

	public void activate(String serviceId) throws ServiceManagerException {
		try {
			getLogic().restartService(serviceGridId, serviceId);
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(ServiceNotActivatableException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void add(T obj) throws ServiceManagerException {
		Service s = null;
		if(obj.getInstanceType().equals(InstanceType.BPEL)) {
			s = new BPELService();
		} else {
			s = new ExternalService();
		}
		s = setProperty(obj, s);
		if(ServiceFactory.getInstance().getGridService().isAutoApproveEnabled()
			|| ServiceFactory.getInstance().getUserService(obj.getGridId())
				.isAdministrator(obj.getOwnerUserId()))
		{
			s.setApproved(true);
			obj.setApproved(true);
		}
		try {
			getLogic().addService(s.getOwnerUserId(), s, MessageUtil.getCoreNodeUrl(),
				true);
			getLogic().activateService(s.getGridId(), s.getServiceId());
		} catch(ServiceAlreadyExistsException e) {
			throw new ServiceManagerException(e);
		} catch(ServiceNotActivatableException e) {
			throw new ServiceManagerException(e);
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void approveService(String serviceId) throws ServiceManagerException {
		try {
			getLogic().transactUpdate(serviceGridId, serviceId, new BlockP<Service>() {
				@Override
				public void execute(Service entity) {
					entity.setApproved(true);
				}
			});
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void deactivate(String serviceId) throws ServiceManagerException {
		try {
			getLogic().deactivateService(serviceGridId, serviceId);
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void delete(T condition) throws ServiceManagerException {
		deleteById(condition.getServiceId());
	}

	@Override
	public void deleteById(String serviceId) throws ServiceManagerException {
		try {
			getLogic().deactivateService(serviceGridId, serviceId);
			getLogic().deleteService(serviceGridId, serviceId);
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void edit(final T obj) throws ServiceManagerException {
		try {
			getLogic().transactUpdate(serviceGridId, obj.getServiceId(),
				new BlockP<Service>() {
					@Override
					public void execute(Service arg0) {
						try {
							setProperty(obj, arg0);
						} catch(ServiceManagerException e) {
							e.printStackTrace();
						}
					}
				});
		} catch(ServiceNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public final T get(String id) throws ServiceManagerException {
		List<T> list = getList(0, 1
			, new MatchingCondition[]{
				new MatchingCondition("serviceId", id, MatchingMethod.COMPLETE)}
			, new Order[]{}, Scope.ALL);
		if(list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public LangridList<ServiceModel> getOtherList(String serviceGridId, Order order)
	throws ServiceManagerException
	{
		LangridList<ServiceModel> list = new LangridList<ServiceModel>();
		try {
			int i = 0;
			ServiceSearchResult result = null;
			while(true) {
				result = getLogic().searchServices(i, 30
					, userGridId, userId, serviceGridId, false
					, new MatchingCondition[]{}
					, new Order[]{order}, Scope.ACCESSIBLE);
				ServiceDao sd = DaoFactory.createInstance().createServiceDao();
				if(result.getElements() != null) {
					for(Service s : result.getElements()) {
						String typeId = s.getServiceTypeId();
						if(typeId == null || typeId.equals("")) {
							T model = makeServiceModel(s);
							InputStream is = null;
							try {
								is = sd.getServiceInstance(serviceGridId,
									s.getServiceId());
								if(is != null) {
									byte[] body = StreamUtil.readAsBytes(is);
									model.setInstance(body);
								}
							} catch(IOException e) {
								throw new ServiceManagerException(e, this.getClass());
							} finally {
								if(is != null) {
									is.close();
								}
							}
							list.add(model);
						}
					}
				}
				if(result.getElements().length < 30) {
					break;
				}
				i += 30;
			}
		} catch(UserNotFoundException e) {
			e.printStackTrace();
			return list;
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, getClass());
		} catch(IOException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
		return list;
	}

	public final LangridList<T> getList(
		int index, int count, MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		LangridList<T> list = null;
		try {
			list = new LangridList<T>();
			ServiceSearchResult result = getLogic().searchServices(
				index, count, userGridId, userId, serviceGridId, false
				, conditions, orders, scope);
			if(result.getTotalCount() == 0) {
				return list;
			}
			ServiceDao sd = DaoFactory.createInstance().createServiceDao();
			for(Service service : result.getElements()) {
				T model = makeServiceModel(service);
				list.add(model);
				InputStream is = null;
				try {
					is = sd.getServiceInstance(serviceGridId, service.getServiceId());
					if(is != null) {
						byte[] body = StreamUtil.readAsBytes(is);
						model.setInstance(body);
					}
				} catch(IOException e) {
					throw new ServiceManagerException(e, this.getClass());
				} finally {
					if(is != null) {
						is.close();
					}
				}
			}
			//			cache.putInListCache(gridId, index, count
			//				, new Trio<MatchingCondition[], Order[], Scope>(conditions, orders, scope)
			//				, (LangridList<ServiceModel>)list);
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(
				e, this.getClass(), "ServiceManager cannot get service list.");
		} catch(IOException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
		//		}
		return list;
	}

	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		try {
			return getLogic().searchServices(0, 1, userGridId, userId, serviceGridId,
				false
				, conditions, new Order[]{}
				, scope).getTotalCount();
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public LangridList<ExecutionInformationStatisticsModel> getStatisticsList(
		int index, int count, String serviceId, Calendar start, Calendar end,
		Period period, Order[] orders)
	throws ServiceManagerException
	{
		try {
			AccessRankingEntrySearchResult result = new AccessStatLogic()
				.sumUpUserAccess(
					index, count, serviceGridId, serviceId, userGridId, start, end,
					period, orders);
			LangridList<ExecutionInformationStatisticsModel> list = new LangridList<ExecutionInformationStatisticsModel>();
			if(result.getTotalCount() == 0) {
				return list;
			}
			for(AccessRankingEntry entry : result.getElements()) {
				list.add(ServiceModelUtil.makeStatModel(entry));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public int getStatisticsTotalCount(String serviceId, Calendar start, Calendar end,
		Period period)
	throws ServiceManagerException
	{
		try {
			return new AccessStatLogic().sumUpUserAccess(
				0, 1, serviceGridId, serviceId, userGridId, start, end, period,
				new Order[]{}).getTotalCount();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public LangridList<IndividualExecutionInformationModel> getVerboseList(
		int index, int count, String userId, String serviceId, Calendar start,
		Calendar end
		, MatchingCondition[] conditions, Order[] orders)
	throws ServiceManagerException
	{
		try {
			AccessLogSearchResult result = new AccessLogLogic().searchAccessLogs(
				index, count, userGridId, userId, serviceGridId, new String[]{serviceId}
				, start, end, conditions, orders
				);
			LangridList<IndividualExecutionInformationModel> list = new LangridList<IndividualExecutionInformationModel>();
			if(result.getTotalCount() == 0) {
				return list;
			}
			ServiceFactory sf = ServiceFactory.getInstance();
			LangridServiceService<ServiceModel> lService = sf
				.getLangridServiceService(userGridId);
			Map<String, Trio<String, String, String>> idToNameCopyrightLicenseMap = new HashMap<String, Trio<String, String, String>>();
			for(AccessLog log : result.getElements()) {
				IndividualExecutionInformationModel model = ServiceModelUtil
					.makeLogModel(log);
				Trio<String, String, String> ncl = idToNameCopyrightLicenseMap.get(model
					.getServiceId());
				if(ncl != null) {
					model.setServiceName(ncl.getFirst());
					model.setCopyright(ncl.getSecond());
					model.setLicense(ncl.getThird());
				} else {
					ServiceModel sModel = lService.get(log.getServiceId());
					model.setServiceName(sModel == null ? "" : sModel.getServiceName());
					model.setCopyright(sModel == null ? "" : sModel.getCopyrightInfo());
					model.setLicense(sModel == null ? "" : sModel.getLicenseInfo());
					idToNameCopyrightLicenseMap.put(model.getServiceId(), Trio.create(
						model.getServiceName(), model.getCopyright(), model.getLicense()
						));
				}
				list.add(model);
			}
			list.setTotalCount(result.getTotalCount());
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public int getVerboseTotalCount(int index, int count
		, String userId, String serviceId, Calendar start, Calendar end,
		MatchingCondition[] conditions)
	throws ServiceManagerException
	{
		try {
			AccessLogSearchResult result = new AccessLogLogic().searchAccessLogs(
				index, count, userGridId, userId, serviceGridId, new String[]{serviceId},
				start, end
				, conditions, new Order[]{}
				);
			return result.getTotalCount();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	};

	@Override
	public LangridList<IndividualExecutionInformationModel> getLimitOverVerboseList(
		int index, int count, String userId, String serviceId, Calendar start,
		Calendar end, MatchingCondition[] conditions, Order[] orders, int limitCount)
	throws ServiceManagerException {
		if(limitCount == 0) {
			return new LangridList<IndividualExecutionInformationModel>();
		}
		try {
			String[] serviceIds = null;
			if(serviceId != null && !serviceId.equals("")) {
				serviceIds = new String[]{serviceId};
			} else {
				serviceIds = new String[]{};
			}
			AccessLogSearchResult result = new AccessLogLogic()
				.searchLimitOverAccessLogs(
					index, count, userGridId, userId, serviceGridId, serviceIds
					, start, end, conditions, orders, limitCount
				);
			LangridList<IndividualExecutionInformationModel> list = new LangridList<IndividualExecutionInformationModel>();
			if(result.getTotalCount() == 0) {
				return list;
			}
			ServiceFactory sf = ServiceFactory.getInstance();
			LangridServiceService<ServiceModel> lService = sf
				.getLangridServiceService(userGridId);
			Map<String, Trio<String, String, String>> idToNameCopyrightLicenseMap = new HashMap<String, Trio<String, String, String>>();
			for(AccessLog log : result.getElements()) {
				IndividualExecutionInformationModel model = ServiceModelUtil
					.makeLogModel(log);
				Trio<String, String, String> ncl = idToNameCopyrightLicenseMap.get(model
					.getServiceId());
				if(ncl != null) {
					model.setServiceName(ncl.getFirst());
					model.setCopyright(ncl.getSecond());
					model.setLicense(ncl.getThird());
				} else {
					ServiceModel sModel = lService.get(log.getServiceId());
					model.setServiceName(sModel == null ? "" : sModel.getServiceName());
					model.setCopyright(sModel == null ? "" : sModel.getCopyrightInfo());
					model.setLicense(sModel == null ? "" : sModel.getLicenseInfo());
					idToNameCopyrightLicenseMap.put(model.getServiceId(), Trio.create(
						model.getServiceName(), model.getCopyright(), model.getLicense()
						));
				}
				list.add(model);
			}
			list.setTotalCount(result.getTotalCount());
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public int getLimitOverVerboseTotalCount(int index, int count, String userId,
		String serviceId, Calendar start, Calendar end, MatchingCondition[] conditions,
		int limitCount) throws ServiceManagerException
	{
		if(limitCount == 0) {
			return 0;
		}
		try {
			String[] serviceIds = null;
			if(serviceId != null && !serviceId.equals("")) {
				serviceIds = new String[]{serviceId};
			} else {
				serviceIds = new String[]{};
			}
			AccessLogSearchResult result = new AccessLogLogic()
				.searchLimitOverAccessLogs(
					index, count, userGridId, userId, serviceGridId, serviceIds, start,
					end
					, conditions, new Order[]{}, limitCount
				);
			return result.getTotalCount();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void addEndpoint(final ServiceEndpointModel endpoint)
	throws ServiceManagerException {
		try {
			getLogic().transactUpdate(serviceGridId, endpoint.getServiceId(),
				new BlockP<Service>() {
					@Override
					public void execute(Service arg0) {
						try {
							arg0.getServiceEndpoints().add(
								ServiceModelUtil.setEndpointProperty(endpoint,
									new ServiceEndpoint()));
						} catch(ServiceManagerException e) {
							e.printStackTrace();
						}
					}
				});
		} catch(ServiceNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void deleteEndpoint(final ServiceEndpointModel obj)
	throws ServiceManagerException {
		try {
			getLogic().transactUpdate(serviceGridId, obj.getServiceId(),
				new BlockP<Service>() {
					@Override
					public void execute(Service arg0) {
						int index = 0;
						for(ServiceEndpoint se : arg0.getServiceEndpoints()) {
							if(se.getUrl().toString().equals(obj.getUrl())) {
								break;
							}
							index++;
						}
						arg0.getServiceEndpoints().remove(index);
					}
				});
		} catch(ServiceNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void editEndpoint(
		String serviceId, final String beforeUrl, final String beforeUserName,
		final ServiceEndpointModel model)
	throws ServiceManagerException
	{
		try {
			getLogic().transactUpdate(serviceGridId, serviceId, new BlockP<Service>() {
				@Override
				public void execute(Service arg0) {
					for(ServiceEndpoint se : arg0.getServiceEndpoints()) {
						if(se.getUrl().toString().equals(beforeUrl)
							&& se.getAuthUserName().equals(beforeUserName)) {
							se.setAuthPassword(model.getAuthPassword());
							se.setAuthUserName(model.getAuthUserName());
							se.setEnabled(model.isEnabled());
						}
					}
				}
			});
		} catch(ServiceNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public List<ServiceEndpointModel> getEndpointList(String serviceId)
	throws ServiceManagerException {
		try {
			ServiceSearchResult result = getLogic().searchServices(
				0,
				1,
				userGridId,
				userId
				,
				serviceGridId,
				false
				,
				new MatchingCondition[]{new MatchingCondition("serviceId", serviceId,
					MatchingMethod.COMPLETE)}
				, new Order[]{}, Scope.ALL);
			if(result.getTotalCount() == 0) {
				return new LangridList<ServiceEndpointModel>();
			}
			LangridList<ServiceEndpointModel> list = new LangridList<ServiceEndpointModel>();
			for(ServiceEndpoint se : result.getElements()[0].getServiceEndpoints()) {
				list.add(ServiceModelUtil.makeEndpointModel(se));
			}
			return list;
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	//	@Override
	//	public byte[] getInstanceBody(String serviceId) throws ServiceManagerException {
	//		InputStream is = null;
	//		try {
	//			is = DaoFactory.createInstance().createServiceDao().getServiceInstance(
	//				serviceGridId, serviceId);
	//			if(is == null){
	//				return null;
	//			}
	//			byte[] body = StreamUtil.readAsBytes(is);
	//			return body;
	//		} catch(ServiceNotFoundException e) {
	//			throw new ServiceManagerException(e, getClass());
	//		} catch(DaoException e) {
	//			throw new ServiceManagerException(e, getClass());
	//		} catch(IOException e) {
	//			throw new ServiceManagerException(e, getClass());
	//		}finally{
	//			if(is != null){
	//				try {
	//					is.close();
	//				} catch(IOException e) {
	//					throw new ServiceManagerException(e, getClass());
	//				}
	//			}
	//		}
	//	}
	public boolean isActive(String serviceId) throws ServiceManagerException {
		try {
			return getLogic().isServiceActive(serviceGridId, serviceId);
		} catch(ServiceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public boolean isExternalService(String serviceId) throws ServiceManagerException {
		return get(serviceId).getInstanceType().equals(InstanceType.EXTERNAL);
	}

	@Override
	public void setScopeParametar(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}

	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	protected T createNewObject() {
		return null;
	}

	protected ServiceLogic getLogic() throws ServiceManagerException {
		try {
			return new ServiceLogic(MessageUtil.getActiveBpelUrl());
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	protected T makeServiceModel(Service service) throws ServiceManagerException {
		return (T)ServiceModelUtil.makeLangridServiceModel(service);
	}

	protected Service setProperty(T model, Service service)
	throws ServiceManagerException {
		return ServiceModelUtil.setLangridServiceProperty(model, service);
	}

	protected String serviceGridId;
	protected String userGridId;
	protected String userId;
	private static final long serialVersionUID = 1L;
}
