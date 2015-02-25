package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ResourceTypeNotFoundException;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.management.logic.ResourceTypeLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ResourceModelUtil;
import jp.go.nict.langrid.management.web.model.service.ResourceTypeService;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ResourceTypeServiceImpl implements ResourceTypeService {
	public ResourceTypeServiceImpl() throws ServiceManagerException {
	}

	@Override
	public void add(ResourceTypeModel obj) throws ServiceManagerException {
		try {
			ResourceType d = ResourceModelUtil.setTypeProperty(new ResourceType(), obj);
			new ResourceTypeLogic().addResourceType(d);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void delete(String domainId, String resourceTypeId)
	throws ServiceManagerException {
		try {
			new ResourceTypeLogic().deleteResourceType(domainId, resourceTypeId);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void delete(ResourceTypeModel condition) throws ServiceManagerException {
	}

	@Override
	public void delete(String ResourceTypeId) throws ServiceManagerException {
	}

	@Override
	public void edit(final ResourceTypeModel obj) throws ServiceManagerException {
		try {
			new ResourceTypeLogic().transactUpdate(obj.getDomainId(),
				obj.getResourceTypeId(), new BlockP<ResourceType>() {
					@Override
					public void execute(ResourceType entity) {
						ResourceModelUtil.setTypeProperty(entity, obj);
					}
				});
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public ResourceTypeModel get(String resourceTypeId) throws ServiceManagerException {
		return null;
	}

	@Override
	public ResourceTypeModel get(String domainId, String resourceTypeId)
	throws ServiceManagerException {
		try {
			return ResourceModelUtil.makeTypeModel(new ResourceTypeLogic()
				.getResourceType(domainId, resourceTypeId));
		} catch(ResourceTypeNotFoundException e) {
			return ResourceModelUtil.makeOtherResourceTypeModel();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public ResourceType getEntity(String domainId, String resourceTypeId)
	throws ServiceManagerException {
		try {
			return new ResourceTypeLogic().getResourceType(domainId, resourceTypeId);
		} catch(ResourceTypeNotFoundException e) {
			return null;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public LangridList<ResourceTypeModel> getList(int index, int count
		, MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		LangridList<ResourceTypeModel> list = new LangridList<ResourceTypeModel>();
		try {
			List<ResourceType> result = new ResourceTypeLogic().listAllResourceType();
			for(ResourceType d : result) {
				list.add(ResourceModelUtil.makeTypeModel(d));
			}
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
		return list;
	}

	@Override
	public LangridList<ResourceTypeModel> getAllList() throws ServiceManagerException {
		try {
			LangridList<ResourceTypeModel> list = new LangridList<ResourceTypeModel>();
			for(ResourceType type : new ResourceTypeLogic().listAllResourceType()) {
				list.add(ResourceModelUtil.makeTypeModel(type));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	public LangridList<ResourceTypeModel> getAllList(String domainId)
	throws ServiceManagerException {
		try {
			LangridList<ResourceTypeModel> list = new LangridList<ResourceTypeModel>();
			for(ResourceType type : new ResourceTypeLogic().listResourceType(domainId)) {
				list.add(ResourceModelUtil.makeTypeModel(type));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}
	
	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		return getList(0, 1, new MatchingCondition[]{}, new Order[]{}, Scope.ALL).size();
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
