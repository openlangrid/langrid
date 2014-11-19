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

import jp.go.nict.langrid.dao.entity.TemporaryUser;
import jp.go.nict.langrid.dao.entity.TemporaryUserPK;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.DataAttributes;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import jp.go.nict.langrid.p2pgridbasis.data.RequiredAttributeNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.converter.ConvertUtil;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Masato Mori
 * @author Takao Nakaguchi
 */
public class TemporaryUserData extends Data {
	public static final String _dataType = "TemporaryUserData";
	public static final String _IDPrefix = "TemporaryUser_";


	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {
		public String getDataType() {
			return TemporaryUserData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) throws RequiredAttributeNotFoundException {
			return new TemporaryUserData(gid, dataID, lastUpdateDate, attributes);
		}
	}

	/**
	 * The constructor.
	 * @param tempUser
	 * @throws DataConvertException
	 */
	public TemporaryUserData (TemporaryUser tempUser) throws DataConvertException{
		super(tempUser.getGridId(), getDataID(tempUser, null), null, null);
		// Set the updated datetime.
		this.setLastUpdate(tempUser.getUpdatedDateTime() != null ? tempUser.getUpdatedDateTime() : Data.DEFAULT_DATE);
		this.setAttributes(ConvertUtil.encode(tempUser));
	}

	/**
	 * The constructor.
	 * @param gid
	 * @param dataID
	 * @param lastUpdate
	 * @param attributes
	 * @throws RequiredAttributeNotFoundException
	 */
	public TemporaryUserData(String gid, DataID dataID, Calendar lastUpdate, DataAttributes attributes) throws RequiredAttributeNotFoundException {
		super(gid, dataID, lastUpdate, attributes);
	}

	/**
	 * Get the TemporaryUser.
	 * @return
	 * @throws ParseException
	 */
	public TemporaryUser getUser() throws ParseException {
		try {
			TemporaryUser tempUser = new TemporaryUser();
			ConvertUtil.decode(getAttributes(), tempUser);
			return tempUser;
		} catch (DataConvertException e) {
			throw new ParseException(e.getMessage(), 0);
		}
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
		return _dataType;
	}

	/**
	 * Get the DataID.
	 * @return
	 */
	public static DataID getDataID(TemporaryUser data, TemporaryUserPK pk){
		if(pk == null){
			pk = new TemporaryUserPK(data.getGridId()
								   , data.getUserId());
		}

		return new DataID(_IDPrefix
			+ pk.getGridId() + "_"
			+ pk.getUserId());
	}
}
