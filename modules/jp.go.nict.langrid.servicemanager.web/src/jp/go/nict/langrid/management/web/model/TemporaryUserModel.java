package jp.go.nict.langrid.management.web.model;

import java.util.Calendar;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class TemporaryUserModel extends ServiceGridModel {
	/**
	 * 
	 * 
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * 
	 * 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 
	 * 
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * 
	 * 
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 
	 * 
	 */
	public String getParentUserId() {
		return parentUserId;
	}
	
	/**
	 * 
	 * 
	 */
	public void setParentUserId(String parentUserId) {
		this.parentUserId = parentUserId;
	}
	
	/**
	 * 
	 * 
	 */
	public Calendar getBeginAvailableDateTime() {
		return beginAvailableDateTime;
	}
	
	/**
	 * 
	 * 
	 */
	public void setBeginAvailableDateTime(Calendar beginAvailableDateTime) {
		this.beginAvailableDateTime = beginAvailableDateTime;
	}
	
	/**
	 * 
	 * 
	 */
	public Calendar getEndAvailableDateTime() {
		return endAvailableDateTime;
	}

	/**
	 * 
	 * 
	 */
	public void setEndAvailableDateTime(Calendar endAvailableDateTime) {
		this.endAvailableDateTime = endAvailableDateTime;
	}
	
	private String userId;
	
	private String password;
	private String parentUserId;
	private Calendar beginAvailableDateTime;
	private Calendar endAvailableDateTime;

	private static final long serialVersionUID = -7001361224906067421L;
}
