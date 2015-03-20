/*
 * $Id: NewsData.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.entity.News;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.DataAttributes;
import jp.go.nict.langrid.p2pgridbasis.data.DataID;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.converter.ConvertUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class NewsData extends Data{
	public static final String _IDPrefix = "News_";
	public static final String _dataType = "NewsData";

	/**
	 * 
	 * 
	 */
	public static class Instantiator implements jp.go.nict.langrid.p2pgridbasis.data.DataFactory.Instantiator {

		public String getDataType() {
			return NewsData.getDataType();
		}

		public Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) {
			return new NewsData(gid, dataID, lastUpdateDate, attributes);
		}

	}

	/**
	 * The constructor.
	 * @param news
	 * @throws DataConvertException
	 */
	public NewsData(News news) throws DataConvertException{
		super(news.getGridId(), new DataID(_IDPrefix
				+ news.getGridId() + "_"
				+ news.getNodeId() + "_"
				+ news.getNodeLocalId()), null, null);
		// set the updated datetime
		setLastUpdate(news.getUpdatedDateTime() != null ? news.getUpdatedDateTime() : Data.DEFAULT_DATE);
		setAttributes(ConvertUtil.encode(news));
	}

	/**
	 * The constructor.
	 * @param gid Grid ID
	 * @param id Data ID
	 * @param lastUpdate Updated datetime
	 * @param attributes Attribute
	 */
	public NewsData(String gid, DataID id, Calendar lastUpdate, DataAttributes attributes) {
		super(gid, id, lastUpdate, attributes);
	}

	/**
	 * Get the Node
	 * @return
	 * @throws ParseException
	 */
	public News getNews() throws DataConvertException {
		News news = new News();
		DataAttributes attr = getAttributes();
		ConvertUtil.decode(attr, news);

		return news;
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
}
