/*
 * $Id: ServiceMetaAttributeData.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttributePK;
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
public class ServiceMetaAttributeData extends Data{
	public static final String _IDPrefix = "ServiceMetaAttribute_";
	public static final String _dataType = "ServiceMetaAttributeData";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return ServiceMetaAttributeData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new ServiceMetaAttributeData(gid, dataID, lastUpdateDate, attributes);
		}
	}

	/**
	 * The constructor.
	 * @param serviceMetaAttribute
	 * @throws DataConvertException
	 */
	public ServiceMetaAttributeData(ServiceMetaAttribute serviceMetaAttribute) throws DataConvertException{
		super(null, getDataID(serviceMetaAttribute, null), null, null);
		// Set the updated datetime.
		setLastUpdate(Data.DEFAULT_DATE);
		setAttributes(ConvertUtil.encode(serviceMetaAttribute));
	}

	/**
	 * The constructor.
	 * @param gid Grid ID
	 * @param id Data ID
	 * @param lastUpdate Last updated datetime
	 * @param attributes Attribute
	 */
	public ServiceMetaAttributeData(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		super(gid, id, lastUpdate, attributes);
	}

	/**
	 * Get the ServiceMetaAttribute
	 * @return
	 * @throws ParseException
	 */
	public ServiceMetaAttribute getServiceMetaAttribute() throws DataConvertException {
		ServiceMetaAttribute serviceMetaAttribute = new ServiceMetaAttribute();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, serviceMetaAttribute);
		return serviceMetaAttribute;
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
	public static DataID getDataID(ServiceMetaAttribute data, ServiceMetaAttributePK pk){
		if(pk == null){
			pk = new ServiceMetaAttributePK(data.getDomainId(), data.getAttributeId());
		}

		return new DataID(_IDPrefix
			+ pk.getDomainId() + "_"
			+ pk.getAttributeId());
	}
}
