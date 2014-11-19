package jp.go.nict.langrid.service_1_2.foundation.resourcemanagement;

import jp.go.nict.langrid.service_1_2.LangridException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ResourceNotInactiveException extends LangridException {
	/**
	 * 
	 * 
	 */
	public ResourceNotInactiveException(String resourceId) {
		super("language resource \"" + resourceId + "\" is no inactive.");
	}

	/**
	 * 
	 * 
	 */
	public String getResourceId() {
		return resourceId;
	}

	private String resourceId;
	
	private static final long serialVersionUID = -3716905087004410514L;
}
