/*
 * $Id: PacEngineTest.java 10341 2008-03-10 08:27:15Z nakaguchi $
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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.List;

import jp.go.nict.langrid.commons.net.proxy.ProxyUtil;

import org.junit.Before;
import org.junit.Test;

public class PacEngineTest{
	@Before
	public void setUp() throws Exception{
		engine = new PacEngine(pacScript);
	}

	/**
	 * findProxyForUriのテスト。
	 */
	@Test
	public void testFindProxyForUri_Normal1() throws Exception{
		Proxy p1 = createHttpProxy("proxy.example.com", 8080);
		Proxy p2 = createHttpProxy("proxy2.example.com", 8081);

		List<Proxy> r = engine.findProxyForUri(
				new URI("http://www.yahoo.co.jp/")
				);

		assertEquals(2, r.size());
		assertEquals(p1, r.get(0));
		assertEquals(p2, r.get(1));
	}

	/**
	 * findProxyForUriのテスト。localhostのテスト
	 */
	@Test
	public void testFindProxyForUri_Normal2() throws Exception{
		assertEquals(
				ProxyUtil.DIRECT
				, engine.findProxyForUri(new URI("http://127.0.0.1/"))
				);
	}

	/**
	 * findProxyForUriのテスト。localhostのテスト
	 */
	@Test
	public void testFindProxyForUri_Normal3() throws Exception{
		assertEquals(
				ProxyUtil.DIRECT
				, engine.findProxyForUri(new URI("http://localhost/"))
				);
	}

	private static Proxy createHttpProxy(String host, int port){
		return new Proxy(
				Proxy.Type.HTTP
				, InetSocketAddress.createUnresolved(host, port)
				);
	}

	private PacEngine engine;

	private static String pacScript =
		"function FindProxyForURL(url, host){\n"
		+ "  if (isInNet(host, \"127.0.0.0\", \"255.0.0.0\"))\n"
		+ "    return \"DIRECT\";\n"
		+ "  else if (url.substring(0, 5) == \"http:\") {\n"
		+ "    return \"PROXY proxy.example.com:8080; proxy2.example.com:8081\";\n"
		+ "  }\n"
		+ "  else if (url.substring(0, 4) == \"ftp:\") {\n"
		+ "    return \"PROXY proxy.example.com:8080; proxy2.example.com:8081\";\n"
		+ "  }\n"
		+ "  else if (url.substring(0, 7) == \"gopher:\") {\n"
		+ "    return \"PROXY proxy.example.com:8080; proxy2.example.com:8081\";\n"
		+ "  }\n"
		+ "  else if (url.substring(0, 6) == \"https:\" ||\n"
		+ "    url.substring(0, 6) == \"snews:\") {\n"
		+ "    return \"PROXY proxy.example.com:8080; proxy2.example.com:8081\";\n"
		+ "  }\n"
		+ "  else {\n"
		+ "    return \"DIRECT\";\n"
		+ "  }\n"
		+ "}\n"
		;
}
