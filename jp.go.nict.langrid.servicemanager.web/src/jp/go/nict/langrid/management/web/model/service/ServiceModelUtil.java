package jp.go.nict.langrid.management.web.model.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.sql.rowset.serial.SerialBlob;

import jp.go.nict.langrid.dao.AccessRankingEntry;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.AccessLog;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.Invocation;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceAttribute;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.entity.ServiceInterfaceDefinition;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.language.InvalidLanguagePathException;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.logic.ServiceTypeLogic;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.ExecutionInformationStatisticsModel;
import jp.go.nict.langrid.management.web.model.IndividualExecutionInformationModel;
import jp.go.nict.langrid.management.web.model.InterfaceDefinitionModel;
import jp.go.nict.langrid.management.web.model.InvocationModel;
import jp.go.nict.langrid.management.web.model.JavaCompositeServiceModel;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathType;
import jp.go.nict.langrid.management.web.model.enumeration.MetaAttribute;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.languagepath.LanguagePathModel;
import jp.go.nict.langrid.management.web.utility.LanguagePathUtil;

import org.apache.commons.io.IOUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.lob.BlobImpl;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 553 $
 */
public class ServiceModelUtil {
	/**
	 * 
	 * 
	 */
	public static AtomicServiceModel makeAtomicServiceModel(Service service)
	throws ServiceManagerException {
		try {
			AtomicServiceModel model = new AtomicServiceModel();
			// IDs
			model.setGridId(service.getGridId());
			model.setOwnerUserId(service.getOwnerUserId());
			model.setResourceId(service.getResourceId());
			model.setServiceId(service.getServiceId());
			// profile
			model.setServiceName(service.getServiceName());
			if(service.getServiceTypeId() == null || service.getServiceTypeId().equals("")) {
				model.setServiceType(makeOtherServiceTypeModel());
			} else {
				try {
					model.setServiceType(makeServiceTypeModel(
						new ServiceTypeLogic().getServiceType(
							service.getServiceTypeDomainId(), service.getServiceTypeId())));
				} catch(DaoException e) {
					model.setServiceType(makeOtherServiceTypeModel());
					LogWriter.writeWarn("service", "Service type not found.", ServiceModelUtil.class);
				} catch(ObjectNotFoundException e) {
					model.setServiceType(makeOtherServiceTypeModel());
					LogWriter.writeWarn("service", "Service type not found.",ServiceModelUtil.class);
				}
			}
			model.setServiceDescription(service.getServiceDescription());
			Collection<ServiceMetaAttributeModel> collection = model.getServiceType().getMetaAttrbuteList();
			if(service.getServiceTypeId() != null
				&& service.getServiceTypeDomainId() != null
//				&& !isDefinedType(service.getServiceTypeId()))
				&& collection.size() == 0)
			{
				
				collection.add(makeInternalMetaAttributeModel(
					service.getServiceTypeDomainId(), service.getServiceTypeId()));
			}
			
			model.setSupportedLanguagePathModel(
				makeLanguagePathModel(collection, service.getAttributes()));
			model.setMembersOnly(service.isMembersOnly());
			model.setHowToGetMembershipInfo(service.getHowToGetMembershipInfo());
			model.setLicenseInfo(service.getLicenseInfo());
			model.setCopyrightInfo(service.getCopyrightInfo());
			// status
			model.setAllowedAppProvision(service.getAllowedAppProvision());
			model.setAllowedUsage(service.getAllowedUse());
			model.setActive(service.isActive());
			model.setApproved(service.isApproved());
			model.setCreatedDateTime(service.getCreatedDateTime());
			model.setUpdatedDateTime(service.getUpdatedDateTime());
			model.setFederatedUseAllowed(service.isFederatedUseAllowed());
			model.setContainerType(service.getContainerType() == null
				? ServiceContainerType.ATOMIC.name() : service.getContainerType().name());
			if(service.getWrapperSourceCodeUrl() == null) {
				model.setSourceCodeUrl("");
			} else {
				model.setSourceCodeUrl(service.getWrapperSourceCodeUrl() == null ? "" : service.getWrapperSourceCodeUrl().toString());
			}

			// instance
//			byte[] body = ServiceFactory.getInstance().getAtomicServiceService(service.getGridId())
//				.getInstanceBody(service.getServiceId());
//			if(body != null){
//				model.setInstance(new SerialBlob(body));
//			}
			model.setInstanceSize(service.getInstanceSize());
			model.setInstanceType(service.getInstanceType());

			return model;
		} catch(InvalidLanguageTagException e) {
			throw new ServiceManagerException(e, ServiceModelUtil.class);
		} catch(InvalidLanguagePathException e) {
			throw new ServiceManagerException(e, ServiceModelUtil.class);
//		} catch(SerialException e) {
//			throw new ServiceManagerException(e, ServiceModelUtil.class);
//		} catch(SQLException e) {
//			throw new ServiceManagerException(e, ServiceModelUtil.class);
		}
	}

