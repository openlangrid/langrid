package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DomainNotFoundException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.management.logic.DomainLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DomainService;
import jp.go.nict.langrid.management.web.model.service.LangridList;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class DomainServiceImpl implements DomainService {
	public DomainServiceImpl() throws ServiceManagerException {
	}

	@Override
	public void add(DomainModel obj) throws ServiceManagerException {
		try {
			Domain d = setProperty(new Domain(), obj);
			new DomainLogic().addDomain(d);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void delete(String id) throws ServiceManagerException {
		try {
			new DomainLogic().deleteDomain(id);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void edit(final DomainModel obj) throws ServiceManagerException {
		try {
			new DomainLogic().transactUpdate(obj.getDomainId(), new BlockP<Domain>() {
				@Override
				public void execute(Domain entity) {
					setProperty(entity, obj);
				}
			});
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public LangridList<DomainModel> getAllList() throws ServiceManagerException {
		LangridList<DomainModel> list = new LangridList<DomainModel>();
		try {
			List<Domain> result = new DomainLogic().listAllDomain();
			for(Domain d : result) {
				list.add(makeModel(d));
			}
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
		return list;
	}

	@Override
	public LangridList<DomainModel> getListOnGrid(String gridId)
	throws ServiceManagerException
	{
		LangridList<DomainModel> list = new LangridList<DomainModel>();
		try {
			List<Domain> result = new DomainLogic().listDomain(gridId);
			for(Domain d : result) {
				list.add(makeModel(d));
			}
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
		return list;
	}

	@Override
	public LangridList<DomainModel> getList(int index, int count
		, MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		LangridList<DomainModel> list = new LangridList<DomainModel>();
		try {
			List<Domain> result = new DomainLogic().listDomain(serviceGridId);
			for(Domain d : result) {
				list.add(makeModel(d));
			}
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
		return list;
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

	@Override
	public DomainModel get(String id) throws ServiceManagerException {
		try {
			Domain d = new DomainLogic().getDomain(id);
			return makeModel(d);
		} catch(DomainNotFoundException e) {
			return null;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}
	
	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	private String serviceGridId;
	private String userGridId;
	private String userId;

	private DomainModel makeModel(Domain entity) {
		DomainModel dm = new DomainModel();
		dm.setDomainId(entity.getDomainId());
		dm.setDomainName(entity.getDomainName());
		dm.setDescription(entity.getDescription());
		dm.setOwnerUserGridId(entity.getOwnerUserGridId());
		dm.setOwnerUserId(entity.getOwnerUserId());
		dm.setCreatedDateTime(entity.getCreatedDateTime());
		dm.setUpdatedDateTime(entity.getUpdatedDateTime());
		return dm;
	}

	private Domain setProperty(Domain domain, DomainModel model) {
		domain.setDomainId(model.getDomainId());
		domain.setDomainName(model.getDomainName());
		domain.setDescription(model.getDescription());
		domain.setOwnerUserGridId(model.getOwnerUserGridId());
		domain.setOwnerUserId(model.getOwnerUserId());
		domain.setCreatedDateTime(model.getCreatedDateTime());
		domain.setUpdatedDateTime(model.getUpdatedDateTime());
		return domain;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void delete(DomainModel condition) throws ServiceManagerException {
	}
}
