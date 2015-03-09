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
package jp.go.nict.langrid.servicecontainer.handler.jsonrpc.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.TypeVariable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.io.DuplicatingInputStream;
import jp.go.nict.langrid.commons.io.IndentedWriter;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.lang.ClassResource;
import jp.go.nict.langrid.commons.lang.ClassResourceLoader;
import jp.go.nict.langrid.commons.lang.ClassUtil;
import jp.go.nict.langrid.commons.net.URLUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.rpc.intf.RpcAnnotationUtil;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.commons.rpc.json.JsonRpcRequest;
import jp.go.nict.langrid.commons.rpc.json.JsonRpcResponse;
import jp.go.nict.langrid.commons.transformer.UTF8ByteArrayToStringTransformer;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.CollectionUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.util.function.Function;
import jp.go.nict.langrid.commons.util.function.Predicate;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.commons.ws.ServletConfigServiceContext;
import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.commons.ws.param.HttpServletRequestParameterContext;
import jp.go.nict.langrid.commons.ws.param.ServletConfigParameterContext;
import jp.go.nict.langrid.commons.ws.param.URLParameterContext;
import jp.go.nict.langrid.commons.ws.servlet.AlternateInputHttpServletRequestWrapper;
import jp.go.nict.langrid.commons.ws.servlet.InputStreamServletInputStream;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONException;
import jp.go.nict.langrid.servicecontainer.executor.StreamingNotifier;
import jp.go.nict.langrid.servicecontainer.handler.RIProcessor;
import jp.go.nict.langrid.servicecontainer.handler.ServiceFactory;
import jp.go.nict.langrid.servicecontainer.handler.ServiceLoader;
import jp.go.nict.langrid.servicecontainer.handler.annotation.ServicesUtil;
import jp.go.nict.langrid.servicecontainer.handler.jsonrpc.JsonRpcDynamicHandler;
import jp.go.nict.langrid.servicecontainer.handler.jsonrpc.JsonRpcHandler;
import jp.go.nict.langrid.servicecontainer.handler.loader.ServiceFactoryLoader;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.servicecontainer.service.composite.Invocation;

import org.apache.commons.lang.StringEscapeUtils;

