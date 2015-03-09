package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ResourceAlreadyExistsException;
import jp.go.nict.langrid.dao.ResourceNotFoundException;
import jp.go.nict.langrid.dao.ResourceSearchResult;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.management.logic.ResourceLogic;
import jp.go.nict.langrid.management.logic.ResourceLogicException;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.enumeration.UserRole;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ResourceModelUtil;
import jp.go.nict.langrid.management.web.model.service.ResourceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

public class ResourceServiceImpl implements ResourceService {
	public ResourceServiceImpl() throws ServiceManagerException {
	}

	@Override
	public ResourceModel get(String id) throws ServiceManagerException {
		List<ResourceModel> list = getList(0, 1, new MatchingCondition[]{
			new MatchingCondition("gridId", userGridId, MatchingMethod.COMPLETE)
			, new MatchingCondition("resourceId", id, MatchingMethod.COMPLETE)
		}, new Order[]{}, Scope.ALL);
		if(list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public LangridList<ResourceModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		try {
			if(serviceGridId != null && !serviceGridId.equals("")) {
				conditions = ArrayUtil.append(conditions, new MatchingCondition(
					"gridId", serviceGridId
					));
			}
			ResourceSearchResult result = new ResourceLogic().searchResources(
				index, count, serviceGridId, userId, conditions, orders, scope);
			if(result == null || result.getTotalCount() == 0) {
				return new LangridList<ResourceModel>();
			}
			LangridList<ResourceModel> list = new LangridList<ResourceModel>();
			for(Resource resource : result.getElements()) {
				list.add(ResourceModelUtil.makeModel(resource));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		try {
			return new ResourceLogic().searchResources(0, 1, serviceGridId, userId,
				conditions, new Order[]{}
				, scope).getTotalCount();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void add(ResourceModel resource) throws ServiceManagerException {
		try {
			if(ServiceFactory.getInstance().getGridService().isAutoApproveEnabled()
				|| ServiceFactory.getInstance().getUserService(resource.getGridId())
					.getUserRoles(resource.getOwnerUserId()).contains(UserRole.ADMINISTRATOR))
			{
				resource.setApproved(true);
			}
			ResourceLogic logic = new ResourceLogic();
			logic.addResource(ResourceModelUtil.setProperty(resource, new Resource()),
				true);
			logic.activateResource(serviceGridId, resource.getResourceId());
		} catch(ResourceAlreadyExistsException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(ResourceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void edit(final ResourceModel obj) throws ServiceManagerException {
		try {
			obj.setGridId(serviceGridId);
			new ResourceLogic().transactUpdate(serviceGridId, obj.getResourceId(),
				new BlockP<Resource>() {
					@Override
					public void execute(Resource arg0) {
						try {
							ResourceModelUtil.setProperty(obj, arg0);
						} catch(ServiceManagerException e) {
							e.printStackTrace();
						}
					}
				});
		} catch(ResourceNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void delete(ResourceModel model) throws ServiceManagerException {
		deleteById(model.getResourceId());
	}

	@Override
	public void deleteById(String resourceId) throws ServiceManagerException {
		try {
			new ResourceLogic().deactivateResource(serviceGridId, resourceId);
			new ResourceLogic().deleteResource(serviceGridId, resourceId);
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(ResourceLogicException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void approveResource(String resourceId) throws ServiceManagerException {
		try {
			new ResourceLogic().transactUpdate(serviceGridId, resourceId,
				new BlockP<Resource>() {
					@Override
					public void execute(Resource entity) {
						entity.setApproved(true);
					}
				});
		} catch(ResourceNotFoundException e) {
			throw new ServiceManagerException(e);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}
	
	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	@Override
	public void setScopeParameter(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}

	private String serviceGridId;
	private String userGridId;
	private String userId;
	private static final long serialVersionUID = 1L;
}
