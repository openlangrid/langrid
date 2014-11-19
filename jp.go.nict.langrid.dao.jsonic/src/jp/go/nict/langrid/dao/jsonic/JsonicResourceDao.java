/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2014 Language Grid Project.
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

import java.util.Collections;
import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ResourceAlreadyExistsException;
import jp.go.nict.langrid.dao.ResourceDao;
import jp.go.nict.langrid.dao.ResourceNotFoundException;
import jp.go.nict.langrid.dao.ResourceSearchResult;
import jp.go.nict.langrid.dao.entity.Resource;

public class JsonicResourceDao implements ResourceDao {

	@Override
	public void clear() throws DaoException {
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Resource> listAllResources(String resourceGridId)
	throws DaoException {
		return Collections.EMPTY_LIST;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Resource> listResourcesOfUser(String userGridId, String userId)
	throws DaoException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public ResourceSearchResult searchResources(int startIndex, int maxCount,
			String resourceGridId, MatchingCondition[] conditions,
			Order[] orders) throws DaoException {
		return new ResourceSearchResult(new Resource[]{}, 0, true);
	}

	@Override
	public void addResource(Resource resource) throws DaoException,
			ResourceAlreadyExistsException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteResource(String resourceGridId, String resourceId)
			throws ResourceNotFoundException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteResourcesOfGrid(String gridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteResourcesOfUser(String userGridId, String userId)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Resource getResource(String resourceGridId, String resourceId)
	throws ResourceNotFoundException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isResourceExist(String resourceGridId, String resourceId)
	throws DaoException {
		return false;
	}
}
