/*
 * $Id: PacUtilFunctionsTest.java 10341 2008-03-10 08:27:15Z nakaguchi $
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

import org.junit.Before;
import org.junit.Test;

public class PacUtilFunctionsTest{
	@Before
	public void setUp() throws Exception{
		util = new PacUtilFunctions();
	}

	@Test
	public void testDnsResolve_Normal1() {
		assertEquals(
				"127.0.0.1"
				, util.dnsResolve("localhost")
				);
	}

	@Test
	public void testDnsResolve_Normal2() {
		assertEquals(
				"127.0.0.1"
				, util.dnsResolve("127.0.0.1")
				);
	}

	private PacUtilFunctions util;
}
