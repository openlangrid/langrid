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

import static jp.go.nict.langrid.dao.entity.ServiceContainerType.ATOMIC;

import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.entity.ServiceContainerType;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class ServiceContainerTypeSearchSupport<T>
extends EnumSearchSupport<T, ServiceContainerType> {
	public ServiceContainerTypeSearchSupport() {
		super(ServiceContainerType.class);
	}

	public void appendWhereClouse(
			MatchingCondition c, String elementAlias, String parameterName
			, StringBuilder query, Map<String, Object> parameters) {
		ServiceContainerType value = getValue(c);
		if(value == null) return;
		if(value.equals(ATOMIC)){
			query.append(String.format(
					" and (containerType=:%s or (containerType is null and instanceType=0))"
					, parameterName
					));
		} else{
			query.append(String.format(
					" and (containerType=:%s or (containerType is null and instanceType=1))"
					, parameterName
					));
		}
		parameters.put(parameterName, value);
	}

	public boolean isFileteringNeeded() {
		return false;
	}

	public void filterResults(MatchingCondition condition, List<T> results) {
	}
}
