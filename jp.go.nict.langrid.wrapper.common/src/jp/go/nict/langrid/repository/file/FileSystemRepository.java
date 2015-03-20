/*
 * $Id: FileSystemRepository.java 308 2010-12-01 06:12:19Z t-nakaguchi $
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
package jp.go.nict.langrid.repository.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.commons.io.FileUtil;
import jp.go.nict.langrid.repository.Storage;
import jp.go.nict.langrid.repository.StorageRepository;

/**
 * ファイルシステムを使用したレポジトリの基本機能を提供する。
 * レポジトリ内では、各ストレージ毎にサブディレクトリが割り当てられる。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 308 $
 */
public class FileSystemRepository implements StorageRepository{
	/**
	 * コンストラクタ。レポジトリとして使用するディレクトリを指定する。
	 * @param aRepositoryPath レポジトリとして使用するディレクトリ
	 * @throws IOException レポジトリの構築に失敗した
	 */
	public FileSystemRepository(File aRepositoryPath)
		throws IOException
	{
		assert aRepositoryPath != null;
		repositoryPath = aRepositoryPath;
		if(!FileUtil.assertDirectoryExists(aRepositoryPath)){
			throw new IOException(
				"failed to create repository directory: "
				+ aRepositoryPath.toString()
				);
		}
	}

	/**
	 * リポジトリ内のストレージ一覧を返す。
	 * @return ストレージ一覧
	 */
	public Iterable<Storage> listStorages(){
		List<Storage> storages = new ArrayList<Storage>();
		String[] dirs = repositoryPath.list(new FilenameFilter(){
			public boolean accept(File dir, String name){
				if(new File(dir, name).isDirectory()) return true;
				return false;
			}
		});
		for(String dir : dirs){
			storages.add(getStorage(dir));
		}
		return storages;
	}

	/**
	 * ストレージを取得する。返されたストレージが既に存在するとは限らない。
	 * @param name 取得するストレージの名前
	 * @return ストレージ
	 */
	public FileStorage getStorage(String name){
		return new FileStorage(repositoryPath, name);
	}

	/**
	 * リポジトリ内容を消去する。
	 * @throws IOException リポジトリ内容の消去に失敗した
	 */
	public void clearStorages() throws IOException{
		if(!FileUtil.forceDelete(repositoryPath)){
			throw new IOException("can't delete files in: \"" + repositoryPath + "\"");
		}
	}

	/**
	 * リポジトリ自体を削除する。
	 * @throws IOException リポジトリの削除に失敗した
	 */
	public void delete() throws IOException{
		clearStorages();
		if(!repositoryPath.delete()){
			throw new IOException("can't delete path: \"" + repositoryPath + "\"");
		}
	}

	private File repositoryPath;
}
