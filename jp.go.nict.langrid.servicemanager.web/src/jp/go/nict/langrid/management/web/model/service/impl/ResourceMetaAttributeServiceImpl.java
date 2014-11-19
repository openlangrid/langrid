package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ResourceMetaAttributeNotFoundException;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.management.logic.ResourceTypeLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ResourceMetaAttributeService;
import jp.go.nict.langrid.management.web.model.service.ResourceModelUtil;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ResourceMetaAttributeServiceImpl implements ResourceMetaAttributeService {
	@Override
	public ResourceMetaAttributeModel get(String resourceTypeId)
	throws ServiceManagerException {
		return null;
	}

	@Override
	public ResourceMetaAttributeModel get(String domainId, String attributeId)
	throws ServiceManagerException {
		try {
			return ResourceModelUtil.makeResourceMetaAttributeModel(
				new ResourceTypeLogic().getResourceMetaAttribute(domainId, attributeId));
		} catch(ResourceMetaAttributeNotFoundException e) {
			return null;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public LangridList<ResourceMetaAttributeModel> getListOnDomain(int first, int count,
		String domainId)
	throws ServiceManagerException {
		LangridList<ResourceMetaAttributeModel> list = new LangridList<ResourceMetaAttributeModel>();
		try {
			List<ResourceMetaAttribute> result = new ResourceTypeLogic()
				.listResourceMetaAttributes(domainId);
			for(ResourceMetaAttribute d : result.subList(first, first + count)) {
				list.add(ResourceModelUtil.makeResourceMetaAttributeModel(d));
			}
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
		return list;
	}

	@Override
	public LangridList<ResourceMetaAttributeModel> getAllListOnDomain(String domainId)
	throws ServiceManagerException {
		LangridList<ResourceMetaAttributeModel> list = new LangridList<ResourceMetaAttributeModel>();
		try {
			List<ResourceMetaAttribute> result = new ResourceTypeLogic()
				.listResourceMetaAttributes(domainId);
			for(ResourceMetaAttribute d : result) {
				list.add(ResourceModelUtil.makeResourceMetaAttributeModel(d));
			}
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
		return list;
	}

	public LangridList<ResourceMetaAttributeModel> getList(int index, int count
		, MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		LangridList<ResourceMetaAttributeModel> list = new LangridList<ResourceMetaAttributeModel>();
		try {
			List<ResourceMetaAttribute> result = new ResourceTypeLogic()
				.listAllResourceMetaAttributes();
			for(ResourceMetaAttribute d : result.subList(index, index + count)) {
				list.add(ResourceModelUtil.makeResourceMetaAttributeModel(d));
			}
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
		return list;
	}

	/**
	 * noop
	 */
	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		return 0;
	}

	public void add(ResourceMetaAttributeModel obj) throws ServiceManagerException {
		try {
			ResourceMetaAttribute d = ResourceModelUtil.setResourceMetaAttribute(obj,
				new ResourceMetaAttribute());
			new ResourceTypeLogic().addResourceMetaAttribute(d);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	public void edit(final ResourceMetaAttributeModel obj) throws ServiceManagerException {
		try {
			new ResourceTypeLogic().transactAttributeUpdate(
				obj.getDomainId(), obj.getAttributeId(),
				new BlockP<ResourceMetaAttribute>()
				{
					@Override
					public void execute(ResourceMetaAttribute entity) {
						ResourceModelUtil.setResourceMetaAttribute(obj, entity);
					}
				});
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void delete(ResourceMetaAttributeModel model) throws ServiceManagerException {
		try {
			new ResourceTypeLogic().deleteResourceMetaAttribute(model.getDomainId(),
				model.getAttributeId());
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void deleteById(String domainId, String attributeId)
	throws ServiceManagerException {
		try {
			new ResourceTypeLogic().deleteResourceMetaAttribute(domainId, attributeId);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	@Override
	public void setScopeParametar(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}

	private String serviceGridId;
	private String userGridId;
	private String userId;
	private static final long serialVersionUID = 1L;
}