	public static Service setAtomicServiceProperty(AtomicServiceModel model, Service service)
	throws ServiceManagerException {
		service.setGridId(model.getGridId());
		service.setServiceId(model.getServiceId());
		service.setServiceName(model.getServiceName());
		service.setServiceDescription(model.getServiceDescription());
		if(model.getServiceType() != null) {
			service.setServiceTypeDomainId(model.getServiceType().getDomainId());
			service.setServiceTypeId(model.getServiceType().getTypeId());
			service.setAttributes(getSupportedLanguageList(model, service.getAttributes()));
		}
		service.setActive(model.isActive());
		service.setCopyrightInfo(model.getCopyrightInfo());
		service.setInstanceSize(model.getInstanceSize());
		service.setInstanceType(model.getInstanceType());
		service.setInstance(new BlobImpl(model.getInstance()));
		service.setOwnerUserId(model.getOwnerUserId());
		service.setLicenseInfo(model.getLicenseInfo());
		service.setResourceId(model.getResourceId());
		service.setApproved(model.isApproved());
		service.setMembersOnly(model.isMembersOnly());
		service.setHowToGetMembershipInfo(model.getHowToGetMembershipInfo());
		service.setAllowedAppProvision(model.getAllowedAppProvision());
		service.setAllowedUse(model.getAllowedUsage());
		service.setFederatedUseAllowed(model.isFederatedUseAllowed());
		service.setContainerType(ServiceContainerType.valueOf(model.getContainerType()));
		// NOTICE: this try block is must place to last sequence.
		try {
			if(model.getSourceCodeUrl() != null && !model.getSourceCodeUrl().equals("")) {
				service.setWrapperSourceCodeUrl(new URL(model.getSourceCodeUrl()));
			}
		} catch(MalformedURLException e) {
			throw new ServiceManagerException(e);
		}
		return service;
	}

	/**
	 * 
	 * 
	 */
	public static CompositeServiceModel makeCompositeServiceModel(Service service)
	throws ServiceManagerException {
		return makeCompositeServiceModel(new CompositeServiceModel(), service);
	}
	
	/**
	 * 
	 * 
	 */
	public static JavaCompositeServiceModel makeJavaCompositeServiceModel(Service service)
	throws ServiceManagerException {
		JavaCompositeServiceModel model = new JavaCompositeServiceModel();
		if(service.getWrapperSourceCodeUrl() != null){
			model.setSourceCodeUrl(service.getWrapperSourceCodeUrl().toExternalForm());
		}
		return (JavaCompositeServiceModel)makeCompositeServiceModel(model, service);
	}

