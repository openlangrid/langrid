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
import jp.go.nict.langrid.dao.entity.Resource;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 216 $
 */
public class ResourceBooleanSearchSupport<T extends Resource>
implements SearchSupport<T>
{
	/**
	 * 
	 * 
	 */
	public ResourceBooleanSearchSupport(){
	}

	public void appendWhereClouse(
			MatchingCondition c, String elementAlias, String parameterName
			, StringBuilder query, Map<String, Object> parameters) {
		boolean v = c.getMatchingValue().toString().equalsIgnoreCase("true");
		query.append(" and ");
		query.append(elementAlias);
		query.append(".");
		query.append(c.getFieldName());
		query.append(" = ");
		query.append(":");
		query.append(parameterName);
		parameters.put(parameterName, v);
	}

	public boolean isFileteringNeeded() {
		return false;
	}

	public void filterResults(MatchingCondition condition, List<T> results) {
	}
}
