/*
 * $Id: ServiceClientImpl.java 562 2012-08-06 10:56:18Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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
package jp.go.nict.langrid.client.ws_1_2.protobuf.impl;

import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Fault;
import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.client.ws_1_2.ServiceClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridError;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.protobuf.transformer.Pb2WS_TranslationWithPosition_Transformer;
import jp.go.nict.langrid.client.ws_1_2.protobuf.transformer.Pb2WS_TypedMorpheme_Transformer;
import jp.go.nict.langrid.client.ws_1_2.protobuf.transformer.WS2Pb_Morpheme_Transformer;
import jp.go.nict.langrid.client.ws_1_2.protobuf.transformer.WS2Pb_Translation_Transformer;
import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.binding.DynamicBindingUtil;
import jp.go.nict.langrid.commons.cs.calltree.CallNode;
import jp.go.nict.langrid.commons.cs.calltree.CallTreeUtil;
import jp.go.nict.langrid.commons.protobufrpc.DefaultRpcController;
import jp.go.nict.langrid.commons.protobufrpc.URLRpcChannel;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.service_1_2.transformer.StringToDictMatchingMethodTransformer;
import jp.go.nict.langrid.service_1_2.transformer.StringToPartOfSpeechTransformer;

import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 562 $
 */
public abstract class ServiceClientImpl implements ServiceClient{
	/**
	 * 
	 * 
	 */
	public ServiceClientImpl(URL url){
		this.url = url;
	}

	public RpcChannel createChannel(){
		return new URLRpcChannel(
				getUrl(), getUserId(), getPassword()
				);
	}

	public List<Header> getRequestHeaders(){
		List<Header> h = new ArrayList<Header>();
		addHeaders(h);
		return h;
	}

	/**
	 * 
	 * 
	 */
	public Collection<BindingNode> getTreeBindings(){
		return treeBindings;
	}

	/**
	 * 
	 * 
	 */
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

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	protected URL getUrl(){
		return url;
	}

	protected String getUserId(){
		return userId;
	}

	protected String getPassword(){
		return password;
	}

	protected void addHeaders(List<Header> headers){
		do{
			String tvalue = DynamicBindingUtil.encodeTree(treeBindings);
			if(tvalue.length() > 0){
				headers.add(
						Header.newBuilder()
							.setName(LangridConstants.ACTOR_SERVICE_TREEBINDING)
							.setValue(tvalue)
							.build()
						);
			}
		} while(false);
	}

	protected static interface ServiceExecutor<T, U>{
		Trio<U, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, T request
				);
	}

	protected <T, U> U execute(ServiceExecutor<T, U> executor, T request
			, String methodName, Object... args)
	throws LangridException{
		DefaultRpcController controller = new DefaultRpcController();
		URLRpcChannel channel = new URLRpcChannel(
				getUrl(), getUserId(), getPassword()
				);
		Trio<U, List<Header>, Fault> r = executor.execute(
				controller, channel, request
				);
		if(controller.failed()){
			Fault f = r.getThird();
			if(r.getThird() != null){
				LangridError le = errors.get(f.getFaultDetail().split(":")[0]);
				if(le == null){
					le = LangridError.E050;
				}
				throw new LangridException(
						f.getFaultDetail()
						, getUrl(), methodName, args, le);
			}
			Throwable e = controller.errorException();
			LangridError le = LangridError.E050;
			String sgException = channel.getResponseProperties().get("X-ServiceGrid-Exception");
			if(sgException != null){
				LangridError le2 = errors.get(sgException.split(" ")[0]);
				if(le2 != null) le = le2;
			}
			if(e != null){
				throw new LangridException(e, getUrl(), methodName, args, le);
			} else{
				throw new LangridException(controller.errorText(), getUrl(), methodName, args, le);
			}
		}
		processResponseHttpHeaders(channel.getResponseProperties());
		processResponseHeaders(r.getSecond(), methodName, args);
		return r.getFirst();
	}

	private void processResponseHttpHeaders(Map<String, String> httpHeaders){
		lastName = httpHeaders.get(LangridConstants.HTTPHEADER_SERVICENAME);
		lastCopyright = httpHeaders.get(LangridConstants.HTTPHEADER_SERVICECOPYRIGHT);
		lastLicense = httpHeaders.get(LangridConstants.HTTPHEADER_SERVICELICENSE);
	}

	private void processResponseHeaders(Collection<Header> headers, String methodName, Object[] args)
	throws LangridException{
		lastCallTree = new ArrayList<CallNode>();
		for(Header h : headers){
			if(h.getName().equals(LangridConstants.ACTOR_SERVICE_CALLTREE)){
				try{
					lastCallTree = CallTreeUtil.decodeTree(h.getValue());
				} catch(ParseException e){
					throw new LangridException(e, url, methodName, args, LangridError.E000);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> T convert(Object source, Class<T> targetClass){
		if(source == null) return null;
		if(targetClass.isAssignableFrom(source.getClass())) return (T)source;
		return converter.convert(source, targetClass);
	}

	@SuppressWarnings("unused")
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
	private Collection<BindingNode> treeBindings 
			= new ArrayList<BindingNode>();
	private Hashtable<String, String> httpHeaders = new Hashtable<String, String>();
	private String lastName;
	private String lastCopyright;
	private String lastLicense;
	private Collection<CallNode> lastCallTree = new ArrayList<CallNode>();
	private static Converter converter = new Converter();
	private static Map<String, LangridError> errors = new HashMap<String, LangridError>();
	static{
		converter.addTransformerConversion(new Pb2WS_TranslationWithPosition_Transformer());
		converter.addTransformerConversion(new Pb2WS_TypedMorpheme_Transformer());
		converter.addTransformerConversion(new WS2Pb_Morpheme_Transformer());
		converter.addTransformerConversion(new WS2Pb_Translation_Transformer());
		converter.addTransformerConversion(new StringToPartOfSpeechTransformer());
		converter.addTransformerConversion(new StringToDictMatchingMethodTransformer());
		errors.put("NoValidEndpointsException", LangridError.E451);
		errors.put("jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException", LangridError.E056);
	}
}
