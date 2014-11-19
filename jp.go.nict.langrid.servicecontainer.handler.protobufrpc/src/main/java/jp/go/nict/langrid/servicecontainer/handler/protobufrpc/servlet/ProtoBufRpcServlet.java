/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.handler.protobufrpc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.lang.ClassResource;
import jp.go.nict.langrid.commons.lang.ClassResourceLoader;
import jp.go.nict.langrid.commons.net.URLUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.rpc.intf.Description;
import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.commons.transformer.UTF8ByteArrayToStringTransformer;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.param.ServletConfigParameterContext;
import jp.go.nict.langrid.servicecontainer.executor.StreamingNotifier;
import jp.go.nict.langrid.servicecontainer.handler.ServiceFactory;
import jp.go.nict.langrid.servicecontainer.handler.ServiceLoader;
import jp.go.nict.langrid.servicecontainer.handler.protobufrpc.ProtoBufDynamicHandler;
import jp.go.nict.langrid.servicecontainer.handler.protobufrpc.ProtoBufHandler;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.servicecontainer.service.composite.Invocation;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

@SuppressWarnings("serial")
public class ProtoBufRpcServlet extends HttpServlet {
	@Override
	public void init() throws ServletException {
		super.init();
		ParameterContext ct = new ServletConfigParameterContext(getServletConfig(), true);
		String mapping = ct.getString("mapping", "");
		for(String s : mapping.split(",")){
			String[] v = s.split(":");
			if(v.length != 2) continue;
			String name = v[0].trim();
			String clazz = v[1].trim();
			String failMsg = "failed to load handler: [" + name
					+ ":" + clazz + "].";
			try {
				handlers.put(
						name
						, (ProtoBufHandler)Class.forName(clazz).newInstance()
						);
			} catch(InstantiationException e){
				logger.log(Level.WARNING, failMsg, e);
			} catch(IllegalAccessException e){
				logger.log(Level.WARNING, failMsg, e);
			} catch(ClassNotFoundException e){
				logger.log(Level.WARNING, failMsg, e);
			} catch(ClassCastException e){
				logger.log(Level.WARNING, failMsg, e);
			}
		}
		try{
			ClassResourceLoader.load(this);
		} catch (IllegalArgumentException e) {
			throw new ServletException(e);
		} catch (IOException e) {
			throw new ServletException(e);
		} catch(IllegalAccessException e){
			throw new ServletException(e);
		}
	}

	@ClassResource(path="css.txt", converter=UTF8ByteArrayToStringTransformer.class)
	private String css;

