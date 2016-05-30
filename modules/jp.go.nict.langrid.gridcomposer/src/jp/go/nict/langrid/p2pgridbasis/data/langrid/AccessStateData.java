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

import jp.go.nict.langrid.dao.entity.AccessStat;
import jp.go.nict.langrid.dao.entity.AccessStatPK;
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
public class AccessStateData extends Data {
	public static final String _dataType = "AccessStateData";
	public static final String _IDPrefix = "AccessState_";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return AccessStateData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new AccessStateData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param state
	 * @throws DataConvertException
	 */
	public AccessStateData(AccessStat state) throws DataConvertException {
		super(state.getServiceAndNodeGridId(), getDataID(state, null), null, null);
		// set the updated date
		this.setLastUpdate(state.getLastAccessDateTime() != null ? state.getLastAccessDateTime() : Data.DEFAULT_DATE);
		this.setAttributes(ConvertUtil.encode(state));
	}

	/**
	 * The constructor.
	 * @param gid
	 * @param dataId
	 * @param lastUpdateDate
	 * @param attributes
	 */
	public AccessStateData(String gid, DataID dataId, Calendar lastUpdateDate, DataAttributes attributes) {
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
	 * get AccessState
	 * @return
	 * @throws DataConvertException
	 */
	public AccessStat getAccessState() throws ParseException, DataConvertException {
		AccessStat state = new AccessStat();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, state);

		return state;
	}

	/**
	 * get DataID
	 * @return
	 */
	public static DataID getDataID(AccessStat data, AccessStatPK pk){
		if(pk == null){
			pk = new AccessStatPK(data.getUserGridId()
								 , data.getUserId()
								 , data.getServiceAndNodeGridId()
								 , data.getServiceId()
								 , data.getNodeId()
								 , data.getBaseDateTime()
								 , data.getPeriod());
		}

		return new DataID(_IDPrefix
				+ String.format("%04d",pk.getBaseDateTime().get(Calendar.YEAR))
				+ String.format("%02d",pk.getBaseDateTime().get(Calendar.MONTH)+1)
				+ String.format("%02d",pk.getBaseDateTime().get(Calendar.DATE)) + "_"
				+ pk.getUserGridId() + "_"
				+ pk.getUserId() + "_"
				+ pk.getServiceAndNodeGridId() + "_"
				+ pk.getServiceId() + "_"
				+ pk.getNodeId() + "_"
				+ pk.getPeriod().toString());
		}
}
