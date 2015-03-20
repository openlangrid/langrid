package jp.go.nict.langrid.management.web.model.service;

import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

public interface ScheduleService extends DataService<ScheduleModel> {
	
	/**
	 * 
	 * 
	 */
	public List<ScheduleModel> getListByConditions(int index, int count
				, String targetGridId, String targetId, ScheduleActionType actionType
				, ScheduleTargetType targetType
			)
	throws ServiceManagerException;
	
	public ScheduleModel getByConditions(
			String gridId, String targetId, ScheduleActionType actionType
			, ScheduleTargetType targetType, boolean isRelated)
	throws ServiceManagerException;
	
	public ScheduleModel getNearestStatus(
			String targetId, ScheduleTargetType targetType)
	throws ServiceManagerException;
	
	public LangridList<ScheduleModel> getListPassedBookingTime(
	   ScheduleTargetType type, Calendar fiducialTime)
	throws ServiceManagerException;
	
	public boolean canSchedule(
      String id, ScheduleTargetType targetType, ScheduleActionType actionType)
   throws ServiceManagerException;
}
