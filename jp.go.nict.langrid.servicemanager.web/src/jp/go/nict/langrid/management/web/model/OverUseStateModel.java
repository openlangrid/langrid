package jp.go.nict.langrid.management.web.model;

import java.util.Calendar;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class OverUseStateModel extends AccessLimitControlModel {
	/**
	 * 
	 * 
	 */
	public Calendar getBaseDateTime() {
		return baseDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setBaseDateTime(Calendar baseDateTime) {
		this.baseDateTime = baseDateTime;
	}

	/**
	 * 
	 * 
	 */
	public long getCurrentCount() {
		return currentCount;
	}

	/**
	 * 
	 * 
	 */
	public void setCurrentCount(long currentCount) {
		this.currentCount = currentCount;
	}
	
	/**
	 * 
	 * 
	 */
	public Calendar getLastAccessDateTime() {
		return lastAccessDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setLastAccessDateTime(Calendar lastAccessDateTime) {
		this.lastAccessDateTime = lastAccessDateTime;
	}

	private Calendar baseDateTime;
	private long currentCount;
	private Calendar lastAccessDateTime;

	private static final long serialVersionUID = 3863809145617658260L;
}
