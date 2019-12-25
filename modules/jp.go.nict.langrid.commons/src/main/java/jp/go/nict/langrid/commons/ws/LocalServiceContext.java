/*
 * $Id: LocalServiceContext.java 1183 2014-04-10 13:59:44Z t-nakaguchi $
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcHeader;

/**
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1183 $
 */
public class LocalServiceContext extends ServiceContext{
	/**
	 * 
	 * 
	 */
	public LocalServiceContext(){
	}

	/**
	 * 
	 * 
	 */
	public LocalServiceContext(String authUserId){
		this.userId = authUserId;
	}

	@Override
	public URL getRequestUrl(){
		try {
			return new URL(requestRootUrl + "langrid-1.2/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public MimeHeaders getRequestMimeHeaders() {
		return requestMimeHeaders;
	}

	/**
	 * 
	 * 
	 */
	public void setRequestMimeHeaders(MimeHeaders value){
		this.requestMimeHeaders = value;
	}

	@Override
	public Iterable<RpcHeader> getRequestRpcHeaders(){
		return requestRpcHeaders;
	}

	/**
	 * 
	 * 
	 */
	public void setRequestRpcHeaders(Iterable<RpcHeader> value){
		this.requestRpcHeaders = new ArrayList<RpcHeader>();
		for(RpcHeader h : value){
			this.requestRpcHeaders.add(h);
		}
	}

	/**
	 * 
	 * 
	 */
	public void setRequestRootUrl(String url){
		this.requestRootUrl = url;
	}

	@Override
	public String getAuthUserGridId() {
		return gridId;
	}

	@Override
	public String getAuthUser(){
		return userId;
	}

	/**
	 * 
	 * 
	 */
	public void setAuthUser(String userId, String password){
		this.userId = userId;
		this.password = password;
	}

	/**
	 * 
	 * 
	 */
	public void setAuthUser(String gridId, String userId, String password){
		this.gridId = gridId;
		this.userId = userId;
		this.password = password;
	}

	@Override
	public String getAuthPass(){
		return password;
	}

	@Override
	public String getRemoteAddress() {
		return remoteAddress;
	}

	/**
	 * 
	 * 
	 */
	public void setRemoteAddress(String value){
		this.remoteAddress = value;
	}

	@Override
	public String getInitParameter(String param){
		return System.getProperty(param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getSessionProperty(String name) {
		return (T)sessionProps.get(name);
	}

	@Override
	public void setSessionProperty(String name, Object value) {
		sessionProps.put(name, value);
	}

	@Override
	public String getPersistentProperty(String name) {
		return props.getProperty(name);
	}

	@Override
	public void setPersistentProperty(String name, String value) {
		props.setProperty(name, value);
	}

	@Override
	public String getRealPath(String path) {
		return realPathBase + path;
	}

	/**
	 * 
	 * 
	 */
	public void setRealPathBase(String base){
		this.realPathBase = base;
	}

	private MimeHeaders requestMimeHeaders = new MimeHeaders();
	private List<RpcHeader> requestRpcHeaders = new ArrayList<RpcHeader>();
	private String gridId = "grid1";
	private String userId = "user1";
	private String password = "pass";
	private String remoteAddress ="127.0.0.1";
	private String requestRootUrl = "http://localhost/";
	private String realPathBase = ".";
	private Map<String, Object> sessionProps = new HashMap<>();
	private FilePersistentProperties props = new FilePersistentProperties(
			new File("local.properties"));
}
