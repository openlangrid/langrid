/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.ws.axis2;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.BasicAuthUtil;
import jp.go.nict.langrid.commons.ws.FilePersistentProperties;
import jp.go.nict.langrid.commons.ws.HttpServletRequestUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;

import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.transport.http.HTTPConstants;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class Axis2ServiceContext extends ServiceContext{
	@Override
	public String getAuthUserGridId() {
		return null;
	}

	@Override
	public String getAuthUser(){
		HttpServletRequest request = getRequest();
		if(request == null) return null;
		return request.getRemoteUser();
	}

	@Override
	public String getAuthPass(){
		HttpServletRequest request = getRequest();
		if(request == null) return null;
		Pair<String, String> userAndPass = BasicAuthUtil.decode(
				request.getHeader("Authorization")
				);
		if(userAndPass == null) return null;
		return userAndPass.getSecond();
	}

	@Override
	public String getRemoteAddress() {
		HttpServletRequest request = getRequest();
		if(request == null) return null;
		return request.getRemoteAddr();
	}

	@Override
	public String getInitParameter(String param){
		return getServiceOption(param);
	}

	/**
	 * 
	 * 
	 */
	public String getServiceOption(String name){
		AxisService s = getService();
		if(s == null) return null;
		Object o = s.getParameter(name);
		if(o == null) return null;
		return o.toString();
	}

	@Override
	public MimeHeaders getRequestMimeHeaders() {
		MimeHeaders headers = new MimeHeaders();
		HttpServletRequest req = getRequest();
		Enumeration<?> en = req.getHeaderNames();
		while(en.hasMoreElements()){
			String name = en.toString();
			headers.addHeader(name, req.getHeader(name));
		}
		return headers;
	}

	
	@Override
	public Iterable<RpcHeader> getRequestRpcHeaders(){
		MessageContext c = getMessageContext();
		if(c == null) return null;
		final org.apache.axiom.soap.SOAPHeader h = c.getEnvelope().getHeader();
		return new Iterable<RpcHeader>() {
			@Override
			@SuppressWarnings("unchecked")
			public Iterator<RpcHeader> iterator() {
				final Iterator<SOAPHeaderBlock> it = h.examineAllHeaderBlocks();
				return new Iterator<RpcHeader>() {
					@Override
					public boolean hasNext() {
						return it.hasNext();
					}
					@Override
					public RpcHeader next() {
						SOAPHeaderBlock b = it.next();
						return new RpcHeader(b.getNamespace().getNamespaceURI()
								, b.getLocalName(), b.getText());
					};
					@Override
					public void remove() {
						it.remove();
					};
				};
			}
		};
	}

	@Override
	public String getRealPath(String path) {
		return getHttpServlet().getServletContext().getRealPath(path);
	}

	@Override
	public URL getRequestUrl() {
		return HttpServletRequestUtil.getRequestUrl(getRequest());
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

	/**
	 * 
	 * 
	 */
	protected MessageContext getMessageContext(){
		try{
			return MessageContext.getCurrentMessageContext();
		} catch(NoClassDefFoundError e){
			return null;
		}
	}

	private synchronized void prepareProperties(){
		if(props != null) return;
		props = new FilePersistentProperties(new File(
				getHttpServlet().getServletContext().getRealPath(
						"WEB-INF/node.properties")
				));
	}

	/**
	 * 
	 * 
	 */
	private AxisService getService(){
		MessageContext c = getMessageContext();
		if(c == null) return null;
		else return getMessageContext().getAxisService();
	}

	/**
	 * 
	 * 
	 */
	private Object getMessageProperty(String aKey){
		MessageContext c = getMessageContext();
		if(c == null) return null;
		else return c.getProperty(aKey);
	}

	/**
	 * 
	 * 
	 */
	private HttpServletRequest getRequest(){
		return (HttpServletRequest) getMessageProperty(
			HTTPConstants.MC_HTTP_SERVLETREQUEST
			);
	}

	/**
	 * 
	 * 
	 */
	private HttpServlet getHttpServlet(){
		return (HttpServlet) getMessageProperty(
			HTTPConstants.MC_HTTP_SERVLET
			);
	}

	private FilePersistentProperties props;
}
