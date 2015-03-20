/*
 * $Id: ResourceLoader.java 130 2010-04-24 05:27:21Z t-nakaguchi $
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
package jp.go.nict.langrid.testresource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 指定されたパスのリソースをロードするローダ。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 130 $
 */
public class ResourceLoader{
	/**
	 * コンストラクタ。
	 * @param path リソースパス
	 */
	public ResourceLoader(String path){
		this.path = path;
	}

	/**
	 * リソースのパスを返す。
	 * @return リソースのパス
	 */
	public String getPath(){
		return path;
	}

	/**
	 * リソースをロードするInputStreamを返す。
	 * @return InputStream
	 * @throws IOException InputStreamの作成に失敗した
	 */
	public InputStream load() throws IOException{
		return ResourceLoader.class.getResourceAsStream(path);
	}

	private String path;
}
