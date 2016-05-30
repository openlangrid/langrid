/*
 * $Id: PacUtilTest.java 11630 2009-02-20 10:06:21Z Takao Nakaguchi $
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import org.junit.Test;

public class PacUtilTest{
	@Test
	public void testToProxies_Normal1() throws Exception{
		List<Proxy> proxies = PacUtil.toProxies("PROXY www.yahoo.co.jp:3128");
		assertEquals(proxies.size(), 1);
		assertEquals(
				new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("www.yahoo.co.jp", 3128))
				, proxies.get(0)
				);
	}

	@Test
	public void testToProxies_Normal2() throws Exception{
		List<Proxy> proxies = PacUtil.toProxies(
				"PROXY localhost:8080; SOCKS www.hoge.com:8081"
				);
		assertEquals(proxies.size(), 2);
		assertEquals(
				new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("localhost", 8080))
				, proxies.get(0)
				);
		assertEquals(
				new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("www.hoge.com", 8081))
				, proxies.get(1)
				);
	}

	@Test
	public void testToProxies_Normal3() throws Exception{
		List<Proxy> proxies = PacUtil.toProxies("DIRECT");
		assertEquals(proxies.size(), 1);
		assertEquals(Proxy.NO_PROXY, proxies.get(0));
	}

	@Test
	public void testToProxies_Normal4() throws Exception{
		List<Proxy> proxies = PacUtil.toProxies(
				"PROXY localhost:8080; direct"
				);
		assertEquals(proxies.size(), 2);
		assertEquals(
				new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved("localhost", 8080))
				, proxies.get(0)
				);
		assertEquals(Proxy.NO_PROXY, proxies.get(1));
	}

	@Test
	public void testToProxies_Normal5() throws Exception{
		List<Proxy> proxies = PacUtil.toProxies(
				"SOCKS hunyu.jp:8080; hoe.jp:2255"
				);
		assertEquals(proxies.size(), 2);
		assertEquals(
				new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("hunyu.jp", 8080))
				, proxies.get(0)
				);
		assertEquals(
				new Proxy(Proxy.Type.SOCKS, InetSocketAddress.createUnresolved("hoe.jp", 2255))
				, proxies.get(1)
				);
	}

	@Test
	public void testProxySettingPattern_Normal1() throws Exception{
		assertTrue(matches("DIRECT"));
		assertTrue(matches("direct"));
		assertTrue(matches("PROXY some.proxy:3128"));
		assertTrue(matches("proxy some.proxy:3128"));
		assertTrue(matches("SOCKS some.proxy:3128"));
		assertTrue(matches("socks some.proxy:3128"));
	}

	@Test
	public void testProxySettingPattern_Normal2() throws Exception{
		assertTrue(matches("PROXY 127.0.0.1:3128"));
	}

	@Test
	public void testProxySettingPattern_Abnormal1() throws Exception{
		assertFalse(matches("PROXY portless.proxy"));
		assertFalse(matches("INVALID some.proxy:3128"));
		assertFalse(matches("PROXY 127.0.0.1:312811"));
	}

	private boolean matches(String value) throws Exception{
		return PacUtil.proxySettingPattern.matcher(value).matches();
	}
}
