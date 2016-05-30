/*
 * $Id$
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2013 NICT Language Grid Project.
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
import java.io.FileFilter;

/**
 * 
 * @author Takao Nakaguchi
 */
public class FileFilters {
	public static FileFilter chain(final FileFilter... filters){
		return new FileFilter() {
			@Override
			public boolean accept(File file) {
				for(FileFilter f : filters){
					if(!f.accept(file)) return false;
				}
				return true;
			}
		};
	}

	public static FileFilter notEquals(final String name){
		return new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				return !pathname.getName().equals(name);
			}
		};
	}

	public static FileFilter endsWith(final String suffix){
		return new FileFilter(){
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(suffix);
			}
		};
	}

	public static FileFilter isFile(){
		return new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isFile();
			}
		};
	}

	public static FileFilter isDirectory(){
		return new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};
	}

	public static FileFilter isFileEndsWith(String suffix){
		return chain(isFile(), endsWith(suffix));
	}

	public static FileFilter isDirectoryEndsWith(String suffix){
		return chain(isDirectory(), endsWith(suffix));
	}
}
