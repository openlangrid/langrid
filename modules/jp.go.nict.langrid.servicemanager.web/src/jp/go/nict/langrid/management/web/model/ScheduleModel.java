package jp.go.nict.langrid.management.web.model;

import java.util.Calendar;

import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ScheduleModel extends ServiceGridModel {
	/**
	 * 
	 * 
	 */
	public ScheduleActionType getActionType() {
		return actionType;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getBookingDateTime() {
		return bookingDateTime;
	}

	/**
	 * 
	 * 
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * 
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 
	 * 
	 */
	public String getTargetId() {
		return targetId;
	}

	/**
	 * 
	 * 
	 */
	public ScheduleTargetType getTargetType() {
		return targetType;
	}

	/**
	 * 
	 * 
	 */
	public boolean isRelated() {
		return isRelated;
	}

	/**
	 * 
	 * 
	 */
	public void setActionType(ScheduleActionType actionType) {
		this.actionType = actionType;
	}

	/**
	 * 
	 * 
	 */
	public void setBookingDateTime(Calendar bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * 
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 
	 * 
	 */
	public void setRelated(boolean isRelated) {
		this.isRelated = isRelated;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	/**
	 * 
	 * 
	 */
	public void setTargetType(ScheduleTargetType targetType) {
		this.targetType = targetType;
	}

	private int id;
	private String targetId;
	private Calendar bookingDateTime;
	private ScheduleActionType actionType;
	private ScheduleTargetType targetType;
	private boolean isRelated;
	private String nodeId;

	private static final long serialVersionUID = 7751870771597332302L;
}
