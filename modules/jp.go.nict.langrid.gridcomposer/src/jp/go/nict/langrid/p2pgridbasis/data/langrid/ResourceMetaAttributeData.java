/*
 * $Id: ResourceMetaAttributeData.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
import java.util.Calendar;

import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttributePK;
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
public class ResourceMetaAttributeData extends Data{
	public static final String _IDPrefix = "ResourceMetaAttribute_";
	public static final String _dataType = "ResourceMetaAttributeData";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return ResourceMetaAttributeData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new ResourceMetaAttributeData(gid, dataID, lastUpdateDate, attributes);
		}
	}

	/**
	 * The constructor.
	 * @param resourceMetaAttribute
	 * @throws DataConvertException
	 */
	public ResourceMetaAttributeData(ResourceMetaAttribute resourceMetaAttribute) throws DataConvertException{
		super(null, getDataID(resourceMetaAttribute, null), null, null);
		// Set the updated datetime.
		setLastUpdate(Data.DEFAULT_DATE);
		setAttributes(ConvertUtil.encode(resourceMetaAttribute));
	}

	/**
	 * The constructor.
	 * @param gid Grid ID
	 * @param id Data ID
	 * @param lastUpdate Last updated datetime
	 * @param attributes Attribute
	 */
	public ResourceMetaAttributeData(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		super(gid, id, lastUpdate, attributes);
	}

	/**
	 * Get the ResourceMetaAttribute.
	 * @return
	 * @throws ParseException
	 */
	public ResourceMetaAttribute getResourceMetaAttribute() throws DataConvertException {
		ResourceMetaAttribute resourceMetaAttribute = new ResourceMetaAttribute();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, resourceMetaAttribute);
		return resourceMetaAttribute;
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
	 * Get the DataID
	 * @return
	 */
	public static DataID getDataID(ResourceMetaAttribute data, ResourceMetaAttributePK pk){
		if(pk == null){
			pk = new ResourceMetaAttributePK(data.getDomainId(), data.getAttributeId());
		}

		return new DataID(_IDPrefix
			+ pk.getDomainId() + "_"
			+ pk.getAttributeId());
	}
}
