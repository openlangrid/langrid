/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.p2pgridbasis.data.langrid;


import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Invocation;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceAttribute;
import jp.go.nict.langrid.dao.entity.ServiceDeployment;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.entity.ServicePK;
import jp.go.nict.langrid.dao.entity.WebappService;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.DataAttributes;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.converter.ConvertUtil;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Masato Mori
 * @author Takao Nakaguchi
 */
public class ServiceData extends Data{
	public static final String _IDPrefix = "Service_";
	public static final String _dataType = "ServiceData";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return ServiceData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new ServiceData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param service
	 * @throws DataConvertException
	 */
	public ServiceData(Service service) throws DataConvertException{
		super(service.getGridId(), getDataID(service, null), null, null);
		// Set the updated datetime.
		setLastUpdate(service.getUpdatedDateTime() != null ? service.getUpdatedDateTime() : Data.DEFAULT_DATE);
		setAttributes(ConvertUtil.encode(service));
	}

	/**
	 * The constructor.
	 * @param gid Grid ID
	 * @param id Data ID
	 * @param lastUpdate Last updated datetime
	 * @param attributes Attribute
	 */
	public ServiceData(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		super(gid, id, lastUpdate, attributes);
	}

	/**
	 * Get the BPELService
	 * @return BPELService
	 * @throws ParseException
	 */
	public BPELService getBPELService() throws DataConvertException {
		BPELService service = new BPELService();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, service);

		//AllowedAppProvision
		Set<String> provs = getAllowedAppProvision(attr);
		if(provs != null){
			service.setAllowedAppProvision(provs);
		}

		//AllowedUse
		Set<String> uses = getAllowedUse(attr);
		if(uses != null){
			service.setAllowedUse(uses);
		}

		//ServiceEndpoint
		List<ServiceEndpoint> endPoint_list = getServiceEndpoint(attr);
		if(endPoint_list != null){
			for(ServiceEndpoint endPoint:endPoint_list)
			service.getServiceEndpoints().add(endPoint);
		}

		//ServiceDeployment
		List<ServiceDeployment> deployment_list = getServiceDeployment(attr);
		if(deployment_list != null){
			for(ServiceDeployment deployment:deployment_list)
			service.getServiceDeployments().add(deployment);
		}

		//PartnerServiceNamespaceURIs
		List<String> namespace_list = getPartnerServiceNamespaceURIs(attr);
		if(namespace_list != null){
			service.setPartnerServiceNamespaceURIs(namespace_list);
		}

		//Invocation
		List<Invocation> i_list = getInvocations(attr);
		if(i_list != null){
			service.getInvocations().addAll(i_list);
		}

		//Attribute
		List<ServiceAttribute> a_list = getServiceAttribute(attr);
		if(a_list != null){
			for(ServiceAttribute a:a_list)
			service.setAttribute(a);
		}

