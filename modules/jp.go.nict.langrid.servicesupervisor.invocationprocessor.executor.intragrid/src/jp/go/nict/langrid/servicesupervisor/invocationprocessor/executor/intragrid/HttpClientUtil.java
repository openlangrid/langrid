/*
 * $Id: HttpClientUtil.java 407 2011-08-25 02:21:46Z t-nakaguchi $
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
package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.intragrid;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Logger;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import jp.go.nict.langrid.commons.net.ssl.SSLUtil;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.SSLSocketFactorySSLProtocolSocketFactory;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.SocketFactoryProtocolSocketFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 407 $
 */
public class HttpClientUtil {
	/**
	 * 
	 * 
	 */
	public static HttpClient createHttpClientWithHostConfig(URL url){
		HttpClient client = new HttpClient();
		int port = url.getPort();
		if(port == -1){
			port = url.getDefaultPort();
			if(port == -1){
				port = 80;
			}
		}
		if((url.getProtocol().equalsIgnoreCase("https")) && (sslSocketFactory != null)){
			Protocol https = new Protocol(
					"https"
					, (ProtocolSocketFactory)
					new SSLSocketFactorySSLProtocolSocketFactory(sslSocketFactory)
					, port
					);
			client.getHostConfiguration().setHost(
					url.getHost(), url.getPort(), https
					);
		} else{
			Protocol http = new Protocol(
					"http", new SocketFactoryProtocolSocketFactory(
							SocketFactory.getDefault()
							), port
							);
			client.getHostConfiguration().setHost(
					url.getHost(), url.getPort(), http
					);
		}
		try{
			List<Proxy> proxies = ProxySelector.getDefault().select(
					url.toURI()
					);
			for(Proxy p : proxies){
				if(p.equals(Proxy.NO_PROXY)) continue;
				if(!p.type().equals(Proxy.Type.HTTP)) continue;
				InetSocketAddress addr = (InetSocketAddress)p.address();
				client.getHostConfiguration().setProxy(
					addr.getHostName(), addr.getPort()
					);
				client.getState().setProxyCredentials(AuthScope.ANY, new UsernamePasswordCredentials("", ""));
				break;
			}
		} catch(URISyntaxException e){
			e.printStackTrace();
		}
		return client;
	}

	private static Logger logger = Logger.getLogger(HttpClientUtil.class.getName());
	private static SSLSocketFactory sslSocketFactory;
	static{
		try{
			sslSocketFactory = SSLUtil.createTrustfulSocketFactory("TLS");
		} catch(KeyManagementException e){
			logger.warning("failed to create trustful socket factory.");
		} catch(NoSuchAlgorithmException e){
			logger.warning("failed to create trustful socket factory.");
		}
	}
}
