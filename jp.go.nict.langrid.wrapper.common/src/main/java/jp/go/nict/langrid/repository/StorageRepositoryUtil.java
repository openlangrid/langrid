/*
 * $Id: StorageRepositoryUtil.java 308 2010-12-01 06:12:19Z t-nakaguchi $
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

import jp.go.nict.langrid.commons.lang.StringUtil;

/**
 * リポジトリに関するユーティリティメソッドを集めたクラス。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 308 $
 */
public class StorageRepositoryUtil {
	/**
	 * 指定されたプリフィクスを使い、ユニークな名前を作成してストレージを作成する。
	 * 返されたストレージは存在する。
	 * @param repository リポジトリ
	 * @param prefix ストレージ名の先頭に付加する文字列
	 * @return 作成されたストレージ
	 * @throws IOException ストレージの作成に失敗した
	 */
	public static Storage createUniqueStorage(
			StorageRepository repository, String prefix)
		throws IOException
	{
		for(int i = 0; i < 1000; i++){
			Storage s = repository.getStorage(
					prefix + StringUtil.randomString(8)
					);
			if(s.create()) return s;
		}
		throw new IOException("failed to create unique storage.");
	}
}
