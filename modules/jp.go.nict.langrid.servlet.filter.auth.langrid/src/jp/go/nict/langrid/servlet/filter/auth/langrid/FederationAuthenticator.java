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
package jp.go.nict.langrid.servlet.filter.auth.langrid;

import java.util.Optional;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.util.MimeHeadersUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.FederationNotFoundException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.FederationLogic;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class FederationAuthenticator extends AbstractLangridBasicAuthenticator{
	@Override
	protected boolean doAuthenticate(
			ServletServiceContext context
			, ServletRequest request, String authUser, String authPass)
	throws UserNotFoundException, DaoException{
		String accessToken = authPass;
		String selfGridId = context.getSelfGridId();
		FederationLogic fl = new FederationLogic();
		String[] federationResponses = context.getRequestMimeHeaders().getHeader(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_FEDERATIONRESPONSE
				);
		String federationResponse = federationResponses != null ? StringUtil.join(federationResponses, ",") : null;
		if(federationResponse != null){
			// request about connecting/disconnecting federation
			String previousGridId = authUser;
			try{
				Federation f = fl.getValidFederation(previousGridId, selfGridId);
				if(accessToken.equals(f.getTargetGridAccessToken())){
					context.setAuthorized(authUser, authUser, authPass);
					return true;
				}

				return false;
			} catch(FederationNotFoundException e){
				return false;
			}
		}
		// service call or request of information sharing from other grid.
		String targetGridUserId = authUser;
		String targetGridAccessToken = authPass;
		String sourceGridId = MimeHeadersUtil.getJoinedValue(
				context.getRequestMimeHeaders(), LangridConstants.HTTPHEADER_FEDERATEDCALL_SOURCEGRIDID
				);
		String[] callerUserGridIdAndId = Optional.ofNullable(MimeHeadersUtil.getJoinedValue(
				context.getRequestMimeHeaders(), LangridConstants.HTTPHEADER_FEDERATEDCALL_CALLERUSER
				)).map(s -> s.split(":")).orElse(null);
		if(sourceGridId == null || callerUserGridIdAndId == null || callerUserGridIdAndId.length != 2){
			return false;
		}
		String callerUserGridId = callerUserGridIdAndId[0];
		String callerUserId = callerUserGridIdAndId[1];

		Federation f = fl.getValidFederation(sourceGridId, selfGridId);
		if(f.getTargetGridAccessToken().equals(targetGridAccessToken) &&
				(	// forward
					f.getTargetGridId().equals(selfGridId) && f.getTargetGridUserId().equals(targetGridUserId))
				||
				(	// backward
					f.getSourceGridId().equals(selfGridId) && f.getSourceGridUserId().equals(targetGridUserId))
				){
			context.setAuthorized(callerUserGridId, callerUserId, authPass);
			return true;
		}
		Logger.getLogger(getClass().getName()).info(String.format(
				"auth failed. sourceGrid[%s], targetGridUser[%s], caller[%s|%s],"
				+ "with federation[%s|%s].",
				sourceGridId, targetGridUserId, callerUserGridId, callerUserId,
				f.getSourceGridId(), f.getTargetGridId()));
		return false;
	}
}
