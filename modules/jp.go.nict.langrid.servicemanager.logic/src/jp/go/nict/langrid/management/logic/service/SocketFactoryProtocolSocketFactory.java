/*
 * $Id: SocketFactoryProtocolSocketFactory.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.management.logic.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class SocketFactoryProtocolSocketFactory implements ProtocolSocketFactory
{
	/**
	 * 
	 * 
	 */
	public SocketFactoryProtocolSocketFactory(SocketFactory factory){
		this.factory = factory;
	}

	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return factory.createSocket(host, port);
	}

	public Socket createSocket(
			String host, int port, InetAddress localAddress, int localPort) throws IOException, UnknownHostException {
		return factory.createSocket(host, port, localAddress, localPort);
	}

	public Socket createSocket(
			String host, int port, InetAddress localAddress, int localPort
			, HttpConnectionParams params)
		throws IOException, UnknownHostException, ConnectTimeoutException
	{
    	return factory.createSocket(host, port, localAddress, localPort);
	}

	private SocketFactory factory;
}