	/**
	 * 
	 * 
	 */
	public static CompositeServiceModel makeCompositeServiceModel(
		CompositeServiceModel model, Service service)
	throws ServiceManagerException {
		try {
			model.setServiceId(service.getServiceId());
			model.setServiceName(service.getServiceName());
			if(service.getServiceTypeId() == null || service.getServiceTypeId().equals("")) {
				model.setServiceType(makeOtherServiceTypeModel());
			} else {
				try {
					model.setServiceType(makeServiceTypeModel(
						new ServiceTypeLogic().getServiceType(
							service.getServiceTypeDomainId(), service.getServiceTypeId())));
				} catch(DaoException e) {
					model.setServiceType(makeOtherServiceTypeModel());
					LogWriter.writeWarn("service", "Service type not found.", ServiceModelUtil.class);
				} catch(ObjectNotFoundException e) {
					model.setServiceType(makeOtherServiceTypeModel());
					LogWriter.writeWarn("service", "Service type not found.", ServiceModelUtil.class);
				}
			}
			
			model.setInstanceSize(service.getInstanceSize());
			model.setInstanceType(service.getInstanceType());

			model.setServiceDescription(service.getServiceDescription());
			model.setCopyrightInfo(service.getCopyrightInfo());
			model.setCreatedDateTime(service.getCreatedDateTime());
			model.setUpdatedDateTime(service.getUpdatedDateTime());
			model.setGridId(service.getGridId());
			model.setLicenseInfo(service.getLicenseInfo());
			model.setOwnerUserId(service.getOwnerUserId());
			model.setActive(service.isActive());
			model.setApproved(service.isApproved());
			model.setAllowedAppProvision(service.getAllowedAppProvision());
			model.setAllowedUsage(service.getAllowedUse());
			model.setFederatedUseAllowed(service.isFederatedUseAllowed());
			model.setContainerType(service.getContainerType() == null
				? ServiceContainerType.COMPOSITE.name() : service.getContainerType().name());
			Collection<ServiceMetaAttributeModel> collection = model.getServiceType().getMetaAttrbuteList();
			if(service.getServiceTypeId() != null
				&& service.getServiceTypeDomainId() != null
				&& (collection.size() == 0))
//				&& !isDefinedType(service.getServiceTypeId()))
			{
				collection.add(makeInternalMetaAttributeModel(
					service.getServiceTypeDomainId(), service.getServiceTypeId()));
			}
			model.setSupportedLanguagePathModel(
				makeLanguagePathModel(collection, service.getAttributes()));
			model.setMembersOnly(service.isMembersOnly());
			model.setHowToGetMembershipInfo(service.getHowToGetMembershipInfo());
			
			for(Invocation i : service.getInvocations()){
				model.getInvocations().add(makeInvocationModel(i));
			}
		} catch(InvalidLanguageTagException e) {
			throw new ServiceManagerException(e, ServiceModelUtil.class);
		} catch(InvalidLanguagePathException e) {
			throw new ServiceManagerException(e, ServiceModelUtil.class);
		}

		// NOTICE: this try block is must place to last sequence.
//		try{
//			byte[] body = ServiceFactory.getInstance().getCompositeServiceService(service.getGridId())
//					.getInstanceBody(service.getServiceId());
//			if(body != null){
//				model.setInstance(new SerialBlob(body));
//			}
//		} catch(SerialException e) {
//			throw new ServiceManagerException(e, ServiceModelUtil.class);
//		} catch(SQLException e) {
//			throw new ServiceManagerException(e, ServiceModelUtil.class);
//		}
		return model;

	}
	
	public static Service setJavaCompositeServiceProperty(CompositeServiceModel model, Service service)
	throws ServiceManagerException
	{
		try {
			JavaCompositeServiceModel jmodel = (JavaCompositeServiceModel)model;
			String wurl = jmodel.getSourceCodeUrl();
			if(wurl != null && wurl.length() > 0){
				service.setWrapperSourceCodeUrl(new URL(wurl));
			}
			return setCompositeServiceProperty(model, service);
		} catch(MalformedURLException e) {
			throw new ServiceManagerException(e, ServiceModelUtil.class);
		}
	}
	
