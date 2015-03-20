/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.executor.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServiceContextWrapper;
import jp.go.nict.langrid.commons.ws.util.MimeHeadersUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class JavaServiceContext extends ServiceContextWrapper{
	/**
	 * 
	 * 
	 * 
	 */
	public JavaServiceContext(ServiceContext wrapped
			, Map<String, Object> mimeHeaders
			, List<RpcHeader> headers){
		super(wrapped);
		this.mimeHeaders = mimeHeaders;
		this.headers = headers;
	}

	@Override
	public MimeHeaders getRequestMimeHeaders() {
		return MimeHeadersUtil.fromStringObjectMap(mimeHeaders);
	}

	@Override
	public Iterable<RpcHeader> getRequestRpcHeaders(){
		return headers;
	}

	private Map<String, Object> mimeHeaders = new HashMap<String, Object>();
	private List<RpcHeader> headers = new ArrayList<RpcHeader>();
}
