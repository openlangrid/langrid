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

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcHeader;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class ServiceContextWrapper extends ServiceContext{
	/**
	 * 
	 * 
	 */
	public ServiceContextWrapper(ServiceContext wrapped){
		this.wrapped = wrapped;
	}

	@Override
	public URL getRequestUrl(){
		return wrapped.getRequestUrl();
	}

	@Override
	public MimeHeaders getRequestMimeHeaders(){
		return wrapped.getRequestMimeHeaders();
	}

	@Override
	public Iterable<RpcHeader> getRequestRpcHeaders(){
		return wrapped.getRequestRpcHeaders();
	}

	@Override
	public String getAuthUserGridId() {
		return wrapped.getAuthUserGridId();
	}

	@Override
	public String getAuthUser(){
		return wrapped.getAuthUser();
	}

	@Override
	public String getAuthPass(){
		return wrapped.getAuthPass();
	}

	@Override
	public String getRemoteAddress(){
		return wrapped.getRemoteAddress();
	}

	@Override
	public String getInitParameter(String parameter){
		return wrapped.getInitParameter(parameter);
	}

	@Override
	public <T> T getSessionProperty(String name) {
		return wrapped.getSessionProperty(name);
	}

	@Override
	public void setSessionProperty(String name, Object value) {
		wrapped.setSessionProperty(name, value);
	}

	@Override
	public void removeSessionProeprty(String name) {
		wrapped.removeSessionProeprty(name);
	}

	@Override
	public String getPersistentProperty(String name){
		return wrapped.getPersistentProperty(name);
	}

	@Override
	public void setPersistentProperty(String name, String value){
		wrapped.setPersistentProperty(name, value);
	}

	@Override
	public String getRealPath(String path){
		return wrapped.getRealPath(path);
	}

	private ServiceContext wrapped;
}
