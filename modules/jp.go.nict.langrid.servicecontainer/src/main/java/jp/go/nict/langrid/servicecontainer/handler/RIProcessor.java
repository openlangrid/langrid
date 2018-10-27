/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
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
package jp.go.nict.langrid.servicecontainer.handler;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.commons.lang.reflect.MethodUtil;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.cosee.AspectBase;
import jp.go.nict.langrid.cosee.DynamicBindingRewriter;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.cosee.EndpointRewriter;
import jp.go.nict.langrid.cosee.SoapHeaderElementFactory;
import jp.go.nict.langrid.cosee.ws.DefaultSoapHeaderElementFactory;

/**
 * The processor for receiving and replying request and invocating component services.
 * @author Takao Nakaguchi
 */
public class RIProcessor {
	/**
	 * 
	 * 
	 */
	public static ServiceContext getCurrentServiceContext(){
		Stack<RIProcessorContext> st = contexts.get();
		if(st.size() == 0) return null;
		return st.peek().getContext();
	}

	public static RIProcessorContext getCurrentProcessorContext(){
		Stack<RIProcessorContext> st = contexts.get();
		if(st.size() == 0) return null;
		return st.peek();
	}

	/**
	 * 
	 * 
	 */
	public static RIProcessorContext start(ServiceContext context){
		return start(context, new DefaultSoapHeaderElementFactory());
	}

	/**
	 * 
	 * 
	 */
	public static RIProcessorContext start(ServiceContext context, SoapHeaderElementFactory soapHeaderElementFactory){
		long pid = pidGen.incrementAndGet();
		RIProcessorContext c = new RIProcessorContext(
				context, pid, new HeaderMessageHandler()
				);
		c.getHeaderMessageHandler().beginProcess(context, pid);
		contexts.get().push(c);
		return c;
	}

	public static void finish(){
		finish(new MimeHeaders(), new ArrayList<RpcHeader>());
	}

	/**
	 * 
	 * 
	 */
	public static void finish(MimeHeaders resMimeHeaders, Collection<RpcHeader> resRpcHeaders){
		Stack<RIProcessorContext> stack = contexts.get();
		if(stack.size() == 0){
			contexts.remove();
			return;
		}
		RIProcessorContext c = stack.pop();
		if(stack.size() == 0) contexts.remove();
		for(RIProcessorContext sub : c.getSubContexts()){
			sub.getHeaderMessageHandler().appendResponseHeaders(
					sub.getProcessId(), resMimeHeaders, resRpcHeaders);
		}
		c.getHeaderMessageHandler().appendResponseHeaders(
				c.getProcessId(), resMimeHeaders, resRpcHeaders
				);
	}

	public static void fork(RIProcessorContext orgContext){
		long pid = orgContext.getProcessId();
		RIProcessorContext c = new RIProcessorContext(
				orgContext.getContext(), pid, orgContext.getHeaderMessageHandler()
				);
		contexts.get().push(c);
	}

	/**
	 * 
	 * 
	 */
	public static RIProcessorContext join(){
		Stack<RIProcessorContext> stack = contexts.get();
		RIProcessorContext c = null;
		if(stack.size() > 0){
			c = stack.pop();
		}
		if(stack.size() == 0) contexts.remove();
		return c;
	}

	public static void mergeContext(RIProcessorContext context){
		Stack<RIProcessorContext> stack = contexts.get();
		if(stack.size() == 0) return;
		stack.peek().getSubContexts().add(context);
	}

	/**
	 * allocate the new invocation id.
	 * @return invocation id
	 */
	public static long newInvocationId(){
		return iidGen.incrementAndGet();
	}

	public static void initEndpointRewriters(EndpointRewriter[] rewriters){
		Stack<RIProcessorContext> stack = contexts.get();
		if(stack.size() == 0) return;
		RIProcessorContext c = stack.peek();
		c.getHeaderMessageHandler().initEndpointRewriters(
				c.getContext(), c.getProcessId(), rewriters);
	}

	/**
	 * 
	 * 
	 */
	public static Endpoint rewriteEndpoint(long iid, String invocationName,
			EndpointRewriter[] rewriters, Method method, Object[] args){
		return rewriteEndpoint(iid, invocationName, rewriters, defaultEP,
				method, args);
	}

	/**
	 * 
	 * 
	 */
	public static Endpoint rewriteEndpoint(long iid, String invocationName,
			EndpointRewriter[] rewriters, Endpoint original,
			Method method, Object[] args){
		Stack<RIProcessorContext> stack = contexts.get();
		if(stack.size() == 0){
			throw new RuntimeException("invalid context.");
		}
		RIProcessorContext c = stack.peek();
		Endpoint ep = c.getHeaderMessageHandler().rewriteEndpoint(
				c.getProcessId(), processUri, iid, invocationName, serviceUri, original,
				method.getName(), MethodUtil.getParameterNames(method), args, rewriters
				);
		return ep ;
	}

