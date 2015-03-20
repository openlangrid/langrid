/*
 * $Id: ServiceClientAxisStubAdapter.java 562 2012-08-06 10:56:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.impl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;

import jp.go.nict.langrid.client.ws_1_2.ServiceClient;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.binding.DynamicBindingUtil;
import jp.go.nict.langrid.commons.cs.calltree.CallNode;
import jp.go.nict.langrid.commons.cs.calltree.CallTreeUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.soap.SoapHeaderRpcHeadersAdapter;
import jp.go.nict.langrid.service_1_2.transformer.StringToDictMatchingMethodTransformer;
import jp.go.nict.langrid.service_1_2.transformer.StringToPartOfSpeechTransformer;

import org.apache.axis.Message;
import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 562 $
 */
public class ServiceClientAxisStubAdapter
implements ServiceClient, Serializable
{
	/**
	 * 
	 * 
	 */
	public ServiceClientAxisStubAdapter(Stub stub, URL url){
		this.stub = stub;
		AxisStubUtil.setUrl(stub, url);
	}

	public void setUserId(String userId){
		AxisStubUtil.setUserName(stub, userId);
	}

	public void setPassword(String password){
		AxisStubUtil.setPassword(stub, password);
	}

	public Collection<BindingNode> getTreeBindings(){
		headerModified = true;
		return treeBindings;
	}

	@Override
	public void addRequestHeader(String name, String value) {
		headerModified = true;
		httpHeaders.put(name, value);
	}

	public String getLastName(){
		return lastName;
	}

	public String getLastCopyrightInfo() {
		return lastCopyright;
	}

	public String getLastLicenseInfo() {
		return lastLicense;
	}

	public Collection<CallNode> getLastCallTree(){
		return lastCallTree;
	}

	@SuppressWarnings("unchecked")
	public void preInvoke(){
		if(headerModified){
			AxisStubUtil.setMimeHeaders(stub, ((Map<String, Object>)(Map<?, ?>)httpHeaders).entrySet());
			AxisStubUtil.setSoapHeaders(stub, getMessageHeader());
			headerModified = false;
		}
	}

	public void postInvoke(){
		Message message = null;
		if(stub._getCall() != null){
			message = stub._getCall().getMessageContext().getResponseMessage();
		}
		if(message == null){
			lastName = null;
			lastCopyright = null;
			lastLicense = null;
			lastCallTree = null;
		} else{
			MimeHeaders headers = message.getMimeHeaders();
			lastName = getJoinedAndDecodedHeader(
					headers, LangridConstants.HTTPHEADER_SERVICENAME, "<br>"
					);
			lastCopyright = getJoinedAndDecodedHeader(
					headers, LangridConstants.HTTPHEADER_SERVICECOPYRIGHT, "<br>"
					);
			lastLicense = getJoinedAndDecodedHeader(
					headers, LangridConstants.HTTPHEADER_SERVICELICENSE, "<br>"
					);
			try{
				lastCallTree = CallTreeUtil.extractNodes(
						new SoapHeaderRpcHeadersAdapter(message.getSOAPHeader())
						);
			} catch(SOAPException e){
			} catch(ParseException e){
			}
		}
	}

	private Iterable<Map.Entry<QName, Object>> getMessageHeader(){
		Map<QName, Object> bindings = new HashMap<QName, Object>();
		String tvalue = DynamicBindingUtil.encodeTree(treeBindings);
		bindings.put(new QName(LangridConstants.ACTOR_SERVICE_TREEBINDING
					, "binding"), tvalue
					);
		return bindings.entrySet();
	}

	private String getJoinedAndDecodedHeader(
			MimeHeaders headers, String headerName, String separator)
	{
		String[] values = headers.getHeader(headerName);
		if(values != null){
			try{
				return URLDecoder.decode(
						StringUtil.join(values, separator)
						, "UTF-8"
						);
			} catch(UnsupportedEncodingException e){
				throw new RuntimeException(e);
			}
		} else{
			return null;
		}
	}

	private Stub stub;
	private boolean headerModified;
	private Collection<BindingNode> treeBindings = new ArrayList<BindingNode>();
	private Hashtable<String, String> httpHeaders = new Hashtable<String, String>();
	private String lastName;
	private String lastCopyright;
	private String lastLicense;
	private Collection<CallNode> lastCallTree = new ArrayList<CallNode>();
	private static Converter converter = new Converter();
	private static final long serialVersionUID = 5708773854342542725L;

	static{
		converter.addTransformerConversion(new StringToPartOfSpeechTransformer());
		converter.addTransformerConversion(new StringToDictMatchingMethodTransformer());
	}
}
