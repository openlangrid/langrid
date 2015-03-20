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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.transformer.StringToEnumTransformer;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.dao.MatchingCondition;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 389 $
 */
public class EnumSearchSupport<T, U extends Enum<U>>
implements SearchSupport<T>
{
	/**
	 * 
	 * 
	 */
	public EnumSearchSupport(Class<U> clazz){
		this.enumClass = clazz;
		this.transformer = new StringToEnumTransformer<U>(clazz);
	}

	public void appendWhereClouse(
			MatchingCondition c, String elementAlias, String parameterName
			, StringBuilder query, Map<String, Object> parameters) {
		U value = getValue(c);
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

	protected U getValue(MatchingCondition c){
		try{
			Object v = c.getMatchingValue();
			if(v.getClass().equals(enumClass)){
				return enumClass.cast(v);
			} else{
				return transformer.transform(c.getMatchingValue().toString());
			}
		} catch(TransformationException e){
			if(e.getCause() instanceof InvocationTargetException
					&& e.getCause().getCause() instanceof IllegalArgumentException){
				logger.info(
					"invalid Enum value specified. ignore this condition." +
					" field: \"" + c.getFieldName() + "\" value: \""
					+ c.getMatchingValue() + "\""
					);
			} else{
				logger.log(Level.WARNING
						, "failed to get Enum object. ignore this condition." +
						" field: \"" + c.getFieldName() + "\" value: \""
						+ c.getMatchingValue() + "\""
						, e);
			}
			return null;
		}
	}

	private Class<U> enumClass;
	private StringToEnumTransformer<U> transformer;

	private static Logger logger = Logger.getLogger("jp.go.nict.langrid.dao");
}
