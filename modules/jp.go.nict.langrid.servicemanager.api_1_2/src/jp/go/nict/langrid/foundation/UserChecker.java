/*
 * $Id: UserChecker.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.TemporaryUser;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserNotFoundException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class UserChecker {
	/**
	 * 
	 * 
	 */
	public static final String ROLE_ADMINISTRATOR = "langridadmin";

	/**
	 * 
	 * 
	 */
	public static final String ROLE_USER = "langriduser";

	/**
	 * 
	 * 
	 */
	public UserChecker(ServiceContext sc, DaoContext dc
			, String gridId, UserDao userDao, ServiceDao serviceDao){
		this.sc = sc;
		this.dc = dc;
		this.gridId = gridId;
		this.userDao = userDao;
		this.serviceDao = serviceDao;
	}

	/**
	 * 
	 * 
	 */
	public DaoContext getDaoContext(){
		return dc;
	}

	/**
	 * 
	 * 
	 */
	public String getGridId(){
		return gridId;
	}

	/**
	 * 
	 * 
	 */
	public String getUserId(){
		return sc.getAuthUser();
	}

	/**
	 * 
	 * 
	 */
	public User getUser()
	throws DaoException, jp.go.nict.langrid.dao.UserNotFoundException
	{
		return userDao.getUser(gridId, sc.getAuthUser());
	}

	/**
	 * 
	 * 
	 */
	public void checkAuthorized()
		throws NoAccessPermissionException
	{
		if((sc.getAuthUser() == null) || (sc.getAuthUser().length() == 0)){
			throw new NoAccessPermissionException("");
		}
	}

	/**
	 * 
	 * 
	 */
	public boolean isAuthUserAdmin()
	throws ServiceConfigurationException
	{
		try{
			return userDao.hasUserRole(gridId, sc.getAuthUser(), ROLE_ADMINISTRATOR);
		} catch(jp.go.nict.langrid.dao.DaoException e){
			throw new ServiceConfigurationException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public void checkAuthUserAdmin()
		throws NoAccessPermissionException, ServiceConfigurationException
	{
		try{
			if(!userDao.hasUserRole(gridId, sc.getAuthUser(), ROLE_ADMINISTRATOR)){
				throw new NoAccessPermissionException(sc.getAuthUser());
			}
		} catch(jp.go.nict.langrid.dao.DaoException e){
			throw new ServiceConfigurationException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public void checkAuthUserEquals(String userId)
		throws NoAccessPermissionException
	{
		if(!sc.getAuthUser().equals(userId)){
			throw new NoAccessPermissionException(sc.getAuthUser());
		}
	}

	/**
	 * 
	 * 
	 */
	public void checkAuthUserExists()
		throws NoAccessPermissionException, ServiceConfigurationException
	{
		try{
			if(!userDao.isUserExist(gridId, sc.getAuthUser())){
				throw new NoAccessPermissionException(sc.getAuthUser());
			}
		} catch(jp.go.nict.langrid.dao.DaoException e){
			throw new ServiceConfigurationException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public void checkAuthUserIsServiceOwner(String serviceId)
		throws NoAccessPermissionException, ServiceConfigurationException
		, UserNotFoundException, ServiceNotFoundException
	{
		if(isAuthUserAdmin()) return;
		if(serviceId.equals("*")) return;

		try{
			dc.beginTransaction();
			try{
				User u = getUser();
				if(!serviceDao.getService(gridId, serviceId).getOwnerUserId().equals(u.getUserId())){
						throw new NoAccessPermissionException(sc.getAuthUser());
				}
			} finally{
				dc.commitTransaction();
			}
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw new UserNotFoundException(e.getUserId());
		} catch(jp.go.nict.langrid.dao.ServiceNotFoundException e){
			throw new ServiceNotFoundException(serviceId);
		} catch(DaoException e){
			throw new ServiceConfigurationException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public void checkAuthUserIsResourceOwner(String resourceId)
	throws NoAccessPermissionException, ServiceConfigurationException
	, UserNotFoundException
	{
		if(isAuthUserAdmin()) return;
		if(resourceId.equals("*")) return;
		
		try{
			dc.beginTransaction();
			try{
				User u = getUser();
				Resource resource = DaoFactory.createInstance().createResourceDao().getResource(
						u.getGridId(), resourceId);
				if(!resource.getOwnerUserId().equals(u.getUserId())){
					throw new NoAccessPermissionException(sc.getAuthUser());
				}
			} finally{
				dc.commitTransaction();
			}
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw new UserNotFoundException(e.getUserId());
		} catch(DaoException e){
			throw new ServiceConfigurationException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public void checkAuthUserIsParent(String childUserId)
	throws NoAccessPermissionException, ServiceConfigurationException
	, UserNotFoundException
	{
		if(isAuthUserAdmin()) return;

		try{
			dc.beginTransaction();
			try{
				User u = getUser();
				TemporaryUser tu = DaoFactory.createInstance().createTemporaryUserDao().getUser(
						u.getGridId(), childUserId);
				if(!tu.getParentUserId().equals(u.getUserId())){
					throw new NoAccessPermissionException(u.getUserId());
				}
			} finally{
				dc.commitTransaction();
			}
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw new UserNotFoundException(e.getUserId());
		} catch(DaoException e){
			throw new ServiceConfigurationException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static UserChecker get(AbstractLangridService service)
	throws DaoException
	{
		return service.getUserChecker();
	}

	private ServiceContext sc;
	private DaoContext dc;
	private String gridId;
	private UserDao userDao;
	private ServiceDao serviceDao;
}
