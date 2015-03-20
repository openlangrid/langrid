/*
 * $Id: ResourceTypeData.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ResourceTypePK;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.DataAttributes;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.converter.ConvertUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class ResourceTypeData extends Data{
	public static final String _IDPrefix = "ResourceType_";
	public static final String _dataType = "ResourceTypeData";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return ResourceTypeData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new ResourceTypeData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param resourceType
	 * @throws DataConvertException
	 */
	public ResourceTypeData(ResourceType resourceType) throws DataConvertException{
		super(null, getDataID(resourceType, null), null, null);
		// Set the updated datetime.
		setLastUpdate(resourceType.getUpdatedDateTime() != null ? resourceType.getUpdatedDateTime() : Data.DEFAULT_DATE);
		setAttributes(ConvertUtil.encode(resourceType));
	}

	/**
	 * The constructor.
	 * @param gid Grid ID
	 * @param id Data ID
	 * @param lastUpdate Last updated datetime
	 * @param attributes Attribute
	 */
	public ResourceTypeData(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		super(gid, id, lastUpdate, attributes);
	}

	/**
	 * Get the ResourceType.
	 * @return
	 * @throws ParseException
	 */
	public ResourceType getResourceType() throws DataConvertException {
		ResourceType resourceType = new ResourceType();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, resourceType);

		List<ResourceMetaAttribute> list = getMetaAttributes(attr);
		if(list != null){
			resourceType.setMetaAttributeCollection(list);
		}

		return resourceType;
	}

	private List<ResourceMetaAttribute> getMetaAttributes(DataAttributes attr) {
		List<ResourceMetaAttribute> ret = new ArrayList<ResourceMetaAttribute>();
		String value = attr.getValue("metaAttribute_list");
		if(value == null){
			return null;
		}

		for(String line : value.split("###MetaAttribute###")){
			ResourceMetaAttribute a = new ResourceMetaAttribute();
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
	public static DataID getDataID(ResourceType data, ResourceTypePK pk){
		if(pk == null){
			pk = new ResourceTypePK(data.getDomainId(), data.getResourceTypeId());
		}

		return new DataID(_IDPrefix
			+ pk.getDomainId() + "_"
			+ pk.getResourceTypeId());
	}
}
