/*
 * $Id: ProxySelectingSecureAxisSocketFactory.java 477 2012-05-22 07:43:21Z t-nakaguchi $
 * 
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * このソースコードはapache-axis1.4に含まれるorg.apache.axis.components.net.JSSESocketFactory
 * クラスのソースコードを元に、ProxySelectorによるプロキシ選択機能を追加した物です。
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
package jp.go.nict.langrid.client.axis;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;

import jp.go.nict.langrid.commons.net.proxy.ProxyUtil;
import jp.go.nict.langrid.commons.net.ssl.SSLUtil;

import org.apache.axis.AxisProperties;
import org.apache.axis.components.net.BooleanHolder;
import org.apache.axis.components.net.JSSESocketFactory;
import org.apache.axis.components.net.SecureSocketFactory;
import org.apache.axis.components.net.TransportClientProperties;
import org.apache.axis.components.net.TransportClientPropertiesFactory;
import org.apache.axis.utils.Messages;
import org.apache.axis.utils.StringUtils;
import org.apache.axis.utils.XMLUtils;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 477 $
 */
public class ProxySelectingSecureAxisSocketFactory extends JSSESocketFactory{
	/**
	 * 
	 * 
	 */
	public static void install(){
		AxisProperties.setProperty(SecureSocketFactory.class.getName(),
				ProxySelectingSecureAxisSocketFactory.class.getName());
	}

	/**
	 * 
	 * 
	 */
	public ProxySelectingSecureAxisSocketFactory(Hashtable<?, ?> attributes) {
		super(attributes);
	}

	public Socket create(
			String host, int port, StringBuffer otherHeaders, BooleanHolder useFullURL)
			throws Exception
	{
		logger.info("create socket");
        if (sslFactory == null) {
            initFactory();
        }
        if (port == -1) {
            port = 443;
        }

        TransportClientProperties tcp = TransportClientPropertiesFactory.create("https");

        boolean hostInNonProxyList = isHostInNonProxyList(host, tcp.getNonProxyHosts());

		List<Proxy> proxies = ProxySelector.getDefault().select(
				new URI("https://" + host + ":" + port + "/"));

		Socket sslSocket = null;
        if(hostInNonProxyList || proxies.equals(ProxyUtil.DIRECT)
        		|| proxies.size() == 0)
        {
            // direct SSL connection
            sslSocket = sslFactory.createSocket(host, port);
        } else do{
        	Socket tunnel = null;
        	int tunnelPort = -1;
        	if (tcp.getProxyHost().length() != 0) {
                // Default proxy port is 80, even for https
                tunnelPort = (tcp.getProxyPort().length() != 0)
                                 ? Integer.parseInt(tcp.getProxyPort())
                                 : 80;
                if (tunnelPort < 0)
                    tunnelPort = 80;

                // Create the regular socket connection to the proxy
                tunnel = new Socket(tcp.getProxyHost(), tunnelPort);
        	} else{
        		tunnel = createTunnelSocket(proxies);
        		if(tunnel == null){
        			sslSocket = sslFactory.createSocket(host, port);
        			break;
        		}
        		tunnelPort = tunnel.getPort();
        	}

            // The tunnel handshake method (condensed and made reflexive)
            OutputStream tunnelOutputStream = tunnel.getOutputStream();
            PrintWriter out = new PrintWriter(
                    new BufferedWriter(new OutputStreamWriter(tunnelOutputStream)));

            // More secure version... engage later?
            // PasswordAuthentication pa =
            // Authenticator.requestPasswordAuthentication(
            // InetAddress.getByName(tunnelHost),
            // tunnelPort, "SOCK", "Proxy","HTTP");
            // if(pa == null){
            // printDebug("No Authenticator set.");
            // }else{
            // printDebug("Using Authenticator.");
            // tunnelUser = pa.getUserName();
            // tunnelPassword = new String(pa.getPassword());
            // }
            out.print("CONNECT " + host + ":" + port + " HTTP/1.0\r\n"
                    + "User-Agent: AxisClient");
            if (tcp.getProxyUser().length() != 0 &&
                tcp.getProxyPassword().length() != 0) {

                // add basic authentication header for the proxy
                String encodedPassword = XMLUtils.base64encode((tcp.getProxyUser()
                        + ":"
                        + tcp.getProxyPassword()).getBytes());

                out.print("\nProxy-Authorization: Basic " + encodedPassword);
            }
            out.print("\nContent-Length: 0");
            out.print("\nPragma: no-cache");
            out.print("\r\n\r\n");
            out.flush();
            InputStream tunnelInputStream = tunnel.getInputStream();

            logger.finest(Messages.getMessage("isNull00", "tunnelInputStream",
                        "" + (tunnelInputStream
                        == null)));
            String replyStr = "";

            // Make sure to read all the response from the proxy to prevent SSL negotiation failure
            // Response message terminated by two sequential newlines
            int newlinesSeen = 0;
            boolean headerDone = false;    /* Done on first newline */

            while (newlinesSeen < 2) {
                int i = tunnelInputStream.read();

                if (i < 0) {
                    throw new IOException("Unexpected EOF from proxy");
                }
                if (i == '\n') {
                    headerDone = true;
                    ++newlinesSeen;
                } else if (i != '\r') {
                    newlinesSeen = 0;
                    if (!headerDone) {
                        replyStr += String.valueOf((char) i);
                    }
                }
            }
            if (StringUtils.startsWithIgnoreWhitespaces("HTTP/1.0 200", replyStr) &&
                    StringUtils.startsWithIgnoreWhitespaces("HTTP/1.1 200", replyStr)) {
                throw new IOException(Messages.getMessage("cantTunnel00",
                        new String[]{
                            tcp.getProxyHost(),
                            "" + tunnelPort,
                            replyStr}));
            }

            // End of condensed reflective tunnel handshake method
            sslSocket = sslFactory.createSocket(tunnel, host, port, true);
            logger.finest(Messages.getMessage("setupTunnel00",
                          tcp.getProxyHost(),
                        "" + tunnelPort));
        } while(false);

        ((SSLSocket) sslSocket).startHandshake();
        logger.finest(Messages.getMessage("createdSSL00"));
        return sslSocket;
    }

	private Socket createTunnelSocket(List<Proxy> proxies)
		throws IOException
	{
		Socket sock = null;
		for(Proxy p : proxies){
			if(p.type().equals(Proxy.Type.HTTP)){
				if(!(p.address() instanceof InetSocketAddress)){
					continue;
				}
				InetSocketAddress addr = (InetSocketAddress)p.address();
				sock = new Socket(Proxy.NO_PROXY);
				if(addr.getAddress() != null){
					sock.connect(new InetSocketAddress(addr.getAddress(), addr.getPort()));
				} else{
					sock.connect(new InetSocketAddress(addr.getHostName(), addr.getPort()));
				}
				break;
			}
		}
		return sock;
	}

	protected void initFactory() throws IOException {
		try{
			sslFactory = SSLUtil.createTrustfulSocketFactory("TLS");
		} catch(KeyManagementException e){
			logger.log(Level.SEVERE, "failed to create socket factory", e);
		} catch(NoSuchAlgorithmException e){
			logger.log(Level.SEVERE, "failed to create socket factory", e);
		}
		if(sslFactory == null){
			super.initFactory();
		}
	}

	private static Logger logger = Logger.getLogger(
			ProxySelectingSecureAxisSocketFactory.class.getName());
}
