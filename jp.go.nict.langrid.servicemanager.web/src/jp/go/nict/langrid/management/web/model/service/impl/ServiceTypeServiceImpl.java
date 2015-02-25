package jp.go.nict.langrid.management.web.model.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceTypeNotFoundException;
import jp.go.nict.langrid.dao.entity.ServiceInterfaceDefinition;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.logic.ServiceTypeLogic;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceModelUtil;
import jp.go.nict.langrid.management.web.model.service.ServiceTypeService;
import jp.go.nict.langrid.management.web.utility.FileUtil;

import org.apache.commons.lang.SystemUtils;

public class ServiceTypeServiceImpl implements ServiceTypeService {
   @Override
   public void add(ServiceTypeModel obj) throws ServiceManagerException {
      try {
         new ServiceTypeLogic().addServiceType(ServiceModelUtil.setServiceTypeProperty(obj, new ServiceType()));
      } catch(DaoException e) {
         throw new ServiceManagerException(e);
      }
   }

   @Override
   public void edit(final ServiceTypeModel obj) throws ServiceManagerException {
      try {
         new ServiceTypeLogic().transactUpdate(obj.getDomainId(), obj.getTypeId(), new BlockP<ServiceType>() {
            @Override
            public void execute(ServiceType arg0) {
               ServiceModelUtil.setServiceTypeProperty(obj, arg0);
            }
         });
      } catch(DaoException e) {
         throw new ServiceManagerException(e);
      }
   }

   @Override
   public void delete(ServiceTypeModel condition) throws ServiceManagerException {
      deleteById(condition.getDomainId(), condition.getTypeId());
   }

   @Override
   public void deleteById(String domainId, String typeId) throws ServiceManagerException {
      try {
         new ServiceTypeLogic().deleteServiceType(domainId, typeId);
      } catch(DaoException e) {
         throw new ServiceManagerException(e);
      }
   }

   /**
    * noop
    */
   @Override
   public ServiceTypeModel get(String id) throws ServiceManagerException {
      return null;
   }

   @Override
   public ServiceTypeModel get(String domainId, String id) throws ServiceManagerException {
      try {
    	  try{
    		  return ServiceModelUtil.makeServiceTypeModel(new ServiceTypeLogic().getServiceType(domainId, id));
	      } catch(ServiceTypeNotFoundException e ){
	    	  return ServiceModelUtil.makeOtherServiceTypeModel();
	      }
      } catch(ServiceTypeNotFoundException e) {
    	  return null;
      } catch(DaoException e) {
         throw new ServiceManagerException(e);
      }
   }

   @Override
   public ServiceType getEntity(String domainId, String typeId)
   throws ServiceManagerException {
      try {
         for(ServiceType type : new ServiceTypeLogic().listServiecType(domainId)) {
            if(type.getServiceTypeId().equals(typeId)) {
               return type;
            }
         }
      } catch(DaoException e) {
         throw new ServiceManagerException(e);
      }
      return null;
   }

   @Override
   public LangridList<ServiceTypeModel> getAllList() throws ServiceManagerException {
      try {
         List<ServiceType> result = new ServiceTypeLogic().listAllServiceType();
         TreeMap<String, ServiceType> map = new TreeMap<String, ServiceType>();
         for(ServiceType def : result) {
        	 map.put(def.getServiceTypeName(), def);
         }
         
         LangridList<ServiceTypeModel> list = new LangridList<ServiceTypeModel>();
         for(ServiceType def : map.values()) {
            list.add(ServiceModelUtil.makeServiceTypeModel(def));
         }
         return list;
      } catch(DaoException e) {
         throw new ServiceManagerException(e);
      }
   }
   
   @Override
   public LangridList<ServiceTypeModel> getAllList(String domainId)
   throws ServiceManagerException {
      try {
         List<ServiceType> result = new ServiceTypeLogic().listServiecType(domainId);
         LangridList<ServiceTypeModel> list = new LangridList<ServiceTypeModel>();
         for(ServiceType def : result) {
            list.add(ServiceModelUtil.makeServiceTypeModel(def));
         }
         return list;
      } catch(DaoException e) {
         throw new ServiceManagerException(e);
      }
   }

   @Override
      public LangridList<ServiceTypeModel> getList(
         int index, int count, MatchingCondition[] conditions, Order[] orders, Scope scope)
      throws ServiceManagerException
      {
         LangridList<ServiceTypeModel> list = new LangridList<ServiceTypeModel>();
         for(ServiceTypeModel model : getAllList().subList(index, index + count)){
            list.add(model);
         }
         return list;
      }

   @Override
   public LangridList<ServiceTypeModel> getAtomicServiceTypeList()
   throws ServiceManagerException {
      LangridList<ServiceTypeModel> list = new LangridList<ServiceTypeModel>();
      for(ServiceTypeModel model : getAllList()){
         if( ! model.getTypeId().equals("BackTranslation")
            && ! model.getTypeId().equals("MultihopTranslation"))
         {
            list.add(model);
         }
      }
      return list;
   }

   @Override
   public int getTotalCount(MatchingCondition[] conditions, Scope scope)
   throws ServiceManagerException
   {
      try {
         return new ServiceTypeLogic().listAllServiceType().size();
      } catch(DaoException e) {
         throw new ServiceManagerException(e);
      }
   }
   
   public List<String> getDefinitionFileNames(String domainId, String typeId, String protocolId)
   throws ServiceManagerException
   {
      try {
         for(ServiceInterfaceDefinition d : new ServiceTypeLogic().getServiceType(
            domainId, typeId).getInterfaceDefinitions().values())
         {
            List<String> nameList = new ArrayList<String>();
            String filePath = "/";
            if(SystemUtils.IS_OS_UNIX){
               filePath = "\\";
            }
            if(d.getProtocolId().equals(protocolId)){
               ZipInputStream zis = new ZipInputStream(d.getDefinition().getBinaryStream());
               try {
                  for (ZipEntry ze = zis.getNextEntry(); ze != null; ze = zis.getNextEntry()) {
                     if (ze.isDirectory()) {continue;}
                     nameList.add(FileUtil.getFileNameWithoutPath(ze.getName()));
                  }
               } catch(IOException e) {
                  throw new ServiceManagerException(e);
               }finally{
                  if(zis != null){
                     try {
                        zis.close();
                     } catch(IOException e) {
                        e.printStackTrace();
                     }
                  }
               }
               return nameList;
            }
         }
         return null;
      } catch(DaoException e) {
         throw new ServiceManagerException(e);
      } catch(SQLException e) {
         throw new ServiceManagerException(e);
      }
   }

   @Override
   public void setScopeParameter(String serviceGridId, String userGridId, String userId) {
      this.serviceGridId = serviceGridId;
      this.userGridId = userGridId;
      this.userId = userId;
   }
   
   @Override
	public boolean isExist(String dataId) throws ServiceManagerException {
	   return get(dataId) != null;
	}

   private String serviceGridId;
   private String userGridId;
   private String userId;
}
