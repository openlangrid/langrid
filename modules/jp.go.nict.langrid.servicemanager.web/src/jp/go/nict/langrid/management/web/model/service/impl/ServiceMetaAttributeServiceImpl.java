package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.logic.ServiceTypeLogic;
import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceMetaAttributeService;
import jp.go.nict.langrid.management.web.model.service.ServiceModelUtil;

public class ServiceMetaAttributeServiceImpl implements ServiceMetaAttributeService {
	@Override
	public ServiceMetaAttributeModel get(String id) throws ServiceManagerException {
		return null;
	}

	@Override
	public ServiceMetaAttributeModel get(String domainId, String attributeId)
	throws ServiceManagerException {
		try {
			return ServiceModelUtil.makeMetaAttributeModel(
				new ServiceTypeLogic().getServiceMetaAttribute(domainId, attributeId));
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public LangridList<ServiceMetaAttributeModel> getListOnDomain(int first, int count,
		String domainId)
	throws ServiceManagerException {
		try {
			List<ServiceMetaAttribute> result = new ServiceTypeLogic()
				.listServiecMetaAttribute(domainId);
			LangridList<ServiceMetaAttributeModel> list = new LangridList<ServiceMetaAttributeModel>();
			for(ServiceMetaAttribute def : result) {
				list.add(ServiceModelUtil.makeMetaAttributeModel(def));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public LangridList<ServiceMetaAttributeModel> getAllListOnDomain(String domainId)
	throws ServiceManagerException {
		try {
			List<ServiceMetaAttribute> result = new ServiceTypeLogic()
				.listServiecMetaAttribute(domainId);
			LangridList<ServiceMetaAttributeModel> list = new LangridList<ServiceMetaAttributeModel>();
			for(ServiceMetaAttribute def : result) {
				list.add(ServiceModelUtil.makeMetaAttributeModel(def));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public LangridList<ServiceMetaAttributeModel> getList(
		int index, int count, MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		try {
			LangridList<ServiceMetaAttributeModel> list = new LangridList<ServiceMetaAttributeModel>();
			for(ServiceMetaAttribute smam : new ServiceTypeLogic()
				.listAllServiceMetaAttribute().subList(index, index + count)) {
				list.add(ServiceModelUtil.makeMetaAttributeModel(smam));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	//   @Override
	//   public LangridList<ServiceMetaAttributeModel> getAllList() throws ServiceManagerException {
	//   	try {
	//   		List<ServiceMetaAttribute> result = new ServiceTypeLogic().listAllServiceMetaAttribute();
	//   		LangridList<ServiceMetaAttributeModel> list = new LangridList<ServiceMetaAttributeModel>();
	//   		for(ServiceMetaAttribute def : result) {
	//   			list.add(ServiceModelUtil.makeMetaAttributeModel(def));
	//   		}
	//   		return list;
	//   	} catch(DaoException e) {
	//   		throw new ServiceManagerException(e);
	//   	}
	//   }
	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException
	{
		try {
			return new ServiceTypeLogic().listAllServiceMetaAttribute().size();
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void add(ServiceMetaAttributeModel obj) throws ServiceManagerException {
		try {
			new ServiceTypeLogic().addServiceMetaAttribute(ServiceModelUtil
				.setMetaAttribute(obj, new ServiceMetaAttribute()));
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void edit(final ServiceMetaAttributeModel obj) throws ServiceManagerException {
		try {
			new ServiceTypeLogic().transactUpdateMetaAttribute(
				obj.getDomainId(), obj.getAttributeId(),
				new BlockP<ServiceMetaAttribute>()
				{
					@Override
					public void execute(ServiceMetaAttribute arg0) {
						ServiceModelUtil.setMetaAttribute(obj, arg0);
					}
				});
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void delete(ServiceMetaAttributeModel condition)
	throws ServiceManagerException {
		deleteById(condition.getDomainId(), condition.getAttributeId());
	}

	@Override
	public void deleteById(String domainId, String attributeId)
	throws ServiceManagerException {
		try {
			new ServiceTypeLogic().deleteServiceMetaAttribute(domainId, attributeId);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void setScopeParameter(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}
	
	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	private String serviceGridId;
	private String userGridId;
	private String userId;
}