	public static Service setCompositeServiceProperty(CompositeServiceModel model, Service service)
	throws ServiceManagerException
	{
		service.setGridId(model.getGridId());
		service.setServiceId(model.getServiceId());
		service.setServiceName(model.getServiceName());
		service.setServiceDescription(model.getServiceDescription());
		if(model.getServiceType() != null) {
			service.setServiceTypeDomainId(model.getServiceType().getDomainId());
			service.setServiceTypeId(model.getServiceType().getTypeId());
			service.setAttributes(getSupportedLanguageList(model, service.getAttributes()));
		}
		service.setActive(model.isActive());
		service.setCopyrightInfo(model.getCopyrightInfo());
		service.setInstance(new BlobImpl(model.getInstance()));
		service.setInstanceSize(model.getInstanceSize());
		service.setInstanceType(model.getInstanceType());
		service.setOwnerUserId(model.getOwnerUserId());
		service.setLicenseInfo(model.getLicenseInfo());
		service.setApproved(model.isApproved());
		service.setMembersOnly(model.isMembersOnly());
		service.setHowToGetMembershipInfo(model.getHowToGetMembershipInfo());
		service.setAllowedAppProvision(model.getAllowedAppProvision());
		service.setAllowedUse(model.getAllowedUsage());
		service.setFederatedUseAllowed(model.isFederatedUseAllowed());
		service.setContainerType(ServiceContainerType.valueOf(model.getContainerType()));
			
		service.setInvocations(setInvocationList(model.getInvocations(), service.getInvocations()));
	
		Set<Invocation> set = new HashSet<Invocation>();
		for(InvocationModel im : model.getInvocations()){
			Boolean found = false;
			for ( Invocation sm : service.getInvocations()) {
				if ( sm.getInvocationName().equals(im.getInvocationName() ) &&
					 sm.getOwnerServiceGridId().equals(im.getOwnerServiceGridId()) &&
					 sm.getOwnerServiceId().equals(im.getOwnerServiceId()) ) {
					set.add(setInvocation(im, sm));				
					found = true;
					break;
				}
			}
			if ( !found ) {
				set.add(setInvocation(im, new Invocation()));
			}
		}
		service.setInvocations(set);

		return service;
	}

	/**
	 * 
	 * 
	 */
	public static ServiceModel makeLangridServiceModel(Service service)
	throws ServiceManagerException {
		try {
			ServiceModel model = new ServiceModel();
			model.setServiceId(service.getServiceId());
			model.setServiceName(service.getServiceName());
			if(service.getServiceTypeId() == null) {
				model.setServiceType(makeOtherServiceTypeModel());
			} else {
//				try{
					ServiceTypeModel stm = ServiceFactory.getInstance().getServiceTypeService(service.getGridId()).get(
						service.getServiceTypeDomainId(), service.getServiceTypeId()
					);
					model.setServiceType(stm != null ? stm : makeOtherServiceTypeModel());
//					model.setServiceType(makeServiceTypeModel(
//						new ServiceTypeLogic().getServiceType(
//							service.getServiceTypeDomainId(), service.getServiceTypeId())));
//				}catch(ServiceTypeNotFoundException e){
//					model.setServiceType(makeOtherServiceTypeModel());
//				}
			}
			model.setServiceDescription(service.getServiceDescription());
			model.setCopyrightInfo(service.getCopyrightInfo());
			model.setCreatedDateTime(service.getCreatedDateTime());
			model.setUpdatedDateTime(service.getUpdatedDateTime());
			model.setGridId(service.getGridId());
			model.setContainerType(service.getContainerType() == null
				? "" : service.getContainerType().name());
			model.setLicenseInfo(service.getLicenseInfo());
			model.setOwnerUserId(service.getOwnerUserId());
			model.setSupportedLanguagePathModel(
				makeLanguagePathModel(model.getServiceType().getMetaAttrbuteList()
					, service.getAttributes()));
			model.setActive(service.isActive());
			model.setApproved(service.isApproved());
			model.setAllowedAppProvision(service.getAllowedAppProvision());
			model.setAllowedUsage(service.getAllowedUse());
			model.setMembersOnly(service.isMembersOnly());
			model.setHowToGetMembershipInfo(service.getHowToGetMembershipInfo());
			model.setFederatedUseAllowed(service.isFederatedUseAllowed());

			// NOTICE: this try block is must place to last sequence.
//			LangridServiceService<ServiceModel> modelService = ServiceFactory.getInstance().getLangridServiceService(
//				service.getGridId());
//			byte[] body = modelService.getInstanceBody(service.getServiceId());
//			if(body != null){
//				model.setInstance(new SerialBlob(body));
//			}
//			model.setInstanceSize(service.getInstanceSize());
//			model.setInstanceType(service.getInstanceType().name());

			model.setInstanceSize(service.getInstanceSize());
			model.setInstanceType(service.getInstanceType());
			
			return model;
		} catch(InvalidLanguageTagException e) {
			throw new ServiceManagerException(e, ServiceModelUtil.class);
		} catch(InvalidLanguagePathException e) {
			throw new ServiceManagerException(e, ServiceModelUtil.class);
//		} catch(SerialException e) {
//			throw new ServiceManagerException(e, ServiceModelUtil.class);
//		} catch(SQLException e) {
//			throw new ServiceManagerException(e, ServiceModelUtil.class);
		}
	}

