/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
package jp.go.nict.langrid.client.axis;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.net.proxy.ProxyUtil;

import org.apache.axis.AxisProperties;
import org.apache.axis.components.net.BooleanHolder;
import org.apache.axis.components.net.DefaultSocketFactory;
import org.apache.axis.components.net.SocketFactory;
import org.apache.axis.components.net.TransportClientProperties;
import org.apache.axis.components.net.TransportClientPropertiesFactory;
import org.apache.axis.encoding.Base64;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.axis.utils.Messages;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 562 $
 */
@SuppressWarnings("unchecked")
public class ProxySelectingAxisSocketFactory extends DefaultSocketFactory{
	/**
	 * 
	 * 
	 */
	public static void install(){
		AxisProperties.setProperty(SocketFactory.class.getName(),
				ProxySelectingAxisSocketFactory.class.getName());
	}

	/**
	 * 
	 * 
	 */
	public ProxySelectingAxisSocketFactory(Hashtable<?, ?> attributes) {
		super(attributes);
	}

	public Socket create(
			String host, int port, StringBuffer otherHeaders, BooleanHolder useFullURL
			) throws Exception
	{
		int timeout = 0;
		if (attributes != null) {
			String value = (String)attributes.get(CONNECT_TIMEOUT);
			timeout = (value != null) ? Integer.parseInt(value) : 0;
		}

		TransportClientProperties tcp = TransportClientPropertiesFactory.create("http");

		Socket sock = null;
		boolean hostInNonProxyList = isHostInNonProxyList(host, tcp.getNonProxyHosts());

		if (tcp.getProxyUser().length() != 0) {
			StringBuffer tmpBuf = new StringBuffer();

			tmpBuf.append(tcp.getProxyUser())
					.append(":")
					.append(tcp.getProxyPassword());
			otherHeaders.append(HTTPConstants.HEADER_PROXY_AUTHORIZATION)
					.append(": Basic ")
					.append(Base64.encode(tmpBuf.toString().getBytes()))
					.append("\r\n");
		}
		if (port == -1) {
			port = 80;
		}
		if(hostInNonProxyList){
			sock = create(host, port, timeout);
			logger.finest(Messages.getMessage("createdHTTP00"));
		} else if ((tcp.getProxyHost().length() == 0) ||
				(tcp.getProxyPort().length() == 0)){
			sock = createSocketAccordingProxySelector(host, port, timeout, useFullURL);
		} else {
			sock = create(tcp.getProxyHost(),
					new Integer(tcp.getProxyPort()).intValue(),
					timeout);
			logger.finest(Messages.getMessage("createdHTTP01", tcp.getProxyHost(),
					tcp.getProxyPort()));
			useFullURL.value = true;
		}
		return sock;
	}

	private Socket createSocketAccordingProxySelector(
			String host, int port, int timeout, BooleanHolder useFullURL)
			throws IllegalAccessException, InstantiationException
			, InvocationTargetException, IOException
			, UnknownHostException, URISyntaxException
	{
		Socket sock = null;
		List<Proxy> proxies = ProxySelector.getDefault().select(
				new URI(String.format("http://%s:%d/", host, port))
				);
		if(proxies.equals(ProxyUtil.DIRECT) || proxies.size() == 0){
			sock = create(host, port, timeout);
			logger.finest(Messages.getMessage("createdHTTP00"));
		} else{
			for(Proxy p : proxies){
				if(p.type().equals(Proxy.Type.HTTP)){
					if(!(p.address() instanceof InetSocketAddress)){
						continue;
					}

					InetSocketAddress addr = (InetSocketAddress)p.address();
					if(addr.getAddress() != null){
						sock = create(
								Proxy.NO_PROXY, addr.getAddress(), addr.getPort()
								, timeout);
					} else{
						sock = create(
								Proxy.NO_PROXY, addr.getHostName(), addr.getPort()
								, timeout);
					}
					useFullURL.value = true;
					logger.finest(Messages.getMessage(
								"createdHTTP01", addr.getHostName()
								, Integer.toString(addr.getPort())
								));
					break;
				} else if(p.type().equals(Proxy.Type.SOCKS)){
					sock = create(p, host, port, timeout);
					logger.finest(Messages.getMessage("createdHTTP00"));
					break;
				}
			}
			if(sock == null){
				sock = create(host, port, timeout);
				logger.finest(Messages.getMessage("createdHTTP00"));
			}
		}
		return sock;
	}

