/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jp.go.nict.langrid.commons.io.RegexFileNameFilter;
import jp.go.nict.langrid.commons.util.function.Consumer;
import jp.go.nict.langrid.dao.AbstractDaoContext;
import jp.go.nict.langrid.dao.ConnectException;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.EntityListener;
import jp.go.nict.langrid.dao.TransactionListener;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicDaoContext
extends AbstractDaoContext
implements DaoContext{
	public File getBaseDir(){
		return baseDir;
	}

	public File getGridsBaseDir(){
		return new File(baseDir, "grids");
	}

	public File getGridBaseDir(String gridId){
		return new File(getGridsBaseDir(), gridId);
	}

	public File getDomainsBaseDir(){
		return new File(baseDir, "domains");
	}

	public File getDomainBaseDir(String domainId){
		return new File(getDomainsBaseDir(), domainId);
	}

	public <T> List<T> listAll(File path, Class<T> elementClass)
	throws DaoException{
		return listAll(path, elementClass, new Consumer<T>() {
			@Override
			public void accept(T value) {
			}
		});
	}

	public <T> List<T> listAll(File path, Class<T> elementClass, Consumer<T> proc)
	throws DaoException{
		List<T> users = new ArrayList<T>();
		File[] files = path.listFiles(new RegexFileNameFilter(".*\\.json"));
		if(files == null) return users;
		try{
			for(File f : files){
				T e = JsonicUtil.decode(f, elementClass);
				proc.accept(e);
				users.add(e);
			}
			return users;
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	@Override
	public <T> void addEntityListener(Class<T> clazz, EntityListener<T> listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> void removeEntityListener(Class<T> clazz, EntityListener<T> listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addTransactionListener(TransactionListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeTransactionListener(TransactionListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void beginTransaction() throws ConnectException, DaoException {
	}

	@Override
	public void commitTransaction() throws DaoException {
	}

	@Override
	public void rollbackTransaction() throws DaoException {
	}

	@Override
	public int getTransactionNestCount() {
		return 0;
	}

	@Override
	public <T> T loadEntity(Class<T> clazz, Serializable id)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void mergeEntity(Object entity) throws DaoException {
		// do nothing
	}

	@Override
	public void updateEntity(Object entity) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void refreshEntity(Object entity) throws DaoException {
		throw new UnsupportedOperationException();
	}

	public static void setContextDir(File contextDir){
		JsonicDaoContext.contextDir = contextDir;
		JsonicDaoContext.baseDir = new File(contextDir, basePath);
	}

	private static File contextDir = new File("./");
	private static String basePath = "";
	private static File baseDir;
	private static void loadProperties() throws IOException{
		InputStream is = JsonicDaoContext.class.getResourceAsStream("/jsonicdao.properties");
		if(is == null) return;
		try{
			Properties p = new Properties();
			p.load(is);
			Object dir = p.get("basePath");
			if(dir != null){
				basePath = dir.toString();
			}
			baseDir = new File(contextDir, basePath); 
		} finally{
			is.close();
		}
	}
	static{
		try{
			loadProperties();
		} catch(IOException e){
			throw new RuntimeException(e);
		}
	}
}
