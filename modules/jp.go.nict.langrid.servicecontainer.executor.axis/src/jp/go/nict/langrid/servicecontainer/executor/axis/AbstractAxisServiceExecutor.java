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
package jp.go.nict.langrid.servicecontainer.executor.axis;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;

import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.soap.SoapHeaderRpcHeadersAdapter;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.transformer.StringToPartOfSpeechTransformer;
import jp.go.nict.langrid.servicecontainer.executor.AbstractServiceExecutor;

import org.apache.axis.Message;
import org.apache.axis.client.Stub;
import org.apache.axis.transport.http.HTTPConstants;

/**
 * 
 * 
 */
public abstract class AbstractAxisServiceExecutor extends AbstractServiceExecutor{
	public AbstractAxisServiceExecutor(String invocationName){
		super(invocationName);
	}

	public AbstractAxisServiceExecutor(
			String invocationName, long invocationId, Endpoint endpoint){
		super(invocationName, invocationId, endpoint);
	}

	@SuppressWarnings("unchecked")
	protected long preprocessSoap(Stub stub)
	throws ServiceNotActiveException{
		Map<String, Object> httpHeaders = new Hashtable<String, Object>();
		List<RpcHeader> headers = new ArrayList<RpcHeader>();
		Pair<Endpoint, Long> r = preprocess(httpHeaders, headers);

		Endpoint endpoint = r.getFirst();
		stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, endpoint.getAddress().toString());
		stub.setUsername( endpoint.getUserName());
		stub.setPassword( endpoint.getPassword());
		for(RpcHeader h : headers){
			stub.setHeader(new org.apache.axis.message.SOAPHeaderElement(
					h.getNamespace(), h.getName(), h.getValue()
					));
		}
		stub._setProperty("HTTPPreemptive", "true");
		Map<String, Object> origHttpHeaders = (Map<String, Object>)stub._getProperty(
				HTTPConstants.REQUEST_HEADERS);
		if(origHttpHeaders == null){
			stub._setProperty(HTTPConstants.REQUEST_HEADERS, httpHeaders);
		} else{
			origHttpHeaders.putAll(httpHeaders);
		}
		return r.getSecond();
	}

	protected void postprocessSoap(long iid, Stub stub, long deltaTime)
	throws SOAPException{
		Message res = stub._getCall().getResponseMessage();
		SOAPFault f = res.getSOAPBody().getFault();
		postprocess(iid, deltaTime
				, res.getMimeHeaders()
                , new SoapHeaderRpcHeadersAdapter(res.getSOAPHeader())
                , new RpcFault(f.getFaultCode(), f.getFaultString(), f.getDetail().getNodeValue())
                );
    }

	protected <T> T convert(Object value, Class<T> targetClass){
		return converter.convert(value, targetClass);
	}

	private static jp.go.nict.langrid.commons.beanutils.Converter converter;
	static{
		converter = new jp.go.nict.langrid.commons.beanutils.Converter();
		converter.addTransformerConversion(new StringToPartOfSpeechTransformer());
	}
}
