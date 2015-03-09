package jp.go.nict.langrid.management.web.model.service.impl;

import jp.go.nict.langrid.dao.AccessLimitNotFoundException;
import jp.go.nict.langrid.dao.AccessLimitSearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.management.logic.AccessLimitLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.AccessLimitControlModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AccessLimitControlService;
import jp.go.nict.langrid.management.web.model.service.LangridList;

public class AccessLimitControlServiceImpl implements AccessLimitControlService {
	public AccessLimitControlServiceImpl() throws ServiceManagerException {
	}

	public void add(AccessLimitControlModel obj) throws ServiceManagerException {
		try {
			new AccessLimitLogic().setAccessLimit(
				obj.getUserGridId(), obj.getUserId(), obj.getServiceGridId(),
				obj.getServiceId()
				, obj.getPeriod(), obj.getLimitType(), obj.getLimitCount());
		} catch(UserNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(ServiceNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void delete(AccessLimitControlModel condition) throws ServiceManagerException {
		try {
			new AccessLimitLogic().deleteAccessLimit(condition.getUserGridId(),
				condition.getUserId(), serviceGridId
				, condition.getServiceId(), condition.getPeriod(),
				condition.getLimitType());
		} catch(AccessLimitNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void edit(AccessLimitControlModel obj) throws ServiceManagerException {
		add(obj);
	}

	public AccessLimitControlModel get(String id) throws ServiceManagerException {
		return null;
	}

	public LangridList<AccessLimitControlModel> getList(
		int index, int count, MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		return new LangridList<AccessLimitControlModel>();
	}

	public LangridList<AccessLimitControlModel> getList(
		String userGridId, String userId, String serviceGridId, String serviceId)
	throws ServiceManagerException
	{
		try {
			AccessLimit[] results = new AccessLimitLogic().getMyAccessLimits(
				userGridId, userId, serviceGridId, serviceId);
			LangridList<AccessLimitControlModel> list = new LangridList<AccessLimitControlModel>();
			if(results.length == 0) {
				return list;
			}
			for(AccessLimit al : results) {
				list.add(makeModel(al));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public LangridList<AccessLimitControlModel> getList(
		int index, int count, String userGridId, String serviceId, Order[] orders)
	throws ServiceManagerException
	{
		try {
			AccessLimitSearchResult result = new AccessLimitLogic().searchAccessLimits(
				index, count, userGridId, serviceGridId, serviceId, orders);
			LangridList<AccessLimitControlModel> list = new LangridList<AccessLimitControlModel>();
			if(result.getTotalCount() == 0) {
				return list;
			}
			for(AccessLimit al : result.getElements()) {
				list.add(makeModel(al));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public int getTotalCount(String serviceId, String userGridId)
	throws ServiceManagerException {
		try {
			return new AccessLimitLogic().searchAccessLimits(
				0, 1, userGridId, serviceGridId, serviceId, new Order[]{})
				.getTotalCount();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException
	{
		return 0;
	}
	
	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return false;
	}

	@Override
	public void setScopeParameter(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}

	private String serviceGridId;
	private String userGridId;

	private AccessLimitControlModel makeModel(AccessLimit entity) {
		AccessLimitControlModel model = new AccessLimitControlModel();
		model.setCreatedDateTime(entity.getCreatedDateTime());
		model.setLimitCount(entity.getLimitCount());
		model.setLimitType(entity.getLimitType());
		model.setPeriod(entity.getPeriod());
		model.setServiceGridId(entity.getServiceGridId());
		model.setServiceId(entity.getServiceId());
		model.setUpdatedDateTime(entity.getUpdatedDateTime());
		model.setUserGridId(entity.getUserGridId());
		model.setUserId(entity.getUserId());
		return model;
	}

	private String userId;
}
