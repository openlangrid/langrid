/*
 * $Id: SubnetUtil.java 888 2013-06-14 06:21:33Z t-nakaguchi $
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 888 $
 */
public class SubnetUtil {
	/**
	 * 
	 * 
	 */
	public static boolean isInSubnet(InetAddress address)
			throws IOException, SocketException
	{
		byte[] subnetMask = detectSubnetMask().getAddress();
		if(address.getAddress().length != subnetMask.length) return false;
		byte[] maskedAddr = mask(address.getAddress(), subnetMask);
		InetAddress[] myAddresses = getLocalAddresses();
		for(InetAddress a : myAddresses){
			if(a.getAddress().length != subnetMask.length) continue;
			byte[] addr = mask(a.getAddress(), subnetMask);
			if(Arrays.equals(addr, maskedAddr)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 
	 */
	public static InetAddress detectSubnetMask(){
		try{
			if(System.getProperty("os.name").indexOf("Windows") == -1){
				Process process = Runtime.getRuntime().exec("/sbin/ifconfig");
				InputStream is = process.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line;
				while((line = br.readLine()) != null){
					if(line.indexOf("127.0.0.1") != -1) continue;
					int i = line.indexOf("netmask");
					if(i != -1 && line.indexOf("boradcast") != -1){
						InetAddress r = parseLinuxSubnetMask(line);
						if(r == null) break;
						return r;
					}
					i = line.indexOf("Mask:");
					if(i != -1){
						String addr = line.substring(i + 5);
						return InetAddress.getByName(addr);
					}
				}
			} else{
				Process process = Runtime.getRuntime().exec("ipconfig");
				InputStream is = process.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "Shift_JIS"));
				String line;
				while((line = br.readLine()) != null){
					if(line.indexOf("Subnet") == -1 && line.indexOf("サブネット マスク") == -1) continue;
					return parseWindowsSubnetMask(line);
				}
			}
		} catch(IOException e){
		}
		try{
			return InetAddress.getByName("255.255.255.0");
		} catch(UnknownHostException exp){
			return null;
		}
	}

	private static InetAddress[] getLocalAddresses() throws SocketException{
		Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
		List<InetAddress> ret = new ArrayList<InetAddress>();
		while(e.hasMoreElements()){
			NetworkInterface i = e.nextElement();
			Enumeration<InetAddress> addresses = i.getInetAddresses();
			while(addresses.hasMoreElements()){
				ret.add(addresses.nextElement());
			}
		}
		return ret.toArray(new InetAddress[]{});
	}

	private static byte[] mask(byte[] address, byte[] mask){
		byte[] ret = new byte[address.length];
		for(int i = 0; i < ret.length; i++){
			ret[i] = (byte)(address[i] & mask[i]);
		}
		return ret;
	}

	static InetAddress parseWindowsSubnetMask(String line) throws UnknownHostException{
		int beginIndex = line.indexOf(":") + 2;
		int endIndex = line.length();
		return InetAddress.getByName(line.substring(beginIndex, endIndex));

	}

	static InetAddress parseLinuxSubnetMask(String line) throws UnknownHostException{
		int e = line.indexOf("broadcast");
		if(e == -1) return null;
		e--;
		String v = line.substring(line.indexOf("netmask") + 8, e);
		if(v.startsWith("0x")){
			try{
				long address = Long.parseLong(v.substring(2), 16);
				return InetAddress.getByAddress(new byte[]{
						(byte)(address >> 24)
						, (byte)((address >> 16) & 0xff)
						, (byte)((address >> 8) & 0xff)
						, (byte)(address & 0xff)
				});
			} catch(NumberFormatException e2){
			}
		} else{
			return InetAddress.getByName(v);
		}
		return null;
	}
}
