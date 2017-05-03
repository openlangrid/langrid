/*
 * $Id: ServiceContext.java 1183 2014-04-10 13:59:44Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.ws;

import java.net.URL;

import jp.go.nict.langrid.commons.net.URLUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1183 $
 */
public abstract class ServiceContext{
	/**
	 * 
	 * 
	 */
	public abstract URL getRequestUrl();

	/**
	 * 
	 * 
	 * @deprecated
	 */
	@Deprecated
	public String getRequestRootURL(){
		return URLUtil.getRootUrl(getRequestUrl());
	}

	/**
	 * 
	 * 
	 * @deprecated
	 */
	@Deprecated
	public String getRequestContextURL(){
		return URLUtil.getContextUrl(getRequestUrl());
	}

	/**
	 * 
	 * 
	 */
	public abstract MimeHeaders getRequestMimeHeaders();

	/**
	 * 
	 * 
	 */
	public abstract Iterable<RpcHeader> getRequestRpcHeaders();
	
	/**
	 * 
	 * 
	 */
	public abstract String getAuthUserGridId();

	/**
	 * 
	 * 
	 */
	public abstract String getAuthUser();

	/**
	 * 
	 * 
	 */
	public abstract String getAuthPass();

	/**
	 * 
	 * 
	 */
	public abstract String getRemoteAddress();

	/**
	 * 
	 * 
	 */
	public String getSelfGridId(){
		return getInitParameter("langrid.node.gridId");
	}

	/**
	 * 
	 * 
	 */
	public String getSelfNodeId(){
		return getInitParameter("langrid.node.nodeId");
	}

	/**
	 * 
	 * 
	 */
	public String getInitParameter(InitParam parameter){
		return getInitParameter(parameter.getParameterName());
	}

	/**
	 * 
	 * 
	 */
	public abstract String getInitParameter(String parameter);

	/**
	 * 
	 * 
	 */
	public abstract String getPersistentProperty(String name);

	/**
	 * 
	 * 
	 */
	public abstract void setPersistentProperty(String name, String value);

	public abstract String getRealPath(String path);
}
