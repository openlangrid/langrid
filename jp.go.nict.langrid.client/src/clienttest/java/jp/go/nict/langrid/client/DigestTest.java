/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) The Language Grid Project.
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
package jp.go.nict.langrid.client;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import jp.go.nict.langrid.client.jsonrpc.JsonRpcClientFactory;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.net.HttpURLConnectionUtil;

import org.junit.Assert;
import org.junit.Test;

public class DigestTest {
	public interface HelloService{
		String hello();
	}

	@Test
	public void test1() throws Exception{
		HelloService s = new JsonRpcClientFactory().create(
				HelloService.class, new URL("http://127.0.0.1:8080/digest/helloNoAuth"));
		Assert.assertTrue(s.hello().length() > 0);
	}

	@Test
	public void test2() throws Exception{
		HelloService s = new JsonRpcClientFactory().create(
				HelloService.class, new URL("http://127.0.0.1:8080/digest/hello"),
				"tomcat", "tomcat");
		((RequestAttributes)s).setAuthMethod(AuthMethod.DIGEST);
		Assert.assertTrue(s.hello().length() > 0);
	}

	@Test
	public void test3() throws Exception{
		URL url = new URL("http://127.0.0.1:8080/digest/hello");
		HttpURLConnection c = (HttpURLConnection)url.openConnection();
		Map<String, String> param = HttpURLConnectionUtil.parseWwwAuthenticateHeader(c.getHeaderField("WWW-Authenticate"));
		c.disconnect();

		HttpURLConnection c2 = (HttpURLConnection)url.openConnection();
		String v = HttpURLConnectionUtil.createDigestAuthValue(
				param, "GET", "/digest/hello", "tomcat", "tomcat", 1);
		c2.setRequestProperty("Authorization", v);
		Assert.assertTrue(StreamUtil.readAsString(c2.getInputStream(), "UTF-8").length() > 0);
	}
}
