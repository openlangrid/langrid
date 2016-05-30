package jp.go.nict.langrid.management.web.model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ServiceMetaAttributeModel extends ServiceDomainModel {
	/**
	 * 
	 * 
	 */
	public String getAttributeId() {
		return attributeId;
	}

	/**
	 * 
	 * 
	 */
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * 
	 * 
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * 
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * 
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * 
	 * 
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	private String description;
	private String attributeId;
	private String attributeName;

	private static final long serialVersionUID = 5973744611805225728L;
}
