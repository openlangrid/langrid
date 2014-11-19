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
package jp.go.nict.langrid.foundation;

import java.util.Map;

import jp.go.nict.langrid.dao.entity.AttributedElement;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class AttributedElementUpdater {
	/**
	 * 
	 * 
	 */
	public static <T extends jp.go.nict.langrid.dao.entity.Attribute> void updateAttributes(
			AttributedElement<T> element, Attribute[] attributes
			, Map<String, Class<?>> elementProperties
			){
		for(Attribute a : attributes){
			String name = a.getName();
			if(a.getValue().length() != 0){
				if(elementProperties.containsKey(name)){
					// 
					// 
					continue;
				}
				element.setAttributeValue(name, a.getValue());
			} else{
				element.removeAttribute(name);
			}
		}
	}
}
