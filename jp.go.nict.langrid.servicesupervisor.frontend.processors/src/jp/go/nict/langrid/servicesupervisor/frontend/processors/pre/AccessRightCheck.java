/*
 * $Id:AccessRightCheck.java 5456 2007-10-01 07:53:07Z nakaguchi $
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
package jp.go.nict.langrid.servicesupervisor.frontend.processors.pre;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.AccessRight;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException;
import jp.go.nict.langrid.servicesupervisor.frontend.NoAccessPermissionException;
import jp.go.nict.langrid.servicesupervisor.frontend.Preprocess;
import jp.go.nict.langrid.servicesupervisor.frontend.ProcessContext;
import jp.go.nict.langrid.servicesupervisor.frontend.SystemErrorException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:5456 $
 */
public class AccessRightCheck implements Preprocess {
	public void process(ProcessContext context, MimeHeaders requestMimeHeaders)
	throws AccessLimitExceededException, NoAccessPermissionException,
	SystemErrorException{
		User u = context.getCallerUser();
		Service s = context.getTargetService();
		if(u.getGridId().equals(s.getGridId())){
			if(u.isAdminUser()) return;
			if(u.getUserId().equals(s.getOwnerUserId())) return;
		}

		try{
			AccessRightDao dao = context.getAccessRightDao();
			String ugid = u.getGridId();
			String uid = u.getUserId();
			String sgid = s.getGridId();
			String sid = s.getServiceId();
			if(!isAccessible(dao, ugid, uid, sgid, sid)){
				throw new NoAccessPermissionException(ugid, uid);
			}
		} catch(DaoException e){
			logger.log(Level.SEVERE, "exception when checking access right.", e);
			throw new SystemErrorException(
					ExceptionUtil.getMessageWithStackTrace(e)
					);
		}
	}

	/**
	 * 
	 * 
	 */
	public static boolean isAccessible(AccessRightDao dao
			, String userGridId, String userId
			, String serviceGridId, String serviceId)
	throws DaoException{
		AccessRight sdef = dao.getServiceDefaultAccessRight(serviceGridId, serviceId);
		AccessRight gdef = dao.getGridDefaultAccessRight(userGridId, serviceGridId, serviceId);
		AccessRight ar = dao.getAccessRight(userGridId, userId, serviceGridId, serviceId);
		boolean permitted = false;
		if(ar != null){
			permitted = ar.isPermitted();
		} else if(gdef != null){
			permitted = gdef.isPermitted();
		} else if(sdef != null){
			permitted = sdef.isPermitted();
		}
		return permitted;
	}

	private static Logger logger = Logger.getLogger(
			AccessRightCheck.class.getName());
}