	public static Service setLangridServiceProperty(ServiceModel model, Service service)
	throws ServiceManagerException {
		return service;
	}

	public static ServiceTypeModel makeServiceTypeModel(ServiceType entity) throws ServiceManagerException {
		ServiceTypeModel model = new ServiceTypeModel();
		model.setDomainId(entity.getDomainId());
		model.setTypeId(entity.getServiceTypeId());
		model.setTypeName(entity.getServiceTypeName());
		model.setDescription(entity.getDescription());
		Collection<ServiceMetaAttribute> list = entity.getMetaAttributes().values();
		Set<String> idSet = new HashSet<String>();
		for(ServiceMetaAttribute attr : list) {
			idSet.add(attr.getAttributeId());
		}
		model.setTypeSet(LanguagePathUtil.getPathTypeSet(entity.getServiceTypeId(), idSet));
		List<InterfaceDefinitionModel> interfaceList = new ArrayList<InterfaceDefinitionModel>();
		for(ServiceInterfaceDefinition sid : entity.getInterfaceDefinitions().values()) {
			interfaceList.add(makeInterfaceModel(sid));
		}
		model.setInterfaceList(interfaceList);
		List<ServiceMetaAttributeModel> attrList = new ArrayList<ServiceMetaAttributeModel>();
		for(ServiceMetaAttribute sma : entity.getMetaAttributes().values()) {
			attrList.add(makeMetaAttributeModel(sma));
		}
		model.setMetaAttrbuteList(attrList);
		return model;
	}

	public static ServiceType setServiceTypeProperty(ServiceTypeModel model,
		ServiceType entity) {
		entity.setDescription(model.getDescription());
		entity.setDomainId(model.getDomainId());
		entity.setServiceTypeId(model.getTypeId());
		entity.setServiceTypeName(model.getTypeName());
		List<ServiceInterfaceDefinition> interfaceList = new ArrayList<ServiceInterfaceDefinition>();
		for(InterfaceDefinitionModel m : model.getInterfaceList()) {
			ServiceInterfaceDefinition sid = new ServiceInterfaceDefinition();
			interfaceList.add(setInterfaceDefinition(m, sid, entity));
		}
		entity.setInterfaceDefinitionCollection(interfaceList);
		if(model.getMetaAttrbuteList() != null && model.getMetaAttrbuteList().size() != 0) {
			List<ServiceMetaAttribute> attrList = new ArrayList<ServiceMetaAttribute>();
			for(ServiceMetaAttributeModel m : model.getMetaAttrbuteList()) {
				ServiceMetaAttribute sma = new ServiceMetaAttribute();
				attrList.add(setMetaAttribute(m, sma));
			}
			entity.setMetaAttributeCollection(attrList);
		}
		return entity;
	}

