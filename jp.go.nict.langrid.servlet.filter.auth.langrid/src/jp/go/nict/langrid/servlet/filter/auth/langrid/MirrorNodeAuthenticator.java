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
import jp.go.nict.langrid.dao.NodeNotFoundException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.Node;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class MirrorNodeAuthenticator extends AbstractLangridBasicAuthenticator{
	@Override
	protected boolean doAuthenticate(
			ServletServiceContext context
			, ServletRequest request, String authUser, String authPass)
	throws UserNotFoundException, DaoException{
		String[] sourceNodeIds = context.getRequestMimeHeaders().getHeader(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_SOURCEGRIDID
				);
		String sourceNodeId = sourceNodeIds != null ? StringUtil.join(sourceNodeIds, ",") : null;
		String[] userGridIdAndIds = context.getRequestMimeHeaders().getHeader(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_CALLERUSER
				);
		String[] userGridIdAndId = userGridIdAndIds != null ? StringUtil.join(userGridIdAndIds, ",").split(":") : null;
		if(sourceNodeIds == null || userGridIdAndId == null || userGridIdAndId.length != 2){
			return false;
		}
		String selfGridId = context.getSelfGridId();
		String callerUserGridId = userGridIdAndId[0];
		String callerUserId = userGridIdAndId[1];
		if(!selfGridId.equals(callerUserGridId)) return false;

		Node n = null;
		try{
			n = getDaoFactory().createNodeDao().getNode(
					selfGridId, sourceNodeId);
		} catch(NodeNotFoundException e){
			return false;
		}

		if(!authUser.equals(n.getOwnerUserId())) return false;
		if(!authPass.equals(n.getAccessToken())) return false;

		context.setAuthorized(callerUserGridId, callerUserId, authPass);
		return true;
	}
}
