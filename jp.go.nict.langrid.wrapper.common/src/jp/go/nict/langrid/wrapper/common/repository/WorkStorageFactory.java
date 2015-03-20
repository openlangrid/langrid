/*
 * $Id: WorkStorageFactory.java 265 2010-10-03 10:25:32Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.wrapper.common.repository;

import java.io.IOException;

import jp.go.nict.langrid.repository.Storage;
import jp.go.nict.langrid.repository.StorageRepository;
import jp.go.nict.langrid.repository.file.FileSystemRepository;
import jp.go.nict.langrid.wrapper.common.util.WorkFileUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public class WorkStorageFactory {
	/**
	 * 
	 * 
	 */
	public static Storage createStorage(String storageName){
		return repository.getStorage(storageName);
	}

	private static StorageRepository repository;

	static{
		try{
			repository = new FileSystemRepository(
					WorkFileUtil.getWorkDirectory()
					);
		} catch(IOException e){
			new RuntimeException(e);
		}
	}
}
