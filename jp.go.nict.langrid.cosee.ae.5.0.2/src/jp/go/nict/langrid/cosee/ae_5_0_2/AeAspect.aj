/*
 * $Id: AeAspect.aj 1002 2013-11-26 02:50:21Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 2 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.cosee.ae_5_0_2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.soap.SOAPException;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.soap.SoapFaultRpcFaultAdapter;
import jp.go.nict.langrid.commons.ws.soap.SoapHeaderRpcHeadersAdapter;
import jp.go.nict.langrid.cosee.AppAuthEndpointRewriter;
import jp.go.nict.langrid.cosee.AspectBase;
import jp.go.nict.langrid.cosee.BasicAuthEndpointRewriter;
import jp.go.nict.langrid.cosee.DynamicBindingRewriter;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.cosee.EndpointRewriter;

import org.activebpel.rt.axis.bpel.handlers.AeBpelHandler;
import org.activebpel.rt.axis.bpel.handlers.AeHTTPSender;
import org.activebpel.rt.axis.bpel.invokers.AeAxisInvokeContext;
import org.activebpel.rt.axis.bpel.invokers.AeRpcStyleInvoker;
import org.activebpel.rt.bpel.IAeEndpointReference;
import org.activebpel.rt.bpel.impl.AeBaseQueueManager;
import org.activebpel.rt.bpel.impl.AeInMemoryProcessManager;
import org.activebpel.rt.bpel.impl.queue.AeReply;
import org.activebpel.rt.bpel.server.engine.invoke.AeInvokeContext;
import org.activebpel.wsio.IAeWebServiceEndpointReference;
import org.activebpel.wsio.IAeWebServiceResponse;
import org.activebpel.wsio.invoke.IAeInvoke;
import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.client.Call;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.axis.transport.http.HTTPConstants;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1002 $
 */
