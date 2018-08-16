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
package jp.go.nict.langrid.client.axis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import jp.go.nict.langrid.client.AuthMethod;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.binding.DynamicBindingUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;

import org.apache.axis.client.Stub;

public class AxisStubRequestAttributes implements RequestAttributes{
	public AxisStubRequestAttributes(Stub stub){
		this.stub = stub;
		mightBeModified = true;
	}

	@Override
	public void setUserId(String userId) {
		AxisStubUtil.setUserName(stub, userId);
	}

	@Override
	public void setPassword(String password) {
		AxisStubUtil.setPassword(stub, password);
	}

	@Override
	public void setConnectTimeout(int timeoutMillis) {
		AxisStubUtil.setTimeout(stub, timeoutMillis);
	}

	@Override
	public void setTimeout(int timeoutMillis) {
		AxisStubUtil.setTimeout(stub, timeoutMillis);
	}

	@Override
	public void setRequestContentCompression(boolean requestContentCompression) {
	}

	@Override
	public void setRequestContentCompressionThreashold(int bytes) {
	}

	@Override
	public void setRequestContentCompressionAlgorithm(String algorithm) {
	}

	@Override
	public void setResponseContentCompression(boolean responseContentCompression) {
	}

	@Override
	public void setAuthMethod(AuthMethod method) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addRequestMimeHeader(String name, String value) {
		mightBeModified = true;
		headers.put(name, value);
	}

	@Override
	public void addRequestMimeHeaders(Map<String, Object> headers) {
		mightBeModified = true;
		this.headers.putAll(headers);
	}

	public Map<String, Object> getRequestMimeHeaders() {
		mightBeModified = true;
		return headers;
	}

	@Override
	public void addRequestRpcHeader(String namespace, String name, String value) {
		mightBeModified = true;
		soapHeaders.put(new QName(namespace, name), value);
	}

	@Override
	public void addRequestRpcHeaders(Map<QName, Object> headers) {
		mightBeModified = true;
		soapHeaders.putAll(headers);
	}

	public Map<QName, Object> getRequestRpcHeaders(){
		mightBeModified = true;
		return soapHeaders;
	}

	@Override
	public Collection<BindingNode> getTreeBindings() {
		mightBeModified = true;
		return bindings;
	}

	@Override
	public void setUserParam(Object param) {
		mightBeModified = true;
		String key = LangridConstants.HTTPHEADER_SERVICEINVOCATION_USERPARAM;
		if(param != null) {
			headers.put(key, param);
		} else {
			headers.remove(key);
		}
	}

	void setUpStub(){
		if(mightBeModified){
			AxisStubUtil.setMimeHeaders(stub, headers.entrySet());
			Map<QName, Object> mergedSoapHeaders = new HashMap<QName, Object>(soapHeaders);
			if(bindings.size() > 0){
				String tvalue = DynamicBindingUtil.encodeTree(this.bindings);
				mergedSoapHeaders.put(new QName(LangridConstants.ACTOR_SERVICE_TREEBINDING
							, "binding"), tvalue
							);
			}
			AxisStubUtil.setSoapHeaders(stub, mergedSoapHeaders.entrySet());
			mightBeModified = false;
		}
	}

	private boolean mightBeModified = false;
	private Stub stub;
	private Map<String, Object> headers = new HashMap<String, Object>();
	private Map<QName, Object> soapHeaders = new HashMap<QName, Object>();
	private List<BindingNode> bindings = new ArrayList<BindingNode>();
}
