/*
 * $Id:HibernateDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.ConnectException;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoContext;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 496 $
 */
public class FileDaoContext implements DaoContext{
	public FileDaoContext(String path){
		this.path = path;
	}

	public void beginTransaction()
	throws ConnectException, DaoException{
		int n = nest.get();
		nest.set(n + 1);
	}

	public void commitTransaction() throws DaoException {
		int n = nest.get();
		if(n == 0){
			throw new DaoException("too many commit call");
		}
		n--;
		nest.set(n);
	}

	public void rollbackTransaction() throws DaoException {
		int n = nest.get();
		if(n == 0){
			throw new DaoException("too many rollback call");
		}
		n--;
		nest.set(n);
	}

	public int getTransactionNestCount(){
		return nest.get();
	}

	public <T> T loadEntity(Class<T> clazz, Serializable id) throws DaoException{
		throw new UnsupportedOperationException();
	}

	public void mergeEntity(Object entity) throws DaoException{
		throw new UnsupportedOperationException();
	}

	public void refreshEntity(Object entity) throws DaoException{
		throw new UnsupportedOperationException();
	}

	public void updateEntity(Object entity) throws DaoException{
		beginTransaction();
		try{
			store(entity);
			commitTransaction();
		} catch(IOException e){
			rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public Blob createBlob(InputStream stream) throws IOException{
		throw new UnsupportedOperationException();
	}

	public String load(Class<?> entityClass, String id)
	throws IOException{
		return getMap(entityClass).get(id);
	}

	public <T> Collection<T> list(Class<T> entityClass)
	throws IOException{
		@SuppressWarnings("unchecked") Collection<T> ret
			= (Collection<T>)getMap(entityClass).values();
		return ret;
	}

	public void store(Object value) throws IOException{
		throw new UnsupportedOperationException();
	}

	private Map<String, String> getMap(Class<?> clazz)
	throws IOException{
		Map<String, String> map = cache.get(clazz);
		if(map == null){
			map = new LinkedHashMap<String, String>();
			try(InputStream is = new FileInputStream(new File(path, clazz.getSimpleName() + ".dat"));
				Reader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader r = new BufferedReader(isr)){
				String line = null;
				while((line = r.readLine()) != null){
					if(line.charAt(0) == '#') continue;
					String[] value = line.split(",");
					if(value.length != 2) continue;
					map.put(value[0].trim(), value[1].trim());
				}
			} catch(FileNotFoundException e){
				logger.warning("file for dao not found: " + clazz.getSimpleName() + ".dat");
			}
			cache.put(clazz, map);
		}
		return map;
	}

	private String path;
	private Map<Class<?>, Map<String, String>> cache
			= new ConcurrentHashMap<Class<?>, Map<String, String>>();

	private static ThreadLocal<Integer> nest = new ThreadLocal<Integer>(){
		protected Integer initialValue(){ return 0;}
	};

	private static Logger logger = Logger.getLogger(
			FileDaoContext.class.getName());
}
