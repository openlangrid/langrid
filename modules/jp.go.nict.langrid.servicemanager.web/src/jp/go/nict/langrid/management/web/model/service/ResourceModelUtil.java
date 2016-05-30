package jp.go.nict.langrid.management.web.model.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourceAttribute;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.language.InvalidLanguagePathException;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.language.util.LanguagePathUtil;
import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathType;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;
import jp.go.nict.langrid.management.web.utility.StringUtil;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 553 $
 */
public class ResourceModelUtil {
   public static ResourceModel makeModel(Resource entity) throws ServiceManagerException {
      try {
         ResourceModel model = new ResourceModel();
         // IDs
         model.setGridId(entity.getGridId());
         model.setOwnerUserId(entity.getOwnerUserId());
         model.setResourceId(entity.getResourceId());
         // profile
         model.setResourceName(entity.getResourceName());
         try {
            if(entity.getResourceTypeId() == null || entity.getResourceTypeId().equals("")) {
               model.setResourceType(makeOtherResourceTypeModel());
            } else {
               ResourceTypeModel typeModel = ServiceFactory.getInstance().getResourceTypeService(entity.getGridId()).get(
                  entity.getResourceTypeDomainId(), entity.getResourceTypeId());
               model.setResourceType(typeModel);
            }
         } catch(ServiceManagerException e) {
            model.setResourceType(makeOtherResourceTypeModel());
         }
         model.setResourceDescription(entity.getResourceDescription());
         
         Collection<ResourceMetaAttributeModel> collection = model.getResourceType().getMetaAttrbuteList();
         if(entity.getResourceTypeId() != null && entity.getResourceTypeDomainId() != null
         	&& (collection.size() == 0))
//         	&& ! isDefinedType(entity.getResourceTypeId()))
         {
            collection.add(makeInternalResourceMetaAttributeModel(
               entity.getResourceTypeDomainId(), entity.getResourceTypeId()));
         }
         model.setSupportedLanguagePathModel(
            makeLanguagePathModel(collection, entity.getAttributes()));
         model.setCopyrightInfo(entity.getCopyrightInfo());
         model.setLicenseInfo(entity.getLicenseInfo());
         // optional
         model.setOsInfo(entity.getOsInfo());
         model.setCpuInfo(entity.getCpuInfo());
         model.setMemoryInfo(entity.getMemoryInfo());
         model.setSpecialNoteInfo(entity.getSpecialNoteInfo());
         // status
         model.setCreatedDateTime(entity.getCreatedDateTime());
         model.setUpdatedDateTime(entity.getUpdatedDateTime());
         model.setActive(entity.isActive());
         model.setApproved(entity.isApproved());
         return model;
      } catch(InvalidLanguageTagException e) {
         throw new ServiceManagerException(e);
      } catch(InvalidLanguagePathException e) {
         throw new ServiceManagerException(e);
      }
   }

   public static Resource setProperty(ResourceModel model, Resource entity) throws ServiceManagerException{
      // IDs
      entity.setGridId(model.getGridId());
      entity.setOwnerUserId(model.getOwnerUserId());
      entity.setResourceId(model.getResourceId());
      // profile
      entity.setResourceName(model.getResourceName());
      
      if(model.getResourceType() != null){
    	  entity.setResourceTypeDomainId(model.getResourceType().getDomainId());
          entity.setResourceTypeId(model.getResourceType().getResourceTypeId());
         entity.setAttributes(getSupportedLanguageList(model, entity.getAttributes()));
      }
      
      entity.setResourceDescription(model.getResourceDescription());
      entity.setCopyrightInfo(model.getCopyrightInfo());
      entity.setLicenseInfo(model.getLicenseInfo());
      // optional
      entity.setOsInfo(StringUtil.nullString(model.getOsInfo()));
      entity.setCpuInfo(StringUtil.nullString(model.getCpuInfo()));
      entity.setMemoryInfo(StringUtil.nullString(model.getMemoryInfo()));
      entity.setSpecialNoteInfo(StringUtil.nullString(model.getSpecialNoteInfo()));
      // status
      entity.setApproved(model.isApproved());
      
      return entity;
   }

   public static ResourceTypeModel makeTypeModel(ResourceType entity)
   throws ServiceManagerException
   {
      ResourceTypeModel model = new ResourceTypeModel();
      model.setDomainId(entity.getDomainId());
      model.setResourceTypeId(entity.getResourceTypeId());
      model.setResourceTypeName(entity.getResourceTypeName());
      model.setDescription(entity.getDescription());
      model.setCreatedDateTime(entity.getCreatedDateTime());
      model.setUpdatedDateTime(entity.getUpdatedDateTime());
      List<ResourceMetaAttributeModel> metaList = new ArrayList<ResourceMetaAttributeModel>();
      Collection<ResourceMetaAttribute> list = entity.getMetaAttributes().values();
      Set<String>idSet = new HashSet<String>();
      for(ResourceMetaAttribute attr : list) {
         idSet.add(attr.getAttributeId());
         metaList.add(makeResourceMetaAttributeModel(attr));
      }
      model.setTypeSet(jp.go.nict.langrid.management.web.utility.LanguagePathUtil.getPathTypeSet(
         entity.getResourceTypeId(), idSet));
      model.setMetaAttrbuteList(metaList);
      return model;
   }
   
   public static ResourceType setTypeProperty(ResourceType entity, ResourceTypeModel model) {
      entity.setDomainId(model.getDomainId());
      entity.setResourceTypeId(model.getResourceTypeId());
      entity.setResourceTypeName(model.getResourceTypeName());
      entity.setDescription(model.getDescription());
      List<ResourceMetaAttribute> attrList = new ArrayList<ResourceMetaAttribute>();
      for(ResourceMetaAttributeModel m : model.getMetaAttrbuteList()) {
         ResourceMetaAttribute sma = new ResourceMetaAttribute();
         attrList.add(setResourceMetaAttribute(m, sma));
      }
      entity.setMetaAttributeCollection(attrList);
      entity.setCreatedDateTime(model.getCreatedDateTime());
      entity.setUpdatedDateTime(model.getUpdatedDateTime());
      return entity;
   }

