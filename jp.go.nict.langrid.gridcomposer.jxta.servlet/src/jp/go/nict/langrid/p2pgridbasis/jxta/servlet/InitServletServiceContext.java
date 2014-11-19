/*
 * $Id: InitServletServiceContext.java 563 2012-08-06 11:18:55Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.jxta.servlet;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.ws.ServiceContext;

/**
 * サーブレット初期化時のサービスコンテキスト
 * @author $Author: t-nakaguchi $
 * @version $Revision: 563 $
 */
public class InitServletServiceContext extends ServiceContext {
	private ServletConfig config;

	/**
	 * コンストラクタ
	 * @param context
	 */
	public InitServletServiceContext(ServletConfig config) {
		this.config = config;
	}

	public File getApplicationDirectory() {
		return new File(getRealPath("WEB-INF"));
	}

	@Override
	public String getRealPath(String arg0) {
		return config.getServletContext().getRealPath(arg0);
	}

	@Override
	public String getAuthUserGridId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAuthUser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAuthPass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getInitParameter(String parameter) {
		String v = config.getInitParameter(parameter);
		if(v != null) return v;
		return config.getServletContext().getInitParameter(parameter);
	}

	@Override
	public URL getRequestUrl() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getPersistentProperty(String arg0) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPersistentProperty(String arg0, String arg1) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public MimeHeaders getRequestMimeHeaders() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRemoteAddress() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<RpcHeader> getRequestRpcHeaders(){
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
}
