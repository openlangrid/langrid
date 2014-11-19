package jp.go.nict.langrid.management.web.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathType;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ServiceTypeModel extends ServiceDomainModel {
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
	public String getDomainId() {
		return domainId;
	}

	/**
	 * 
	 * 
	 */
	public List<InterfaceDefinitionModel> getInterfaceList() {
		return interfaceList;
	}

	/**
	 * 
	 * 
	 */
	public Collection<ServiceMetaAttributeModel> getMetaAttrbuteList() {
		return metaAttrbuteList;
	}

	/**
	 * 
	 * 
	 */
	public String getTypeId() {
		return typeId;
	}

	/**
	 * 
	 * 
	 */
	public String getTypeName() {
		return typeName;
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
	public void setDescription(String description) {
		this.description = description;
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
	public void setInterfaceList(List<InterfaceDefinitionModel> interfaceList) {
		this.interfaceList = interfaceList;
	}

	/**
	 * 
	 * 
	 */
	public void setMetaAttrbuteList(Collection<ServiceMetaAttributeModel> metaAttrbuteList) {
		this.metaAttrbuteList = metaAttrbuteList;
	}

	/**
	 * 
	 * 
	 */
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * 
	 * 
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	public LanguagePathType getCurrentPathType() {
		if(typeId.equals("ParallelText")) {
			return LanguagePathType.COMBINATION;
		}
		
		if(typeSet.isEmpty() || typeId.equals("Other")) {
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
	private String typeId;
	private String typeName;
	private String description;
	private Collection<ServiceMetaAttributeModel> metaAttrbuteList;
	private List<InterfaceDefinitionModel> interfaceList;
	private Set<LanguagePathType> typeSet;
}