   public static ResourceTypeModel makeOtherResourceTypeModel(
   ){
      ResourceTypeModel model = new ResourceTypeModel();
      model.setDomainId("Other");
      model.setResourceTypeId("Other");
      model.setResourceTypeName("Other");
      model.setDescription("Other");
      List<ResourceMetaAttributeModel> metaList = new ArrayList<ResourceMetaAttributeModel>();
      metaList.add(makeOtherResourceMetaAttributeModel("Other", "Other"));
      model.setMetaAttrbuteList(metaList);
      Set<LanguagePathType> set = new LinkedHashSet<LanguagePathType>();
      set.add(LanguagePathType.UNKNOWN);
      model.setTypeSet(set);
      return model;
   }
   
   public static ResourceMetaAttributeModel makeOtherResourceMetaAttributeModel(String typeDomainId, String typeId){
      ResourceMetaAttributeModel model = new ResourceMetaAttributeModel();
      model.setAttributeId(typeDomainId + "_" + typeId + "_SupportedLanguages");
      model.setAttributeName(typeDomainId + "_" + typeId + "_SupportedLanguages");
      model.setDescription("");
      model.setDomainId(typeDomainId);
      return model;
   }
   
   public static ResourceMetaAttributeModel makeInternalResourceMetaAttributeModel(
      String domainId, String typeId)
   {
      ResourceMetaAttributeModel model = new ResourceMetaAttributeModel();
      model.setDomainId(domainId);
      model.setAttributeId(domainId + ":" + typeId + ":SupportedLanguages");
      model.setAttributeName("Supported Languages");
      return model;
   }

   public static ResourceMetaAttributeModel makeResourceMetaAttributeModel(ResourceMetaAttribute entity) {
      ResourceMetaAttributeModel model = new ResourceMetaAttributeModel();
      model.setDomainId(entity.getDomainId());
      model.setAttributeId(entity.getAttributeId());
      model.setAttributeName(entity.getAttributeName());
      model.setDescription(entity.getDescription());
      return model;
   }

   public static ResourceMetaAttribute setResourceMetaAttribute(
      ResourceMetaAttributeModel model, ResourceMetaAttribute entity)
   {
      entity.setAttributeId(model.getAttributeId());
      entity.setAttributeName(model.getAttributeName());
      entity.setDescription(model.getDescription());
      entity.setDomainId(model.getDomainId());
      return entity;
   }

   public static LanguagePathModel makeLanguagePathModel(
      Collection<ResourceMetaAttributeModel> metaAttributeList, Collection<ResourceAttribute> attributeList)
   throws InvalidLanguageTagException, InvalidLanguagePathException
   {
      LanguagePathModel pathModel = new LanguagePathModel();
      for(ResourceAttribute ra : attributeList) {
         for(ResourceMetaAttributeModel rmam : metaAttributeList) {
            if(ra.getName().equals(rmam.getAttributeId())) {
               if (MetaAttribute.containName(ra.getName().toUpperCase())) {
                  pathModel.replacePath(ra.getName(), LanguagePathUtil.decodeLanguagePathArray(ra.getValue()));
               } else {
                  pathModel.setOtherLanguage(ra.getName(), ra.getValue());
               }
               break;
            }
         }
      }
      return pathModel;
   }
   
   public static boolean isSingleLanguagePathTypel(String typeId){
      return typeId.equals("TextToSpeech");
   }
   
   private static Collection<ResourceAttribute> getSupportedLanguageList(
      ResourceModel model, Collection<ResourceAttribute> originalList)
   {
      List<ResourceAttribute> attrList = new ArrayList<ResourceAttribute>();
      boolean isAdd = true;
      for (ResourceAttribute ra : originalList) {
         for (String key : model.getSupportedLanguagePathModel().getAllKeySet()){
            if (key.equals(ra.getName())) {
               LanguagePath[] paths = model.getSupportedLanguagePathModel().getPath(key);
               if (paths == null || paths.length == 0) {
                  String path = model.getSupportedLanguagePathModel().getOtherLanguage(key);
                  if(path == null || path.equals("")){
                     isAdd = false;
                  }else{
                     ra.setValue(path);
                  }
               } else {
                  ra.setValue(LanguagePathUtil.encodeLanguagePathArray(paths));
               }
               break;
            }
         }
         if (isAdd) {
            attrList.add(ra);
         }
         isAdd = true;
      }
      boolean isBreak = false;
      for (String key : model.getSupportedLanguagePathModel().getAllKeySet()) {
         for (ResourceAttribute ra : originalList) {
            if (key.equals(ra.getName())) {
               isBreak = true;
               break;
            }
         }
         if ( ! isBreak) {
            ResourceAttribute ra = new ResourceAttribute();
            ra.setGridId(model.getGridId());
            ra.setResourceId(model.getResourceId());
            ra.setName(key);

            LanguagePath[] paths = model.getSupportedLanguagePathModel().getPath(key);
            if(paths != null && paths.length != 0) {
               ra.setValue(LanguagePathUtil.encodeLanguagePathArray(paths));
            } else {
               String path = model.getSupportedLanguagePathModel().getOtherLanguage(key);
               if(path != null && ! path.equals("")) {
                  ra.setValue(path);
               }
            }
            attrList.add(ra);
         }
         isBreak = false;
      }
      return attrList;
   }
}
