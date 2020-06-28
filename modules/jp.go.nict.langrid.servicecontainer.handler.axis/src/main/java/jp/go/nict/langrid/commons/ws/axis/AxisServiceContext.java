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
package jp.go.nict.langrid.commons.ws.axis;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPException;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.transport.http.HTTPConstants;

import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.BasicAuthUtil;
import jp.go.nict.langrid.commons.ws.FilePersistentProperties;
import jp.go.nict.langrid.commons.ws.HttpServletRequestUtil;
import jp.go.nict.langrid.commons.ws.MimeHeaders;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.soap.SoapHeaderRpcHeadersAdapter;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 561 $
 */
public class AxisServiceContext extends ServiceContext{
	public AxisServiceContext(){
		initServlet();
	}

	@Override
	public URL getRequestUrl(){
		return HttpServletRequestUtil.getRequestUrl(getRequest());
	}

	@Override
	public MimeHeaders getRequestMimeHeaders() {
		MessageContext mc = getMessageContext();
		if(mc == null) return null;
		return AxisUtil.toSoapMimeHeaders(mc.getMessage().getMimeHeaders());
	}

	@Override
	public Iterable<RpcHeader> getRequestRpcHeaders() {
		MessageContext mc = getMessageContext();
		if(mc == null) return null;
		try {
			return new SoapHeaderRpcHeadersAdapter(mc.getMessage().getSOAPHeader());
		} catch (SOAPException e) {
			throw new RuntimeException(e);
		}
	}

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
		String value = getServiceOption(param);
		if(value != null) return value;
		HttpServlet servlet = getHttpServlet();
		if(servlet == null) return null;
		value = servlet.getInitParameter(param);
		if(value != null) return value;
		return servlet.getServletContext().getInitParameter(param);
	}

	/**
	 * 
	 * 
	 */
	public EngineConfiguration getEngineConfiguration(){
		return getMessageContext().getAxisEngine().getConfig();
	}

	/**
	 * 
	 * 
	 */
	public static boolean isInAxisServiceContext(){
		try{
			Object mc = Class.forName("org.apache.axis.MessageContext")
				.getMethod("getCurrentContext")
				.invoke(null);
			return mc != null;
		} catch(ClassNotFoundException e){
		} catch(IllegalArgumentException e){
		} catch(SecurityException e){
		} catch(IllegalAccessException e){
		} catch(InvocationTargetException e){
		} catch(NoSuchMethodException e){
		} catch(NoClassDefFoundError e){
		}
		return false;
	}

	/**
	 * 
	 * 
	 */
	public String getServiceOption(String name){
		SOAPService s = getService();
		if(s == null) return null;
		Object o = s.getOption(name);
		if(o == null) return null;
		return o.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getSessionProperty(String name) {
		return (T)getRequest().getSession().getAttribute(name);
	}

	@Override
	public void setSessionProperty(String name, Object value) {
		getRequest().getSession().setAttribute(name, value);
	}

	@Override
	public void removeSessionProperty(String name) {
		getRequest().getSession().removeAttribute(name);
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
		return getHttpServlet().getServletContext().getRealPath(path);
	}

	/**
	 * 
	 * 
	 */
	public MessageContext getMessageContext(){
		try{
			return MessageContext.getCurrentContext();
		} catch(NoClassDefFoundError e){
			return null;
		}
	}

	private void initServlet(){
		MessageContext mc = getMessageContext();
		if(mc == null) return;
		servlet = (HttpServlet)mc.getProperty(HTTPConstants.MC_HTTP_SERVLET);
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
	private SOAPService getService(){
		MessageContext c = getMessageContext();
		if(c == null) return null;
		else return getMessageContext().getService();
	}

	/**
	 * 
	 * 
	 */
	private HttpServletRequest getRequest(){
		MessageContext mc = getMessageContext();
		if(mc != null){
			return AxisUtil.getRequest(mc);
		} else{
			return null;
		}
	}

	/**
	 * 
	 * 
	 */
	public HttpServlet getHttpServlet(){
		if(servlet == null){
			initServlet();
		}
		return servlet;
	}

	public static String getRealPath(MessageContext messageContext, String path){
		HttpServlet sv = (HttpServlet)messageContext.getProperty(HTTPConstants.MC_HTTP_SERVLET);
		if(sv == null) return null;
		return sv.getServletContext().getRealPath(path);
	}

	private HttpServlet servlet;
	private FilePersistentProperties props;
}
