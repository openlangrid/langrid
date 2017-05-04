/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.client.soap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

import org.apache.commons.lang.StringEscapeUtils;
import org.xml.sax.SAXException;

import jp.go.nict.langrid.client.ClientFactory;
import jp.go.nict.langrid.client.RequestAttributes;
import jp.go.nict.langrid.client.ResponseAttributes;
import jp.go.nict.langrid.client.RpcRequestAttributes;
import jp.go.nict.langrid.client.RpcResponseAttributes;
import jp.go.nict.langrid.client.soap.io.SoapRequestWriter;
import jp.go.nict.langrid.client.soap.io.SoapResponseParser;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.io.EmptyInputStream;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.net.HttpURLConnectionUtil;
import jp.go.nict.langrid.commons.net.HttpURLConnectionUtil.WriteProcess;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcFaultUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.Protocols;

public class SoapClientFactory implements ClientFactory {
	static class AxisStublessInvocationHandler implements InvocationHandler{
		public AxisStublessInvocationHandler(URL url, Converter converter){
			this.url = url;
			this.converter = converter;
			this.reqAttrs = new RpcRequestAttributes();
			this.resAttrs = new RpcResponseAttributes();
		}
		protected void preInvocation() {
		}
		protected void postInvocation() {
		}
		public Object invoke(Object proxy, final Method method, final Object[] args)
		throws Throwable {
			Class<?> clz = method.getDeclaringClass();
			if(RequestAttributes.class.isAssignableFrom(clz)){
				if(method.getName().equals("setTargetNamespace")){
					targetNamespace = args[0].toString();
					return null;
				} else{
					return method.invoke(reqAttrs, args);
				}
			} else if(clz.equals(ResponseAttributes.class)){
				return method.invoke(resAttrs, args);
			} else{
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
				try{
					con.setDoOutput(true);
					con.setConnectTimeout(10 * 1000);
					con.setReadTimeout(60 * 10 * 1000);
					con.setRequestProperty(LangridConstants.HTTPHEADER_PROTOCOL, Protocols.SOAP_RPCENCODED);
					con.setRequestProperty("Accept", "application/soap+xml");
					con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
					con.setRequestProperty("SOAPAction", "\"\"");
					reqAttrs.setUpConnection(con);
					OutputStream os = HttpURLConnectionUtil.processWriteRequest(
							con,
							reqAttrs.isRequestContentCompression(),
							reqAttrs.getRequestContentComporessionThreashold(),
							reqAttrs.getRequestContentCompressionAlgorithm(),
							new WriteProcess() {
								@Override
								public void write(OutputStream out) throws IOException {
									try {
										SoapRequestWriter.writeSoapRequest(
												outputStreamFilter.filter(out), targetNamespace, reqAttrs.getAllRpcHeaders(), method, args);
									} catch (IllegalAccessException e) {
										throw new IOException(e);
									} catch (InvocationTargetException e) {
										throw new IOException(e);
									}
								}
							});
					os.flush();
					InputStream is = HttpURLConnectionUtil.openResponseStream(con);
					if(is == null){
						is = new EmptyInputStream();
					}
					int code = con.getResponseCode();
					if(code != 200 && code != 500){
						String response = "";
						try{
							response = StringEscapeUtils.unescapeXml(
									StreamUtil.readAsString(is, "UTF-8")
									);
						} catch(IOException e){
						}
						con.disconnect();
						if(response.length() == 0) response = "HTTP " + code;
						if(code == 403){
							throw new RuntimeException(response + "  with "
									+ reqAttrs.getUserId() + ":*****");
						}
						throw new RuntimeException(response);
					}
					is = inputStreamFilter.filter(is);
					try{
						Class<?> rt = method.getReturnType();
						Trio<Collection<RpcHeader>, RpcFault, ?> ret
							= SoapResponseParser.parseSoapResponse(rt, method.getName(), is, converter);
						if(ret == null) throw new RuntimeException("null response");
						resAttrs.loadAttributes(con, ret.getFirst());
						if(ret.getSecond() != null){
							RpcFault f = ret.getSecond();
							throw RpcFaultUtil.rpcFaultToThrowable(f);
						}
						return ret.getThird();
					} catch(IOException e){
						throw new RuntimeException(e);
					} catch(SAXException e){
						throw new RuntimeException(e);
					} finally{
						is.close();
					}
				} finally{
					con.disconnect();
				}
			}
		}
		private URL url;
		private Converter converter;
		private String targetNamespace;
		private RpcRequestAttributes reqAttrs;
		private RpcResponseAttributes resAttrs;
	}

	public SoapClientFactory() {
	}

	public <T> T create(Class<T> interfaceClass, URL url){
		AxisStublessInvocationHandler h = new AxisStublessInvocationHandler(url, converter);
		return create(interfaceClass, h);
	}

	public <T> T create(Class<T> interfaceClass, URL url, String userId, String password){
		AxisStublessInvocationHandler h = new AxisStublessInvocationHandler(url, converter);
		h.reqAttrs.setUserId(userId);
		h.reqAttrs.setPassword(password);
		return create(interfaceClass, h);
	}

	private <T> T create(Class<T> interfaceClass, AxisStublessInvocationHandler h){
		return interfaceClass.cast(Proxy.newProxyInstance(
				interfaceClass.getClassLoader()
				, new Class<?>[]{interfaceClass, SoapRequestAttributes.class, ResponseAttributes.class}
				, h
				));
	}

	public Converter getConverter(){
		return converter;
	}

	private Converter converter = new Converter();

	public static InputStreamFilter setInputStreamFilter(InputStreamFilter inputStreamFilter){
		InputStreamFilter cur = SoapClientFactory.inputStreamFilter;
		SoapClientFactory.inputStreamFilter = inputStreamFilter;
		return cur;
	}

	public static OutputStreamFilter setOutputStreamFilter(OutputStreamFilter outputStreamFilter){
		OutputStreamFilter cur = SoapClientFactory.outputStreamFilter;
		SoapClientFactory.outputStreamFilter = outputStreamFilter;
		return cur;
	}

	private static InputStreamFilter inputStreamFilter = new InputStreamFilter() {
		@Override
		public InputStream filter(InputStream is) {
			return is;
		}
	};
	private static OutputStreamFilter outputStreamFilter = new OutputStreamFilter() {
		@Override
		public OutputStream filter(OutputStream os) {
			return os;
		}
	};
}