public privileged aspect AeAspect extends AspectBase{
	/**
	 * 
	 * 
	 */
	public AeAspect(){
		super(new EndpointRewriter[]{
				 new DynamicBindingRewriter()
				 , new AppAuthEndpointRewriter()
				 , new BasicAuthEndpointRewriter()
				});
	}

/*
    [iajc] weaveinfo Join point
    'method-execution(
      long
      org.activebpel.rt.bpel.impl.AeBusinessProcessEngine.queueReceiveData(
        org.activebpel.rt.bpel.impl.IAeProcessPlan
        , org.activebpel.rt.message.IAeMessageData
        , org.activebpel.rt.bpel.impl.reply.IAeReplyReceiver
        , org.activebpel.wsio.receive.IAeMessageContext
      )
    )'
    in Type
    'org.activebpel.rt.bpel.impl.AeBusinessProcessEngine' (AeBusinessProcessEngine.java:704)
    advised by around advice from
    'jp.go.nict.langrid.aspect.ae.InvokeAspect' (InvokeAspect.aj:78)
 */
	/**
	 * 
	 * 
	 */
	long around()
	: execution(long AeInMemoryProcessManager.getNextProcessId())
	{
		long processId = proceed();
		beginProcess(serviceContext, processId);
		return processId;
	}

	/**
	 * 
	 * 
	 */
	Call around(AeInvokeContext context)
		:
		execution(Call org.activebpel.rt.axis.bpel.AeAxisInvokeHandler.createCall(AeInvokeContext))
		&& args(context)
	{
		Call ret = proceed(context);
		IAeEndpointReference ref = context.getEndpoint();
		IAeInvoke invoke = context.getInvoke();
		URI serviceNamespace = null;
		try{
			serviceNamespace = new URI(ref.getServiceName().getNamespaceURI());
		} catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
		long processId = invoke.getProcessId();
		try{
			URI processUri = new URI(invoke.getProcessName().getNamespaceURI());
			Endpoint ep = makeEndpoint(ref);
			ep = rewriteEndpoint(
					processId, processUri
					, invoke.getLocationId()
					, makePartnerLinkName(invoke.getPartnerLink())
					, serviceNamespace, ep
					);
			logger_.info("rewrite endpoint to " + ep.getAddress()
					+ "[" + ep.getUserName() + ":****]");
			ret.setTargetEndpointAddress(ep.getAddress().toURL());
			ret.setUsername(ep.getUserName());
			ret.setPassword(ep.getPassword());
			return ret;
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		} catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	void around(final AeAxisInvokeContext context):
		execution(void AeRpcStyleInvoker.invokeRpcCall(AeAxisInvokeContext))
		&& args(context)
	{
		IAeInvoke invoke = context.getInvoke();
		String partnerLinkName = makePartnerLinkName(invoke.getPartnerLink());
		Call c = context.getCall();
		Map<String, Object> httpHeaders = new Hashtable<String, Object>();
		List<RpcHeader> rpcHeaders = new ArrayList<RpcHeader>();
		appendInvocationHeaders(
				invoke.getProcessId(), invoke.getLocationId()
				, partnerLinkName, httpHeaders, rpcHeaders
				);
		Map<String, Object> headers = (Map<String, Object>)c.getProperty(HTTPConstants.REQUEST_HEADERS);
		if(headers == null){
			c.setProperty(HTTPConstants.REQUEST_HEADERS, httpHeaders);
		} else{
			headers.putAll(httpHeaders);
		}
		for(RpcHeader h : rpcHeaders){
			c.addHeader(new SOAPHeaderElement(h.getNamespace(), h.getName(), h.getValue()));
		}
		c.setProperty("HTTPPreemptive", "true");

		long s = System.currentTimeMillis();
		try{
			proceed(context);
		} finally{
			long deltaTime = System.currentTimeMillis() - s;
			Message message = context.getCall().getResponseMessage();
			try{
				processInvocationResponseHeaders(
						invoke.getProcessId()
						, invoke.getLocationId()
						, partnerLinkName
						, deltaTime
						, message.getMimeHeaders()
						, new SoapHeaderRpcHeadersAdapter(message.getSOAPHeader())
						, new SoapFaultRpcFaultAdapter(message.getSOAPBody().getFault())
						);
			} catch(SOAPException e){
				logger_.log(Level.SEVERE, "failed to get soap header", e);
			}
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	before(AeReply reply, Map processProperties) :
		execution(* AeBaseQueueManager.sendReply(AeReply, *, *, Map))
		&& args(reply, *, *, processProperties)
	{
		processProperties.put("X-LanguageGrid-PID", reply.getProcessId());
	}

	after() returning(IAeWebServiceResponse r):
	call(IAeWebServiceResponse *.queueReceiveData(..))
	&& within(AeBpelHandler)
	{
		currentWSR.set(r);
	}

	@SuppressWarnings("rawtypes")
	after(MessageContext c):
		execution(void AeBpelHandler.invoke(MessageContext))
		&& args(c)
	{
		Map properties = currentWSR.get().getBusinessProcessProperties();
		long pid = (Long)properties.get("X-LanguageGrid-PID");
		try{
			appendResponseHeaders(
					pid
					, c.getResponseMessage().getMimeHeaders()
					, new SoapHeaderRpcHeadersAdapter(c.getResponseMessage().getSOAPHeader())
					);
		} catch(SOAPException e){
			logger_.log(Level.SEVERE, "failed to get soap header", e);
		} finally{
			properties.remove("X-LanguageGrid-PID");
			currentWSR.remove();
			endProcess(pid);
		}
	}

	Object around(MessageContext c) throws AxisFault:
		execution(void AeHTTPSender.invoke(MessageContext) throws AxisFault)
		&& args(c){
		return proceed(c);
	}
	static byte[] bytes;
	static {
		try{
			bytes = StreamUtil.readAsBytes(AeAspect.class.getResourceAsStream("ServiceNotActiveExceptionBytes.txt"));
		} catch(IOException e){
			throw new RuntimeException(e);
		}
	}

	private static Endpoint makeEndpoint(IAeWebServiceEndpointReference ref)
	throws URISyntaxException{
		return new Endpoint(
				null, new URI(ref.getAddress())
				, ref.getUsername()
				, ref.getPassword()
				);
	}

	private static String makePartnerLinkName(String name){
		if(!name.startsWith(prefix)) return name;
		if(!name.endsWith(suffix)) return name;
		return name.substring(prefix.length(), name.length() - suffix.length());
	}

	private ThreadLocal<IAeWebServiceResponse> currentWSR
			= new ThreadLocal<IAeWebServiceResponse>();

	private ServiceContext serviceContext = new AeServiceContext();

	private static final String prefix = "/process/partnerLinks/partnerLink[@name='";
	private static final String suffix = "']";

	// 
	// 
	private static Logger logger_ = Logger.getLogger(AeAspect.class.getName());
/*
	static{
		AxisProperties.setProperty(
				SocketFactory.class.getName()
				, ProxySelectingAxisSocketFactory.class.getName()
				);
		AxisProperties.setProperty(
				SecureSocketFactory.class.getName()
				, ProxySelectingSecureAxisSocketFactory.class.getName()
				);
	}
*/
}
