/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class ServiceTypeSearchSupport<T> implements SearchSupport<T> {
	public void appendWhereClouse(
			MatchingCondition c, String elementAlias, String parameterName
			, StringBuilder query, Map<String, Object> parameters) {
		String value = c.getMatchingValue().toString();
		if(value.equalsIgnoreCase("OTHER")){
			query.append(" and serviceTypeId is null");
			return;
		}
		String operator = "like";
		switch(c.getMatchingMethod()){
			case COMPLETE:
				operator = "=";
				break;
			case PREFIX:
				value = value + "%";
				break;
			case SUFFIX:
				value = "%" + value;
				break;
			case PARTIAL:
			case LANGUAGEPATH:
				value = "%" + value + "%";
				break;
		}
		query.append(" and lower(").append(elementAlias).append(".");
		query.append("serviceTypeId").append(") ");
		query.append(operator).append(" :").append(parameterName);
		parameters.put(parameterName, value.toLowerCase());
	}

	public boolean isFileteringNeeded() {
		return false;
	}

	public void filterResults(MatchingCondition condition, List<T> results) {
	}
}