	public static ServiceTypeModel makeOtherServiceTypeModel() {
		ServiceTypeModel model = new ServiceTypeModel();
		model.setDomainId("Other");
		model.setTypeId("Other");
		model.setTypeName("Other");
		model.setDescription("Other");
		List<ServiceMetaAttributeModel> metaList = new ArrayList<ServiceMetaAttributeModel>();
		metaList.add(makeOtherServiceMetaAttributeModel("Other", "Other"));
		model.setMetaAttrbuteList(metaList);
		Set<LanguagePathType> set = new LinkedHashSet<LanguagePathType>();
		set.add(LanguagePathType.UNKNOWN);
		model.setTypeSet(set);
		return model;
	}

	public static ServiceMetaAttributeModel makeOtherServiceMetaAttributeModel(
		String typeDomainId, String typeId) {
		ServiceMetaAttributeModel model = new ServiceMetaAttributeModel();
		model.setAttributeId(typeDomainId + "_" + typeId + "_SupportedLanguages");
		model.setAttributeName(typeDomainId + "_" + typeId + "_SupportedLanguages");
		model.setDescription("");
		model.setDomainId(typeDomainId);
		return model;
	}

	public static ServiceEndpointModel makeEndpointModel(ServiceEndpoint endpoint) {
		ServiceEndpointModel sem = new ServiceEndpointModel();
		sem.setAuthPassword(endpoint.getAuthPassword());
		sem.setAuthUserName(endpoint.getAuthUserName());
		sem.setEnabled(endpoint.isEnabled());
		sem.setGridId(endpoint.getGridId());
		sem.setServiceId(endpoint.getServiceId());
		sem.setUrl(endpoint.getUrl().toString());
		sem.setProtocolId(endpoint.getProtocolId());
		sem.setAverage(endpoint.getAveResponseMillis());
		sem.setExperience(endpoint.getExperience());
		return sem;
	}

	public static ServiceEndpoint setEndpointProperty(
		ServiceEndpointModel model, ServiceEndpoint endpoint)
	throws ServiceManagerException {
		try {
			endpoint.setAuthPassword(model.getAuthPassword());
			endpoint.setAuthUserName(model.getAuthUserName());
			endpoint.setEnabled(model.isEnabled());
			endpoint.setGridId(model.getGridId());
			endpoint.setServiceId(model.getServiceId());
			endpoint.setUrl(new URL(model.getUrl()));
			endpoint.setProtocolId(model.getProtocolId());
			
			endpoint.setAveResponseMillis(model.getAverage());
			endpoint.setExperience(model.getExperience());
		} catch(MalformedURLException e) {
			throw new ServiceManagerException(e);
		}
		return endpoint;
	}

	public static IndividualExecutionInformationModel makeLogModel(AccessLog entity) {
		IndividualExecutionInformationModel model = new IndividualExecutionInformationModel();
		model.setAddress(entity.getAddress());
		model.setAgent(entity.getAgent());
		model.setCallNest(entity.getCallNest());
		model.setCallTree(entity.getCallTree());
		model.setDateTime(entity.getDateTime());
		model.setFaultCode(entity.getFaultCode());
		model.setFaultString(entity.getFaultString());
		model.setHost(entity.getHost());
		model.setNodeId(entity.getNodeId());
		model.setReferer(entity.getReferer());
		model.setRequestUri(entity.getRequestUri());
		model.setResponseBytes(entity.getResponseBytes());
		model.setResponseCode(entity.getResponseCode());
		model.setServiceId(entity.getServiceId());
		model.setServiceGridId(entity.getServiceAndNodeGridId());
		model.setUserGridId(entity.getUserGridId());
		model.setUserId(entity.getUserId());
		model.setResponseTimeMillis(entity.getResponseMillis());
		model.setProtocol(entity.getProtocolId());
		model.setRequestBytes(entity.getRequestBytes());
		return model;
	}

