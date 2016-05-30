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
import java.util.Calendar;

import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.dao.entity.AccessRightPK;
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
public class AccessRightData extends Data {
	public static final String _dataType = "AccessRightData";
	public static final String _IDPrefix = "AccessRight_";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return AccessRightData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new AccessRightData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor
	 * @param access
	 * @throws DataConvertException
	 */
	public AccessRightData(AccessRight access) throws DataConvertException {
		super(access.getServiceGridId(), getDataID(access, null), null, null);
		// set updated date
		this.setLastUpdate(access.getUpdatedDateTime() != null ? access.getUpdatedDateTime() : Data.DEFAULT_DATE);
		this.setAttributes(ConvertUtil.encode(access));
	}

	/**
	 * The constructor.
	 * @param gid
	 * @param dataId
	 * @param lastUpdateDate
	 * @param attributes
	 */
	public AccessRightData(String gid, DataID dataId, Calendar lastUpdateDate, DataAttributes attributes) {
		super(gid, dataId, lastUpdateDate, attributes);
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
	 * get AccessRight
	 * @return
	 * @throws DataConvertException
	 */
	public AccessRight getAccessRight() throws ParseException, DataConvertException {
		AccessRight right = new AccessRight();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, right);

		return right;
	}

	/**
	 * get DataID
	 * @return
	 */
	public static DataID getDataID(AccessRight data, AccessRightPK pk){
		if(pk == null){
			pk = new AccessRightPK(data.getUserGridId()
								 , data.getUserId()
								 , data.getServiceGridId()
								 , data.getServiceId());
		}

		return new DataID(_IDPrefix
				+ pk.getUserGridId() + "_"
				+ pk.getUserId() + "_"
				+ pk.getServiceGridId() + "_"
				+ pk.getServiceId());
	}
}
