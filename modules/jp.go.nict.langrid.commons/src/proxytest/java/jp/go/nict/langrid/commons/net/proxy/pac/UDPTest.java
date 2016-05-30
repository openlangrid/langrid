package jp.go.nict.langrid.commons.net.proxy.pac;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import junit.framework.TestCase;

public class UDPTest extends TestCase{
	public void test() throws Exception{
		/*
		 * # クライアントが自分を0.0.0.0として255.255.255.255に向けて DHCPDISCOVERを送信する
		 * # DHCPサーバはクライアントに使用してもよいIPアドレスを含んだDHCPOFFERを送信する
		 */
		DatagramSocket s = new DatagramSocket();
		DatagramPacket p = new DatagramPacket(
				new byte[]{1, 1, 6, 0,
						0, 0, 0, 0
						
						,53, 1, 8}
				, 3
				, new InetSocketAddress("255.255.255.255", 67)
				);
		s.send(p);
		s.close();
	}
}
