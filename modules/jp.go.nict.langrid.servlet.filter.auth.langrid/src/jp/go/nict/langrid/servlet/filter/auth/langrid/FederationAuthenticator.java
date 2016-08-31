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

import java.util.logging.Logger;

import javax.servlet.ServletRequest;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.FederationDao;
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
		FederationDao fd = getDaoFactory().createFederationDao();
		String[] federationResponses = context.getRequestMimeHeaders().getHeader(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_FEDERATIONRESPONSE
				);
		String federationResponse = federationResponses != null ? StringUtil.join(federationResponses, ",") : null;
		if(federationResponse != null){
			// request about connecting/disconnecting federation
			try{
				Federation f = null;
				String selfGridId = context.getSelfGridId();
				if(fd.isFederationExist(selfGridId, authUser)){
					f = fd.getFederation(selfGridId, authUser);
				} else if(fd.isFederationExist(authUser, selfGridId)) {
					f = fd.getFederation(authUser, selfGridId);
				}
				if(authPass.equals(f.getTargetGridAccessToken())){
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
		String[] sourceGridIds = context.getRequestMimeHeaders().getHeader(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_SOURCEGRIDID
				);
		String sourceGridId = sourceGridIds != null ? StringUtil.join(sourceGridIds, ",") : null;
		String[] userGridIdAndIds = context.getRequestMimeHeaders().getHeader(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_CALLERUSER
				);
		String[] userGridIdAndId = userGridIdAndIds != null ? StringUtil.join(userGridIdAndIds, ",").split(":") : null;

		if(sourceGridId == null || userGridIdAndId == null || userGridIdAndId.length != 2){
			return false;
		}
		String selfGridId = context.getSelfGridId();
		String callerUserGridId = userGridIdAndId[0];
		String callerUserId = userGridIdAndId[1];

		try{
			Federation f = fd.getFederation(sourceGridId, selfGridId);
			if(isValidFederation(f, targetGridUserId, targetGridAccessToken)){
				context.setAuthorized(callerUserGridId, callerUserId, authPass);
				return true;
			}
/*			Logger.getLogger(getClass().getName()).info(String.format(
					"auth failed. sourceGrid[%s], targetGridUser[%s], caller[%s|%s],"
					+ "with federation[%s|%s].",
					sourceGridId, targetGridUserId, callerUserGridId, callerUserId,
					f.getSourceGridId(), f.getTargetGridId()));
*/		} catch(FederationNotFoundException e){
			// search federation route
			FederationLogic fl = new FederationLogic();
			for(Federation f : fd.listFederationsFrom(sourceGridId)){
				if(fl.isReachable(f.getTargetGridId(), selfGridId)){
					if(isValidFederation(f, targetGridUserId, targetGridAccessToken)){
						context.setAuthorized(callerUserGridId, callerUserId, authPass);
						return true;
					} else{
						Logger.getLogger(getClass().getName()).info(String.format(
								"not valid federation. sourceGrid[%s], targetGridUser[%s], caller[%s|%s],"
								+ "with federation[%s|%s].",
								sourceGridId, targetGridUserId, callerUserGridId, callerUserId,
								f.getSourceGridId(), f.getTargetGridId()));
					}
				} else{
					Logger.getLogger(getClass().getName()).info(String.format(
							"not reachable. from %s to %s.",
							f.getTargetGridId(), selfGridId));
				}
			}
		}
		return false;
	}

	private boolean isValidFederation(
			Federation f, String targetGridUserId, String targetGridAccessToken){
		if(f.isRequesting() || !f.isConnected()) return false;
		if(!targetGridUserId.equals(f.getTargetGridUserId())) return false;
		if(!targetGridAccessToken.equals(f.getTargetGridAccessToken())) return false;
		return true;
	}
}
