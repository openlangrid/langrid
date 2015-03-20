package jp.go.nict.langrid.management.web.view.model;

import java.io.Serializable;

import jp.go.nict.langrid.dao.OrderDirection;



/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ServiceSortValue implements Serializable{
	/**
	 * 
	 * 
	 */
	public ServiceSortValue(String columnName, String displayValue, OrderDirection direction) {
		this.columnName = columnName;
		this.displayValue = displayValue;
		this.direction = direction;
	}
	
	public String getColumnName() {
		return columnName;
	}
	 
	public OrderDirection getDirection() {
		return direction;
	}
	
	public String getDisplayValue() {
		return displayValue;
	}
	
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public void setDirection(OrderDirection direction) {
		this.direction = direction;
	}
	
	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	private String columnName;
	private OrderDirection direction;
	private String displayValue;

	private static final long serialVersionUID = 1L;
}