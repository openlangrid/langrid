package jp.go.nict.langrid.management.web.model;


/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ResourceMetaAttributeModel extends ServiceDomainModel {
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
	public String getAttributeId() {
		return attributeId;
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
	public void setDescription(String description) {
		this.description = description;
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
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	private String attributeId;
	private String attributeName;
	private String description;

	private static final long serialVersionUID = 1963853269034732434L;
}
