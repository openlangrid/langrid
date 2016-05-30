/*
 * $Id: PacUtil.java 376 2011-08-23 02:47:25Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.net.proxy.pac;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.net.proxy.CachingProxySelector;
import jp.go.nict.langrid.commons.net.proxy.ChainProxySelector;
import jp.go.nict.langrid.commons.net.proxy.SocketProxySelector;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 376 $
 */
public class PacUtil {
	/**
	 * 
	 * 
	 */
	public static final String WPAD_SCRIPT_URL = "http://wpad/wpad.dat";

	/**
	 * 
	 * 
	 */
	public static void setupDefaultProxySelector(){
		ProxySelector.setDefault(getDefaultProxySelector());
	}

	/**
	 * 
     * 
	 */
	public static ProxySelector getDefaultProxySelector() {
		ProxySelector wpad = new WPADProxySelector();
		ProxySelector def = ProxySelector.getDefault();
		ProxySelector socket = new SocketProxySelector(def);

		return new CachingProxySelector(
				disableWpad ?
						new ChainProxySelector(def, socket) :
						new ChainProxySelector(def, socket, wpad)
				);
    }

	/**
	 * 
	 * 
	 */
	public static String findPacFromDNS()
		throws IOException
	{
		URL url = new URL(WPAD_SCRIPT_URL);
		return new String(
				StreamUtil.readAsBytes(url.openConnection(Proxy.NO_PROXY).getInputStream())
				, "UTF-8"
				);
	}

	/**
	 * 
	 * 
	 */
	public static List<Proxy> toProxies(String result)
		throws ProxyFormatException
	{
		return toProxies(result, false);
	}

	/**
	 * 
	 * 
	 */
	public static List<Proxy> toProxies(String result, boolean resolveAddress)
		throws ProxyFormatException
	{
		ArrayList<Proxy> proxies = new ArrayList<Proxy>();

		Proxy.Type previousProxyType = null;
		Scanner s = new Scanner(result);
		while(s.hasNext()){
			if(s.findInLine(proxySettingPattern) == null){
				throw new ProxyFormatException(result);
			}

			MatchResult m = s.match();
/*
			for(int i = 0; i <= m.groupCount(); i++){
				System.out.println("(" + i + "): " + m.group(i));
			}
//*/
			if(m.group(5) != null){
				proxies.add(Proxy.NO_PROXY);
			} else{
				String pt = m.group(1).toUpperCase();
				String host = m.group(2);
				int port = Integer.parseInt(m.group(4));

				Proxy.Type proxyType;
				if(pt.length() > 0){
					proxyType = proxyTypes.get(pt);
					previousProxyType = proxyType;
				} else{
					proxyType = previousProxyType;
				}

				InetSocketAddress addr = resolveAddress ?
						new InetSocketAddress(host, port)
						: InetSocketAddress.createUnresolved(host, port);

				proxies.add(new Proxy(proxyType, addr));
			}
		}

		return proxies;
	}

	static final Pattern proxySettingPattern;

	private static Map<String, Proxy.Type> proxyTypes;
	private static boolean disableWpad = false;
	static{
		proxyTypes = new HashMap<String, Proxy.Type>();
		proxyTypes.put("DIRECT", Proxy.Type.DIRECT);
		proxyTypes.put("PROXY", Proxy.Type.HTTP);
		proxyTypes.put("SOCKS", Proxy.Type.SOCKS);

		proxySettingPattern = Pattern.compile(
				"(PROXY|SOCKS|)"
				+ "\\s"
				+ "(\\w+(\\.\\w+)*)"
				+ ":(\\d{1,5})|(DIRECT)"
				, Pattern.CASE_INSENSITIVE
				);
		try{
			String dw = System.getProperty(
					"jp.go.nict.langrid.commons.net.proxy.pac.PacUtil.disableWpad");
			if(dw != null && dw.equals("true")){
				disableWpad = true;
			}
		} catch(SecurityException e){
		}
	}
}
