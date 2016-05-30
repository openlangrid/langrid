/*
 * $Id: ConvertUtil.java 401 2011-08-25 01:11:16Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.p2pgridbasis.data.langrid.converter;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.GridAttribute;
import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.Invocation;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.NodeAttribute;
import jp.go.nict.langrid.dao.entity.OldResourceType;
import jp.go.nict.langrid.dao.entity.OldServiceType;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourceAttribute;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceAttribute;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;
import jp.go.nict.langrid.dao.entity.ServiceDeployment;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.entity.ServiceInterfaceDefinition;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.entity.UserAttribute;
import jp.go.nict.langrid.language.CountryName;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.p2pgridbasis.data.DataAttributes;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;

import org.apache.axis.encoding.Base64;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class ConvertUtil {
	static public DataAttributes encode(Object data) throws DataConvertException{
		setLangridConverter();
		logger.debug("##### encode #####");
		try {
			DataAttributes attr = new DataAttributes();
			for (PropertyDescriptor descriptor : PropertyUtils.getPropertyDescriptors(data)) {
				if (PropertyUtils.isReadable(data, descriptor.getName())) {
					if(descriptor.getName().equalsIgnoreCase("supportedLanguages")){
						//supportedLanguages
						attr.setAttribute(descriptor.getName(),
								(String)converter.lookup(descriptor.getPropertyType()).convert(String.class,
										PropertyUtils.getProperty(data, descriptor.getName())));
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("instance")){
						// 
						// 
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("wsdl")){
						// 
						// 
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("interfaceDefinitions")){
						// 
						// 
						ServiceType type = (ServiceType)data;
						Map<String, ServiceInterfaceDefinition> map = new HashMap<String, ServiceInterfaceDefinition>();
						map = type.getInterfaceDefinitions();
						String str = "";

						try {
							for(ServiceInterfaceDefinition s : map.values()){
								str = str + "ProtocolId=" + s.getProtocolId() + "\n";
								str = str + "Definition=" + Base64.encode(StreamUtil.readAsBytes(s.getDefinition().getBinaryStream())) + "\n";
								str = str + "###ServiceInterfaceDefinition###\n";
							}
						} catch (IOException e) {
							e.printStackTrace();
						} catch (SQLException e) {
							e.printStackTrace();
						}

						attr.setAttribute("interfaceDefinition_list", str);
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("allowedAppProvision")){
						Service s = (Service)data;
						String value = "";
						for(String str : s.getAllowedAppProvision()){
							value = value + str + "\n";
						}
						attr.setAttribute("allowedAppProvision", value);
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("allowedUse")){
						Service s = (Service)data;
						String value = "";
						for(String str : s.getAllowedUse()){
							value = value + str + "\n";
						}
						attr.setAttribute("allowedUse", value);
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("supportedDomains")){
						Grid g = (Grid)data;
						List<Domain> list = g.getSupportedDomains();
						String value = "";
						for(Domain d:list){
							value = value + d.getDomainId() + "\n";
						}
						attr.setAttribute("supportedDomain_list", value);
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("partnerServiceNamespaceURIs")){
						//partnerServiceNamespaceURIs
						BPELService s = (BPELService)data;
						List<String> list = s.getPartnerServiceNamespaceURIs();
						String value = "";
						for(String str:list){
							value = value + str + "\n";
						}
						attr.setAttribute("partnerServiceNamespaceURI_list", value);
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("serviceDeployments")){
						//ServiceDeployment
						Service s = (Service)data;
						String str = "";
						for(ServiceDeployment sd : s.getServiceDeployments()){
							str = str + "GridId="      + sd.getGridId()                 + "\n";
							str = str + "ServiceId="   + sd.getServiceId()              + "\n";
							str = str + "NodeId="      + sd.getNodeId()                 + "\n";
							str = str + "ServicePath=" + sd.getServicePath()            + "\n";
							str = str + "Enabled="     + String.valueOf(sd.isEnabled()) + "\n";
							str = str + "CreateTime="  + String.valueOf(sd.getCreatedDateTime().getTimeInMillis()) + "\n";
							str = str + "UpdateTime="  + String.valueOf(sd.getUpdatedDateTime().getTimeInMillis()) + "\n";
							if(sd.getDisabledByErrorDate() != null){
								str = str + "ErrorDate=" + String.valueOf(sd.getDisabledByErrorDate().getTimeInMillis()) + "\n";
							}
							if(sd.getDeployedDateTime() != null){
								str = str + "DeployedTime=" + String.valueOf(sd.getDeployedDateTime().getTimeInMillis()) + "\n";
							}
							str = str + "###ServiceDeployment###\n";
						}
						attr.setAttribute("deployment_list", str);
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("serviceEndpoints")){
						//ServiceEndpoint
						StringBuilder str = new StringBuilder();
						Service s = (Service)data;
						for(ServiceEndpoint se : s.getServiceEndpoints()){
							str.append("GridId="        + se.getGridId()                 + "\n");
							str.append("ProtocolId="    + se.getProtocolId()             + "\n");
							str.append("ServiceId="     + se.getServiceId()              + "\n");
							str.append("Enabled="       + String.valueOf(se.isEnabled()) + "\n");
							str.append("Url="           + se.getUrl().toString()         + "\n");
							str.append("AuthUserName="  + se.getAuthUserName()           + "\n");
							str.append("AuthPassword="  + se.getAuthPassword()           + "\n");
							str.append("DisableReason=" + se.getDisableReason()          + "\n");
							str.append("CreateTime="    + String.valueOf(se.getCreatedDateTime().getTimeInMillis()) + "\n");
							str.append("UpdateTime="    + String.valueOf(se.getUpdatedDateTime().getTimeInMillis()) + "\n");
							if(se.getDisabledByErrorDate() != null){
								str.append("ErrorDate=" + String.valueOf(se.getDisabledByErrorDate().getTimeInMillis()) + "\n");
							}
							str.append("###ServiceEndpoint###\n");
						}
						attr.setAttribute("endpoint_list", str.toString());
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("invocations")){
						//Invocation
						String str = "";
						Service s = (Service)data;
						for(Invocation in : s.getInvocations()){
							str = str + "InvocationName="     + in.getInvocationName()     + "\n";
							str = str + "OwnerServiceGridId=" + in.getOwnerServiceGridId() + "\n";
							str = str + "OwnerServiceId="     + in.getOwnerServiceId()     + "\n";
							str = str + "ServiceGridId="      + in.getServiceGridId()      + "\n";
							str = str + "ServiceId="          + in.getServiceId()          + "\n";
							str = str + "ServiceName="        + in.getServiceName()        + "\n";
							str = str + "CreateTime="         + String.valueOf(in.getCreatedDateTime().getTimeInMillis()) + "\n";
							str = str + "UpdateTime="         + String.valueOf(in.getUpdatedDateTime().getTimeInMillis()) + "\n";
							str = str + "###Invocation###\n";
						}
						attr.setAttribute("invocations_list", str);
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("metaAttributes")){
						//metaAttributes
						String str = "";
						if(data.getClass().getName().endsWith("ResourceType")){
							ResourceType r = (ResourceType)data;
							for(ResourceMetaAttribute a : r.getMetaAttributes().values()){
								str = str + "DomainId="    + a.getDomainId() + "\n";
								str = str + "AttributeId=" + a.getAttributeId() + "\n";
								str = str + "###MetaAttribute###\n";
							}
						}else if(data.getClass().getName().endsWith("ServiceType")){
							ServiceType s = (ServiceType)data;
							for(ServiceMetaAttribute a : s.getMetaAttributes().values()){
								str = str + "DomainId="    + a.getDomainId() + "\n";
								str = str + "AttributeId=" + a.getAttributeId() + "\n";
								str = str + "###MetaAttribute###\n";
							}
						}else{
							logger.info("metaAttributes : " + data.getClass().getName());
						}
						attr.setAttribute("metaAttribute_list", str);
						continue;
					}else if(descriptor.getName().equalsIgnoreCase("attributes")){
						//attribute
						String str = "";
						if(data.getClass().getName().endsWith("User")){
							User u = (User)data;
							for(UserAttribute a : u.getAttributes()){
								str = str + "attribute_GridId="     + a.getGridId() + "\n";
								str = str + "attribute_Id="         + a.getUserId() + "\n";
								str = str + "attribute_Name="       + a.getName()   + "\n";
								str = str + "attribute_Value="      + a.getValue()  + "\n";
								str = str + "attribute_CreateTime=" + String.valueOf(a.getCreatedDateTime().getTimeInMillis()) + "\n";
								str = str + "attribute_UpdateTime=" + String.valueOf(a.getUpdatedDateTime().getTimeInMillis()) + "\n";
								str = str + "###Attribute###\n";
							}
						}else if(data.getClass().getName().endsWith("Service")){
							Service s = (Service)data;
							for(ServiceAttribute a : s.getAttributes()){
								str = str + "attribute_GridId="     + a.getGridId()    + "\n";
								str = str + "attribute_Id="         + a.getServiceId() + "\n";
								str = str + "attribute_Name="       + a.getName()      + "\n";
								str = str + "attribute_Value="      + a.getValue()     + "\n";
								str = str + "attribute_CreateTime=" + String.valueOf(a.getCreatedDateTime().getTimeInMillis()) + "\n";
								str = str + "attribute_UpdateTime=" + String.valueOf(a.getUpdatedDateTime().getTimeInMillis()) + "\n";
								str = str + "###Attribute###\n";
							}
						}else if(data.getClass().getName().endsWith("Node")){
							Node s = (Node)data;
							for(NodeAttribute a : s.getAttributes()){
								str = str + "attribute_GridId="     + a.getGridId() + "\n";
								str = str + "attribute_Id="         + a.getNodeId() + "\n";
								str = str + "attribute_Name="       + a.getName()   + "\n";
								str = str + "attribute_Value="      + a.getValue()  + "\n";
								str = str + "attribute_CreateTime=" + String.valueOf(a.getCreatedDateTime().getTimeInMillis()) + "\n";
								str = str + "attribute_UpdateTime=" + String.valueOf(a.getUpdatedDateTime().getTimeInMillis()) + "\n";
								str = str + "###Attribute###\n";
							}
						}else if(data.getClass().getName().endsWith("Grid")){
							Grid s = (Grid)data;
							for(GridAttribute a : s.getAttributes()){
								str = str + "attribute_GridId="     + a.getGridId() + "\n";
								str = str + "attribute_Name="       + a.getName()   + "\n";
								str = str + "attribute_Value="      + a.getValue()  + "\n";
								str = str + "attribute_CreateTime=" + String.valueOf(a.getCreatedDateTime().getTimeInMillis()) + "\n";
								str = str + "attribute_UpdateTime=" + String.valueOf(a.getUpdatedDateTime().getTimeInMillis()) + "\n";
								str = str + "###Attribute###\n";
							}
						}else if(data.getClass().getName().endsWith("Resource")){
							Resource s = (Resource)data;
							for(ResourceAttribute a : s.getAttributes()){
								str = str + "attribute_GridId="     + a.getGridId() + "\n";
								str = str + "attribute_Id="         + a.getResourceId() + "\n";
								str = str + "attribute_Name="       + a.getName()   + "\n";
								str = str + "attribute_Value="      + a.getValue()  + "\n";
								str = str + "attribute_CreateTime=" + String.valueOf(a.getCreatedDateTime().getTimeInMillis()) + "\n";
								str = str + "attribute_UpdateTime=" + String.valueOf(a.getUpdatedDateTime().getTimeInMillis()) + "\n";
								str = str + "###Attribute###\n";
							}
						}
						attr.setAttribute("attribute_list", str);
						continue;
					} else if(
							data instanceof Service
							&& (descriptor.getName().equals("alternateServiceId")
									|| descriptor.getName().equals("useAlternateServices")
									)
							){
						// 
						// 
						continue;
					}

					//Read OK
					if(data instanceof BPELService && descriptor.getName().equals("transferExecution")){
						// ignore
					} else{
						attr.setAttribute(descriptor.getName(),
								BeanUtils.getProperty(data, descriptor.getName()));
					}
				}
				else if (descriptor.getPropertyType().isArray()) {
					logger.debug("name : " + descriptor.getName() + " isArray");
					// 
					// 
					attr.setAttribute(descriptor.getName(),
							(String)converter.lookup(descriptor.getPropertyType()).convert(String.class,
									PropertyUtils.getProperty(data, descriptor.getName())));
				} else {
					logger.debug("Name : " + descriptor.getName());
					for(Method m:data.getClass().getMethods()){
						if(m.getName().equalsIgnoreCase("get" + descriptor.getName())
						|| m.getName().equalsIgnoreCase("is" + descriptor.getName())){
							if(m.getParameterTypes().length != 0){
								// 
								// 
								logger.debug("class : " + data.getClass().getName());
								logger.debug("引数あり:Skip");
								break;
							}
							else{
								// 
								// 
								logger.debug("value : " + m.invoke(data));
							}
							attr.setAttribute(descriptor.getName(), m.invoke(data).toString());
							break;
						}
					}
				}
			}
			return attr;
		} catch (InvocationTargetException e) {
			throw new DataConvertException(e);
		} catch (IllegalArgumentException e) {
			throw new DataConvertException(e);
		} catch (IllegalAccessException e) {
			throw new DataConvertException(e);
		} catch (NoSuchMethodException e) {
			throw new DataConvertException(e);
		}
	}

	public static void decode(DataAttributes from, Object to) throws DataConvertException {
		setLangridConverter();
		logger.debug("##### decode #####");
		try {
			for (PropertyDescriptor descriptor : PropertyUtils.getPropertyDescriptors(to)) {
				logger.debug("Name : " + descriptor.getName() + " / PropertyType : " + descriptor.getPropertyType());
				Object value = from.getValue(descriptor.getName());
				if (PropertyUtils.isWriteable(to, descriptor.getName()) && value != null) {
					//Write OK
					try {
						Converter converter = ConvertUtils.lookup(descriptor.getPropertyType());
						if(!ignoreProps.contains(descriptor.getName())){
							if(converter == null){
								logger.error("no converter is registered : " + descriptor.getName() + " " + descriptor.getPropertyType());
							} else {
								Object obj = converter.convert(descriptor.getPropertyType(), value);
								PropertyUtils.setProperty(to, descriptor.getName(), obj);
							}
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						logger.info(e.getMessage());
					} catch (NoSuchMethodException e) {
						logger.info(e.getMessage());
					}
				} else {
					if(value != null){
						//Write NG。
						logger.debug("isWriteable = false");
					}else{
						//Write NG。
						logger.debug("value = null");
					}
				}
			}
		} catch (IllegalAccessException e) {
			throw new DataConvertException(e);
		} catch (InvocationTargetException e) {
			throw new DataConvertException(e);
		}
	}

	@SuppressWarnings("serial")
	private static Set<String> ignoreProps = new HashSet<String>(){{
		add("allowedAppProvision");
		add("allowedUse");
		add("roles");
	}};

	static private void setLangridConverter() {
		if(ConvertUtils.lookup(Calendar.class) == null){
			logger.debug("### ConvertUtils.register ###");
			ConvertUtils.register(converter, String.class);
			ConvertUtils.register(new CalendarConverter()            , Calendar.class);
			ConvertUtils.register(new CalendarConverter()            , GregorianCalendar.class);
			ConvertUtils.register(new CountryConverter()             , CountryName.class);
			ConvertUtils.register(new InputStreamConverter()         , InputStream.class);
			ConvertUtils.register(new BlobConverter()         , Blob.class);
			ConvertUtils.register(new InstanceTypeConverter()        , InstanceType.class);
			ConvertUtils.register(new LanguageConverter()            , Language.class);
			ConvertUtils.register(new LanguagePathArrayConverter()   , LanguagePath[].class);
			ConvertUtils.register(new LimitTypeConverter()           , LimitType.class);
//			ConvertUtils.register(new NodeTypeConverter()            , NodeType.class);
			ConvertUtils.register(new PeriodConverter()              , Period.class);
			ConvertUtils.register(new ResourceTypeConverter()        , OldResourceType.class);
			ConvertUtils.register(new ServiceContainerTypeConverter(), ServiceContainerType.class);
			ConvertUtils.register(new ServiceTypeConverter()         , OldServiceType.class);
			ConvertUtils.register(new URIConverter()                 , URI.class);
			return;
		}
	}

	static private Logger logger = Logger.getLogger(ConvertUtil.class);
	static private LangridStringConverter converter;
	static {
		converter = new LangridStringConverter();
		converter.register(Calendar.class         , new CalendarConverter());
		converter.register(GregorianCalendar.class, new CalendarConverter());
		converter.register(Language.class         , new LanguageConverter());
		converter.register(LanguagePath[].class   , new LanguagePathArrayConverter());
		converter.register(InputStream.class      , new InputStreamConverter());
		converter.register(Blob.class             , new BlobConverter());
		converter.register(CountryName.class      , new CountryConverter());
		converter.register(LimitType.class        , new LimitTypeConverter());
		converter.register(Period.class           , new PeriodConverter());
		converter.register(ServiceContainerType.class, new ServiceContainerTypeConverter());
	}
}
