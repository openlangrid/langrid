/*
 * $Id: StorageRepository.java 308 2010-12-01 06:12:19Z t-nakaguchi $
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

/**
 * リポジトリを定義するインターフェース。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 308 $
 */
public interface StorageRepository {
	/**
	 * リポジトリ内のストレージ一覧を返す。
	 * @return ストレージ一覧
	 */
	Iterable<Storage> listStorages();

	/**
	 * ストレージを取得する。返されたストレージが既に存在するとは限らない。
	 * @param name 取得するストレージの名前
	 * @return ストレージ
	 */
	Storage getStorage(String name);

	/**
	 * リポジトリ内容を消去する。
	 * @throws IOException リポジトリ内容の消去に失敗した
	 */
	void clearStorages() throws IOException;

	/**
	 * リポジトリ自体を削除する。
	 * @throws IOException リポジトリの削除に失敗した
	 */
	void delete() throws IOException;
}
