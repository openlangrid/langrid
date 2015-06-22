/*
 * $Id: FileStorage.java 308 2010-12-01 06:12:19Z t-nakaguchi $
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.go.nict.langrid.commons.codec.FileNameCodec;
import jp.go.nict.langrid.commons.io.FileUtil;
import jp.go.nict.langrid.repository.Storage;

/**
 * レポジトリ内に格納される各ストレージを実装する。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 308 $
 */
public class FileStorage implements Storage{
	/**
	 * コンストラクタ。
	 * @param repositoryPath レポジトリのパス
	 * @param storageName このストレージの名前
	 */
	public FileStorage(File repositoryPath, String storageName){
		path_ = getStorageFile(repositoryPath, storageName);
		name_ = storageName;
	}

	/**
	 * ストレージの名前を取得する。
	 * @return ストレージの名前
	 */
	public String getName(){
		return name_;
	}

	/**
	 * ストレージが存在するかどうかを返す。
	 * @return 存在する場合true
	 */
	public boolean exists(){
		return path_.exists();
	}

	/**
	 * ストレージを作成する。
	 * 既にストレージが存在する場合、失敗する。
	 * レポジトリ内に、ストレージ名と同じ名前のディレクトリを作成する。
	 * @return 作成に成功した場合true
	 */
	public boolean create(){
		return path_.mkdir();
	}

	/**
	 * ストレージを破棄する。
	 * clearEntitiesを呼び出して内容を消去した後、ストレージ自身を破棄します。
	 * @throws IOException 削除に失敗した
	 */
	public void delete()
		throws IOException
	{
		if(!exists()) return;
		clearEntities();
		if(!path_.delete()){
			throw new IOException("failed to delete storage path");
		}
	}

	/**
	 * エンティティ名を列挙する。
	 * @return エンティティ名の配列
	 */
	public String[] listEntityNames(){
		return path_.list(new FilenameFilter(){
			public boolean accept(File dir, String name){
				if(new File(dir, codec.encode(name)).isFile()) return true;
				return false;
			}
		});
	}

	/**
	 * ストレージをクリアする。
	 * @throws FileNotFoundException ストレージが見つからない
	 */
	public void clearEntities()
		throws IOException
	{
		if(!FileUtil.forceDelete(path_)){
			throw new IOException("failed to delete");
		}
	}

	/**
	 * ストレージ内にエンティティが存在するかどうかを返す。
	 * @param entityName 存在を確認するファイル名
	 * @return 存在する場合true
	 */
	public boolean entityExists(String entityName){
		return getEntityFile(entityName).exists();
	}

	/**
	 * ストレージ内のエンティティへの読み込みストリームを作成して返す。
	 * @param aFileName ストリームを作成するエンティティ名
	 * @return 作成された読み込みストリーム
	 * @throws FileNotFoundException 情報を読み込むファイルを開けない
	 */
	public InputStream getInputStream(String aFileName)
		throws IOException
	{
		if(!exists()) create();
		return new FileInputStream(getEntityFile(aFileName));
	}

	/**
	 * ストレージ内のエンティティへの書き出しストリームを作成して返す。
	 * @param entityName ストリームを作成するエンティティ名
	 * @return 作成された書き出しストリーム
	 * @throws FileNotFoundException 情報を書き出すファイルを開けない
	 */
	public OutputStream getOutputStream(String entityName)
		throws IOException
	{
		if(!exists()) create();
		return new FileOutputStream(getEntityFile(entityName));
	}

	/**
	 * ユニークエンティティを作成する。
	 * エンティティ名がユニークでない場合、falseを返す。
	 * @return ユニークな場合true
	 * @throws IOException エンティティの作成に失敗した
	 */
	public boolean createEntity(String entityName) throws IOException{
		if(!exists()) create();
		return getEntityFile(entityName).createNewFile();
	}

	/**
	 * ストレージ内のエンティティを削除する。
	 * @param entityName エンティティ名
	 * @throws IOException 削除に失敗した
	 */
	public void deleteEntity(String entityName) throws IOException {
		if(!getEntityFile(entityName).delete()){
			throw new IOException("failed to delete file");
		}
	}

	protected File getStorageFile(File repositoryPath, String storageName){
		return new File(
				repositoryPath
				, codec.encode(storageName)
				);
	}

	/**
	 * エンティティ名からファイルを取得する。
	 * @param entityName ファイルを取得するエンティティ名
	 * @return ファイル
	 */
	protected File getEntityFile(String entityName){
		return new File(path_, codec.encode(entityName));
	}
	
	private File path_;
	private String name_;
	private static FileNameCodec codec;
	static{
		codec = FileNameCodec.getInstance();
	}
}
