/*
 * $Id: PacProxySelector.java 192 2010-10-02 11:31:22Z t-nakaguchi $
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
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.net.DNSUtil;
import jp.go.nict.langrid.commons.net.URLUtil;
import jp.go.nict.langrid.commons.net.proxy.ProxyUtil;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 192 $
 */
public class PacProxySelector extends ProxySelector {
	/**
	 * 
	 * 
	 */
	public PacProxySelector(String pacScript) {
		engine = new PacEngine(pacScript);
	}

	/**
	 * 
	 * 
	 */
	public PacProxySelector(final URL pacScriptUrl) {
		pacLoadingThread = new Thread(new Runnable(){
			public void run(){
				String script = null;
				try{
					script = getScript(pacScriptUrl);
				} catch(UnknownHostException e){
					String host = pacScriptUrl.getHost();
					for(String s : DNSUtil.listSuffixes()){
						try{
							script = getScript(URLUtil.replaceHost(
									pacScriptUrl, host + "." + s
									));
						} catch(MalformedURLException ex){
						} catch(UnknownHostException ex){
						}
					}
				}
				if(script != null){
					engine = new PacEngine(script);
				}
			}

			private String getScript(URL url)
			throws UnknownHostException
			{
				try{
					HttpURLConnection con = (HttpURLConnection)url.openConnection(
							Proxy.NO_PROXY);
					con.setConnectTimeout(3000);
					con.setReadTimeout(3000);
					InputStream is = con.getInputStream();
					try{
						return StreamUtil.readAsString(is, "UTF-8");
					} finally{
						is.close();
						con.disconnect();
					}
				} catch(UnknownHostException e){
					throw e;
				} catch(UnsupportedEncodingException e){
					throw new RuntimeException(e);
				} catch(SocketTimeoutException e){
				} catch(IOException e){
					logger.log(Level.WARNING, "failed to read pac script.", e);
				}
				return null;
			}
		});
		pacLoadingThread.start();
	}

	@Override
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
	}

	/**
	 * 
	 * 
	 */
	@Override
	public List<Proxy> select(URI uri) {
		try{
			PacEngine engine = getEngine();
			if(engine == null){
				return ProxyUtil.DIRECT;
			} else{
				return engine.findProxyForUri(uri);
			}
		} catch(ProxyFormatException e){
			logger.log(Level.WARNING, "proxy format error occurred", e);
			return ProxyUtil.DIRECT;
		} catch(UnknownHostException e){
			logger.log(Level.WARNING, "invalid host name: " + uri, e);
			return ProxyUtil.DIRECT;
		}
	}

	protected PacEngine getEngine(){
		Thread t = pacLoadingThread;
		if(t == null){
			return engine;
		} else{
			try{
				t.join(5000);
				pacLoadingThread = null;
				return engine;
			} catch(InterruptedException e){
				return engine;
			}
		}
	}

	private volatile PacEngine engine;
	private Thread pacLoadingThread;

	private static Logger logger = Logger.getLogger(PacProxySelector.class.getName());
}
