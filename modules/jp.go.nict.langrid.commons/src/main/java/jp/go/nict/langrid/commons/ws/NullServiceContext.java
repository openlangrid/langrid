/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
import java.util.Collections;

import jp.go.nict.langrid.commons.rpc.RpcHeader;

/**
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1183 $
 */
public class NullServiceContext extends ServiceContext{
	/**
	 * 
	 * 
	 */
	public NullServiceContext(){
	}

	@Override
	public URL getRequestUrl(){
		return null;
	}

	@Override
	public MimeHeaders getRequestMimeHeaders() {
		return new MimeHeaders();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterable<RpcHeader> getRequestRpcHeaders(){
		return Collections.EMPTY_LIST;
	}

	@Override
	public String getAuthUserGridId() {
		return null;
	}

	@Override
	public String getAuthUser(){
		return null;
	}

	@Override
	public String getAuthPass(){
		return null;
	}

	@Override
	public String getRemoteAddress() {
		return null;
	}

	@Override
	public String getInitParameter(String param){
		return null;
	}

	@Override
	public <T> T getSessionProperty(String name) {
		return null;
	}

	@Override
	public void setSessionProperty(String name, Object value) {
	}

	@Override
	public void removeSessionProperty(String name) {
	}

	@Override
	public String getPersistentProperty(String name) {
		return null;
	}

	@Override
	public void setPersistentProperty(String name, String value) {
	}

	@Override
	public String getRealPath(String path) {
		return null;
	}
}
