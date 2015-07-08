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
package jp.go.nict.langrid.commons.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.security.MessageDigestUtil;

public class HttpURLConnectionUtil {
	public static Map<String, String> parseWwwAuthenticateHeader(String header){
		Map<String, String> ret = new HashMap<String, String>();
		for(String s : header.substring("Digest ".length()).split(",")){
			String[] v = s.split("=");
			ret.put(v[0].trim(), StringUtil.dequote(v[1].trim()));
		}
		return ret;
	}

	public static String createDigestAuthValue(
			Map<String, String> wwwAuthElements,
			String method, String uri, String userName, String password, int nc){
		String qop = wwwAuthElements.get("qop").split(",")[0];
		String ncs = String.format("%08d", nc);
		String cnonce = StringUtil.randomString(10);
		String response = MessageDigestUtil.digestByMD5(
				MessageDigestUtil.digestByMD5(
						userName + ":" + wwwAuthElements.get("realm") + ":" + password)
				+ ":" + wwwAuthElements.get("nonce") + ":" + ncs + ":" + cnonce
				+ ":" + qop
				+ ":" + MessageDigestUtil.digestByMD5(method + ":" + uri)
				);
		return String.format(
				"Digest username=\"%s\", realm=\"%s\", nonce=\"%s\", "
				+ "uri=\"%s\", qop=%s, nc=%08d, cnonce=\"%s\", "
				+ "response=\"%s\", opaque=\"%s\"",
				userName, wwwAuthElements.get("realm"), wwwAuthElements.get("nonce"),
				uri, qop, 1, cnonce, response, wwwAuthElements.get("opaque")
				);
	}

	/**
	 * Creates and returns the content stream.
	 * This method is aware of IOException when calling getInputStream and content-codings (gzip, deflate).
	 * @param con
	 * @return Proper InputStream or null.
	 * @throws IOException
	 */
	public static InputStream openResponseStream(HttpURLConnection con) throws IOException{
		InputStream is = null;
		try{
			is = con.getInputStream();
		} catch(IOException e){
			is = con.getErrorStream();
		}
		if(is != null){
			String ce = con.getContentEncoding();
			if(ce != null){
				if(ce.equals("gzip")) {
					is = new GZIPInputStream(is);
				} else if(ce.equals("deflate")){
					is = new DeflaterInputStream(is);
				}
			}
		}
		return is;
	}

	public static interface WriteProcess{
		void write(OutputStream out) throws IOException;
	}
	public static OutputStream processWriteRequest(HttpURLConnection con,
			boolean compressContent, int compressThreashold, String compressAlgorithm,
			WriteProcess proc)
	throws IOException{
		if(compressContent){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			proc.write(baos);
			byte[] content = baos.toByteArray();
			if(content.length >= compressThreashold){
				if(compressAlgorithm.equals("gzip")){
					con.addRequestProperty("Content-Encoding", "gzip");
					OutputStream os = con.getOutputStream();
					GZIPOutputStream gzos = new GZIPOutputStream(os);
					gzos.write(content);
					gzos.finish();
					gzos.flush();
					return os;
				} else{
					con.addRequestProperty("Content-Encoding", "deflate");
					OutputStream os = con.getOutputStream();
					DeflaterOutputStream dos = new DeflaterOutputStream(os);
					dos.write(content);
					dos.finish();
					dos.flush();
					return os;
				}
			} else{
				OutputStream os = con.getOutputStream();
				os.write(content);
				return os;
			}
		} else{
			OutputStream os = con.getOutputStream();
			proc.write(os);
			return os;
		}
	}
}