	public static ExecutionInformationStatisticsModel makeStatModel(
		AccessRankingEntry entry) {
		ExecutionInformationStatisticsModel model = new ExecutionInformationStatisticsModel();
		model.setAccessCount(entry.getAccessCount());
		model.setServiceName(entry.getServiceName());
		model.setResponseBytes(entry.getResponseBytes());
		model.setUserOrganization(entry.getUserOrganization());
		model.setUserId(entry.getUserId());
		model.setServiceId(entry.getServiceId());
		model.setServiceGridId(entry.getServiceGridId());
		model.setUserGridId(entry.getUserGridId());
		return model;
	}

	public static ServiceMetaAttributeModel makeMetaAttributeModel(
		ServiceMetaAttribute entity) {
		ServiceMetaAttributeModel model = new ServiceMetaAttributeModel();
		model.setDomainId(entity.getDomainId());
		model.setAttributeId(entity.getAttributeId());
		model.setAttributeName(entity.getAttributeName());
		model.setDescription(entity.getDescription());
		return model;
	}

	public static ServiceMetaAttributeModel makeInternalMetaAttributeModel(
		String domainId, String typeId) {
		ServiceMetaAttributeModel model = new ServiceMetaAttributeModel();
		model.setDomainId(domainId);
		model.setAttributeId(domainId + ":" + typeId + ":SupportedLanguages");
		model.setAttributeName("Supported Languages");
		return model;
	}

	public static ServiceMetaAttribute setMetaAttribute(
		ServiceMetaAttributeModel model, ServiceMetaAttribute entity) {
		entity.setAttributeId(model.getAttributeId());
		entity.setAttributeName(model.getAttributeName());
		entity.setDescription(model.getDescription());
		entity.setDomainId(model.getDomainId());
		return entity;
	}
	
	/**
	 * 
	 * 
	 */
	public static Set<Invocation> setInvocationList(List<InvocationModel> list, Set<Invocation> set)
	throws ServiceManagerException
	{
		Set<Invocation> newSet = new HashSet<Invocation>();
		// edit and delete
		// -edit: if same properties model found in list.
		// -delete: if same properties model is not found in list.
		for(Invocation i : set){
			for(InvocationModel im : list){
				if(i.getInvocationName().equals(im.getInvocationName())
					&& i.getOwnerServiceGridId().equals(im.getOwnerServiceGridId())
					&& i.getOwnerServiceGridId().equals(im.getOwnerServiceGridId()))
				{
					newSet.add(setInvocation(im, i));
					break;
				}
			}
		}
		
		// add
		boolean isAdd = true;
		for(InvocationModel im : list) {
			for(Invocation i : set) {
				if(i.getInvocationName().equals(im.getInvocationName())
					&& i.getOwnerServiceGridId().equals(im.getOwnerServiceGridId())
					&& i.getOwnerServiceGridId().equals(im.getOwnerServiceGridId()))
				{
					isAdd = false;
				}
			}
			if(isAdd) {
				newSet.add(setInvocation(im, new Invocation()));
				isAdd = true;
			}
		}
		return newSet;
	}
	
	/**
	 * 
	 * 
	 */
	public static InvocationModel makeInvocationModel(Invocation entity) throws ServiceManagerException {
		InvocationModel model = new InvocationModel();
		model.setOwnerServiceGridId(entity.getOwnerServiceGridId());
		model.setOwnerServiceId(entity.getOwnerServiceId());
		model.setInvocationName(entity.getInvocationName());
		model.setServiceGridId(entity.getServiceGridId());
		model.setServiceId(entity.getServiceId());
		model.setServiceName(entity.getServiceName());
		model.setServiceTypeId(entity.getServiceTypeId());
		model.setDomainId(entity.getDomainId());
		return model;
	}
	
	/**
	 * 
	 * 
	 */
	public static Invocation setInvocation(InvocationModel model, Invocation entity) throws ServiceManagerException {
		entity.setOwnerServiceId(model.getOwnerServiceId());
		entity.setOwnerServiceGridId(model.getOwnerServiceGridId());
		entity.setInvocationName(model.getInvocationName());
		entity.setServiceGridId(model.getServiceGridId());
		entity.setServiceId(model.getServiceId());
		entity.setServiceName(model.getServiceName());
		entity.setServiceTypeId(model.getServiceTypeId());
		entity.setDomainId(model.getDomainId());
		return entity;
	}

