/*
 * $Id: ServiceClientImpl.java 562 2012-08-06 10:56:18Z t-nakaguchi $
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.rpc.ServiceException;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;

import jp.go.nict.langrid.client.ws_1_2.ServiceClient;
import jp.go.nict.langrid.client.ws_1_2.error.ExceptionConverter;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.binding.DynamicBindingUtil;
import jp.go.nict.langrid.commons.cs.calltree.CallNode;
import jp.go.nict.langrid.commons.cs.calltree.CallTreeUtil;
import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.soap.SoapHeaderRpcHeadersAdapter;
import jp.go.nict.langrid.service_1_2.transformer.StringToDictMatchingMethodTransformer;
import jp.go.nict.langrid.service_1_2.transformer.StringToPartOfSpeechTransformer;

import org.apache.axis.AxisEngine;
import org.apache.axis.Message;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;
import org.apache.axis.transport.http.HTTPConstants;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 562 $
 */
public abstract class ServiceClientImpl
implements ServiceClient, Serializable
{
	/**
	 * 
	 * 
	 */
	public ServiceClientImpl(URL url){
		this.url = url;
	}

	public void setUserId(String userId){
		this.userId = userId;
		if(stub != null){
			stub.setUsername(userId);
		}
	}

	public void setPassword(String password){
		this.password = password;
		if(stub != null){
			stub.setPassword(password);
		}
	}

	public Collection<BindingNode> getTreeBindings(){
		treeBindingMayChanged = true;
		return treeBindings;
	}

	@Override
	public void addRequestHeader(String name, String value) {
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

	/**
	 * 
	 * 
	 */
	protected Object invoke(Object... arguments)
		throws LangridException
	{
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String methodName = ste.getMethodName();

		Stub s = null;
		try{
			s = getStub();
		} catch(ServiceException e){
			throw ExceptionConverter.convertToE000(
					e, url, methodName, arguments);
		}

		Method selfMethod = null;
		for(Method m : getClass().getMethods()){
			if(!m.getName().equals(methodName)) continue;
			if(!isAssignableFrom(m.getParameterTypes(), arguments)) continue;
			selfMethod = m;
			break;
		}
		if(selfMethod == null){
			throw ExceptionConverter.convertToE000(
					"no suitable self method."
					, url, methodName
					, arguments);
		}
		Object[] convertedArgs = null;
		try {
			for(Method m : s.getClass().getMethods()){
				if(!m.getName().equals(methodName)) continue;
				try{
					convertedArgs = convert(arguments, m.getParameterTypes());
				} catch(ConversionException e){
					continue;
				}
				Object result = m.invoke(s, convertedArgs);
				return convert(result, selfMethod.getReturnType());
			}
			throw new RuntimeException("no suitable stub method.");
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException. actual args:");
			for(Object o : convertedArgs){
				System.out.println(o.getClass() + ": \"" + o + "\"");
			}
			throw ExceptionConverter.convertToE000(
					e, url, methodName
					, arguments
					);
		} catch (IllegalAccessException e) {
			throw ExceptionConverter.convertToE000(
					e, url, methodName
					, arguments
					);
		} catch (InvocationTargetException e) {
			throw ExceptionConverter.convert(
					e, url, methodName
					, arguments
					);
		} finally{
			Message message = null;
			if(s._getCall() != null){
				message = s._getCall().getMessageContext().getResponseMessage();
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
	}

	protected abstract Stub createStub(URL url)
	throws ServiceException;

	@SuppressWarnings("unchecked")
	protected <T> T convert(Object source, Class<T> targetClass){
		if(source == null) return null;
		if(targetClass.isAssignableFrom(source.getClass())) return (T)source;
		return converter.convert(source, targetClass);
	}

	@SuppressWarnings("unchecked")
	protected void setUpService(Service service){
		AxisEngine engine = service.getEngine();
		Hashtable<Object, Object> headers =
			(Hashtable<Object, Object>)engine.getOption(
					HTTPConstants.REQUEST_HEADERS);
		if(headers != null){
			for(Map.Entry<?, ?> e : headers.entrySet()){
				httpHeaders.put(
						(String)e.getKey()
						, (String)e.getValue()
						);
			}
		}
		engine.setOption(HTTPConstants.REQUEST_HEADERS, httpHeaders);
	}

	private Stub getStub()
	throws ServiceException
	{
		if(stub == null){
			stub = createStub(url);
			stub.setUsername(userId);
			stub.setPassword(password);
		}
		setUpStubHeaders();
		return stub;
	}

	private void setUpStubHeaders(){
		if(stub == null) return;
		if(!treeBindingMayChanged) return;

		stub.clearHeaders();
		String tvalue = DynamicBindingUtil.encodeTree(treeBindings);
		if(tvalue.length() > 0){
			stub.setHeader(
					LangridConstants.ACTOR_SERVICE_TREEBINDING
					, "binding", tvalue
					);
		}
		treeBindingMayChanged = false;
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

	private boolean isAssignableFrom(Class<?>[] argTypes, Object[] args){
		if(argTypes.length != args.length) return false;
		for(int i = 0; i < argTypes.length; i++){
			if(args[i] == null){
				if(argTypes[i].isPrimitive()) return false;
				continue;
			}
			if(ClassUtil.isAssignableFrom(argTypes[i], args[i].getClass())) continue;
			return false;
		}
		return true;
	}

	private Object[] convert(Object[] arguments, Class<?>[] convertTypes)
	throws ConversionException
	{
		if(arguments.length != convertTypes.length){
			throw new ConversionException("length not match.");
		}
		Object[] convertedArgs = new Object[arguments.length];
		for(int i = 0; i < arguments.length; i++){
			convertedArgs[i] = converter.convert(arguments[i], convertTypes[i]);
		}
		return convertedArgs;
	}

	private URL url;
	private String userId;
	private String password;
	private transient Stub stub;
	private transient boolean treeBindingMayChanged;
	private Collection<BindingNode> treeBindings 
			= new ArrayList<BindingNode>();
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
