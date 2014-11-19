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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GridAlreadyExistsException;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.GridNotFoundException;
import jp.go.nict.langrid.dao.entity.Grid;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicGridDao implements GridDao {
	public JsonicGridDao(JsonicDaoContext context){
		this.context = context;
	}

	@Override
	public void clear() throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Grid> listAllGrids() throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addGrid(Grid grid)
	throws GridAlreadyExistsException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteGrid(String gridId)
	throws GridNotFoundException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Grid getGrid(String gridId)
	throws GridNotFoundException, DaoException {
		try{
			return JsonicUtil.decode(getFile(gridId), Grid.class);
		} catch(FileNotFoundException e){
			throw new GridNotFoundException(gridId);
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isGridExist(String gridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	private File getFile(String gridId){
		return new File(getBaseDir(), gridId + ".json");
	}
	
	private File getBaseDir(){
		return context.getBaseDir();
	}

	private JsonicDaoContext context;
}
