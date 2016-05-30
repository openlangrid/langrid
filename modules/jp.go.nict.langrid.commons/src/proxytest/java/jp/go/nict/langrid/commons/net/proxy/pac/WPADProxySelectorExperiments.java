/*
 * $Id: WPADProxySelectorExperiments.java 11283 2008-09-16 07:58:21Z nakaguchi $
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

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

import jp.go.nict.langrid.commons.net.proxy.Experiment;
import jp.go.nict.langrid.commons.net.proxy.ProxyUtil;
import jp.go.nict.langrid.commons.runner.MultithreadRunnable;
import jp.go.nict.langrid.commons.runner.RepeatingMultithreadRunner;
import jp.go.nict.langrid.commons.runner.SystemOutStatusReporter;

public class WPADProxySelectorExperiments  extends Experiment{
	public static void main(String[] args){
		runExperiments(WPADProxySelectorExperiments.class);
	}

	public void run1() throws Exception{
		List<Proxy> proxies = ps.select(
				new URI("http://www.yahoo.co.jp/")
				);
		for(Proxy p : proxies){
			System.out.println(p);
		}
	}

	public void run2() throws Exception{
		List<Proxy> proxies = ps.select(
				new URI("https://www.google.com/")
				);
		for(Proxy p : proxies){
			System.out.println(p);
		}
	}

	public void run3() throws Exception{
		new RepeatingMultithreadRunner(10, 5, 0, new SystemOutStatusReporter()).runAndWait(
				new MultithreadRunnable(){
					public void run() throws Exception {
						List<Proxy> proxies = ps.select(
								new URI("https://www.google.com/")
								);
						if(proxies.equals(ProxyUtil.DIRECT)){
							for(Proxy p : proxies){
								System.out.println(p);
							}
						}
					};
				}
				);
	}

	private ProxySelector ps = new WPADProxySelector();
}
