/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class CalendarSearchSupport<T>
implements SearchSupport<T>{
	/**
	 * 
	 * 
	 */
	public CalendarSearchSupport(){
	}

	public void appendWhereClouse(
			MatchingCondition c, String elementAlias, String parameterName
			, StringBuilder query, Map<String, Object> parameters) {
		Date v = null;
		try{
			if(c.getMatchingValue() != null){
				v = CalendarUtil.decodeFromSimpleDate(c.getMatchingValue().toString()).getTime();
			}
		} catch(ParseException e){
			return;
		}
		query.append(" and ");
		query.append(elementAlias);
		query.append(".");
		query.append(c.getFieldName());
		query.append(" ");
		if(c.getMatchingMethod().equals(MatchingMethod.EQ) && v == null){
			query.append("is NULL");
			return;
		}

		String operator = "=";
		switch(c.getMatchingMethod()){
			case GT:
				operator = ">";
				break;
			case GE:
				operator = ">=";
				break;
			case LT:
				operator = "<";
				break;
			case LE:
				operator = "<=";
				break;
		}
		query.append(operator);
		query.append(" ");
		query.append(":");
		query.append(parameterName);
		Calendar ca = Calendar.getInstance();
		ca.setTime(v);
		parameters.put(parameterName, ca);
	}

	public boolean isFileteringNeeded() {
		return false;
	}

	public void filterResults(MatchingCondition condition, List<T> results) {
	}
}
