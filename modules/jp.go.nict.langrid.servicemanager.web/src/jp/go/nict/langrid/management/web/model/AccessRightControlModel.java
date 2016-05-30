package jp.go.nict.langrid.management.web.model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class AccessRightControlModel extends AccessControlModel {
	/**
	 * 
	 * 
	 */
	public boolean isPermitted() {
		return permitted;
	}

	/**
	 * 
	 * 
	 */
	public void setPermitted(boolean permitted) {
		this.permitted = permitted;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceOwnerUserId() {
		return serviceOwnerUserId;
	}

	/**
	 * 
	 * 
	 */
	public void setServiceOwnerUserId(String serviceOwnerUserId) {
		this.serviceOwnerUserId = serviceOwnerUserId;
	}

	private boolean permitted;
	private String serviceOwnerUserId;

	private static final long serialVersionUID = -1397015205924555466L;
}
