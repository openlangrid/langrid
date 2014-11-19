package jp.go.nict.langrid.management.web.model;

import java.util.Collection;
import java.util.Set;

import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathType;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ResourceTypeModel extends ServiceDomainModel {
	/**
	 * 
	 * 
	 */
	public String getDomainId() {
		return domainId;
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
	public String getResourceTypeId() {
		return resourceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public String getResourceTypeName() {
		return resourceTypeName;
	}
	
	/**
	 * 
	 * 
	 */
	public void setDomainId(String domainId) {
		this.domainId = domainId;
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
	public void setResourceTypeId(String resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	/**
	 * 
	 * 
	 */
	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	/**
	 * 
	 * 
	 */
	public Set<LanguagePathType> getTypeSet() {
		return typeSet;
	}

	/**
	 * 
	 * 
	 */
	public void setTypeSet(
		Set<LanguagePathType> typeSet) {
		this.typeSet = typeSet;
	}

	/**
	 * 
	 * 
	 */
	public Collection<ResourceMetaAttributeModel> getMetaAttrbuteList() {
		return metaAttrbuteList;
	}

	/**
	 * 
	 * 
	 */
	public void setMetaAttrbuteList(
		Collection<ResourceMetaAttributeModel> metaAttrbuteList) {
		this.metaAttrbuteList = metaAttrbuteList;
	}
	
	/**
	* 
	* 
	*/
	public LanguagePathType getCurrentPathType() {
		if(resourceTypeId.equals("ParallelText") || resourceTypeId.equals("MultilingualDictionary")) {
			return LanguagePathType.COMBINATION;
		}
		
		if(typeSet.isEmpty() || resourceTypeId.equals("Other")) {
			return LanguagePathType.UNKNOWN;
		}
		for(LanguagePathType lpt : typeSet) {
			if( ! lpt.equals(LanguagePathType.COMBINATION)) {
				return lpt;
			} 
		}
		return LanguagePathType.UNKNOWN;
	}

	private String domainId;
	
	private String resourceTypeId;
	private String resourceTypeName;
	private String description;
	private Collection<ResourceMetaAttributeModel> metaAttrbuteList;
	private Set<LanguagePathType> typeSet;

}
