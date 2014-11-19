package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.OperationRequestSearchResult;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.OperationRequest;
import jp.go.nict.langrid.dao.entity.OperationType;
import jp.go.nict.langrid.management.logic.OperationRequestLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.OperationRequestModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.OperationRequestService;

public class OperationRequestServiceImpl implements OperationRequestService {
	public OperationRequestServiceImpl() throws ServiceManagerException {
	}

	public void add(OperationRequestModel obj) throws ServiceManagerException {
		try {
			// TODO
			obj.setNodeId("nict1");
			new OperationRequestLogic().addOperationRequest(setProperty(obj,
				new OperationRequest()));
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	public void delete(OperationRequestModel condition) throws ServiceManagerException {
		try {
			new OperationRequestLogic().deleteNews(condition.getGridId(),
				condition.getId());
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void deleteByTargetId(String targetId, OperationType type)
	throws ServiceManagerException
	{
		List<OperationRequestModel> list = getList(0, 1, new MatchingCondition[]{
			new MatchingCondition("targetId", targetId, MatchingMethod.COMPLETE)
			, new MatchingCondition("gridId", serviceGridId, MatchingMethod.COMPLETE)
			, new MatchingCondition("targetType", type)
		}, new Order[]{}, Scope.ALL);
		if(list.size() == 0) {
			return;
		}
		delete(list.get(0));
	}

	public void deleteList(List<OperationRequestModel> list)
	throws ServiceManagerException {
		OperationRequestLogic logic;
		try {
			logic = new OperationRequestLogic();
			for(OperationRequestModel model : list) {
				logic.deleteNews(serviceGridId, model.getId());
			}
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	public void edit(OperationRequestModel obj) throws ServiceManagerException {
	}

	public OperationRequestModel get(String id) throws ServiceManagerException {
		OperationRequestSearchResult result;
		try {
			result = new OperationRequestLogic().searchOperationRequests(0, 1,
				new MatchingCondition[]{
				new MatchingCondition("id", Integer.valueOf(id))
				}, new Order[]{});
			if(result.getTotalCount() == 0) {
				return null;
			}
			return makeModel(result.getElements()[0]);
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public OperationRequestModel getByTargetId(String targetId, OperationType type)
	throws ServiceManagerException
	{
		List<OperationRequestModel> list = getList(0, 1, new MatchingCondition[]{
			new MatchingCondition("targetId", targetId, MatchingMethod.COMPLETE)
			, new MatchingCondition("gridId", serviceGridId, MatchingMethod.COMPLETE)
			, new MatchingCondition("targetType", type)
		}, new Order[]{}, Scope.ALL);
		if(list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public LangridList<OperationRequestModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException {
		try {
			LangridList<OperationRequestModel> list = new LangridList<OperationRequestModel>();
			OperationRequestSearchResult result = new OperationRequestLogic()
				.searchOperationRequests(index, count, conditions, orders);
			if(result.getTotalCount() == 0) {
				return list;
			}
			for(OperationRequest or : result.getElements()) {
				list.add(makeModel(or));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException
	{
		try {
			return new OperationRequestLogic().searchOperationRequests(0, 1, conditions,
				new Order[]{}).getTotalCount();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
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

	private String serviceGridId;
	private String userGridId;

	private OperationRequestModel makeModel(OperationRequest entity) {
		OperationRequestModel model = new OperationRequestModel();
		model.setContents(entity.getContents());
		model.setCreatedDateTime(entity.getCreatedDateTime());
		model.setGridId(entity.getGridId());
		model.setUpdatedDateTime(entity.getUpdatedDateTime());
		model.setRequestedUserId(entity.getRequestedUserId());
		model.setType(entity.getTargetType());
		model.setTargetId(entity.getTargetId());
		model.setId(entity.getId());
		model.setNodeId(entity.getNodeId());
		return model;
	}

	private OperationRequest setProperty(
		OperationRequestModel model, OperationRequest entity)
	{
		entity.setContents(model.getContents());
		entity.setGridId(model.getGridId());
		entity.setTargetId(model.getTargetId());
		entity.setTargetType(model.getType());
		entity.setRequestedUserId(model.getRequestedUserId());
		entity.setNodeId(model.getNodeId());
		return entity;
	}

	private String userId;
	private static final long serialVersionUID = 1L;
}
