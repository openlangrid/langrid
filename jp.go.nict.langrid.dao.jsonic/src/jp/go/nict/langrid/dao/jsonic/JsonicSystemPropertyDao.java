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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.SystemPropertyDao;
import jp.go.nict.langrid.dao.entity.SystemProperty;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicSystemPropertyDao implements SystemPropertyDao {
	public JsonicSystemPropertyDao(JsonicDaoContext context){
		this.context = context;
	}

	@Override
	public void clear() throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<SystemProperty> listAllProperties(String gridId)
			throws DaoException {
		try{
			File file = new File(getBaseDir(gridId), "SystemProperty.json");
			Map map = JsonicUtil.decode(file, Map.class);
			List<SystemProperty> props = new ArrayList<SystemProperty>();
			for(Object entry : map.entrySet()){
				Map.Entry et = (Map.Entry)entry;
				props.add(new SystemProperty(
						gridId
						, et.getKey().toString()
						, et.getValue().toString()
						));
			}
			return props;
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public String getProperty(String gridId, String name) throws DaoException {
		try{
			File file = new File(getBaseDir(gridId), "SystemProperty.json");
			Map map = JsonicUtil.decode(file, Map.class);
			return map.get(name).toString();
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	@Override
	public void setProperty(String gridId, String name, String value) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deletePropertiesOfGrid(String gridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	private File getBaseDir(String gridId){
		return new File(context.getBaseDir(), gridId);
	}

	private JsonicDaoContext context;
}
