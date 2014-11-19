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
package jp.go.nict.langrid.dao.jsonic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicUtil {
	public static <T> T decode(File file, Class<T> clazz)
	throws IOException{
		InputStream is = new FileInputStream(file);
		try{
			return JSON.decode(is, clazz);
		} finally{
			is.close();
		}
	}

	public static <T> T decode(File file, JSON json, Class<T> clazz)
	throws IOException{
		InputStream is = new FileInputStream(file);
		try{
			return json.parse(is, clazz);
		} finally{
			is.close();
		}
	}
}
