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
import java.util.Collections;
import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.FederationAlreadyExistsException;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.FederationNotFoundException;
import jp.go.nict.langrid.dao.entity.Federation;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicFederationDao implements FederationDao {
	public JsonicFederationDao(JsonicDaoContext context){
		this.context = context;
	}

	@Override
	public void clear() throws DaoException {
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Federation> list() throws DaoException {
		return Collections.EMPTY_LIST;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Federation> listFromOldest() throws DaoException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<String> listTargetGridIds(String sourceGridId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<String> listSourceGridIds(String targetGridId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Federation> listFederationsFrom(String sourceGridId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Federation> listFederationsToward(String targetGridId)
	throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFederationExist(String sourceGridId, String targetGridId)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Federation getFederation(String sourceGridId, String targetGridId)
	throws FederationNotFoundException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addFederation(Federation federation)
	throws FederationAlreadyExistsException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addFederation(String sourceGridId, String targetGridId)
	throws FederationAlreadyExistsException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteFederation(String sourceGridId, String targetGridId)
	throws FederationNotFoundException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteFederationsOf(String gridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRequesting(String sourceGridId, String targetGridId,
			boolean isRequesting) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setConnected(String sourceGridId, String targetGridId,
			boolean isRequesting) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unused")
	private File getFile(String gridId){
		return new File(getBaseDir(), gridId + ".json");
	}
	
	private File getBaseDir(){
		return context.getBaseDir();
	}

	private JsonicDaoContext context;
}
