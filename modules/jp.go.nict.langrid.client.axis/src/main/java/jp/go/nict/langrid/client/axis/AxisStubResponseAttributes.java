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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;

import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.commons.cs.calltree.CallNode;
import jp.go.nict.langrid.commons.cs.calltree.CallTreeUtil;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.soap.SoapHeaderRpcHeadersAdapter;
import jp.go.nict.langrid.commons.ws.util.MimeHeadersUtil;

import org.apache.axis.Message;
import org.apache.axis.client.Stub;
import org.w3c.dom.Element;

public class AxisStubResponseAttributes implements ResponseAttributes{
	public AxisStubResponseAttributes(Stub stub) {
		this.stub = stub;
	}

	void reload(){
		serviceName = null;
		copyright = null;
		licenseInfo = null;
		callTree.clear();
		if(stub._getCall() == null) return;

		Message message = stub._getCall().getMessageContext().getResponseMessage();
		if(message == null) return;

		mimeHeaders = message.getMimeHeaders();
		serviceName = MimeHeadersUtil.getJoinedAndDecodedValue(
				mimeHeaders, LangridConstants.HTTPHEADER_SERVICENAME, ", "
					);
		copyright = MimeHeadersUtil.getJoinedAndDecodedValue(
				mimeHeaders, LangridConstants.HTTPHEADER_SERVICECOPYRIGHT, ", "
					);
		licenseInfo = MimeHeadersUtil.getJoinedAndDecodedValue(
				mimeHeaders, LangridConstants.HTTPHEADER_SERVICELICENSE, ", "
					);
		try{
			soapHeader = message.getSOAPHeader();
			soapFault = message.getSOAPBody().getFault();
			callTree = CallTreeUtil.extractNodes(new SoapHeaderRpcHeadersAdapter(soapHeader));
		} catch(SOAPException e){
		} catch(ParseException e){
		}
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
	@SuppressWarnings("unchecked")
	public Map<String, Object> getResponseMimeHeaders(){
		Iterator<MimeHeader> it = mimeHeaders.getAllHeaders();
		Map<String, Object> ret = new HashMap<String, Object>();
		while(it.hasNext()){
			MimeHeader h = it.next();
			ret.put(h.getName(), Arrays.asList(mimeHeaders.getHeader(h.getName())));
		}
		return ret;
	}

	@Override
	public Iterable<RpcHeader> getResponseRpcHeaders(){
		return new SoapHeaderRpcHeadersAdapter(soapHeader);
	}

	@Override
	public RpcFault getResponseRpcFault(){
		if(soapFault == null) return null;
		Element d = soapFault.getDetail();
		return new RpcFault(soapFault.getFaultCode(), soapFault.getFaultString()
				, d != null ? d.getNodeValue() : null);
	}

	private Stub stub;
	private String serviceName;
	private String copyright;
	private String licenseInfo;
	private List<CallNode> callTree = new ArrayList<CallNode>();
	private MimeHeaders mimeHeaders;
	private SOAPHeader soapHeader;
	private SOAPFault soapFault;
}
