/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) Language Grid Project.
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
package jp.go.nict.langrid.dao.jsonic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import jp.go.nict.langrid.commons.io.RegexFileNameFilter;
import jp.go.nict.langrid.commons.util.function.Functions;
import jp.go.nict.langrid.dao.DaoException;

public abstract class AbstractJsonicDao {
	public AbstractJsonicDao(JsonicDaoContext context){
		this.context = context;
	}

	public JsonicDaoContext getContext() {
		return context;
	}

	public <T> List<T> listAll(File path, Class<T> elementClass)
	throws DaoException{
		return listAll(path, elementClass, Functions.<T>truePredicate(), Functions.<T>nullComsumer());
	}

	public <T> List<T> listAll(File path, Class<T> elementClass,
			Predicate<T> filter, Consumer<T> proc)
	throws DaoException{
		List<T> users = new ArrayList<T>();
		File[] files = path.listFiles(new RegexFileNameFilter(".*\\.json"));
		if(files == null) return users;
		try{
			for(File f : files){
				T e = JsonicUtil.decode(f, elementClass);
				if(filter.test(e)){
					proc.accept(e);
					users.add(e);
				}
			}
			return users;
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	private JsonicDaoContext context;
}
