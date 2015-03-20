package jp.go.nict.langrid.management.web.model;


/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public abstract class ServiceGridModel extends ServiceManagerModel {
	/**
	 * 
	 * 
	 */
	public String getGridId() {
		return gridId;
	}
	
	/**
	 * 
	 * 
	 */
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}
	
	private String gridId;
}
