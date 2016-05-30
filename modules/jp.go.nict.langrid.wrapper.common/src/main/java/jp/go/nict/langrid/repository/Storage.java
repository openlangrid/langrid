/*
 * $Id:ServiceProfileWriter.java 3990 2007-01-15 10:00:33Z nakaguchi $
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

/**
 * 抽象ストレージ。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 308 $
 */
public interface Storage {
	/**
	 * このストレージが存在するかどうかを返す。
	 * @return 存在する場合true
	 */
	boolean exists();

	/**
	 * このストレージの名前を取得する。
	 * @return 名前
	 */
	String getName();

	/**
	 * ストレージを作成する。
	 * 既に存在する場合、falseを返す。
	 * @return 作成された場合true
	 * @throws IOException 作成に失敗した
	 */
	boolean create() throws IOException;
	
	/**
	 * このストレージを削除する。
	 * 存在しない場合は何もしない。
	 * @throws IOException 削除に失敗した
	 */
	void delete() throws IOException;


	/**
	 * エンティティ名を列挙する。
	 * @return エンティティ名の配列
	 */
	String[] listEntityNames();

	/**
	 * このストレージ内のエンティティを全て削除する。
	 * @throws IOException 削除に失敗した
	 */
	void clearEntities() throws IOException;


	/**
	 * このストレージ内にエンティティが存在するかどうかを返す。
	 * @param entityName エンティティ名
	 * @return 存在する場合true
	 */
	boolean entityExists(String entityName);

	/**
	 * 入力ストリームを取得する。
	 * エンティティが存在しない場合、作成される。
	 * @param entityName エンティティ名
	 * @return 入力ストリーム
	 * @throws IOException 入力ストリームの取得に失敗した
	 */
	InputStream getInputStream(String entityName) throws IOException;

	/**
	 * 出力ストリームを取得する。
	 * エンティティが存在しない場合、作成される。
	 * @param entityName エンティティ名
	 * @return 出力ストリーム
	 * @throws IOException 出力ストリームの取得に失敗した
	 */
	OutputStream getOutputStream(String entityName) throws IOException;

	/**
	 * ユニークエンティティを作成する。
	 * エンティティ名がユニークでない場合、falseを返す。
	 * @param entityName エンティティ名
	 * @return ユニークな場合true
	 * @throws IOException エンティティの作成に失敗した
	 */
	boolean createEntity(String entityName) throws IOException;

	/**
	 * エンティティを削除する。
	 * 存在しない場合は何もしない。
	 * @param entityName 削除するエンティティの名前
	 * @throws IOException 削除に失敗した
	 */
	void deleteEntity(String entityName) throws IOException;
}
