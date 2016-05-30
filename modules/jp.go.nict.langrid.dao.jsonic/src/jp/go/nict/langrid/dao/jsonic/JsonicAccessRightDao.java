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

import java.util.List;

import jp.go.nict.langrid.commons.util.ListUtil;
import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.AccessRightNotFoundException;
import jp.go.nict.langrid.dao.AccessRightSearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.dao.entity.ServicePK;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicAccessRightDao implements AccessRightDao {
	public JsonicAccessRightDao(JsonicDaoContext context){
		this.context = context;
	}

	@Override
	public void clear() throws DaoException {
	}

	@Override
	public void clearExceptDefaults() throws DaoException {
	}

	@Override
	public List<AccessRight> listAccessRights(String serviceGridId)
	throws DaoException {
		return ListUtil.emptyList();
	}

	@Override
	public Iterable<ServicePK> listAccessibleServices(String userGridId,
			String userid) throws DaoException {
		return ListUtil.emptyList();
	}

	@Override
	public AccessRightSearchResult searchAccessRights(int startIndex,
			int maxCount, String userGridId, String userId,
			String serviceGridId, String[] serviceIds, Order[] orders)
			throws DaoException {
		return new AccessRightSearchResult(new AccessRight[]{}, 0, true);
	}

	@Override
	public AccessRightSearchResult searchAccessRightsAccordingToDefaultAndOwner(
			int startIndex, int maxCount, String userGridId, String userId,
			String serviceGridId, String[] serviceIds, String ownerUserId,
			Order[] orders) throws DaoException {
		return new AccessRightSearchResult(new AccessRight[]{}, 0, true);
	}

	@Override
	public AccessRight getAccessRight(String userGridId, String userId,
			String serviceGridId, String serviceId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public AccessRight setAccessRight(String userGridId, String userId,
			String serviceGridId, String serviceId, boolean permitted)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAccessRight(String userGridId, String userId,
			String serviceGridId, String serviceId)
			throws AccessRightNotFoundException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public AccessRight getGridDefaultAccessRight(String userGridId,
			String serviceGridId, String serviceId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public AccessRight setGridDefaultAccessRight(String userGridId,
			String serviceGridId, String serviceId, boolean permitted)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteGridDefaultAccessRight(String userGridId,
			String serviceGridId, String serviceId)
			throws AccessRightNotFoundException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void adjustUserRights(String userGridId, String serviceGridId,
			String serviceId, String ownerUserId, boolean permitted)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public AccessRight getServiceDefaultAccessRight(String serviceGridId,
			String serviceId) throws DaoException {
		return new AccessRight("*", "*", "*", "*", true);
	}

	@Override
	public AccessRight setServiceDefaultAccessRight(String serviceGridId,
			String serviceId, boolean permitted) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void adjustGridDefaultRights(String serviceGridId, String serviceId,
			boolean permitted) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAccessRightsOfGrid(String gridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAccessRightsOfService(String serviceGridId,
			String serviceId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAccessRightsOfUser(String userGridId, String userId)
			throws DaoException {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unused")
	private JsonicDaoContext context;
}
