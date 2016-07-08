/*
 * $Id: Example2.java 10341 2008-03-10 08:27:15Z nakaguchi $
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
package jp.go.nict.langrid.commons.net.proxy.examples;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.util.List;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.net.proxy.ProxyUtil;
import jp.go.nict.langrid.commons.net.proxy.pac.PacUtil;

/**
 * 手動でプロキシセレクタを使用してSocketで接続する例。
 * @author $Author: nakaguchi $
 * @version $Revision: 10341 $
 */
public class Example2 {
	public static void main(String[] args) throws Exception{
		// デフォルトのプロキシセレクタをセットアップ。
		PacUtil.setupDefaultProxySelector();

		// プロキシセレクタを使って手動でプロキシを選択。
		List<Proxy> proxies = ProxySelector.getDefault().select(
				new URI("http://www.google.co.jp/")
				);

		// プロキシセレクタを介さないよう、直接接続を行うソケットを作成。
		try(Socket sock = new Socket(Proxy.NO_PROXY)){
			if((proxies.size() == 0) || ProxyUtil.DIRECT.equals(proxies)){
				// プロキシ情報が無い場合、直接接続しに行く。
				sock.connect(new InetSocketAddress("www.google.co.jp", 80));
			} else{
				// 見つかったプロキシの最初の要素を使用する。
				InetSocketAddress addr = (InetSocketAddress)proxies.get(0).address();
				if(addr.isUnresolved()){
					// 解決されていない場合はconnectにそのまま渡すとUnknownHostException
					// が発生するため、解決させる。
					addr = new InetSocketAddress(addr.getHostName(), addr.getPort());
				}
				sock.connect(addr);
			}
	
			// GETリクエストを送信。
			sock.getOutputStream().write("GET http://www.google.co.jp/ HTTP/1.0\n\n".getBytes());
	
			// 結果を出力。
			StreamUtil.transfer(
					sock.getInputStream()
					, System.out
					);
		}
	}
}
