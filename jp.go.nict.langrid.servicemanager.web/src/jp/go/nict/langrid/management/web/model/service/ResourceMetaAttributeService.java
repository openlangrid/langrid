package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public interface ResourceMetaAttributeService extends DataService<ResourceMetaAttributeModel> {
	public void deleteById(String domainId, String attributeId) throws ServiceManagerException;
	
	public ResourceMetaAttributeModel get(String domainId, String attributeId) throws ServiceManagerException;
	
	public LangridList<ResourceMetaAttributeModel> getListOnDomain(int first, int count, String domainId) throws ServiceManagerException;

	public LangridList<ResourceMetaAttributeModel> getAllListOnDomain(String domainId) throws ServiceManagerException;
}
