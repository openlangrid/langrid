/*
 * $Id: Resources_1_2.java 130 2010-04-24 05:27:21Z t-nakaguchi $
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
 * リソースをロードする。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 130 $
 */
public enum Resources_1_2{
	/**
	 * JServer翻訳サービスのWSDL。
	 */
	JSERVER_WSDL("JServer.wsdl"),

	/**
	 * JServerJServer2ホップ翻訳サービスのWSDL。
	 */
	JSERVERJSERVER_JA2HOP_WSDL("JServerJServer_ja2hop/JServerJServer_ja2hop.wsdl"),

	/**
	 * JServerJServer2ホップ翻訳サービスのBPEL。
	 */
	JSERVERJSERVER_JA2HOP_BPEL("JServerJServer_ja2hop/JServerJServer_ja2hop.bpel"),

	/**
	 * JServerJServer2ホップ翻訳サービスのJServerのWSDL。
	 */
	JSERVERJSERVER_JA2HOP_WSDL_JSERVER("JServerJServer_ja2hop/JServer.wsdl"),

	/**
	 * JServer翻訳を呼び出すBPELのWSDL。
	 */
	JSERVERPROCESS_WSDL_NOPLINK("JServerProcess_NoPlink.wsdl"),

	/**
	 * JServer翻訳を呼び出すBPELのWSDL。
	 */
	JSERVERPROCESS_BPEL_NOPLINK("JServerProcess_NoPlink.bpel"),

	/**
	 * JServerとCLWTを使った多言語翻訳のBPEL。
	 */
	MULTILINGALTRANSLATIONJSERVER_BPEL(
			"MultilingualTranslationJServer/MultilingualTranslationJServer.bpel"),

	/**
	 * JServerとCLWTを使った多言語翻訳のWSDL。
	 */
	MULTILINGALTRANSLATIONJSERVER_WSDL(
			"MultilingualTranslationJServer/MultilingualTranslationJServer.wsdl"),

	/**
	 * JServerとCLWTを使った多言語翻訳が参照するJServerのWSDL。
	 */
	MULTILINGALTRANSLATIONJSERVER_WSDL_JSERVER(
			"MultilingualTranslationJServer/JServer.wsdl"),

	/**
	 * JServerとCLWTを使った多言語翻訳が参照するCLWTのWSDL。
	 */
	MULTILINGALTRANSLATIONJSERVER_WSDL_CLWT(
			"MultilingualTranslationJServer/CLWT.wsdl"),

	;

	/**
	 * ファイル名を取得する。
	 * @return ファイル名
	 */
	public String getFileName(){
		return fileName;
	}

	/**
	 * リソースをロードする。
	 * @return サービス
	 * @throws IOException ロードに失敗した
	 */
	public InputStream load() throws IOException{
		return loader.load();
	}

	private Resources_1_2(String filename){
		this.fileName = filename;
		this.loader = new ResourceLoader("ws_1_2/" + filename);
	}

	private String fileName;
	private ResourceLoader loader;
}
