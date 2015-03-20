/*
 * $Id: AccessLimitData.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.AccessLimitPK;
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
public class AccessLimitData extends Data {
	public static final String _dataType = "AccessLimitData";
	public static final String _IDPrefix = "AccessLimit_";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return AccessLimitData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new AccessLimitData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param limit
	 * @throws DataConvertException
	 */
	public AccessLimitData(AccessLimit limit) throws DataConvertException {
		super(limit.getServiceGridId(), getDataID(limit, null), null, null);
		// Set the updated datetime.
		this.setLastUpdate(limit.getUpdatedDateTime() != null ? limit.getUpdatedDateTime() : Data.DEFAULT_DATE);
		this.setAttributes(ConvertUtil.encode(limit));
	}

	/**
	 * The constructor.
	 * @param gid
	 * @param dataId
	 * @param lastUpdateDate
	 * @param attributes
	 */
	public AccessLimitData(String gid, DataID dataId, Calendar lastUpdateDate, DataAttributes attributes) {
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
	 * Get the AccessLimit.
	 * @return
	 * @throws DataConvertException
	 */
	public AccessLimit getAccessLimit() throws ParseException, DataConvertException {
		AccessLimit limit = new AccessLimit();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, limit);

		return limit;
	}
	/**
	 * Gets the DataID.
	 * @return
	 */
	public static DataID getDataID(AccessLimit data, AccessLimitPK pk){
		if(pk == null){
			pk = new AccessLimitPK(data.getUserGridId()
								, data.getUserId()
								, data.getServiceGridId()
								, data.getServiceId()
								, data.getPeriod()
								, data.getLimitType());
		}

		return new DataID(_IDPrefix
			+ pk.getUserGridId() + "_"
			+ pk.getUserId() + "_"
			+ pk.getServiceGridId() + "_"
			+ pk.getServiceId() + "_"
			+ pk.getPeriod().toString() + "_"
			+ pk.getLimitType().toString());
	}
}
