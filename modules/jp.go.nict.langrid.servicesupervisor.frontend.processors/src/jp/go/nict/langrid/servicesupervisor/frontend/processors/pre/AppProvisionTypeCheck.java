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
package jp.go.nict.langrid.servicesupervisor.frontend.processors.pre;

import static jp.go.nict.langrid.dao.entity.AppProvisionType.CLIENT_CONTROL;

import java.util.HashSet;
import java.util.Set;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.ws.LangridConstants;
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
 */
public class AppProvisionTypeCheck implements Preprocess {
	public void process(ProcessContext context, MimeHeaders requestMimeHeaders)
	throws AccessLimitExceededException, NoAccessPermissionException, SystemErrorException{
		String selfGridId = context.getProcessingNode().getGridId();
		User u = context.getCallerUser();
		Service s = context.getTargetService();
		if(!s.getGridId().equals(selfGridId)) return;
		if(!s.getGridId().equals(selfGridId)) return;
		if(u.getGridId().equals(s.getGridId())){
			if(u.getUserId().equals(s.getOwnerUserId())) return;
		}

		String[] aps = requestMimeHeaders.getHeader(
				LangridConstants.HTTPHEADER_TYPEOFAPPPROVISION
				);
		if(aps == null){
			String p = u.getDefaultAppProvisionType();
			if(p == null){
				p = CLIENT_CONTROL.name();
			}
			aps = new String[]{p};
		}

		Set<String> allowed = s.getAllowedAppProvision();
		if(allowed == null){
			allowed = new HashSet<String>();
			allowed.add(CLIENT_CONTROL.name());
		}
		for(String a : aps){
			if(!allowed.contains(a)){
				throw new NoAccessPermissionException(u.getGridId(), u.getUserId()
						, a + " is not allowed.");
			}
		}
	}
}
