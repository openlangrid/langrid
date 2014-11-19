package jp.go.nict.langrid.management.web.model.service;

import java.util.List;

import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public interface ServiceTypeService extends DataService<ServiceTypeModel> {
   /**
    * 
    * 
    */
   public ServiceTypeModel get(String domainId, String id) throws ServiceManagerException;

   /**
    * 
    * 
    */
   public ServiceType getEntity(String domainId, String typeId) throws ServiceManagerException;

   /**
    * 
    * 
    */
   public LangridList<ServiceTypeModel> getAllList() throws ServiceManagerException;

   /**
    * 
    * 
    */
   public LangridList<ServiceTypeModel> getAllList(String domainId) throws ServiceManagerException;
   
   /**
    * 
    * 
    */
   public LangridList<ServiceTypeModel> getAtomicServiceTypeList() throws ServiceManagerException;
   
   /**
    * 
    * 
    */
   public void deleteById(String domainId, String typeId) throws ServiceManagerException;
   
   /**
    * 
    * 
    */
   public List<String> getDefinitionFileNames(String domainId, String typeId, String protocolId)
   throws ServiceManagerException;
}
