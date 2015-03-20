/*
 * $Id: EqualsBuilderUtil.java 205 2010-10-02 13:53:40Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import jp.go.nict.langrid.commons.util.CollectionUtil;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 205 $
 */
public class EqualsBuilderUtil {
	public static <T> EqualsBuilder appendAsSet(EqualsBuilder builder
			, Collection<T> lhs, Collection<T> rhs){
		return builder.appendSuper(CollectionUtil.equalsAsSet(
				lhs, rhs
				));
	}

	public static <T> EqualsBuilder appendAsSet(EqualsBuilder builder
			, Collection<T> lhs, Collection<T> rhs
			, Class<T> clazz, String equalsMethodName)
	throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		return builder.appendSuper(CollectionUtil.equalsAsSet(
				lhs, rhs, clazz, equalsMethodName
				));
	}
}
