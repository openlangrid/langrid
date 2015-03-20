package jp.go.nict.langrid.management.web.model;

import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class AccessLimitControlModel extends AccessControlModel {
	/**
	 * 
	 * 
	 */
	public int getLimitCount() {
		return limitCount;
	}

	/**
	 * 
	 * 
	 */
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}
	
	/**
	 * 
	 * 
	 */
	public LimitType getLimitType() {
		return limitType;
	}
	
	/**
	 * 
	 * 
	 */
	public void setLimitType(LimitType limitType) {
		this.limitType = limitType;
	}
	
	/**
	 * 
	 * 
	 */
	public Period getPeriod() {
		return period;
	}
	
	/**
	 * 
	 * 
	 */
	public void setPeriod(Period period) {
		this.period = period;
	}

	private int limitCount;
	private LimitType limitType;
	private Period period;

	private static final long serialVersionUID = 3571131222667028900L;
}
