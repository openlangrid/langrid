package jp.go.nict.langrid.service_1_2.foundation.resourcemanagement;

import jp.go.nict.langrid.service_1_2.LangridException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ResourceNotFoundException extends LangridException {
	/**
	 * 
	 * 
	 */
	public ResourceNotFoundException(String resourceId) {
		super(resourceId + " not found.");
		this.resourceId = resourceId;
	}

	/**
	 * 
	 * 
	 */
	public String getResourceId() {
		return resourceId;
	}

	private String resourceId;
	
	private static final long serialVersionUID = -8326776599178689074L;

}
