package jp.go.nict.langrid.management.web.model.service;

import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public interface ServiceMetaAttributeService extends DataService<ServiceMetaAttributeModel> {
   public ServiceMetaAttributeModel get(String domainId, String attributeId) throws ServiceManagerException;
   
   public LangridList<ServiceMetaAttributeModel> getListOnDomain(int first, int count, String domainId) throws ServiceManagerException;
   
   public LangridList<ServiceMetaAttributeModel> getAllListOnDomain(String domainId) throws ServiceManagerException;

//   public LangridList<ServiceMetaAttributeModel> getAllList() throws ServiceManagerException;

   public void deleteById(String domainId, String attributeId) throws ServiceManagerException;
   
}
