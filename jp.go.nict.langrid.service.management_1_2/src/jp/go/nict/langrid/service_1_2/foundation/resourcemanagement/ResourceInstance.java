package jp.go.nict.langrid.service_1_2.foundation.resourcemanagement;

import java.io.Serializable;

import jp.go.nict.langrid.service_1_2.LanguagePath;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ResourceInstance implements Serializable {
	/**
	 * 
	 * 
	 */
	public ResourceInstance() {
	}
	/**
	 * 
	 * 
	 */
	public ResourceInstance(String resourceTYpe, LanguagePath[] supportedLanguages) {
		this.resourceType = resourceTYpe;
		this.supportedLanguages = supportedLanguages;
	}

	/**
	* 
	* 
	*/
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * 
	 * 
	 */
	public LanguagePath[] getSupportedLanguages() {
		return supportedLanguages;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * 
	 * 
	 */
	public void setSupportedLanguages(LanguagePath[] supportedLanguages) {
		this.supportedLanguages = supportedLanguages;
	}

	private String resourceType;

	private LanguagePath[] supportedLanguages;
	private static final long serialVersionUID = 7387197509240235890L;
}
