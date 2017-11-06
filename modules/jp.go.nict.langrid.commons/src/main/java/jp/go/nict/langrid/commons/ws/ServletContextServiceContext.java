/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) Language Grid Project.
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

import java.io.File;
import java.net.URL;
import java.util.Collections;

import javax.servlet.ServletContext;
import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcHeader;

/**
 * @author Takao Nakaguchi
 */
public class ServletContextServiceContext extends ServiceContext{
	/**
	 * 
	 * 
	 */
	public ServletContextServiceContext(ServletContext context){
		this.servletContext = context;
	}

	@Override
	public URL getRequestUrl() {
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
	public String getAuthUserGridId(){
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
		return servletContext.getInitParameter(param);
	}

	@Override
	public String getPersistentProperty(String name) {
		prepareProperties();
		return props.getProperty(name);
	}

	@Override
	public void setPersistentProperty(String name, String value) {
		prepareProperties();
		props.setProperty(name, value);
	}

	@Override
	public String getRealPath(String path) {
		String ret = servletContext.getRealPath(path);
		if(ret == null && !path.startsWith("/")){
			ret = servletContext.getRealPath("/" + path);
		}
		return ret;
	}

	private synchronized void prepareProperties(){
		if(props != null) return;
		props = new FilePersistentProperties(new File(
				getRealPath("WEB-INF/node.properties")
				));
	}

	private ServletContext servletContext;
	private FilePersistentProperties props;
}