		return service;
	}

	/**
	 * Get the ExternalService
	 * @return ExternalService
	 * @throws ParseException
	 */
	public ExternalService getExternalService() throws DataConvertException {
		ExternalService service = new ExternalService();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, service);

		//AllowedAppProvision
		Set<String> provs = getAllowedAppProvision(attr);
		if(provs != null){
			service.setAllowedAppProvision(provs);
		}

		//AllowedUse
		Set<String> uses = getAllowedUse(attr);
		if(uses != null){
			service.setAllowedUse(uses);
		}

		//ServiceEndpoint
		List<ServiceEndpoint> endPoint_list = getServiceEndpoint(attr);
		if(endPoint_list != null){
			for(ServiceEndpoint endPoint:endPoint_list)
			service.getServiceEndpoints().add(endPoint);
		}

		//ServiceDeployment
		List<ServiceDeployment> deployment_list = getServiceDeployment(attr);
		if(deployment_list != null){
			for(ServiceDeployment deployment:deployment_list)
			service.getServiceDeployments().add(deployment);
		}

		//Invocation
		List<Invocation> i_list = getInvocations(attr);
		if(i_list != null){
			service.getInvocations().addAll(i_list);
		}

		//Attribute
		List<ServiceAttribute> a_list = getServiceAttribute(attr);
		if(a_list != null){
			for(ServiceAttribute a:a_list)
			service.setAttribute(a);
		}

		return service;
	}

	/**
	 * Get the WebappService.
	 * @return WebappService
	 * @throws ParseException
	 */
	public WebappService getWebappService() throws DataConvertException {
		WebappService service = new WebappService();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, service);


		//AllowedAppProvision
		Set<String> provs = getAllowedAppProvision(attr);
		if(provs != null){
			service.setAllowedAppProvision(provs);
		}

		//AllowedUse
		Set<String> uses = getAllowedUse(attr);
		if(uses != null){
			service.setAllowedUse(uses);
		}

		//ServiceEndpoint
		List<ServiceEndpoint> endPoint_list = getServiceEndpoint(attr);
		if(endPoint_list != null){
			for(ServiceEndpoint endPoint:endPoint_list)
			service.getServiceEndpoints().add(endPoint);
		}

		//ServiceDeployment
		List<ServiceDeployment> deployment_list = getServiceDeployment(attr);
		if(deployment_list != null){
			for(ServiceDeployment deployment:deployment_list)
			service.getServiceDeployments().add(deployment);
		}

		//Attribute
		List<ServiceAttribute> a_list = getServiceAttribute(attr);
		if(a_list != null){
			for(ServiceAttribute a:a_list)
			service.setAttribute(a);
		}

		return service;
	}

	private Set<String> getAllowedAppProvision(DataAttributes attr) {
		Set<String> ret = new HashSet<String>();
		String value = attr.getValue("allowedAppProvision");

		if(value == null){
			return null;
		}

		for(String line:value.split("\n")){
			ret.add(line);
		}

		return ret;
	}

	private Set<String> getAllowedUse(DataAttributes attr) {
		Set<String> ret = new HashSet<String>();
		String value = attr.getValue("allowedUse");

		if(value == null){
			return null;
		}

		for(String line:value.split("\n")){
			ret.add(line);
		}

		return ret;
	}

	/**
	 * Get the ServiceAttribute
	 * @param attr
	 * @return
	 */
	private List<ServiceAttribute> getServiceAttribute(DataAttributes attr){
		List<ServiceAttribute> ret_list = new ArrayList<ServiceAttribute>();
		String value = attr.getValue("attribute_list");
		if(value == null){
			return null;
		}

		for(String line : value.split("###Attribute###")){
			ServiceAttribute a = new ServiceAttribute();
			for(String str : line.split("\n")){
				//GridId
				if(str.startsWith("attribute_GridId=")){
					str = str.replaceAll("attribute_GridId=", "");
					a.setGridId(str);
				}else if(str.startsWith("attribute_Id=")){
				//serviceId
					str = str.replaceAll("attribute_Id=", "");
					a.setServiceId(str);
				}else if(str.startsWith("attribute_Name=")){
				//Name
					str = str.replaceAll("attribute_Name=", "");
					a.setName(str);
				}else if(str.startsWith("attribute_Value=")){
				//Value
					str = str.replaceAll("attribute_Value=", "");
					a.setValue(str);
				}else if(str.startsWith("attribute_CreateTime=")){
				//CreatedDateTime
					str = str.replaceAll("attribute_CreateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					a.setCreatedDateTime(calendar);
				}else if(str.startsWith("attribute_UpdateTime=")){
				//UpdatedDateTime
					str = str.replaceAll("attribute_UpdateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					a.setUpdatedDateTime(calendar);
				}
			}
			ret_list.add(a);
		}
		return ret_list;
	}

	/**
	 * Get the Invocation.
	 * @param attr
	 * @return
	 */
	private List<Invocation> getInvocations(DataAttributes attr) {
		List<Invocation> ret_list = new ArrayList<Invocation>();
		String value = attr.getValue("invocations_list");

		if(value == null){
			return null;
		}

		for(String line : value.split("###Invocation###")){
			Invocation in = new Invocation();
			for(String str : line.split("\n")){
				//InvocationName
				if(str.startsWith("InvocationName=")){
					str = str.replaceAll("InvocationName=", "");
					in.setInvocationName(str);
				}else if(str.startsWith("OwnerServiceGridId=")){
				//OwnerServiceGridId
						str = str.replaceAll("OwnerServiceGridId=", "");
						in.setOwnerServiceGridId(str);
				}else if(str.startsWith("OwnerServiceId=")){
				//OwnerServiceId
						str = str.replaceAll("OwnerServiceId=", "");
						in.setOwnerServiceId(str);
				}else if(str.startsWith("ServiceGridId=")){
				//ServiceGridId
						str = str.replaceAll("ServiceGridId=", "");
						if(!str.matches("null")){
							in.setServiceGridId(str);
						}
				}else if(str.startsWith("ServiceId=")){
				//ServiceId
						str = str.replaceAll("ServiceId=", "");
						if(!str.matches("null")){
							in.setServiceId(str);
						}
				}else if(str.startsWith("ServiceName=")){
				//ServiceName
					str = str.replaceAll("ServiceName=", "");
					if(!str.matches("null")){
						in.setServiceName(str);
					}
				}else if(str.startsWith("CreateTime=")){
				//createdDateTime
					str = str.replaceAll("CreateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					in.setCreatedDateTime(calendar);
				}else if(str.startsWith("UpdateTime=")){
				//updatedDateTime
					str = str.replaceAll("UpdateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					in.setUpdatedDateTime(calendar);
				}
			}
			ret_list.add(in);
		}
		return ret_list;
	}

	/**
	 */
	private List<String> getPartnerServiceNamespaceURIs(DataAttributes attr) {
		List<String> ret = new ArrayList<String>();
		String value = attr.getValue("partnerServiceNamespaceURI_list");

		if(value == null){
			return null;
		}

		for(String line:value.split("\n")){
			ret.add(line);
		}

		return ret;
	}

	/**
	 * Get the ServiceEndpoint
	 * @return ServiceEndpoint
	 */
	private List<ServiceEndpoint> getServiceEndpoint(DataAttributes attr) {
		List<ServiceEndpoint> ret_list = new ArrayList<ServiceEndpoint>();
		String value = attr.getValue("endpoint_list");
		if(value == null){
			return null;
		}

		for(String line : value.split("###ServiceEndpoint###")){
			ServiceEndpoint se = new ServiceEndpoint();
			for(String str : line.split("\n")){
				if(str.startsWith("GridId=")){
					//GridId
					str = str.replaceAll("GridId=", "");
					se.setGridId(str);
				}else if(str.startsWith("ProtocolId=")){
					//ProtocolId
					str = str.replaceAll("ProtocolId=", "");
					se.setProtocolId(str);
				}else if(str.startsWith("ServiceId=")){
					//serviceId
					str = str.replaceAll("ServiceId=", "");
					se.setServiceId(str);
				}else if(str.startsWith("Url=")){
					//url
					str = str.replaceAll("Url=", "");
					try {
						se.setUrl(new URL(str));
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}else if(str.startsWith("AuthUserName=")){
				//authUserName
					str = str.replaceAll("AuthUserName=", "");
					se.setAuthUserName(str);
				}else if(str.startsWith("AuthPassword=")){
				//authPassword
					str = str.replaceAll("AuthPassword=", "");
					se.setAuthPassword(str);
				}else if(str.startsWith("DisableReason=")){
				//disableReason
					str = str.replaceAll("DisableReason=", "");
					se.setDisableReason(str);
				}else if(str.startsWith("Enabled=")){
				//enabled
					str = str.replaceAll("Enabled=", "");
					se.setEnabled(Boolean.valueOf(str));
				}else if(str.startsWith("ErrorDate=")){
				//disabledByErrorDate
					str = str.replaceAll("ErrorDate=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					se.setDisabledByErrorDate(calendar);
				}else if(str.startsWith("CreateTime=")){
				//createdDateTime
					str = str.replaceAll("CreateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					se.setCreatedDateTime(calendar);
				}else if(str.startsWith("UpdateTime=")){
				//updatedDateTime
					str = str.replaceAll("UpdateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					se.setUpdatedDateTime(calendar);
				}
			}
			ret_list.add(se);
		}
		return ret_list;
	}

	/**
	 * Get the ServiceDeployment.
	 * @return ServiceDeployment
	 */
	private List<ServiceDeployment> getServiceDeployment(DataAttributes attr) {
		List<ServiceDeployment> ret_list = new ArrayList<ServiceDeployment>();
		String value = attr.getValue("deployment_list");

		if(value == null){
			return null;
		}

		for(String line : value.split("###ServiceDeployment###")){
			ServiceDeployment sd = new ServiceDeployment();
			for(String str : line.split("\n")){
				if(str.startsWith("GridId=")){
				//GridId
					str = str.replaceAll("GridId=", "");
					sd.setGridId(str);
				}else if(str.startsWith("ServiceId=")){
				//ServiceId
					str = str.replaceAll("ServiceId=", "");
					sd.setServiceId(str);
				}else if(str.startsWith("NodeId=")){
				//node
					str = str.replaceAll("NodeId=", "");
					sd.setNodeId(str);
				}else if(str.startsWith("ServicePath=")){
				//ServicePath
					str = str.replaceAll("ServicePath=", "");
					sd.setServicePath(str);
				}else if(str.startsWith("Enabled=")){
				//Enabled
					str = str.replaceAll("Enabled=", "");
					sd.setEnabled(Boolean.valueOf(str));
				}else if(str.startsWith("ErrorDate=")){
				//disabledByErrorDate
					str = str.replaceAll("ErrorDate=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					sd.setDisabledByErrorDate(calendar);
				}else if(str.startsWith("CreateTime=")){
				//createdDateTime
					str = str.replaceAll("CreateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					sd.setCreatedDateTime(calendar);
				}else if(str.startsWith("UpdateTime=")){
				//updatedDateTime
					str = str.replaceAll("UpdateTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					sd.setUpdatedDateTime(calendar);
				}else if(str.startsWith("DeployedTime=")){
				//deployedDateTime
					str = str.replaceAll("DeployedTime=", "");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(0);
					calendar.setTimeInMillis(Long.valueOf(str).longValue());
					sd.setDeployedDateTime(calendar);
				}
			}
			ret_list.add(sd);
		}
		return ret_list;
	}

	/**
	 * Get the data type string.
	 * @return data type string
	 */
	static public String getDataType() {
		return _dataType;
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.data.Data#getType()
	 */
	public String getType() {
		return getDataType();
	}

	/**
	 * Get the DataID.
	 * @return
	 */
	public static DataID getDataID(Service data, ServicePK pk){
		if(pk == null){
			pk = new ServicePK(data.getGridId()
							 , data.getServiceId());
		}

		return new DataID(_IDPrefix
			+ pk.getGridId() + "_"
			+ pk.getServiceId());
	}
}
