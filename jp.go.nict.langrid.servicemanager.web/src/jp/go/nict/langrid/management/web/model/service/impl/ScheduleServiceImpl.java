package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.ServiceActionScheduleSearchResult;
import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.dao.entity.ServiceActionSchedule;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.logic.ServiceActionScheduleLogic;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ScheduleService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

public class ScheduleServiceImpl implements ScheduleService {
	public ScheduleModel get(String id) throws ServiceManagerException {
		// noop
		return null;
	}

	public ScheduleModel getByConditions(
		String targetGridId, String targetId, ScheduleActionType actionType
		, ScheduleTargetType targetType, boolean isRelated)
	throws ServiceManagerException
	{
		return getListByConditions(0, 1, targetGridId, targetId, actionType, targetType)
			.get(0);
	}

	public LangridList<ScheduleModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		return new LangridList<ScheduleModel>() {
		};
	}

	public List<ScheduleModel> getListByConditions(int index, int count,
		String targetGridId
		, String targetId, ScheduleActionType actionType, ScheduleTargetType targetType)
	throws ServiceManagerException {
		List<MatchingCondition> conditions = new ArrayList<MatchingCondition>();
		conditions.add(new MatchingCondition("gridId", targetGridId,
			MatchingMethod.COMPLETE));
		conditions.add(new MatchingCondition("targetId", targetId,
			MatchingMethod.COMPLETE));
		conditions.add(new MatchingCondition("targetType", targetType));
		conditions.add(new MatchingCondition("actionType", actionType));
		try {
			ServiceActionScheduleSearchResult result = new ServiceActionScheduleLogic()
				.searchSchedules(index, count
					, conditions.toArray(new MatchingCondition[]{}), new Order[]{});
			if(result.getTotalCount() == 0) {
				return null;
			}
			LangridList<ScheduleModel> list = new LangridList<ScheduleModel>();
			for(ServiceActionSchedule schedule : result.getElements()) {
				list.add(makeModel(schedule));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public LangridList<ScheduleModel> getListPassedBookingTime(
		ScheduleTargetType type, Calendar fiducialTime)
	throws ServiceManagerException
	{
		try {
			List<ServiceActionSchedule> result = new ServiceActionScheduleLogic()
				.listAll(serviceGridId);
			LangridList<ScheduleModel> list = new LangridList<ScheduleModel>();
			for(ServiceActionSchedule schedule : result) {
				if(type.equals(schedule.getTargetType())
					&& schedule.getBookingDateTime().getTimeInMillis() <= fiducialTime
						.getTimeInMillis())
				{
					list.add(makeModel(schedule));
				}
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public ScheduleModel getNearestStatus(
		String targetId, ScheduleTargetType targetType)
	throws ServiceManagerException
	{
		MatchingCondition[] conditions = new MatchingCondition[]{
			new MatchingCondition("gridId", serviceGridId, MatchingMethod.COMPLETE)
			, new MatchingCondition("targetId", targetId, MatchingMethod.COMPLETE)
			, new MatchingCondition("targetType", targetType)
		};
		Order[] orders = new Order[]{
			new Order("bookingDateTime", OrderDirection.ASCENDANT)
		};
		try {
			ServiceActionScheduleSearchResult result = new ServiceActionScheduleLogic()
				.searchSchedules(0, 1, conditions, orders);
			if(result.getTotalCount() == 0) {
				return null;
			}
			return makeModel(result.getElements()[0]);
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException
	{
		try {
			return new ServiceActionScheduleLogic().searchSchedules(0, 1, conditions,
				new Order[]{}).getTotalCount();
		} catch(Exception e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void add(ScheduleModel obj) throws ServiceManagerException {
		ServiceActionSchedule schedule = new ServiceActionSchedule();
		try {
			new ServiceActionScheduleLogic().addSchedule(setProperty(obj, schedule));
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public void edit(ScheduleModel obj) throws ServiceManagerException {
		// noop
	}

	public void delete(ScheduleModel condition) throws ServiceManagerException {
		try {
			new ServiceActionScheduleLogic().deleteSchedule(serviceGridId,
				condition.getId());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public boolean canSchedule(
		String id, ScheduleTargetType targetType, ScheduleActionType actionType)
	throws ServiceManagerException
	{
		try {
			if(targetType.equals(ScheduleTargetType.RESOURCE)) {
				if(0 < ServiceFactory.getInstance()
					.getAtomicServiceService(serviceGridId).getListByRelatedId(id).size()) {
					return false;
				}
			}
			List<ServiceActionSchedule> list = new ArrayList<ServiceActionSchedule>();
			int start = 0;
			while(true) {
				ServiceActionScheduleSearchResult result = new ServiceActionScheduleLogic()
					.searchSchedules(
						start,
						30
						,
						new MatchingCondition[]{
							new MatchingCondition("targetId", id, MatchingMethod.COMPLETE)
							, new MatchingCondition("targetType", targetType)
						}, new Order[]{
						new Order("bookingDateTime", OrderDirection.DESCENDANT)
						});
				if(result.getTotalCount() != 0) {
					list.addAll(Arrays.asList(result.getElements()));
				}
				if(result.getTotalCount() < 30) {
					break;
				}
				start += 30;
			}
			if(list.size() == 0) {
				if(targetType.equals(ScheduleTargetType.SERVICE)) {
					if(actionType.equals(ScheduleActionType.SUSPENSION)) {
						return ServiceFactory.getInstance()
							.getLangridServiceService(serviceGridId).isActive(id);
					}
					return !ServiceFactory.getInstance()
						.getLangridServiceService(serviceGridId).isActive(id);
				}
				return true;
			}
			return false;
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

	private ScheduleModel makeModel(ServiceActionSchedule entity) {
		ScheduleModel model = new ScheduleModel();
		model.setGridId(entity.getGridId());
		model.setTargetId(entity.getTargetId());
		model.setBookingDateTime(entity.getBookingDateTime());
		model.setActionType(entity.getOperationType());
		model.setTargetType(entity.getTargetType());
		model.setRelated(entity.isRelated());
		model.setId(entity.getId());
		model.setNodeId(entity.getNodeId());
		return model;
	}

	private ServiceActionSchedule setProperty(
		ScheduleModel model, ServiceActionSchedule entity)
	{
		entity.setBookingDateTime(model.getBookingDateTime());
		entity.setGridId(model.getGridId());
		entity.setTargetId(model.getTargetId());
		entity.setTargetType(model.getTargetType());
		entity.setOperationType(model.getActionType());
		entity.setRelated(model.isRelated());
		entity.setNodeId(model.getNodeId());
		return entity;
	}

	private String serviceGridId;
	private String userGridId;
	private String userId;
	private static final long serialVersionUID = 1L;
}
