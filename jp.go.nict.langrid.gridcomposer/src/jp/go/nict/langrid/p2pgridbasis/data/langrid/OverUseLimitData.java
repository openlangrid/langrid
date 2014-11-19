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

import jp.go.nict.langrid.dao.entity.OverUseLimit;
import jp.go.nict.langrid.dao.entity.OverUseLimitPK;
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
public class OverUseLimitData extends Data {
	public static final String _dataType = "OverUseLimitData";
	public static final String _IDPrefix = "OverUseLimit_";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return OverUseLimitData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new OverUseLimitData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param limit
	 * @throws DataConvertException
	 */
	public OverUseLimitData(OverUseLimit limit) throws DataConvertException {
		super(limit.getGridId(), getDataID(limit, null), null, null);
		// set the updated datetime
		this.setLastUpdate(limit.getUpdatedDateTime() != null ? limit.getUpdatedDateTime() : Data.DEFAULT_DATE);
		this.setAttributes(ConvertUtil.encode(limit));
	}

	/**
	 * The constructor.
	 * @param gId
	 * @param dataId
	 * @param lastUpdateDate
	 * @param attributes
	 */
	public OverUseLimitData(String gid, DataID dataId, Calendar lastUpdateDate, DataAttributes attributes) {
		super(gid, dataId, lastUpdateDate, attributes);
	}

	/**
	 * Get the data type string.
	 * @return Data type string.
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
	 * Get the OverUseLimit.
	 * @return
	 * @throws DataConvertException
	 */
	public OverUseLimit getOverUseLimit() throws ParseException, DataConvertException {
		OverUseLimit limit = new OverUseLimit();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, limit);

		return limit;
	}

	/**
	 * Get the DataID
	 * @return
	 */
	public static DataID getDataID(OverUseLimit data, OverUseLimitPK pk){
		if(pk == null){
			pk = new OverUseLimitPK(data.getGridId()
								  , data.getPeriod()
								  , data.getLimitType());
		}

		return new DataID(_IDPrefix
				+ pk.getGridId() + "_"
				+ pk.getPeriod().toString() + "_"
				+ pk.getLimitType().toString());
	}
}
