package jp.go.nict.langrid.management.web.model.service.impl;

import jp.go.nict.langrid.dao.AccessRightNotFoundException;
import jp.go.nict.langrid.dao.AccessRightSearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.management.logic.AccessRightLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.AccessRightControlModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AccessRightControlService;
import jp.go.nict.langrid.management.web.model.service.LangridList;

public class AccessRightControlServiceImpl implements AccessRightControlService {
	public AccessRightControlServiceImpl() throws ServiceManagerException {
	}

	@Override
	public AccessRightControlModel get(String id) throws ServiceManagerException {
		return null;
	}

	@Override
	public LangridList<AccessRightControlModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException {
		return new LangridList<AccessRightControlModel>();
	}

	@Override
	public LangridList<AccessRightControlModel> getList(int index, int count,
		String serviceOwnerUserId, String serviceId, String userGridId, Order[] orders)
	throws ServiceManagerException
	{
		try {
			AccessRightSearchResult result = new AccessRightLogic().searchAccessRights(
				serviceOwnerUserId, index, count, serviceGridId, serviceId, userGridId,
				orders);
			LangridList<AccessRightControlModel> list = new LangridList<AccessRightControlModel>();
			if(result.getTotalCount() == 0) {
				return list;
			}
			for(AccessRight ar : result.getElements()) {
				list.add(makeModel(ar));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void add(AccessRightControlModel obj) throws ServiceManagerException {
		try {
			new AccessRightLogic().setAccessRight(obj.getServiceOwnerUserId(),
				obj.getServiceGridId()
				, obj.getServiceId(), obj.getUserGridId(), obj.getUserId(),
				obj.isPermitted());
		} catch(UserNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void edit(AccessRightControlModel obj) throws ServiceManagerException {
		add(obj);
	}

	@Override
	public void delete(AccessRightControlModel condition) throws ServiceManagerException {
		try {
			new AccessRightLogic().deleteAccessRight(
				condition.getServiceGridId(), condition.getServiceId()
				, condition.getUserGridId(), condition.getUserId()
				);
		} catch(AccessRightNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		return 0;
	}

	@Override
	public void setDefault(String serviceId, String serviceOwnerUserId,
		boolean accessRight)
	throws ServiceManagerException {
		try {
			if(accessRight) {
				new AccessRightLogic().moveToForAllUsers(serviceGridId, serviceId);
			} else {
				new AccessRightLogic().moveToMembersOnly(serviceGridId, serviceId);
			}
			//         new AccessRightLogic().setAccessRight(serviceOwnerUserId, serviceGridId, serviceId, "*"
			//            , "*", accessRight);
		} catch(UserNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void setScopeParameter(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}

	@Override
	public int getTotalCount(
		String serviceGridId, String serviceOwnerUserId, String serviceId,
		String userGridId, String userId)
	throws ServiceManagerException
	{
		try {
			return new AccessRightLogic().searchAccessRights(
				serviceOwnerUserId, 0, 1, serviceGridId, serviceId, userGridId,
				new Order[]{}
				).getTotalCount();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}
	
	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return false;
	}

	private AccessRightControlModel makeModel(AccessRight entity) {
		AccessRightControlModel model = new AccessRightControlModel();
		model.setServiceId(entity.getServiceId());
		model.setServiceGridId(entity.getServiceGridId());
		model.setUserId(entity.getUserId());
		model.setUserGridId(entity.getUserGridId());
		model.setPermitted(entity.isPermitted());
		model.setUpdatedDateTime(entity.getUpdatedDateTime());
		model.setCreatedDateTime(entity.getCreatedDateTime());
		return model;
	}

	private String serviceGridId;
	private String userGridId;
	private String userId;
}
