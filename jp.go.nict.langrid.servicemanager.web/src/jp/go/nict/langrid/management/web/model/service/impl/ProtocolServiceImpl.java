package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.management.logic.ProtocolLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.ProtocolModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ProtocolService;

public class ProtocolServiceImpl implements ProtocolService {
	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		return getList(0, 1, new MatchingCondition[]{}, new Order[]{}, Scope.ALL).size();
	}

	@Override
	public void setScopeParametar(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
	}

	@Override
	public void add(ProtocolModel obj) throws ServiceManagerException {
		try {
			new ProtocolLogic().addProtocol(setProperty(obj, new Protocol()));
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void delete(ProtocolModel condition) throws ServiceManagerException {
		try {
			new ProtocolLogic().deleteProtocol(condition.getProtocolId());
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void deleteById(String id) throws ServiceManagerException {
		try {
			new ProtocolLogic().deleteProtocol(id);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void edit(final ProtocolModel obj) throws ServiceManagerException {
		try {
			new ProtocolLogic().transactUpdate(obj.getProtocolId(),
				new BlockP<Protocol>() {
					@Override
					public void execute(Protocol entity) {
						setProperty(obj, entity);
					}
				});
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public ProtocolModel get(String id) throws ServiceManagerException {
		try {
			List<Protocol> result = new ProtocolLogic().listProtocol(serviceGridId);
			for(Protocol p : result) {
				if(p.getOwnerUserGridId().equals(serviceGridId)
					&& p.getProtocolId().equals(id))
				{
					return makeModel(p);
				}
			}
			return null;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public LangridList<ProtocolModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		return getAllList();
	}

	@Override
	public LangridList<ProtocolModel> getAllList() throws ServiceManagerException {
		try {
			List<Protocol> result = new ProtocolLogic().listProtocol(serviceGridId);
			LangridList<ProtocolModel> list = new LangridList<ProtocolModel>();
			for(Protocol p : result) {
				list.add(makeModel(p));
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
	
	private ProtocolModel makeModel(Protocol entity) {
		ProtocolModel model = new ProtocolModel();
		model.setProtocolId(entity.getProtocolId());
		model.setProtocolName(entity.getProtocolName());
		model.setDescription(entity.getDescription());
		model.setOwnerUserGridId(entity.getOwnerUserGridId());
		model.setOwnerUserId(entity.getOwnerUserId());
		model.setCreatedDateTime(entity.getCreatedDateTime());
		model.setUpdatedDateTime(entity.getUpdatedDateTime());
		return model;
	}

	private Protocol setProperty(ProtocolModel model, Protocol entity) {
		entity.setProtocolId(model.getProtocolId());
		entity.setProtocolName(model.getProtocolName());
		entity.setDescription(model.getDescription());
		entity.setOwnerUserGridId(model.getOwnerUserGridId());
		entity.setOwnerUserId(model.getOwneruserid());
		return entity;
	}

	private String serviceGridId;
}
