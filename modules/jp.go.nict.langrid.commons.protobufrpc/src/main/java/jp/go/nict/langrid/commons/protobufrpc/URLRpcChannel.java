/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2006-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.protobufrpc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.io.CascadingIOException;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.lang.block.BlockPE;
import jp.go.nict.langrid.commons.lang.block.BlockPPE;
import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.Protocols;

import org.apache.commons.codec.binary.Base64;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors.MethodDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class URLRpcChannel implements RpcChannel{
	/**
	 * 
	 * 
	 */
	public URLRpcChannel(URL url){
		this.url = url;
	}

	/**
	 * 
	 * 
	 */
	public URLRpcChannel(URL url, String authUserName, String authPassword){
		this.url = url;
		this.authUserName = authUserName;
		this.authPassword = authPassword;
	}

	public URL getUrl() {
		return url;
	}

	/**
	 * 
	 * 
	 */
	public Map<String, String> getRequestProperties(){
		return requestProperties;
	}

	/**
	 * 
	 * 
	 */
	public Map<String, String> getResponseProperties(){
		return responseProperties;
	}

	public void call(
			BlockPE<OutputStream, IOException> sender
			, BlockPE<InputStream, IOException> successReceiver
			, BlockPPE<InputStream, IOException, IOException> failReceiver){
		HttpURLConnection c = null;
		try{
			c = (HttpURLConnection)url.openConnection();
			c.setDoOutput(true);
			for(Map.Entry<String, String> entry : requestProperties.entrySet()){
				c.addRequestProperty(entry.getKey(), entry.getValue());
			}
			if(!requestProperties.containsKey(LangridConstants.HTTPHEADER_PROTOCOL)){
				c.addRequestProperty(LangridConstants.HTTPHEADER_PROTOCOL, Protocols.PROTOBUF_RPC);
			}
			if(System.getProperty("http.proxyHost") != null){
				String proxyAuthUser = System.getProperty("http.proxyUser");
				if(proxyAuthUser != null){
					String proxyAuthPass = System.getProperty("http.proxyPassword");
					String header = proxyAuthUser + ":" + ((proxyAuthPass != null) ? proxyAuthPass : "");
					c.setRequestProperty(
							"Proxy-Authorization"
							, "Basic " + new String(Base64.encodeBase64(header.getBytes()))
							);
				}
			}
			if(authUserName != null && authUserName.length() > 0){
				String header = authUserName + ":" + ((authPassword != null) ? authPassword : "");
				c.setRequestProperty(
						"Authorization"
						, "Basic " + new String(Base64.encodeBase64(header.getBytes()))
						);
			}
			OutputStream os = c.getOutputStream();
			try{
				sender.execute(os);
			} finally{
				os.close();
			}
			for(Map.Entry<String, List<String>> entry : c.getHeaderFields().entrySet()){
				responseProperties.put(
						entry.getKey()
						, StringUtil.join(entry.getValue().toArray(ArrayUtil.emptyStrings()), ", ")
						);
			}
			InputStream is = c.getInputStream();
			try{
				successReceiver.execute(is);
			} finally{
				is.close();
			}
		} catch(IOException e){
			InputStream es = null;
			if(c != null){
				es = c.getErrorStream();
			}
			try{
				failReceiver.execute(es, e);
			} catch(IOException ee){
			} finally{
				if(es != null){
					try{
						es.close();
					} catch(IOException ee){
					}
				}
			}

		}
	}

	@Override
	public void callMethod(final MethodDescriptor method,
			final RpcController controller, final Message request,
			final Message responsePrototype, final RpcCallback<Message> done) {
		call(new BlockPE<OutputStream, IOException>(){
			@Override
			public void execute(OutputStream p1) throws IOException {
				CodedOutputStream cos = CodedOutputStream.newInstance(p1);
				cos.writeStringNoTag(method.getFullName());
				request.writeTo(cos);
				cos.flush();
			}
		}, new BlockPE<InputStream, IOException>(){
			@Override
			public void execute(InputStream p1) throws IOException {
				done.run(responsePrototype.newBuilderForType().mergeFrom(p1).build());
			}
		}, new BlockPPE<InputStream, IOException, IOException>(){
			@Override
			public void execute(InputStream p1, IOException p2) throws IOException{
				String errorMessage = null;
				if(p1 != null){
					byte[] res = null;
					try{
						res = StreamUtil.readAsBytes(p1);
					} catch(IOException e2){
					}
					if(res != null){
						try{
							done.run(responsePrototype.newBuilderForType().mergeFrom(new ByteArrayInputStream(res)).build());
						} catch(IOException e2){
							done.run(responsePrototype.newBuilderForType().build());
							errorMessage = new String(res, CharsetUtil.UTF_8);
						}
					}
				}
				if(controller instanceof DefaultRpcController){
					DefaultRpcController cont = (DefaultRpcController)controller;
					if(errorMessage != null){
						cont.setException(new CascadingIOException(errorMessage, p2));
					} else{
						cont.setException(p2);
					}
				} else{
					if(errorMessage != null){
						controller.setFailed(errorMessage);
					} else{
						controller.setFailed(p2.getMessage());
					}
				}
			}
		});
	}

	private URL url;
	private String authUserName;
	private String authPassword;
	private Map<String, String> requestProperties = new LinkedHashMap<String, String>();
	private Map<String, String> responseProperties = new LinkedHashMap<String, String>();
}
