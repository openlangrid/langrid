/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.handler.axis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.axis.AxisFault;
import org.apache.axis.Handler;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.description.JavaServiceDesc;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.providers.BasicProvider;
import org.apache.axis.providers.java.RPCProvider;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.axis.utils.bytecode.ParamNameExtractor;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.commons.ws.HttpServletRequestUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.axis.AxisServiceContext;
import jp.go.nict.langrid.commons.ws.axis.AxisUtil;
import jp.go.nict.langrid.commons.ws.soap.SoapHeaderRpcHeadersAdapter;
import jp.go.nict.langrid.cosee.axis.AxisSoapHeaderElementFactory;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.handler.ServiceFactory;
import jp.go.nict.langrid.servicecontainer.handler.ServiceLoader;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class SGJavaProvider extends RPCProvider{
	@Override
	public void initServiceDesc(SOAPService service, MessageContext msgContext)
			throws AxisFault {
		Object classNames = service.getOption("x_sg_interfaces");
		ServiceLoader loader = SGAxisServlet.getCurrentServletServiceLoader();
		if(classNames == null){
			classNames = service.getOption("className");
		}
		if(classNames == null || SGAxisServlet.getCurrentServletServiceContext() == null){
			super.initServiceDesc(service, msgContext);
			return;
		}
		Map<String, Method> methods = new HashMap<String, Method>();
		for(String cn : classNames.toString().split(",")){
			try{
				Class<?> intfClass = Class.forName(cn);
				Set<Class<?>> visited = new HashSet<Class<?>>();
				Deque<Class<?>> classes = new LinkedList<Class<?>>();
				classes.addLast(intfClass);
				visited.add(intfClass);
				while(classes.size() > 0){
					Class<?> intf = classes.pollFirst();
					for(Method m : intf.getDeclaredMethods()){
						int mod = m.getModifiers();
						if((mod & Modifier.PUBLIC) != 0
							&& (mod & Modifier.STATIC) == 0
							&& !m.isSynthetic()
								){
							methods.put(m.getName(), m);
						}
					}
					for(Class<?> s : intf.getInterfaces()){
						if(visited.contains(s)) continue;
						classes.addLast(s);
						visited.add(s);
					}
				}
			} catch(ClassNotFoundException e){
				throw AxisFault.makeFault(e);
			}
		}
		JavaServiceDesc serviceDescription = (JavaServiceDesc)service.getServiceDescription();
		serviceDescription.setAllowedMethods(new ArrayList<String>(methods.keySet()));
		ServiceFactory factory = loader.loadServiceFactory(
				Thread.currentThread().getContextClassLoader()
				, service.getName()
				);

		Class<?> implClass = factory.getService().getClass();
		Class<?> mockClass = factory.getMockService().getClass();
		serviceDescription.loadServiceDescByIntrospection(mockClass);
		service.setOption(BasicProvider.OPTION_WSDL_SERVICEELEMENT, implClass.getSimpleName() + "Service");
		service.setOption(BasicProvider.OPTION_WSDL_PORTTYPE, implClass.getSimpleName());
		String tns = determineTns(implClass, mockClass);
		if(tns != null){
			service.setOption(BasicProvider.OPTION_WSDL_TARGETNAMESPACE, tns);
		}
		
		for(Object o : serviceDescription.getOperations()){
			OperationDesc op = (OperationDesc)o;
			try {
				Method m = methods.get(op.getMethod().getName());
				if(m == null) continue;
				op.setMethod(m);
				try{
					Method implMethod = implClass.getMethod(
							op.getMethod().getName(), op.getMethod().getParameterTypes());
					String[] names = ParamNameExtractor.getParameterNamesFromDebugInfo(implMethod);
					if(names != null){
						for(int i = 0; i < names.length; i++){
							ParameterDesc p = op.getParameter(i);
							if(p != null) p.setName(names[i]);
						}
					}
				} catch(NoSuchMethodException e){
				}

				Annotation[][] annotss = m.getParameterAnnotations();
				int i = 0;
				for(Annotation[] annots : annotss){
					for(Annotation annot : annots){
						if(annot instanceof Parameter){
							op.getParameter(i).setName(((Parameter)annot).name());
						}
					}
					i++;
				}
			} catch (SecurityException e) {
			}
		}
	}

	@Override
	public void processMessage(MessageContext msgContext, SOAPEnvelope reqEnv,
			SOAPEnvelope resEnv, Object obj) throws Exception {
		AxisServiceContext sc = new AxisServiceContext();
		RIProcessor.start(sc, new AxisSoapHeaderElementFactory());
		try{
			super.processMessage(msgContext, reqEnv, resEnv, obj);
		} catch(AxisFault e){
			msgContext.setResponseMessage(new Message(e));
			((HttpServletResponse)msgContext.getProperty(HTTPConstants.MC_HTTP_SERVLETRESPONSE)).setStatus(500);
		} finally{
			Message msg = sc.getMessageContext().getResponseMessage();
			RIProcessor.finish(
					AxisUtil.toSoapMimeHeaders(msg.getMimeHeaders()),
					new SoapHeaderRpcHeadersAdapter(msg.getSOAPHeader()));
		}
	}

	@Override
	protected Object makeNewServiceObject(
			MessageContext msgContext, String clsName)
	throws Exception {
		Object className = msgContext.getService().getOption("className");
		ServiceLoader loader = SGAxisServlet.getCurrentServletServiceLoader();
		if(className != null){
			Class<?> interfaceClass = Class.forName(className.toString());
			String serviceName = getServiceName(SGAxisServlet.getCurrentServletServiceContext());
			return loader.load(serviceName, interfaceClass);
		}
		return super.makeNewServiceObject(msgContext, clsName);
	}

	@Override
	protected String getServiceClassName(Handler service) {
		service.getName();
		return super.getServiceClassName(service);
	}

	public static String getServiceName(ServiceContext serviceContext){
		URL url = serviceContext.getRequestUrl();
		String query = url.getQuery();
		if(query != null){
			String sn = HttpServletRequestUtil.getQueryValue(query, "serviceName");
			if(sn != null){
				return sn;
			}
		}
		String path = url.getPath();
		String[] names = path.split("\\/");
		int n = names.length;
		for(int i = n - 1; i >= 0; i--){
			if(names[i].length() != 0) return names[i];
		}
		return null;
	}

	private String determineTns(Class<?> implClass, Class<?> mockClass){
		Service sa = implClass.getAnnotation(Service.class);
		if(sa != null && sa.namespace().length() > 0){
			return sa.namespace();
		}
		String tns = null;
		for(Class<?> intf : mockClass.getInterfaces()){
			sa = intf.getAnnotation(Service.class);
			if(sa != null && sa.namespace().length() > 0){
				if(tns != null) return null;
				tns = sa.namespace();
			}
		}
		return tns;
	}

	private static final long serialVersionUID = -5086228574438210833L;
}
