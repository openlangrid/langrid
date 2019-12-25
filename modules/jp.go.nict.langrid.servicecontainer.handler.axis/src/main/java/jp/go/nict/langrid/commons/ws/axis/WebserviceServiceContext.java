/*
 * $Id: WebserviceServiceContext.java 561 2012-08-06 10:47:12Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.ws.axis;

import java.net.URL;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.ws.ServiceContext;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 561 $
 */
public class WebserviceServiceContext extends ServiceContext{
	/**
	 * 
	 * 
	 */
	public WebserviceServiceContext(){
		try{
			Class<?> c = Class.forName("jp.go.nict.langrid.commons.ws.axis2.Axis2ServiceContext");
			this.context = (ServiceContext)c.newInstance(); 
		} catch(ClassNotFoundException e){
		} catch(IllegalAccessException e){
		} catch(InstantiationException e){
		}
		if(this.context == null){
			this.context = new AxisServiceContext();
		}
	}

	public URL getRequestUrl(){
		return context.getRequestUrl();
	}

	@Override
	public MimeHeaders getRequestMimeHeaders() {
		return context.getRequestMimeHeaders();
	}

	@Override
	public Iterable<RpcHeader> getRequestRpcHeaders(){
		return context.getRequestRpcHeaders();
	}

	@Override
	public String getAuthUserGridId() {
		return context.getAuthUserGridId();
	}

	@Override
	public String getAuthUser(){
		return context.getAuthUser();
	}

	@Override
	public String getAuthPass(){
		return context.getAuthPass();
	}

	@Override
	public String getRemoteAddress() {
		return context.getRemoteAddress();
	}

	public String getInitParameter(String parameter){
		return context.getInitParameter(parameter);
	}

	@Override
	public <T> T getSessionProperty(String name) {
		return context.getSessionProperty(name);
	}

	@Override
	public void setSessionProperty(String name, Object value) {
		context.setSessionProperty(name, value);
	}

	@Override
	public String getPersistentProperty(String name) {
		return context.getPersistentProperty(name);
	}

	@Override
	public void setPersistentProperty(String name, String value) {
		context.setPersistentProperty(name, value);
	}

	@Override
	public String getRealPath(String path) {
		return context.getRealPath(path);
	}

	private ServiceContext context;
}
