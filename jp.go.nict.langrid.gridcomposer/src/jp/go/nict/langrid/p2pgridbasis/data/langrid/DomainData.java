/*
 * $Id: DomainData.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.DomainPK;
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
public class DomainData extends Data{
	public static final String _IDPrefix = "Domain_";
	public static final String _dataType = "DomainData";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return DomainData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new DomainData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param domain
	 * @throws DataConvertException
	 */
	public DomainData(Domain domain) throws DataConvertException{
		super(null, getDataID(domain, null), null, null);
		// Set the updated datetime.
		setLastUpdate(domain.getUpdatedDateTime() != null ? domain.getUpdatedDateTime() : Data.DEFAULT_DATE);
		setAttributes(ConvertUtil.encode(domain));
	}

	/**
	 * The constructor.
	 * @param gid Grid ID
	 * @param id Data ID
	 * @param lastUpdate last updated datetime
	 * @param attributes attributes
	 */
	public DomainData(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		super(gid, id, lastUpdate, attributes);
	}

	/**
	 * get the node.
	 * @return
	 * @throws ParseException
	 */
	public Domain getDomain() throws DataConvertException {
		Domain domain = new Domain();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, domain);

		return domain;
	}

	/**
	 * get the data type staticaly
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
	 * get the DataID.
	 * @return
	 */
	public static DataID getDataID(Domain data, DomainPK pk){
		if(pk == null){
			pk = new DomainPK(data.getDomainId());
		}

		return new DataID(_IDPrefix
			+ pk.getDomainId());
	}
}