	/**
	 * 
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException{
		ServiceContext sc = new ServletServiceContext(req);
		ServiceLoader loader = new ServiceLoader(sc);
		PrintWriter w = resp.getWriter();
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		w.println("<html><head>");
		w.println(css);
		w.println("</head><body>");
		w.println("<h2>And now... Some ProtobufRpc Services</h2>");
		w.println("<ul>");
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Set<String> names = new TreeSet<String>();
		for(String s : loader.listServiceNames()){
			names.add(s);
		}
		for(String s : names){
			ServiceFactory f = loader.loadServiceFactory(cl, s);
			Object service = f.getService();
			if(service instanceof StreamingNotifier){
				s = s + "(Streaming ready!)";
			}
			Service sa = service.getClass().getAnnotation(Service.class);
			if(sa != null){
				Description[] desc = sa.descriptions();
				if(desc.length > 0){
					s = s + " - " + desc[0].value();
				}
			}
			w.print("<li><b>" + s + "</b><ul>");
			w.print("<li>interfaces<ul>");
			try {
				for(Class<?> clazz : f.getInterfaces()){
					if(StreamingNotifier.class.isAssignableFrom(clazz)) continue;
					w.print("<li>" + prettyName(clazz) + "<ul>");
					try{
						Set<Method> methods = new TreeSet<Method>(new Comparator<Method>() {
							public int compare(Method o1, Method o2) {
								return o1.getName().compareTo(o2.getName());
							}
						});
						methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
						for(Method m : methods){
							if(m.isSynthetic()) continue;
							if((m.getModifiers() & Modifier.PUBLIC) == 0) continue;
							w.print("<li>" + prettyName(m.getReturnType()) + " <b>" + m.getName() + "</b>(");
							boolean first = true;
							Annotation[][] annotss = m.getParameterAnnotations();
							int i = 0;
							for(Class<?> pc : m.getParameterTypes()){
								if(first){
									first = false;
								} else{
									w.print(", ");
								}
								w.print(prettyName(pc));
								for(Annotation annot : annotss[i]){
									if(annot instanceof Parameter){
										w.print(" ");
										w.print(((Parameter)annot).name());
										break;
									}
								}
								i++;
							}
							w.println(")</li>");
						}
					} finally{
						w.println("</ul></li>");
					}
				}
			} catch (SecurityException e) {
			} finally{
				w.println("</ul></li>");
			}
			w.print("<li>implementation<ul>");
			w.println("<li>" + prettyName(service.getClass()) + "</li></ul></li>");
			if(service instanceof AbstractCompositeService){
				boolean first = true;
				for(Pair<Invocation, Class<?>> v : ((AbstractCompositeService)service).invocations()){
					if(first){
						w.println("<li>invocations<ul>");
						first = false;
					}
					w.println("<li><b>" + v.getFirst().name() + (v.getFirst().required() ? "(required)" : "") + "</b>: "
							+ prettyName(v.getSecond()) + "</li>"); 
				}
				if(!first){
					w.println("</ul></li>");
				}
			}
			w.println("</ul></li>");
			w.println("<br/>");
		}
		w.println("</ul>");
		w.println("</body></html>");
	}

	/**
	 * 
	 * 
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException{
		CodedInputStream cis = CodedInputStream.newInstance(req.getInputStream());
		CodedOutputStream cos = CodedOutputStream.newInstance(resp.getOutputStream());

		String fullName = cis.readString();
		String uri = req.getRequestURL().toString();
		String serviceName = uri.substring(uri.lastIndexOf('/') + 1);
		String queryStr = req.getQueryString();
		if(queryStr != null){
			try{
				Map<String, String> params = URLUtil.getQueryParameters(new URL(uri + "?" + queryStr));
				String sn = params.get("serviceName");
				if(sn != null){
					serviceName = sn;
				}
			} catch(MalformedURLException e){
			}
		}

		// The handler is acquired from the service name.
		String[] names = fullName.split("\\.");
		String sn = names[0];
		String mn = names[names.length - 1];
		ProtoBufHandler h = handlers.get(sn.toLowerCase());

		// Execution of service
		if(h == null){
			h = new ProtoBufDynamicHandler();
		}
		h.handle(serviceName, mn, req, resp, cis, cos);
		cos.flush();
	}

	private static String prettyName(Class<?> clazz){
		StringBuilder postfix = new StringBuilder();
		if(clazz.isArray()){
			clazz = clazz.getComponentType();
			appendGenericsInfo(clazz, postfix);
			postfix.append("[]");
		} else{
			appendGenericsInfo(clazz, postfix);
		}
		String n = clazz.getName();
		String sn = clazz.getSimpleName();
		return "<span class=\"info\">" + sn + postfix + "<span>" + n + postfix + "</span></span>";
	}

	private static void appendGenericsInfo(Class<?> clazz, StringBuilder b){
		for(TypeVariable<? extends GenericDeclaration> v : clazz.getTypeParameters()){
			if(b.length() == 0){
				b.append("&lt;");
			} else{
				b.append(",");
			}
			b.append(v.getName());
		}
		if(b.length() != 0) b.append("&gt;");
	}

	private static Map<String, ProtoBufHandler> handlers = new HashMap<String, ProtoBufHandler>();
	private static Logger logger = Logger.getLogger(ProtoBufRpcServlet.class.getName());
	// Mapping of service name and handler
	public static void registerHandler(String serviceName, ProtoBufHandler handler){
		handlers.put(serviceName, handler);
	}
}
