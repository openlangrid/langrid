/*
 * $Id: UserRightCheck.java 247 2010-10-03 02:07:33Z t-nakaguchi $
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
package jp.go.nict.langrid.servicesupervisor.frontend.processors.pre;

import javax.xml.soap.MimeHeaders;

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
 * @author $Author: t-nakaguchi $
 * @version $Revision: 247 $
 */
public class UserRightCheck implements Preprocess {
	public void process(ProcessContext context, MimeHeaders requestMimeHeaders)
	throws AccessLimitExceededException, NoAccessPermissionException, SystemErrorException{
		User u = context.getCallerUser();
		Service s = context.getTargetService();
		if(u.getGridId().equals(s.getGridId())){
			if(u.isAdminUser()) return;
			if(u.getUserId().equals(s.getOwnerUserId())) return;
		}
		if(u.isAbleToCallServices()) return;

		throw new NoAccessPermissionException(u.getGridId(), u.getUserId());
	}
}
