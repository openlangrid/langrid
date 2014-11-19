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

import jp.go.nict.langrid.dao.entity.AccessLog;
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
public class AccessLogData extends Data {
	public static final String _dataType = "AccessLogData";
	public static final String _IDPrefix = "AccessLog_";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return AccessLogData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new AccessLogData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param log
	 * @throws DataConvertException
	 */
	public AccessLogData(AccessLog log) throws DataConvertException {
		//NodeLocalId + NodeId
		super(log.getServiceAndNodeGridId(),
				new DataID(_IDPrefix
						+ log.getServiceAndNodeGridId() + "_"
						+ log.getNodeId() + "_"
						+ log.getNodeLocalId()), null, null);
		// Set the updated datetime.
		this.setLastUpdate(log.getDateTime() != null ? log.getDateTime() : Data.DEFAULT_DATE);
		this.setAttributes(ConvertUtil.encode(log));
	}

	/**
	 * The constructor.
	 * @param gid
	 * @param dataId
	 * @param lastUpdateDate
	 * @param attributes
	 */
	public AccessLogData(String gid, DataID dataId, Calendar lastUpdateDate, DataAttributes attributes) {
		super(gid, dataId, lastUpdateDate, attributes);
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
		return getDataType();
	}

	/**
	 * Get the AccessLog.
	 * @return
	 * @throws DataConvertException
	 */
	public AccessLog getAccessLog() throws ParseException, DataConvertException {
		AccessLog log = new AccessLog();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, log);

		return log;
	}
}
