/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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
package jp.go.nict.langrid.client.ws_1_2.protobuf.impl;

import java.net.URL;
import java.util.List;

import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Fault;
import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.client.protobuf.proto.TranslationProtos.Service;
import jp.go.nict.langrid.client.protobuf.proto.TranslationProtos.TranslateRequest;
import jp.go.nict.langrid.client.protobuf.proto.TranslationProtos.TranslateResponse;
import jp.go.nict.langrid.client.ws_1_2.TranslationClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.commons.protobufrpc.RpcSyncCallback;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.language.Language;

import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;

/**
 * ProtobufRPC implementation of TranslationClient.
 * @author Takao Nakaguchi
 */
public class TranslationClientImpl
extends ServiceClientImpl
implements TranslationClient{
	/**
	 * Constructor.
	 * @param url The invocation url of the service.
	 */
	public TranslationClientImpl(URL url){
		super(url);
	}

	@Override
	public String translate(Language sourceLang, Language targetLang, String source)
	throws LangridException {
		TranslateRequest req = TranslateRequest.newBuilder()
			.addAllHeaders(getRequestHeaders())
			.setSourceLang(sourceLang.getCode())
			.setTargetLang(targetLang.getCode())
			.setSource(source)
			.build();
		return execute(
				executor, req, "translate"
				, sourceLang, targetLang, source
				).getResult();
	}

	private static ServiceExecutor<TranslateRequest, TranslateResponse> executor
			= new ServiceExecutor<TranslateRequest, TranslateResponse>(){
		public Trio<TranslateResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, TranslateRequest request){
			RpcSyncCallback<TranslateResponse> cb = new RpcSyncCallback<TranslateResponse>();
			Service.newStub(channel).translate(controller, request, cb);
			TranslateResponse response = cb.response();
			if(response == null){
				return Trio.create(null, null, null);
			}
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};
}
