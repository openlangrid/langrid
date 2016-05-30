/*
 * $Id: ServiceTypeData.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.data.langrid;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.axis.encoding.Base64;

import jp.go.nict.langrid.dao.entity.ServiceInterfaceDefinition;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.ServiceTypePK;
import jp.go.nict.langrid.dao.util.LobUtil;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.DataAttributes;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.converter.ConvertUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class ServiceTypeData extends Data{
	public static final String _IDPrefix = "ServiceType_";
	public static final String _dataType = "ServiceTypeData";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return ServiceTypeData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new ServiceTypeData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param serviceType
	 * @throws DataConvertException
	 */
	public ServiceTypeData(ServiceType serviceType) throws DataConvertException{
		super(null, getDataID(serviceType, null), null, null);
		// Set the updated datetime.
		setLastUpdate(serviceType.getUpdatedDateTime() != null ? serviceType.getUpdatedDateTime() : Data.DEFAULT_DATE);
		setAttributes(ConvertUtil.encode(serviceType));
	}

	/**
	 * The constructor.
	 * @param gid Grid ID
	 * @param id Data ID
	 * @param lastUpdate Last updated datetime
	 * @param attributes Attribute
	 */
	public ServiceTypeData(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		super(gid, id, lastUpdate, attributes);
	}

	/**
	 * Get the ServiceType.
	 * @return
	 * @throws ParseException
	 */
	public ServiceType getServiceType() throws DataConvertException {
		ServiceType serviceType = new ServiceType();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, serviceType);

		List<ServiceMetaAttribute> list = getMetaAttributes(attr);
		if(list != null){
			serviceType.setMetaAttributeCollection(list);
		}

		try{
			Collection<ServiceInterfaceDefinition> collection = getInterfaceDefinitions(attr, serviceType);
			if(collection != null){
				serviceType.setInterfaceDefinitionCollection(collection);
			}
		} catch(IOException e){
			throw new DataConvertException(e);
		}

		return serviceType;
	}

	private List<ServiceMetaAttribute> getMetaAttributes(DataAttributes attr) {
		List<ServiceMetaAttribute> ret = new ArrayList<ServiceMetaAttribute>();
		String value = attr.getValue("metaAttribute_list");
		if(value == null){
			return null;
		}

		for(String line : value.split("###MetaAttribute###")){
			ServiceMetaAttribute a = new ServiceMetaAttribute();
			for(String str : line.split("\n")){
				if(str.startsWith("DomainId=")){
					str = str.replaceAll("DomainId=", "");
					a.setDomainId(str);
				}else if(str.startsWith("AttributeId=")){
					str = str.replaceAll("AttributeId=", "");
					a.setAttributeId(str);
				}
			}
			ret.add(a);
		}
		return ret;
	}

	private Collection<ServiceInterfaceDefinition> getInterfaceDefinitions(
			DataAttributes attr, ServiceType serviceType)
	throws IOException{
		Collection<ServiceInterfaceDefinition> ret = new ArrayList<ServiceInterfaceDefinition>();
		String value = attr.getValue("interfaceDefinition_list");
		if(value == null){
			return null;
		}

		for(String line : value.split("###ServiceInterfaceDefinition###")){
			ServiceInterfaceDefinition i = new ServiceInterfaceDefinition();
			for(String str : line.split("\n")){
				if(str.startsWith("ProtocolId=")){
					str = str.replaceAll("ProtocolId=", "");
					i.setProtocolId(str);
				}else if(str.startsWith("Definition=")){
					str = str.replaceAll("Definition=", "");
					i.setDefinition(LobUtil.createBlob(Base64.decode(str)));
				}
				i.setServiceType(serviceType);
			}
			ret.add(i);
		}
		return ret;
	}

	/**
	 * Get the data type string.
	 * @return data type string.
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
	public static DataID getDataID(ServiceType data, ServiceTypePK pk){
		if(pk == null){
			pk = new ServiceTypePK(data.getDomainId(), data.getServiceTypeId());
		}

		return new DataID(_IDPrefix
			+ pk.getDomainId() + "_"
			+ pk.getServiceTypeId());
	}
}
