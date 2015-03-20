/*
 * $Id:HibernateUserDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao.hibernate.searchsupport;

import java.sql.Blob;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.dao.MatchingCondition;

import org.hibernate.Hibernate;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 216 $
 */
public class BlobSearchSupport<T> implements SearchSupport<T>{
	public void appendWhereClouse(
			MatchingCondition c, String elementAlias, String parameterName
			, StringBuilder query, Map<String, Object> parameters) {
		Object value = c.getMatchingValue();
		if(!(value instanceof Blob)){
			if(value instanceof byte[]){
				value = Hibernate.createBlob((byte[])value);
			} else{
				value = Hibernate.createBlob(
						StringUtil.toUTF8Bytes(value.toString())
						);
			}
		}
		query.append(" and ");
		query.append(elementAlias);
		query.append(".");
		query.append(c.getFieldName());
		query.append(" = ");
		query.append(":");
		query.append(parameterName);
		parameters.put(parameterName, value);
	}

	public boolean isFileteringNeeded() {
		return false;
	}

	public void filterResults(MatchingCondition condition, List<T> results) {
	}
}
