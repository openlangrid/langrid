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
package jp.go.nict.langrid.commons.rpc.json;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.namespace.QName;

import jp.go.nict.langrid.commons.rpc.RpcHeader;

public class JsonRpcUtil {
	public static JsonRpcRequest createRequest(
			Map<QName, Object> headers, Method method, Object... args){
		JsonRpcRequest req = new JsonRpcRequest();
		req.setId(Integer.toString(id.incrementAndGet()));
		List<RpcHeader> hs = new ArrayList<RpcHeader>();
		for(Map.Entry<QName, Object> h : headers.entrySet()){
			hs.add(new RpcHeader(
					h.getKey().getNamespaceURI(), h.getKey().getLocalPart(), h.getValue().toString()
					));
		}
		req.setHeaders(hs.toArray(new RpcHeader[]{}));
		req.setMethod(method.getName());
		req.setParams(args);
		return req;
	}

	public static JsonRpcRequest createRequest(
			Collection<RpcHeader> headers, Method method, Object... args){
		JsonRpcRequest req = new JsonRpcRequest();
		req.setId(Integer.toString(id.incrementAndGet()));
		req.setHeaders(headers.toArray(new RpcHeader[]{}));
		req.setMethod(method.getName());
		req.setParams(args);
		return req;
	}

	private static AtomicInteger id = new AtomicInteger();
}