	public static long appendInvocationHeaders(
			long iid, String invocationName,
			Method method, Object[] args,
			Map<String, Object> mimeHeaders, Collection<RpcHeader> rpcHeaders){
		Stack<RIProcessorContext> stack = contexts.get();
		if(stack.size() == 0) return iid;
		RIProcessorContext c = stack.peek();
		c.getHeaderMessageHandler().appendInvocationHeaders(
				c.getProcessId(), iid, invocationName,
				method.getName(), MethodUtil.getParameterNames(method), args,
				mimeHeaders, rpcHeaders);
		return iid;
	}

	public static void processInvocationResponseHeaders(
			long invocationId, String invocationName, long deltaTime,
			MimeHeaders mimeHeaders, Iterable<RpcHeader> rpcHeaders
			, RpcFault rpcFault) {
		Stack<RIProcessorContext> stack = contexts.get();
		if(stack.size() == 0) return;
		RIProcessorContext c = stack.peek();
		c.getHeaderMessageHandler().processInvocationResponseHeaders(
				c.getProcessId(), invocationId, invocationName,
				deltaTime, mimeHeaders, rpcHeaders, rpcFault);
	}

	static class HeaderMessageHandler extends AspectBase{
		public HeaderMessageHandler(){
			super(new EndpointRewriter[]{new DynamicBindingRewriter()});
		}

	    public HeaderMessageHandler(EndpointRewriter[] endpointRewriters) {
	        super(endpointRewriters);
	    }

		@Override
		public void beginProcess(ServiceContext serviceContext, long processId) {
			super.beginProcess(serviceContext, processId);
		}

		@Override
		public void initEndpointRewriters(ServiceContext serviceContext, long processId, EndpointRewriter[] rewriters){
			super.initEndpointRewriters(serviceContext, processId, rewriters);
		}

		@Override
		public Endpoint rewriteEndpoint(long processId, URI processUri,
				long invocationId, String partnerLinkName, URI serviceNamespace,
				Endpoint original,
				String methodName, String[] paramNames, Object[] args) {
			return super.rewriteEndpoint(processId, processUri, invocationId,
					partnerLinkName, serviceNamespace, original,
					methodName, paramNames, args);
		}

		@Override
		public Endpoint rewriteEndpoint(long processId, URI processUri,
				long invocationId, String partnerLinkName, URI serviceNamespace,
				Endpoint original,
				String methodName, String[] paramNames, Object[] args,
				EndpointRewriter[] rewriters
				) {
			return super.rewriteEndpoint(processId, processUri, invocationId,
					partnerLinkName, serviceNamespace, original,
					methodName, paramNames, args, rewriters);
		}

		@Override
		public void appendInvocationHeaders(long processId, long invocationId,
				String partnerLinkName,
				String methodName, String[] paramNames, Object[] args,
				Map<String, Object> mimeHeaders,
				Collection<RpcHeader> rpcHeaders) {
			super.appendInvocationHeaders(processId, invocationId, partnerLinkName,
					methodName, paramNames, args, mimeHeaders, rpcHeaders);
		}

		@Override
		public void processInvocationResponseHeaders(long processId,
				long invocationId, String invocationName, long deltaTime,
				MimeHeaders mimeHeaders, Iterable<RpcHeader> soapHeaders
				, RpcFault rpcFault) {
			super.processInvocationResponseHeaders(processId, invocationId, invocationName,
					deltaTime, mimeHeaders, soapHeaders, rpcFault);
		}

		@Override
		public void appendResponseHeaders(long processId,
				MimeHeaders mimeHeaders, Collection<RpcHeader> soapHeader) {
			super.appendResponseHeaders(processId, mimeHeaders, soapHeader);
		}

		@Override
		public Map<String, Object> getProperties(long processId){
			return super.getProperties(processId);
		}
	}

	private static ThreadLocal<Stack<RIProcessorContext>> contexts
			= new ThreadLocal<Stack<RIProcessorContext>>(){
		protected Stack<RIProcessorContext> initialValue() {
			return new Stack<RIProcessorContext>();
		};
	};
	private static AtomicLong pidGen = new AtomicLong(0);
	private static AtomicLong iidGen = new AtomicLong(0);
	private static final URI processUri ;
	private static final URI serviceUri ;
	private static final Endpoint defaultEP ;
	static {
		 try{
			 processUri = new URI("http://langrid.nict.go.jp/process/java/process");
			 serviceUri = new URI("http://langrid.nict.go.jp/service/java/service");
			 defaultEP = new Endpoint(
					 "AbstractService"
					 , new URI("http://langrid.nict.go.jp/langrid-1.2/invoker/AbstractService")
					 , null, null) ;
		} catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
	}
}
