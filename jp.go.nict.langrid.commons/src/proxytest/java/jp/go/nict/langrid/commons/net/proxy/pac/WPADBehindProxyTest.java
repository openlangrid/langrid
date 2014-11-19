/*
 * $Id: NICTWPADTest.java 14916 2011-07-20 02:55:51Z Takao Nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2008 NICT Language Grid Project.
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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;

import jp.go.nict.langrid.commons.io.StreamUtil;

public class WPADBehindProxyTest{
	@Test
	public void test() throws Exception{
		URL url = new URL("http://wpad/wpad.dat");
		URLConnection con = url.openConnection(Proxy.NO_PROXY);
		StreamUtil.transfer(
				con.getInputStream()
				, System.out
				);
	}

	@Test
	public void test2() throws Exception{
		Socket s = new Socket("wpad", 80);
		OutputStream o = s.getOutputStream();
		InputStream i = s.getInputStream();
		o.write("GET /wpad.dat HTTP/1.0\n\n".getBytes());
		StreamUtil.transfer(
				i, System.out);
		s.close();
	}
}