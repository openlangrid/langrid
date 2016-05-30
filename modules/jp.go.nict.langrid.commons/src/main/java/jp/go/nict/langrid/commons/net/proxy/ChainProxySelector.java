/*
 * $Id: ChainProxySelector.java 888 2013-06-14 06:21:33Z t-nakaguchi $
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
import java.net.InetAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 888 $
 */
public class ChainProxySelector extends ProxySelector {
	/**
	 * 
	 * 
	 */
	public ChainProxySelector(ProxySelector... selectors){
		this.considerSubnetAdresses = true;
		this.selectors = selectors;
	}

	/**
	 * 
	 * 
	 */
	public ChainProxySelector(boolean considerSubnetAdresses, ProxySelector... selectors){
		this.considerSubnetAdresses = considerSubnetAdresses;
		this.selectors = selectors;
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		if(previousSelector != null){
			previousSelector.connectFailed(uri, sa, ioe);
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
	public List<Proxy> select(URI uri) {
		if(uri == null || uri.getHost() == null
				|| uri.getHost().equals("wpad") || uri.getHost().startsWith("wpad.")){
			previousSelector = null;
			return ProxyUtil.DIRECT;
		}

		if(considerSubnetAdresses){
			try{
				InetAddress host = InetAddress.getByName(uri.getHost());
				if(SubnetUtil.isInSubnet(host)){
//					logger.info("proxy selected: DIRECT");
					return ProxyUtil.DIRECT;
				}
			} catch(SocketException e){
				logger.log(Level.WARNING, "socket exception", e);
			} catch(UnknownHostException e){
			} catch(IOException e){
				logger.log(Level.WARNING, "socket exception", e);
			}
		}

		return doSelect(uri);
	}

	/**
	 * 
	 * 
	 */
	private List<Proxy> doSelect(URI uri) {
		for(ProxySelector s : selectors){
			List<Proxy> proxies = s.select(uri);
			for(Proxy p : proxies){
				if(!p.equals(Proxy.NO_PROXY)){
					previousSelector = s;
					return proxies;
				}
			}
		}
		previousSelector = null;
		return ProxyUtil.DIRECT;
	}

	private ProxySelector[] selectors;
	private ProxySelector previousSelector;
	private boolean considerSubnetAdresses;

	private static Logger logger = Logger.getLogger(ChainProxySelector.class.getName());
}