	private static InterfaceDefinitionModel makeInterfaceModel(
		ServiceInterfaceDefinition entity) throws ServiceManagerException{
		InterfaceDefinitionModel model = new InterfaceDefinitionModel();
		model.setProtocolId(entity.getProtocolId());
		try {
			byte[] b = IOUtils.toByteArray(entity.getDefinition().getBinaryStream());
			model.setDefinition(new SerialBlob(b));
		} catch(IOException e) {
			throw new ServiceManagerException(e);
		} catch(SQLException e) {
			throw new ServiceManagerException(e);
		}
		return model;
	}

	private static ServiceInterfaceDefinition setInterfaceDefinition(
		InterfaceDefinitionModel model, ServiceInterfaceDefinition entity,
		ServiceType typeEntity)
	{
		entity.setDefinition(model.getDefinition());
		entity.setProtocolId(model.getProtocolId());
		entity.setServiceType(typeEntity);
		return entity;
	}

	/**
	 * 
	 * 
	 */
	private static LanguagePathModel makeLanguagePathModel(
		Collection<ServiceMetaAttributeModel> metaAttributeList
		, Collection<ServiceAttribute> attributeList)
	throws InvalidLanguageTagException, InvalidLanguagePathException {
		LanguagePathModel pathModel = new LanguagePathModel();
		for(ServiceAttribute sa : attributeList) {
			for(ServiceMetaAttributeModel smam : metaAttributeList) {
				if(sa.getName().equals(smam.getAttributeId())) {
					if(MetaAttribute.containName(sa.getName().toUpperCase())) {
						pathModel.replacePath(sa.getName()
							, jp.go.nict.langrid.language.util.LanguagePathUtil
								.decodeLanguagePathArray(sa.getValue()));
					} else {
						pathModel.setOtherLanguage(sa.getName(), sa.getValue());
					}
					break;
				}
			}
		}
		return pathModel;
	}

	private static Collection<ServiceAttribute> getSupportedLanguageList(
		ServiceModel model, Collection<ServiceAttribute> originalList)
	{
		List<ServiceAttribute> attrList = new ArrayList<ServiceAttribute>();
		boolean isAdd = true;
		for(ServiceAttribute sa : originalList) {
			for(String key : model.getSupportedLanguagePathModel().getKeySet()) {
				if(key.equals(sa.getName())) {
					LanguagePath[] paths = model.getSupportedLanguagePathModel().getPath(key);
					if(paths == null || paths.length == 0) {
						String path = model.getSupportedLanguagePathModel().getOtherLanguage(key);
						if(path == null || path.equals("")) {
							isAdd = false;
						} else {
							sa.setValue(path);
						}
					} else {
						sa.setValue(jp.go.nict.langrid.language.util.LanguagePathUtil
							.encodeLanguagePathArray(paths));
					}
					break;
				}
			}
			if(isAdd) {
				attrList.add(sa);
			}
			isAdd = true;
		}
		boolean isBreak = false;
		for(String key : model.getSupportedLanguagePathModel().getAllKeySet()) {
			for(ServiceAttribute ra : originalList) {
				if(key.equals(ra.getName())) {
					isBreak = true;
					break;
				}
			}
			if(!isBreak) {
				ServiceAttribute sa = new ServiceAttribute();
				sa.setGridId(model.getGridId());
				sa.setServiceId(model.getServiceId());
				sa.setName(key);
				LanguagePath[] paths = model.getSupportedLanguagePathModel().getPath(key);
				if(paths != null && paths.length != 0) {
					sa.setValue(jp.go.nict.langrid.language.util.LanguagePathUtil
						.encodeLanguagePathArray(paths));
				} else {
					String path = model.getSupportedLanguagePathModel().getOtherLanguage(
						key);
					if(path != null && !path.equals("")) {
						sa.setValue(path);
					}
				}
				attrList.add(sa);
			}
			isBreak = false;
		}
		return attrList;
	}
}
