package jp.go.nict.langrid.management.web.model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class GridModel extends ServiceGridModel {
	/**
	 * 
	 * 
	 */
	public String getGridName() {
		return gridName;
	}

	/**
	 * 
	 * 
	 */
	public String getOperatorUserId() {
		return operatorUserId;
	}

	/**
	 * 
	 * 
	 */
	public boolean isAutoApproveEnabled() {
		return autoApproveEnabled;
	}

	/**
	 * 
	 * 
	 */
	public boolean isCommercialUseAllowed() {
		return commercialUseAllowed;
	}

	/**
	 * 
	 * 
	 */
	public boolean isHosted() {
		return hosted;
	}

	/**
	 * 
	 * 
	 */
	public void setAutoApproveEnabled(boolean autoApproveEnabled) {
		this.autoApproveEnabled = autoApproveEnabled;
	}

	/**
	 * 
	 * 
	 */
	public void setCommercialUseAllowed(boolean commercialUseAllowed) {
		this.commercialUseAllowed = commercialUseAllowed;
	}


	/**
	 * 
	 * 
	 */
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	/**
	 * 
	 * 
	 */
	public void setHosted(boolean hosted) {
		this.hosted = hosted;
	}

	/**
	 * 
	 * 
	 */
	public void setOperatorUserId(String operatorUserId) {
		this.operatorUserId = operatorUserId;
	}

	/**
	 * 
	 * 
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * 
	 * 
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean isSymmetricRelationEnabled() {
		return symmetricRelationEnabled;
	}
	
	public void setSymmetricRelationEnabled(boolean symmetricRelationEnabled) {
		this.symmetricRelationEnabled = symmetricRelationEnabled;
	}
	
	public boolean isTransitiveRelationEnabled() {
		return transitiveRelationEnabled;
	}
	
	public void setTransitiveRelationEnabled(boolean transitiveRelationEnabled) {
		this.transitiveRelationEnabled = transitiveRelationEnabled;
	}

	private String gridName;
	private String operatorUserId;
	private String url;
	private boolean autoApproveEnabled;
	private boolean hosted;
	private boolean commercialUseAllowed;
	private boolean symmetricRelationEnabled;
	private boolean transitiveRelationEnabled;

	private static final long serialVersionUID = -751181555043874710L;
}
