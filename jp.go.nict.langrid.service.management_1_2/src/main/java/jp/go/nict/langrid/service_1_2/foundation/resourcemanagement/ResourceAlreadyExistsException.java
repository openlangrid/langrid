package jp.go.nict.langrid.service_1_2.foundation.resourcemanagement;

import jp.go.nict.langrid.service_1_2.LangridException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ResourceAlreadyExistsException extends LangridException {
	/**
	 * 
	 * 
	 */
	public ResourceAlreadyExistsException(String resourceId) {
		super(resourceId + " already exists.");
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
	
	private static final long serialVersionUID = 465567892503066497L;

}