	private static boolean plain;
	private static Class<InetSocketAddress> inetClass;
	private static Constructor<InetSocketAddress> inetConstructor;
	private static Constructor<InetSocketAddress> inetConstructorWithInetAddress;
	private static Constructor<Socket> socketConstructor;
	private static Constructor<Socket> socketConstructorWithProxy;
	private static Method connect;

	static {
		try {
			inetClass = (Class<InetSocketAddress>)Class.forName("java.net.InetSocketAddress");
			plain = false;
			inetConstructor = inetClass.getConstructor(String.class, int.class);
			inetConstructorWithInetAddress = inetClass.getConstructor(InetAddress.class, int.class);
			socketConstructor = Socket.class.getConstructor();
			socketConstructorWithProxy = Socket.class.getConstructor(Proxy.class);
			connect = Socket.class.getMethod(
					"connect", inetClass.getSuperclass(), int.class
					);
		} catch (Exception e) {
			plain = true;
		}
	}

	/**
	 * 
	 * Creates a socket with connect timeout using reflection API
	 *
	 * @param host
	 * @param port
	 * @param timeout
	 * 
	 */
	private static Socket create(String host, int port, int timeout)
			throws IllegalAccessException, InstantiationException
			, InvocationTargetException, IOException
			, UnknownHostException
	{
		Socket sock = null;
		if (plain || timeout == 0) {
			sock = new Socket(host, port);
		} else {
			Object address = inetConstructor.newInstance(host, port);
			sock = (Socket)socketConstructor.newInstance();
			connect.invoke(sock, address, timeout);
		}
		return sock;
	}
/*
	private static Socket create(InetAddress inetAddress, int port, int timeout)
			throws IllegalAccessException, InstantiationException
			, InvocationTargetException, IOException
			, UnknownHostException
	{
		Socket sock = null;
		if (plain || timeout == 0) {
			sock = new Socket(inetAddress, port);
		} else {
			Object address = inetConstructorWithInetAddress.newInstance(inetAddress, port);
			sock = socketConstructor.newInstance();
			connect.invoke(sock, address, timeout);
		}
		return sock;
	}
*/
	private static Socket create(Proxy proxy, String host, int port, int timeout)
			throws IllegalAccessException, InstantiationException
			, InvocationTargetException, IOException
			, UnknownHostException
	{
		Socket sock = null;
		if (plain || timeout == 0) {
			sock = new Socket(proxy);
			sock.connect(inetConstructor.newInstance(host, port));
		} else {
			Object address = inetConstructor.newInstance(host, port);
			sock = socketConstructorWithProxy.newInstance(proxy);
			connect.invoke(sock, address, timeout);
		}
		return sock;
	}

	private static Socket create(Proxy proxy, InetAddress inetAddress, int port, int timeout)
			throws IllegalAccessException, InstantiationException
			, InvocationTargetException, IOException
			, UnknownHostException
	{
		Socket sock = null;
		if (plain || timeout == 0) {
			sock = new Socket(proxy);
			sock.connect(inetConstructorWithInetAddress.newInstance(inetAddress, port));
		} else {
			Object address = inetConstructorWithInetAddress.newInstance(inetAddress, port);
			sock = socketConstructorWithProxy.newInstance(proxy);
			connect.invoke(sock, address, timeout);
		}
		return sock;
	}

	private static Logger logger = Logger.getLogger(
			ProxySelectingAxisSocketFactory.class.getName());
}
