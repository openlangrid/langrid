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
package jp.go.nict.langrid.servicesupervisor.frontend;

import java.util.Calendar;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1513 $
 */
public class LogInfo {
	/**
	 * 
	 * 
	 */
	public LogInfo(String remoteAddress, String remoteHost
			, Calendar accessDateTime, String requestUri
			, int requestBytes, long responseMillis
			, int responseCode, int responseBytes
			, String protocolId
			, String referer, String userAgent
			, int callNest, String callTree,
			String userParam)
	{
		this.remoteAddress = remoteAddress;
		this.remoteHost = remoteHost;
		this.accessDateTime = accessDateTime;
		this.requestUri = requestUri;
		this.requestBytes = requestBytes;
		this.responseMillis = responseMillis;
		this.responseCode = responseCode;
		this.responseBytes = responseBytes;
		this.protocolId = protocolId;
		this.referer = referer;
		this.userAgent = userAgent;
		this.callNest = callNest;
		this.callTree = callTree;
		this.userParam = userParam;
	}

	/**
	 * 
	 * 
	 */
	public String getRemoteAddress() {
		return remoteAddress;
	}
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	/**
	 * 
	 * 
	 */
	public String getRemoteHost() {
		return remoteHost;
	}
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	/**
	 * 
	 * 
	 */
	public Calendar getAccessDateTime() {
		return accessDateTime;
	}

	/**
	 * 
	 * 
	 */
	public String getRequestUri() {
		return requestUri;
	}

	/**
	 * 
	 * 
	 */
	public int getRequestBytes() {
		return requestBytes;
	}

	/**
	 * 
	 * 
	 */
	public long getResponseMillis() {
		return responseMillis;
	}

	/**
	 * 
	 * 
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * 
	 * 
	 */
	public int getResponseBytes() {
		return responseBytes;
	}

	/**
	 * 
	 * 
	 */
	public String getProtocolId(){
		return protocolId;
	}

	/**
	 * 
	 * 
	 */
	public String getReferer() {
		return referer;
	}

	/**
	 * 
	 * 
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * 
	 * 
	 */
	public int getCallNest() {
		return callNest;
	}

	/**
	 * 
	 * 
	 */
	public String getCallTree() {
		return callTree;
	}

	public String getUserParam() {
		return userParam;
	}
	public void setUserParam(String userParam) {
		this.userParam = userParam;
	}

	private String remoteAddress;
	private String remoteHost;
	private Calendar accessDateTime;
	private String requestUri;
	private int requestBytes;
	private long responseMillis;
	private int responseCode;
	private int responseBytes;
	private String protocolId;
	private String referer;
	private String userAgent;
	private int callNest;
	private String callTree;
	private String userParam;
}
