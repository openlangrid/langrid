/*
 * $Id: PacEngineUtilScriptTest.java 10341 2008-03-10 08:27:15Z nakaguchi $
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PacEngineUtilScriptTest{
	@Before
	public void setUp() throws Exception{
		engine = new PacEngine(pacScript);
	}

	@Test
	public void testIsInNet_Normal1() throws Exception{
		Assert.assertTrue((Boolean)engine.evaluate(
				"isInNet('127.0.0.1', '127.0.0.0', '255.0.0.0');"
				));
	}

	@Test
	public void testIsInNet_Normal2() throws Exception{
		Assert.assertTrue((Boolean)engine.evaluate(
				"isInNet('localhost', '127.0.0.0', '255.0.0.0');"
				));
	}

	private PacEngine engine;

	private static String pacScript =
		"function FindProxyForURL(url, host){\n"
		+ "  return \"DIRECT\";\n"
		+ "}\n"
		;
}
