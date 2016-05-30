/*
 * $Id: AccessRightLogic.java 302 2010-12-01 02:49:42Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.management.logic;

import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.AccessRightNotFoundException;
import jp.go.nict.langrid.dao.AccessRightSearchResult;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ServiceNotFoundException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.AccessLimit;
import jp.go.nict.langrid.dao.entity.AccessRight;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class AccessRightLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public AccessRightLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getAccessRightDao().clearExceptDefaults();
	}

	@DaoTransaction
	public AccessRightSearchResult searchAccessRights(
			String ownerUserId, int startIndex, int maxCount
			, String serviceGridId, String serviceId
			, String userGridId
			, Order[] orders
			)
		throws DaoException
	{
		return getAccessRightDao().searchAccessRightsAccordingToDefaultAndOwner(
				startIndex, maxCount, userGridId, "", serviceGridId, new String[]{serviceId}
				, ownerUserId
				, orders
				);
	}

	@DaoTransaction
	public AccessRight getAccessRight(
			String serviceGridId, String serviceId
			, String userGridId, String userId)
	throws DaoException, ServiceNotFoundException, UserNotFoundException{
		if(!getServiceDao().isServiceExist(serviceGridId, serviceId)){
			throw new ServiceNotFoundException(
					serviceGridId, serviceId
					);
		}
		if(!userGridId.equals("*") && !userId.equals("*")){
			if(!getUserDao().isUserExist(userGridId, userId)){
				throw new UserNotFoundException(
						userGridId, userId
						);
			}
		}
		AccessRightDao dao = getAccessRightDao();

		AccessRight r = dao.getAccessRight(
				userGridId, userId
				, serviceGridId, serviceId
				);
		if(r == null){
			r = dao.getGridDefaultAccessRight(userGridId, serviceGridId, serviceId);
		}
		if(r == null){
			r = dao.getServiceDefaultAccessRight(serviceGridId, serviceId);
		}
		if(r != null){
			return new AccessRight(
					userGridId, userId, serviceGridId, serviceId
					, r.isPermitted()
					);
		} else{
			return null;
		}
	}

	@DaoTransaction
	public void setAccessRight(
			String serviceOwnerUserId, String serviceGridId, String serviceId
			, String userGridId, String userId
			, boolean permitted
	)
	throws DaoException, UserNotFoundException{
		if(!userId.equals("*") && !getUserDao().isUserExist(userGridId, userId)){
			throw new UserNotFoundException(userGridId, userId);
		}
		if(!getServiceDao().isServiceExist(serviceGridId, serviceId)){
			throw new ServiceNotFoundException(serviceGridId, serviceId);
		}

		AccessRight sdef = getAccessRightDao().getServiceDefaultAccessRight(
				serviceGridId, serviceId);
		if(sdef == null){
			throw new DaoException(
					"default access right of \"" + serviceId
					+ "\" is not set.");
		}
		AccessRight gdef = null;
		if(!userGridId.equals("*")){
			gdef = getAccessRightDao().getGridDefaultAccessRight(
					userGridId, serviceGridId, serviceId);
		}

		if(userGridId.equals("*")){
			if(!userId.equals("*")){
				throw new IllegalArgumentException("userId must be '*' when userGridId is '*'");
			}
			setServiceDefault(sdef, serviceGridId, serviceId, permitted);
		} else if(userId.equals("*")){
			setGridDefault(
					sdef, gdef, userGridId, serviceGridId, serviceId
					, serviceOwnerUserId, permitted
					);
		} else{
			AccessRight def = (gdef != null) ? gdef : sdef;
			setUser(def, userGridId, userId, serviceGridId, serviceId, permitted);
		}
	}

	@DaoTransaction
	public void moveToMembersOnly(String serviceGridId, String serviceId)
	throws DaoException{
		AccessRightDao arDao = getAccessRightDao();
		arDao.deleteAccessRightsOfService(serviceGridId, serviceId);
		arDao.setServiceDefaultAccessRight(serviceGridId, serviceId, false);
	}

	@DaoTransaction
	public void moveToForAllUsers(String serviceGridId, String serviceId)
	throws DaoException{
		AccessRightDao arDao = getAccessRightDao();
		arDao.deleteAccessRightsOfService(serviceGridId, serviceId);
		arDao.setServiceDefaultAccessRight(serviceGridId, serviceId, true);
	}

	private void setServiceDefault(
			AccessRight serviceDefaultRight, String serviceGridId, String serviceId, boolean permitted)
	throws DaoException{
		AccessRightDao arDao = getAccessRightDao();
		if(serviceDefaultRight.isPermitted() == permitted) return;

		arDao.setServiceDefaultAccessRight(serviceGridId, serviceId, permitted);
		arDao.adjustGridDefaultRights(serviceGridId, serviceId, permitted);
	}

	private void setGridDefault(
			AccessRight serviceDefaultRight, AccessRight gridDefaultRight
			, String userGridId
			, String serviceGridId, String serviceId
			, String serviceOwnerUserId
			, boolean permitted)
	throws DaoException{
		AccessRightDao arDao = getAccessRightDao();
		if(gridDefaultRight != null){
			if(gridDefaultRight.isPermitted() == permitted) return;
		} else{
			if(serviceDefaultRight.isPermitted() == permitted) return;
		}
		if(serviceDefaultRight.isPermitted() == permitted){
			arDao.deleteGridDefaultAccessRight(userGridId, serviceGridId, serviceId);
		} else{
			arDao.setGridDefaultAccessRight(userGridId, serviceGridId, serviceId, permitted);
		}
		arDao.adjustUserRights(userGridId, serviceGridId, serviceId, serviceOwnerUserId, permitted);
		/*
		AccessRightDao arDao = getAccessRightDao();
		if(defaultRight.isPermitted() == permitted) return;
		jp.go.nict.langrid.dao.AccessRightSearchResult res
			= arDao.searchAccessRightsAccordingToDefaultAndOwner(
				0, 10000, userGridId, "", serviceGridId, array(serviceId)
				, serviceOwnerUserId
				, array(new jp.go.nict.langrid.dao.Order(
						"userId"
						, jp.go.nict.langrid.dao.OrderDirection.ASCENDANT
						))
				);
		for(AccessRight ar : res.getElements()){
			if(ar.isPermitted() == permitted){
				arDao.deleteAccessRight(userGridId, ar.getUserId(), serviceGridId, ar.getServiceId());
			} else{
				arDao.setAccessRight(userGridId, ar.getUserId(), serviceGridId, ar.getServiceId()
						, !permitted);
			}
		}
		arDao.setServiceDefaultAccessRight(serviceGridId, serviceId, permitted);
		*/
	}

	private void setUser(AccessRight defaultRight
			, String userGridId, String userId, String serviceGridId, String serviceId
			, boolean permitted)
	throws DaoException{
		AccessRightDao arDao = getAccessRightDao();
		if(defaultRight.isPermitted() != permitted){
			arDao.setAccessRight(userGridId, userId, serviceGridId, serviceId, permitted);
		} else{
			arDao.deleteAccessRight(userGridId, userId, serviceGridId, serviceId);
		}
		AccessLimitDao alDao = getAccessLimitDao();
		alDao.deleteAccessLimits(userGridId, userId, serviceGridId, serviceId);
		if(permitted){
			for(AccessLimit l : alDao.getServiceDefaultAccessLimits(userGridId
					, serviceGridId, serviceId)){
				alDao.setAccessLimit(userGridId, userId, serviceGridId, serviceId, l.getPeriod()
						, l.getLimitType(), l.getLimitCount()
						);
			}
		}
	}

	@DaoTransaction
	public void deleteAccessRight(
			String serviceGridId, String serviceId
			, String userGridId, String userId
			)
	throws DaoException, AccessRightNotFoundException{
		getAccessRightDao().deleteAccessRight(userGridId, userId
				, serviceGridId, serviceId);
	}
}
