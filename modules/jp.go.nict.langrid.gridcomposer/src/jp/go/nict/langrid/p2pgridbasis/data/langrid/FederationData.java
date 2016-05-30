/*
 * $Id: FederationData.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.dao.entity.FederationPK;
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
 * @version $Revision: 328 $
 */
public class FederationData extends Data {
	public static final String _dataType = "FederationData";
	public static final String _IDPrefix = "Federation_";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return FederationData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new FederationData(null, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param federation
	 * @throws DataConvertException
	 */
	public FederationData(Federation federation) throws DataConvertException {
		super(null, getDataID(federation, null), null, null);
		// Set the updated datetime.
		this.setLastUpdate(federation.getUpdatedDateTime() != null ? federation.getUpdatedDateTime() : Data.DEFAULT_DATE);
		this.setAttributes(ConvertUtil.encode(federation));
	}

	/**
	 * The constructor.
	 * @param gid
	 * @param dataId
	 * @param lastUpdateDate
	 * @param attributes
	 */
	public FederationData(String gid, DataID dataId, Calendar lastUpdateDate, DataAttributes attributes) {
		super(null, dataId, lastUpdateDate, attributes);
	}

	/**
	 * Get the data type statically.
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
	 * Get the Federation
	 * @return
	 * @throws DataConvertException
	 */
	public Federation getFederation() throws ParseException, DataConvertException {
		Federation federation = new Federation();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, federation);

		return federation;
	}
	/**
	 * Get the DataID.
	 * @return
	 */
	public static DataID getDataID(Federation data, FederationPK pk){
		if(pk == null){
			pk = new FederationPK(data.getSourceGridId()
								, data.getTargetGridId());
		}

		return new DataID(_IDPrefix
			+ pk.getSourceGridId() + "_"
			+ pk.getTargetGridId());
	}
}
