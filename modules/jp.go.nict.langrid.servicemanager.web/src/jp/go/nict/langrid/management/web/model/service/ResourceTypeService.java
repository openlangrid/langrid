package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public interface ResourceTypeService extends DataService<ResourceTypeModel> {
	/**
	 * 
	 * 
	 */
	public void delete(String resourceTypeId)
	throws ServiceManagerException;
	
	public ResourceType getEntity(String domainId, String resourceTypeId) throws ServiceManagerException;
	
	public LangridList<ResourceTypeModel> getAllList() throws ServiceManagerException;

	public LangridList<ResourceTypeModel> getAllList(String domainId) throws ServiceManagerException;
	
	public ResourceTypeModel get(String domainId, String resourceTypeId) throws ServiceManagerException;

	public void delete(String domainId, String resourceTypeId) throws ServiceManagerException;
}
