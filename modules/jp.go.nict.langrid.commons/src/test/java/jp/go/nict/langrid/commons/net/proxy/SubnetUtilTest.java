/*
 * $Id: SubnetUtil.java 617 2013-01-30 01:42:19Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2013 NICT Language Grid Project.
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

import java.net.InetAddress;

import junit.framework.Assert;

import org.junit.Test;

public class SubnetUtilTest {
	@Test
	public void test_windows() throws Exception{
		Assert.assertEquals(InetAddress.getByName("255.255.255.0"), SubnetUtil.parseWindowsSubnetMask("Subnet .... : 255.255.255.0"));
	}

	@Test
	public void test_linux_1() throws Exception{
		Assert.assertEquals(InetAddress.getByName("255.255.255.0"), SubnetUtil.parseLinuxSubnetMask("aasddd netmask 255.255.255.0 broadcast skjlskdj"));
	}

	@Test
	public void test_linux_2() throws Exception{
		Assert.assertEquals(InetAddress.getByName("255.255.255.0"), SubnetUtil.parseLinuxSubnetMask("aasddd netmask 0xffffff00 broadcast skjlskdj"));
	}
}
