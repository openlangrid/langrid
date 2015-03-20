package jp.go.nict.langrid.management.web.model;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public abstract class ServiceManagerModel implements Serializable {	
	/**
	 * 
	 * 
	 */
	public Calendar getCreatedDateTime() {
		return createdDateTime;
	}
	
	/**
	 * 
	 * 
	 */
	public Calendar getUpdatedDateTime() {
		return updatedDateTime;
	}
	
	/**
	 * 
	 * 
	 */
	public void setCreatedDateTime(Calendar registeredDate) {
		this.createdDateTime = registeredDate;
	}
	
	/**
	 * 
	 * 
	 */
	public void setUpdatedDateTime(Calendar updatedDate) {
		this.updatedDateTime = updatedDate;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	private Calendar createdDateTime;
	private Calendar updatedDateTime;

	private static final long serialVersionUID = 134313393014256644L;
}
