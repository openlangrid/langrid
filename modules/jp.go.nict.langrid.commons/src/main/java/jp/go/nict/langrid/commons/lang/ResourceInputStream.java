/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2014 Language Grid Project.
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

import java.io.FilterInputStream;
import java.io.InputStream;

public class ResourceInputStream extends FilterInputStream{
	public ResourceInputStream(String name)
	throws ResourceNotFoundException{
		super(getResourceAsStream(name));
	}

	public ResourceInputStream(Class<?> clazz, String name)
	throws ResourceNotFoundException{
		super(getResourceAsStream(clazz, name));
	}

	private static InputStream getResourceAsStream(String name)
	throws ResourceNotFoundException{
		try{
			return getResourceAsStream(
					Class.forName(new Exception().getStackTrace()[1].getClassName()),
					name);
		} catch(ClassNotFoundException e){
			throw new ResourceNotFoundException(name, e);
		}
	}

	private static InputStream getResourceAsStream(Class<?> clazz, String name)
	throws ResourceNotFoundException{
		InputStream is = clazz.getResourceAsStream(name);
		if(is == null){
			throw new ResourceNotFoundException(name);
		}
		return is;
	}
}
