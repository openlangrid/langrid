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

import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.entity.AttributedElement;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 485 $
 */
public class AttributeSearchSupport<T extends AttributedElement<?>> implements SearchSupport<T>{
	public void appendWhereClouse(
			MatchingCondition c, String elementAlias, String parameterName
			, StringBuilder query, Map<String, Object> parameters) {
		String operator = "like";
		if(c.getMatchingMethod().equals(MatchingMethod.LANGUAGEPATH)){
			return;
		}
		StringBuilder q = new StringBuilder(" and (select count(*) from "
				+ elementAlias + ".attributes attr%1$s"
				+ " where attr%1$s.name=:attrName%1$s");
		String value = c.getMatchingValue().toString();
		if(value.length() == 0){
			q.append(")=0");
			query.append(String.format(q.toString(), parameterName));
			parameters.put("attrName" + parameterName, c.getFieldName());
		} else{
			switch(c.getMatchingMethod()){
				case PREFIX:
					value = value + "%";
					break;
				case SUFFIX:
					value = "%" + value;
					break;
				case PARTIAL:
					value = "%" + value + "%";
					break;
				default:
					operator = "=";
					break;
			}
			q.append(" and lower(attr%1$s.value) %2$s lower(:attrValue%1$s))=1");
			query.append(String.format(q.toString(), parameterName, operator));
			parameters.put("attrName" + parameterName, c.getFieldName());
			parameters.put("attrValue" + parameterName, value);
		}
	}

	public boolean isFileteringNeeded() {
		return false;
	}

	public void filterResults(MatchingCondition condition, List<T> results) {
	}
}
