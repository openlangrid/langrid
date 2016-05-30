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
package jp.go.nict.langrid.client;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.go.nict.langrid.commons.cs.calltree.CallNode;
import jp.go.nict.langrid.commons.cs.calltree.CallTreeUtil;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.ws.LangridConstants;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class RpcResponseAttributes implements ResponseAttributes{
	public RpcResponseAttributes() {
	}

	public void loadAttributes(HttpURLConnection con, Collection<RpcHeader> headers)
	throws ParseException{
		Map<String, List<String>> mheaders = con.getHeaderFields();
		for(Map.Entry<String, List<String>> entry : mheaders.entrySet()){
			String name = entry.getKey();
			if(name != null){
				mimeHeaders.put(entry.getKey(), join(entry.getValue()));
			}
		}
		serviceName = join(mheaders.get(LangridConstants.HTTPHEADER_SERVICENAME));
		copyright = join(mheaders.get(LangridConstants.HTTPHEADER_SERVICECOPYRIGHT));
		licenseInfo = join(mheaders.get(LangridConstants.HTTPHEADER_SERVICELICENSE));
		loadAttributes(headers);
	}

	public void loadAttributes(Collection<RpcHeader> headers)
	throws ParseException{
		rpcHeaders.addAll(headers);
		callTree = CallTreeUtil.extractNodes(headers);
	}

	private static String join(List<String> values){
		if(values == null) return null;
		StringBuilder b = new StringBuilder();
		boolean first = true;
		for(String s : values){
			if(first){
				first = false;
			} else{
				b.append(", ");
			}
			b.append(s);
		}
		return b.toString();
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public String getCopyright() {
		return copyright;
	}

	@Override
	public String getLicenseInfo() {
		return licenseInfo;
	}

	@Override
	public Collection<CallNode> getCallTree() {
		return callTree;
	}

	@Override
	public Map<String, Object> getResponseMimeHeaders() {
		return mimeHeaders;
	}
	
	@Override
	public Iterable<RpcHeader> getResponseRpcHeaders() {
		return rpcHeaders;
	}

	@Override
	public RpcFault getResponseRpcFault() {
		return rpcFault;
	}

	public void setRpcFault(RpcFault rpcFault) {
		this.rpcFault = rpcFault;
	}

	private Map<String, Object> mimeHeaders = new TreeMap<String, Object>();
	private List<RpcHeader> rpcHeaders = new ArrayList<RpcHeader>();
	private RpcFault rpcFault;
	private String serviceName;
	private String copyright;
	private String licenseInfo;
	private List<CallNode> callTree = new ArrayList<CallNode>();
}
