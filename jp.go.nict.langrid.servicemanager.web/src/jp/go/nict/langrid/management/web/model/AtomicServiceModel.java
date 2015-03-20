package jp.go.nict.langrid.management.web.model;

import jp.go.nict.langrid.dao.entity.ServiceContainerType;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class AtomicServiceModel extends ServiceModel {
	/**
	 * 
	 * 
	 */
	public AtomicServiceModel() {
		setContainerType(ServiceContainerType.ATOMIC.name());
	}
	
	/**
	 * 
	 * 
	 */
	public String getSourceCodeUrl() {
		return sourceCodeUrl;
	}

	/**
	 * 
	 * 
	 */
	public void setSourceCodeUrl(String sourceCodeUrl) {
		this.sourceCodeUrl = sourceCodeUrl;
	}

	/**
	 * 
	 * 
	 */
	public String getResourceId() {
		return resourceId;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	private String resourceId;
	private String sourceCodeUrl;

	private static final long serialVersionUID = 8592145614683751589L;
}
