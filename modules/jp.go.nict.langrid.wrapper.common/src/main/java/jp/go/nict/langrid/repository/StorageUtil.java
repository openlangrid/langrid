/*
 * $Id: StorageUtil.java 308 2010-12-01 06:12:19Z t-nakaguchi $
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
package jp.go.nict.langrid.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;

/**
 * Storageクラスに関するユーティリティメソッドを集めたクラス。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 308 $
 */
public class StorageUtil {
	/**
	 * 指定されたプリフィクスを使い、ユニークな名前を作成してストレージを作成する。
	 * 返されたストレージは存在する。
	 * @param storage ストレージ
	 * @param prefix ストレージ名の先頭に付加する文字列
	 * @return 作成されたストレージ
	 * @throws IOException ストレージの作成に失敗した
	 */
	public static String createUniqueEntity(
			Storage storage, String prefix)
		throws IOException
	{
		for(int i = 0; i < 10000; i++){
			String name = prefix + StringUtil.randomString(8);
			if(storage.createEntity(name)){
				return name;
			}
		}
		throw new IOException("failed to create unique entity.");
	}

	/**
	 * エンティティの内容を読み込む。
	 * @param storage エンティティを保持するストレージ
	 * @param entityName エンティティの名前
	 * @return 読み込まれた文字列
	 * @throws IOException 情報の読み込みに失敗した
	 */
	public static byte[] loadBytes(
			Storage storage, String entityName)
		throws IOException
	{
		InputStream is = storage.getInputStream(entityName);
		try{
			return StreamUtil.readAsBytes(is);
		} finally{
			is.close();
		}
	}

	/**
	 * エンティティの内容を文字列として読み込む。
	 * @param storage エンティティを保持するストレージ
	 * @param entityName エンティティの名前
	 * @param decoder 読み込み時に使用するデコーダ
	 * @return 読み込まれた文字列
	 * @throws IOException 情報の読み込みに失敗した
	 */
	public static String loadString(
			Storage storage, String entityName, CharsetDecoder decoder)
		throws IOException
	{
		InputStream is = storage.getInputStream(entityName);
		try{
			return StreamUtil.readAsString(is, decoder);
		} finally{
			is.close();
		}
	}

	/**
	 * ストレージ内のエンティティに文字列を書き出す。
	 * @param storage エンティティを保持するストレージ
	 * @param entityName エンティティの名前
	 * @param data 書き出す文字列
	 * @throws IOException 情報の書き出しに失敗した
	 */
	public static void storeBytes(
			Storage storage, String entityName, byte[] data)
		throws IOException
	{
		OutputStream os = storage.getOutputStream(entityName);
		try{
			os.write(data);
		} finally{
			os.flush();
			os.close();
		}
	}

	/**
	 * ストレージ内のエンティティに文字列を書き出す。
	 * @param storage エンティティを保持するストレージ
	 * @param entityName エンティティの名前
	 * @param data 書き出す文字列
	 * @param encoder 文字列のエンコーダ
	 * @throws IOException 情報の書き出しに失敗した
	 */
	public static void storeString(
			Storage storage, String entityName
			, String data, CharsetEncoder encoder)
		throws IOException
	{
		OutputStream os = storage.getOutputStream(entityName);
		try{
			StreamUtil.writeString(os, data, encoder);
		} finally{
			os.flush();
			os.close();
		}
	}
}
