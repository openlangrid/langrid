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

import javax.servlet.ServletRequest;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.FederationNotFoundException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.Federation;

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
	throws UserNotFoundException, DaoException
	{
		String[] federationResponses = context.getRequestMimeHeaders().getHeader(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_FEDERATIONRESPONSE
				);
		String federationResponse = federationResponses != null ? StringUtil.join(federationResponses, ",") : null;
		if(federationResponse != null){
			try{
				Federation f = null;
				String selfGridId = context.getSelfGridId();
				FederationDao fd = getDaoFactory().createFederationDao();
				if(fd.isFederationExist(selfGridId, authUser)){
					f = getDaoFactory().createFederationDao().getFederation(
						selfGridId, authUser);
				} else if(fd.isFederationExist(authUser, selfGridId)) {
					f = getDaoFactory().createFederationDao().getFederation(
						authUser, selfGridId);
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

		Federation f = null;
		try{
			f = getDaoFactory().createFederationDao().getFederation(
				sourceGridId, selfGridId);
		} catch(FederationNotFoundException e){
			return false;
		}
		if(!authUser.equals(f.getTargetGridUserId())) return false;
		if(!authPass.equals(f.getTargetGridAccessToken())) return false;
		context.setAuthorized(callerUserGridId, callerUserId, authPass);
		return true;
	}
}