@SuppressWarnings("serial")
public class JsonRpcServlet extends HttpServlet {
	@Override
	public void init() throws ServletException {
		super.init();
		ParameterContext ct = new ServletConfigParameterContext(getServletConfig(), true);
		this.dumpRequests = ct.getBoolean("dumpRequests", false);
		this.displayProcessTime = ct.getBoolean("displayProcessTime", false);
		this.getMethodEnabled = ct.getBoolean("getMethodEnabled", true);
		this.additionalResponseHeaders = CollectionUtil.stream(Arrays.asList(
				ct.getStrings("additionalResponseHeaders", new String[]{})
				))
				.map(new Function<String, String[]>(){
					public String[] apply(String value) {
						try {
							return URLDecoder.decode(value, "UTF-8").split(":", 2);
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}
					}
				})
				.filter(new Predicate<String[]>() {
					public boolean test(String[] value) {
						if(value.length != 2){
							logger.warning("invalid additional response header: " + value[0]);
							return false;
						}
						return true;
					}
				}).asList().toArray(new String[][]{});
		this.defaultLoaders = ServicesUtil.getServiceFactoryLoaders(getClass());
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
						, (JsonRpcHandler)Class.forName(clazz).newInstance()
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
			ClassResourceLoader.load(this, JsonRpcServlet.class);
		} catch (IllegalArgumentException e) {
			throw new ServletException(e);
		} catch (IOException e) {
			throw new ServletException(e);
		} catch(IllegalAccessException e){
			throw new ServletException(e);
		}
	}

	protected ServiceFactoryLoader[] getDefaultServiceFactoryLoaders(){
		return defaultLoaders;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		ByteArrayOutputStream dupIn = null;
		if(dumpRequests){
			String qs = req.getQueryString();
			Logger.getAnonymousLogger().info(String.format(
					"method: %s, requestUrl: %s",
					req.getMethod(),
					req.getRequestURL().append(
							qs != null ? "?" + URLDecoder.decode(qs, "UTF-8") : ""
							).toString()
					
					));
			dupIn = new ByteArrayOutputStream();
			req = new AlternateInputHttpServletRequestWrapper(
					req, new InputStreamServletInputStream(new DuplicatingInputStream(req.getInputStream(), dupIn)));
		}
		long s = System.currentTimeMillis();
		try{
			super.service(req, res);
		} finally{
			long d = System.currentTimeMillis() - s;
			if(displayProcessTime){
				Logger.getAnonymousLogger().info("processTime: " + d + "ms.");
			}
			if(dupIn != null && dupIn.size() > 0){
				Logger.getAnonymousLogger().info(String.format(
						"requestInput: %s", new String(dupIn.toByteArray(), "UTF-8")
						));
			}
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException{
		ServiceContext sc = new ServletConfigServiceContext(getServletConfig());
		HttpServletRequestParameterContext param = new HttpServletRequestParameterContext(req);
		if(param.getValue("sample") == null && param.getValue("method") != null && getMethodEnabled){
			doProcess(req, resp);
			return;
		}
		ServiceLoader loader = new ServiceLoader(sc, getDefaultServiceFactoryLoaders());
		if(param.getValue("ts") != null){
			RIProcessor.start(sc);
			try{
				doGenerateTypeScript(loader, getServiceName(req), resp);
			} finally{
				RIProcessor.finish();
			}
			return;
		}
		String sample = param.getValue("sample");
		if(sample != null){
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			PrintWriter os = resp.getWriter();
			String serviceName = getServiceName(req);
			String method = req.getParameter("method");
			os.println("request:");
			printSampleRequest(loader, serviceName, method, os);
			os.println("");
			os.println("");
			os.println("response:");
			printSampleResponse(loader, serviceName, method, os);
			os.flush();
			return;
		}
		String uri = req.getRequestURI();
		String cp = getServletContext().getContextPath();
		int idx = uri.indexOf(cp);
		if(idx == -1){
			super.doGet(req, resp);
			return;
		}
		int sidx = uri.indexOf('/', idx + cp.length() + 1);
		if(sidx != -1){
			resp.sendRedirect(uri.substring(idx, sidx));
			return;
		}
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter w = resp.getWriter();
		w.println("<html><head>");
		w.println(css);
		w.println(js);
		w.println("</head><body>");
		w.println("<h2>And now... Some JsonRpc Services</h2>");
		w.println("<ul>");
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Set<String> names = new TreeSet<String>();
		try{
			for(String s : loader.listServiceNames()){
				names.add(s);
			}
			int id = 0;
			for(String s : names){
				w.print("<li><b>" + s);
				ServiceFactory f = loader.loadServiceFactory(cl, s);
				if(f == null){
					w.println("<font color=\"red\">(Failed to load service factory(null))</font></b></li>");
					continue;
				}
				RIProcessor.start(sc);
				try{
					Object service = f.getService();
					if(service instanceof StreamingNotifier){
						w.print("(Streaming ready!)");
					}
					String sdesc = RpcAnnotationUtil.getServiceDescriptions(Service.class, "en");
					if(sdesc != null && sdesc.length() > 0){
						w.print(" - " + StringEscapeUtils.escapeHtml(sdesc));
					}
					w.print("</b><ul>");
					w.print("<li>interfaces<ul>");
					try {
						Set<Class<?>> visited = new HashSet<Class<?>>();
						for(Class<?> intf : f.getInterfaces()){
							if(visited.contains(intf)) continue;
							visited.add(intf);
							if(StreamingNotifier.class.isAssignableFrom(intf)) continue;
							w.print("<li>" + prettyName(intf));
							String desc = RpcAnnotationUtil.getServiceDescriptions(intf, "en");
							if(desc != null && desc.length() > 0){
								w.print(" - " + StringEscapeUtils.escapeHtml(desc));
							}
							w.print("<ul>");
							try{
								Set<Method> methods = new TreeSet<Method>(new Comparator<Method>() {
									public int compare(Method o1, Method o2) {
										int r = o1.getName().compareTo(o2.getName());
										if(r != 0) return r;
										return o1.getParameterTypes().length - o2.getParameterTypes().length;
									}
								});
								methods.addAll(Arrays.asList(intf.getMethods()));
								for(Method m : methods){
									if(m.isSynthetic()) continue;
									if((m.getModifiers() & Modifier.PUBLIC) == 0) continue;
									printMethod(s, m, getImplementedMethod(service, m), id++, w);
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
					if(service != null){
						w.println("<li>" + prettyName(service.getClass()) + "</li>");
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
					} else{
						w.println("<li><font color=\"red\"><b>failed to load implementation class.</b></font></li>");
					}
					w.println("</ul></li>");
					w.println("</ul></li>");
					w.println("<br/>");
				} finally{
					RIProcessor.finish();
				}
			}
		} catch(IOException e){
			w.println("<pre><font color=\"red\">");
			e.printStackTrace(w);
			w.println("</font></pre>");
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
		doProcess(req, resp);
	}

	private String toTs(Class<?> clazz){
		if(clazz.equals(boolean.class) || clazz.equals(Boolean.class)){
			return "boolean";
		} else if(clazz.equals(char.class) || clazz.equals(Character.class) || clazz.equals(String.class)){
			return "string";
		} else if(clazz.equals(short.class) || clazz.equals(int.class) || clazz.equals(long.class) ||
				clazz.equals(float.class) || clazz.equals(double.class) || Number.class.isAssignableFrom(clazz)){
			return "number";
		} else if(clazz.equals(void.class)){
			return "void";
		} else{
			return "any";
		}
	}

	private void doGenerateTypeScript(ServiceLoader loader, String serviceName, HttpServletResponse resp)
	throws ServletException, IOException{
		ServiceFactory f = loader.loadServiceFactory(Thread.currentThread().getContextClassLoader(), serviceName);
		IndentedWriter w = new IndentedWriter(resp.getWriter());
		w.println("/// <reference path=\"jquery.d.ts\" />");
		w.println("class %s{", serviceName);
		w.indent().println("constructor(path: string){");
		w.indent().println("this.path = path;");
		w.unindent().println("}");
		Object service = f.getService();
		for(Class<?> intf : f.getInterfaces()){
			for(Method m : intf.getMethods()){
				Method imm = getImplementedMethod(service, m);
				if(imm == null) imm = m;
				StringBuilder params = new StringBuilder();
				StringBuilder paramDefs = new StringBuilder();
				Class<?>[] paramTypes = imm.getParameterTypes();
				Trio<String, String, String>[] nameSampleDescs = RpcAnnotationUtil.getParameterInfo(m, imm, "en");
				int n = paramTypes.length;
				for(int i = 0; i < n; i++){
					if(i != 0){
						paramDefs.append(", ");
						params.append(", ");
					}
					String name = nameSampleDescs[i].getFirst();
					paramDefs.append(name).append(": ").append(toTs(paramTypes[i]));
					params.append(name);
				}
				w.println("public %s(%s): %s{", imm.getName(), paramDefs, toTs(imm.getReturnType()));
				w.indent().println("return this.ajax(\"%s\", [%s]);", m.getName(), params);
				w.unindent().println("}");
			}
		}
		w.println("private ajax(methodName: string, params: any[]): any{").indent()
			.println("return $.ajax({").indent()
			.println("type: \"POST\", url: this.path,")
			.println("dataType: \"json\", data: JSON.stringify({").indent()
			.println("method: methodName,")
			.println("params: params")
			.unindent("})")
			.unindent("});")
			.unindent("}")
			.println("private path: string;")
			.unindent("}");
	}

	private Method getImplementedMethod(Object instance, Method method){
		try {
			return instance.getClass().getMethod(method.getName(), method.getParameterTypes());
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			return null;
		}
	}

	private void printMethod(String s, Method inm, Method imm, int id, PrintWriter w){
		if(imm == null) imm = inm;
		w.print("<li>" + prettyName(imm.getReturnType()) + " <b>" + imm.getName() + "</b>(");
		Class<?>[] paramTypes = imm.getParameterTypes();
		Trio<String, String, String>[] nameSampleDescs = RpcAnnotationUtil.getParameterInfo(inm, imm, "en");
		int n = paramTypes.length;
		for(int i = 0; i < n; i++){
			if(i != 0){
				w.print(", ");
			}
			w.print(prettyName(paramTypes[i]));
			String name = nameSampleDescs[i].getFirst();
			String desc = nameSampleDescs[i].getThird();
			if(name.length() > 0){
				w.print(" ");
				if(desc.length() > 0){
					w.print("<span class=\"paramInfo\">" + name + "<span>" + desc + "</span></span>");
				} else{
					w.print(name);
				}
			}
		}
		w.print(String.format(
				") [<span class=\"info2\" onMouseOver=\"loadSample('sample%d','%s','%s','req')\">sample<span id=\"sample%1$d\">loading...</span></span>] "
				+ "<span onClick=\"togglePanel('invokePanel%1$d');return false;\">+</span>"
				, id, s, imm.getName()));

		w.print("<br/>");

		w.print(String.format("<span id=\"invokePanel%d\" class=\"invokePanel\">", id));
		String mdesc = RpcAnnotationUtil.getMethodDescription(inm, imm, "en");
		if(mdesc != null){
			w.println(StringEscapeUtils.escapeHtml(mdesc) + "<br/>");
		}
		w.print(String.format("<form id=\"f%1$d\">", id));

		for(int i = 0; i < n; i++){
			if(i != 0){
				w.print(", ");
			}
			String name = nameSampleDescs[i].getFirst();
			String sample = nameSampleDescs[i].getSecond();
			String desc = nameSampleDescs[i].getThird();
			String d = name;
			if(d.length() > 0){
				if(desc.length() > 0){
					d = d + " - " + desc;
				}
			} else{
				d = desc;
			}
			if(d.length() > 0){
				w.print("<span class=\"paramInfo\"><textarea cols=\"4\" rows=\"1\" placeholder=\"" +
						StringEscapeUtils.escapeHtml(d) + "\">" +
						sample + "</textarea><span>" + d + "</span></span>");
			} else{
				w.print("<textarea cols=\"4\" rows=\"1\">" + sample + "</textarea>");
			}
		}

		w.print(String.format("[<a href=\"#\" onClick=\"invokeMethod('f%d','res%1$d', '%s', '%s');return false;\">invoke</a>]"
				, id, s, imm.getName()));
		w.print(String.format("[<a href=\"#\" onClick=\"clearLog('res%d');return false;\">clear</a>]"
				, id));
		w.print("</form>");
		w.print(String.format("<div id=\"res%d\" class=\"resPanel\"></div>", id));
		w.println("</span></li>");
	}

	private void printSampleRequest(
			ServiceLoader loader, String serviceName, String method, PrintWriter os)
	throws ServletException, IOException{
		ServiceFactory f = loader.loadServiceFactory(
				Thread.currentThread().getContextClassLoader(), serviceName);
		Method met = null;
		for(Class<?> c : f.getInterfaces()){
			for(Method m : c.getMethods()){
				if(m.getName().equals(method)){
					met = m;
					break;
				}
			}
			if(met != null) break;
		}
		try {
			Class<?>[] types = met.getParameterTypes();
			Object[] params = new Object[types.length];
			for(int i = 0; i < types.length; i++){
				params[i] = ClassUtil.newDummyInstance(types[i]);
			}
			JsonRpcRequest jreq = new JsonRpcRequest();
			jreq.setMethod(method);
			jreq.setParams(params);
			JSON.encode(jreq, os);
		} catch (JSONException e) {
			e.printStackTrace(os);
		} catch (InstantiationException e) {
			e.printStackTrace(os);
		} catch (IllegalAccessException e) {
			e.printStackTrace(os);
		}
	}

	private void printSampleResponse(
			ServiceLoader loader, String serviceName, String method, PrintWriter os)
	throws ServletException, IOException{
		ServiceFactory f = loader.loadServiceFactory(
				Thread.currentThread().getContextClassLoader(), serviceName);
		Method met = null;
		for(Class<?> c : f.getInterfaces()){
			for(Method m : c.getMethods()){
				if(m.getName().equals(method)){
					met = m;
					break;
				}
			}
			if(met != null) break;
		}
		try {
			JsonRpcResponse jres = new JsonRpcResponse();
			jres.setResult(ClassUtil.newDummyInstance(met.getReturnType()));
			JSON.encode(jres, os);
		} catch (JSONException e) {
			e.printStackTrace(os);
		} catch (InstantiationException e) {
			e.printStackTrace(os);
		} catch (IllegalAccessException e) {
			e.printStackTrace(os);
		}
	}

	private void doProcess(HttpServletRequest request, HttpServletResponse resp)
	throws ServletException, IOException{
		for(String[] h : additionalResponseHeaders){
			resp.addHeader(h[0], h[1]);
		}
		resp.setCharacterEncoding("UTF-8");

		String serviceName = getServiceName(request);

		JsonRpcRequest req = null;
		if(request.getMethod().equals("GET")){
			req = decodeFromPC(new URLParameterContext(request.getQueryString()));
		} else{
			req = JSON.decode(StreamUtil.readAsString(request.getInputStream(), "UTF-8"), JsonRpcRequest.class);
			String cb = request.getParameter("callback");
			if(cb != null){
				req.setCallback(cb);
			}
			if(req.getHeaders() == null){
				req.setHeaders(new RpcHeader[]{});
			}
		}
		// The handler is acquired from the service name.
		JsonRpcHandler h = handlers.get(serviceName);
		// Execution of service
		if(h == null){
			h = new JsonRpcDynamicHandler();
		}
		ServiceContext sc = new ServletServiceContext(getServletConfig(), request, Arrays.asList(req.getHeaders()));
		ServiceLoader loader = new ServiceLoader(sc, getDefaultServiceFactoryLoaders());
		OutputStream os = resp.getOutputStream();
		h.handle(sc, loader, serviceName, req, resp, os);
		os.flush();
	}

	private static JsonRpcRequest decodeFromPC(ParameterContext param)
	throws JSONException{
		JsonRpcRequest req = new JsonRpcRequest();
		req.setId(param.getValue("id"));
		req.setMethod(param.getValue("method"));
		Object[] v = JSON.decode(param.getString("params", "[]"), Object[].class);
		v = ArrayUtil.append(v, (Object[])param.getStrings("params[]", new String[]{}));
		req.setParams(v);
		req.setHeaders(JSON.decode(param.getString("headers", "[]"), RpcHeader[].class));
		req.setCallback(param.getValue("callback"));
		return req;
	}

	private static String getServiceName(HttpServletRequest req){
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
		return serviceName;
	}

	private static String prettyName(Class<?> clazz){
		StringBuilder postfix = new StringBuilder();
		if(Proxy.isProxyClass(clazz)){
			return "<em>Proxy Implementation (detailed information unavailable)</em>";
		}
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

	private boolean displayProcessTime;
	private boolean dumpRequests;
	private boolean getMethodEnabled;
	private String[][] additionalResponseHeaders = {};
	private ServiceFactoryLoader[] defaultLoaders;

	@ClassResource(path="css.txt", converter=UTF8ByteArrayToStringTransformer.class)
	private String css;
	@ClassResource(path="js.txt", converter=UTF8ByteArrayToStringTransformer.class)
	private String js;

	private static Map<String, JsonRpcHandler> handlers = new HashMap<String, JsonRpcHandler>();
	private static Logger logger = Logger.getLogger(JsonRpcServlet.class.getName());
}
