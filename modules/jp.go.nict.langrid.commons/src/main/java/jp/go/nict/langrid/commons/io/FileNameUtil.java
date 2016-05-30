/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 Language Grid Project.
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
package jp.go.nict.langrid.commons.io;

import java.io.File;

/**
 * @author Takao Nakaguchi
 */
public final class FileNameUtil{
	public static String getExt(File file){
		return getExt(file.getName());
	}

	public static String getExt(String fileName){
		int i = fileName.lastIndexOf('.');
		if(i == -1) return "";
		return fileName.substring(i + 1);
	}

	public static String changeExt(String fileName, String ext){
		String e = getExt(fileName);
		return fileName.substring(0, fileName.length() - e.length()) + ext;
	}
}
