/*
 * This is a program for Language Grid Core Node. This combines multiple
 * language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.executor.protobufrpc;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MimeHeaders;

import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Fault;
import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.protobufrpc.DefaultRpcController;
import jp.go.nict.langrid.commons.protobufrpc.URLRpcChannel;
import jp.go.nict.langrid.commons.protobufrpc.util.PbHeadersToRpcHeadersAdapter;
import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.servicecontainer.service.component.AbstractServiceExecutor;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public abstract class AbstractPbServiceExecutor
extends AbstractServiceExecutor{
	public AbstractPbServiceExecutor(String invocationName){
		super(invocationName);
	}

	public AbstractPbServiceExecutor(String invocationName, long invocationId, Endpoint endpoint){
		super(invocationName, invocationId, endpoint);
	}

	/**
	 * 
	 * 
	 */
	protected Pair<URLRpcChannel, Long> preprocessPb(List<RpcHeader> rpcHeaders){
		Map<String, Object> mimeHeaders = new HashMap<String, Object>();
		Pair<Endpoint, Long> r = preprocess(mimeHeaders, rpcHeaders);
		try{
			Endpoint ep = r.getFirst();
			URLRpcChannel channel = new URLRpcChannel(ep.getAddress().toURL(), ep.getUserName(), ep.getPassword());
			for(Map.Entry<String, Object> entry : mimeHeaders.entrySet()){
				channel.getRequestProperties().put(
						entry.getKey()
						, entry.getValue().toString()
						);
			}
			return Pair.create(channel, r.getSecond());
		} catch(MalformedURLException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	protected void postprocessPb(
			long iid, long deltaTime
			, DefaultRpcController controller, final URLRpcChannel channel
			, List<Header> responseHeaders, Fault fault){
		RpcFault sf = null;
		try{
			if(fault != null && fault.isInitialized()){
				sf = new RpcFault();
				sf.setFaultCode(fault.getFaultCode());
				if(fault.getFaultCode().equals("parameterException")
						|| fault.getFaultCode().equals("accessControlException")){
					sf.setFaultString(fault.getFaultString());
				} else{
					sf.setFaultString(fault.getFaultDetail());
				}
				throw new RuntimeException(fault.getFaultDetail());
			} else if(controller.failed()){
				sf = new RpcFault();
				sf.setFaultCode("Server.generalException");
				Throwable e = controller.errorException();
				String sgException = channel.getResponseProperties().get("X-ServiceGrid-Exception");
				if(sgException != null){
					sf.setFaultString(sgException);
				} else if(e != null){
					sf.setFaultString(ExceptionUtil.getMessageWithStackTrace(e));
				} else{
					sf.setFaultString(controller.errorText());
				}
				if(controller.errorException() != null){
					throw new RuntimeException(controller.errorException());
				} else{
					throw new RuntimeException(controller.errorText());
				}
			}
		} finally{
			MimeHeaders mh = new MimeHeaders(){
				@Override
				public String[] getHeader(String name){
					String value = channel.getResponseProperties().get(name);
					if(value == null) return null;
					else return new String[]{value};
				}
			};
			postprocess(iid, deltaTime, mh, new PbHeadersToRpcHeadersAdapter(responseHeaders), sf);
		}
    }
}
