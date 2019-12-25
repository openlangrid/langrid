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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1242 $
 */
public class ServletServiceContext extends ServiceContext{
	/**
	 * 
	 * 
	 */
	public ServletServiceContext(
			ServletConfig config,
			HttpServletRequest request,
			Iterable<RpcHeader> requestRpcHeaders
			){
		this(request, requestRpcHeaders);
		this.servletConfig = config;
	}

	public ServletServiceContext(
			HttpServletRequest request,
			Iterable<RpcHeader> requestRpcHeaders
			){
		this.request = request;
		this.requestRpcHeaders = requestRpcHeaders;
		this.servletContext = request.getSession().getServletContext();
		String user = request.getRemoteUser();
		if(user != null){
			this.authorizedUserId = user;
		}
	}

	public ServletServiceContext(HttpServletRequest request){
		this.request = request;
		this.requestRpcHeaders = new ArrayList<RpcHeader>();
		this.servletContext = request.getSession().getServletContext();
		String user = request.getRemoteUser();
		if(user != null){
			this.authorizedUserId = user;
		}
	}

	@Override
	public URL getRequestUrl() {
		return HttpServletRequestUtil.getRequestUrl(getRequest());
	}

	@Override
	public MimeHeaders getRequestMimeHeaders() {
		MimeHeaders headers = new MimeHeaders();
		HttpServletRequest req = getRequest();
		Enumeration<?> en = req.getHeaderNames();
		while(en.hasMoreElements()){
			String name = en.nextElement().toString();
			headers.addHeader(name, req.getHeader(name));
		}
		return headers;
	}

	@Override
	public Iterable<RpcHeader> getRequestRpcHeaders(){
		return requestRpcHeaders;
	}

	@Override
	public String getAuthUserGridId(){
		return authorizedUserGridId;
	}

	@Override
	public String getAuthUser(){
		return authorizedUserId;
	}

	@Override
	public String getAuthPass(){
		if(authorizedPassword == null && authorizedUserId != null){
			authorizedPassword = getBasicAuthUserAndPassword().getSecond();
		}
		return authorizedPassword;
	}

	@Override
	public String getRemoteAddress() {
		return request.getRemoteAddr();
	}

	@Override
	public String getInitParameter(String param){
		if(servletConfig != null){
			String v = servletConfig.getInitParameter(param);
			if(v != null) return v;
		}
		ServletContext c = getServletContext();
		if(c == null) return null;
		return c.getInitParameter(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getSessionProperty(String name) {
		return (T)request.getSession().getAttribute(name);
	}

	@Override
	public void setSessionProperty(String name, Object value) {
		request.getSession().setAttribute(name, value);
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
		String ret = getServletContext().getRealPath(path);
		if(ret == null && !path.startsWith("/")){
			ret = getServletContext().getRealPath("/" + path);
		}
		return ret;
	}

	/**
	 * 
	 * 
	 */
	public Pair<String, String> getBasicAuthUserAndPassword(){
		String header = getRequest().getHeader("Authorization");
		if(header == null) return null;
		Pair<String, String> userAndPass = BasicAuthUtil.decode(
				header);
		if(userAndPass == null) return null;
		return userAndPass;
	}

	/**
	 * 
	 * 
	 */
	public void setAuthorized(String gridId, String userId, String password){
		authorizedUserGridId = gridId;
		authorizedUserId = userId;
		authorizedPassword = password;
	}

	/**
	 * 
	 * 
	 */
	private ServletContext getServletContext(){
		return servletContext;
	}

	/**
	 * 
	 * 
	 */
	public HttpServletRequest getRequest(){
		return request;
	}

	private synchronized void prepareProperties(){
		if(props != null) return;
		props = new FilePersistentProperties(new File(
				getRealPath("WEB-INF/node.properties")
				));
	}

	private HttpServletRequest request;
	private Iterable<RpcHeader> requestRpcHeaders = new ArrayList<RpcHeader>();
	private ServletConfig servletConfig;
	private ServletContext servletContext;
	private FilePersistentProperties props;
	private String authorizedUserGridId;
	private String authorizedUserId;
	private String authorizedPassword;
}
