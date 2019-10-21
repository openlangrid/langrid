/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.naming;

import java.util.Arrays;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class JNDIUtil {
	/**
	 * 
	 * 
	 */
	public static boolean isDataSourceAvailable(String dataSourceName){
		try {
			DataSource.class.cast(new InitialContext().lookup(dataSourceName));
			return true;
		} catch (NamingException e) {
			return false;
		}
	}

	/**
	 * 
	 * 
	 */
	public static void createSubContextAndBind(
			Context context, String name, Object value)
	throws NamingException{
		Context current = context;
		String[] subContextNames = name.split("\\/");
		if(subContextNames.length == 1) return;
		subContextNames = Arrays.copyOfRange(
				subContextNames, 0, subContextNames.length - 1
				);
		for(String n : subContextNames){
			current = current.createSubcontext(n);
		}
		context.bind(name, value);
	}
}
