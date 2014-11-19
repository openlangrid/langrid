/*
 * $Id:LangridSessionFactory.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.commons.lang;

import java.net.URL;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class ResourceFinder {
	/**
	 * 
	 * 
	 */
	public static URL find(String name){
		try{
			Class<?> caller = Class.forName(new Exception().getStackTrace()[1].getClassName());
			return find(caller, name);
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * 
	 */
	public static URL find(Class<?> loaderClass, String name){
		URL res = loaderClass.getResource("/" + name);
		if(res != null) return res;
		res = loaderClass.getResource(name);
		if(res != null) return res;
		return null;
	}
}
