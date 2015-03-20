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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourceAttribute;
import jp.go.nict.langrid.dao.entity.ResourcePK;
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
public class ResourceData extends Data{
	public static final String _IDPrefix = "Resource_";
	public static final String _dataType = "ResourceData";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return ResourceData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new ResourceData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param resource
	 * @throws DataConvertException
	 */
	public ResourceData(Resource resource) throws DataConvertException{
		super(resource.getGridId(), getDataID(resource, null), null, null);
		// Set the updated datetime.
		setLastUpdate(resource.getUpdatedDateTime() != null ? resource.getUpdatedDateTime() : Data.DEFAULT_DATE);
		setAttributes(ConvertUtil.encode(resource));
	}

	/**
	 * The constructor.
	 * @param gid Grid ID
	 * @param id Data ID
	 * @param lastUpdate Last updated datetime
	 * @param attributes Attribute
	 */
	public ResourceData(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		super(gid, id, lastUpdate, attributes);
	}

	/**
	 * Get the Resource.
	 * @return
	 * @throws ParseException
	 */
	public Resource getResource() throws DataConvertException {
		Resource resource = new Resource();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, resource);

		List<ResourceAttribute> a_list = getResourceAttribute(attr);
		if(a_list != null){
			for(ResourceAttribute a:a_list)
				resource.setAttribute(a);
		}
		return resource;
	}

	private List<ResourceAttribute> getResourceAttribute(DataAttributes attr){
		List<ResourceAttribute> ret_list = new ArrayList<ResourceAttribute>();
		String value = attr.getValue("attribute_list");
		if(value == null){
			return null;
		}

		for(String line : value.split("###Attribute###")){
			ResourceAttribute a = new ResourceAttribute();
			for(String str : line.split("\n")){
				//GridId
				if(str.startsWith("attribute_GridId=")){
					str = str.replaceAll("attribute_GridId=", "");
					a.setGridId(str);
				}else if(str.startsWith("attribute_Id=")){
				//ResourceID
					str = str.replaceAll("attribute_Id=", "");
					a.setResourceId(str);
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
	public static DataID getDataID(Resource data, ResourcePK pk){
		if(pk == null){
			pk = new ResourcePK(data.getGridId()
						  , data.getResourceId());
		}

		return new DataID(_IDPrefix
			+ pk.getGridId() + "_"
			+ pk.getResourceId());
	}
}
