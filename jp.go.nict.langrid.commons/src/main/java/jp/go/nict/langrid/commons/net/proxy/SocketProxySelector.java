/*
 * $Id: SocketProxySelector.java 888 2013-06-14 06:21:33Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.net.proxy;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 888 $
 */
public class SocketProxySelector extends ProxySelector {
	/**
	 * 
	 * 
	 */
	public SocketProxySelector(ProxySelector parent){
		this.parent = parent;
	}

	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		parent.connectFailed(uri, sa, ioe);
	}

	@Override
	public List<Proxy> select(URI uri) {
		String uristr = uri.toString();
		try{
			URI u = new URI("socket" + uristr.substring(uristr.indexOf(':')));
//			logger.info("detectecting socks proxy for " + u
//					+ " using " + parent.getClass().getName());
			return parent.select(u);
		} catch(URISyntaxException e){
			logger.info("return direct connection.");
			return ProxyUtil.DIRECT;
		}
	}

	private ProxySelector parent;
	private static Logger logger = Logger.getLogger(SocketProxySelector.class.getName());
}
