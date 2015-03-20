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

import jp.go.nict.langrid.client.protobuf.proto.BackTranslationProtos.BackTranslateRequest;
import jp.go.nict.langrid.client.protobuf.proto.BackTranslationProtos.BackTranslateResponse;
import jp.go.nict.langrid.client.protobuf.proto.BackTranslationProtos.Service;
import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Fault;
import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.client.ws_1_2.BackTranslationClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.protobuf.util.BackTranslationProtosUtil;
import jp.go.nict.langrid.commons.protobufrpc.RpcSyncCallback;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;

import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;

public class BackTranslationClientImpl
extends ServiceClientImpl
implements BackTranslationClient{
	/**
	 * Constructor.
	 * @param url The invocation url of the service.
	 */
	public BackTranslationClientImpl(URL url){
		super(url);
	}

	@Override
	public BackTranslationResult backTranslate(Language sourceLang,
			Language intermediateLang, String source) throws LangridException {
		BackTranslateRequest req = BackTranslationProtosUtil.buildBackTranslateRequest(
				getRequestHeaders(), sourceLang.getCode()
				, intermediateLang.getCode(), source
				);
		BackTranslateResponse response = execute(
				executor, req, "backTranslate"
				, sourceLang, intermediateLang, source
				);
		return new BackTranslationResult(
				response.getResult().getIntermediate()
				, response.getResult().getTarget()
				);
	}

	private static ServiceExecutor<BackTranslateRequest, BackTranslateResponse> executor
			= new ServiceExecutor<BackTranslateRequest, BackTranslateResponse>(){
		public Trio<BackTranslateResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, BackTranslateRequest request){
			RpcSyncCallback<BackTranslateResponse> cb = new RpcSyncCallback<BackTranslateResponse>();
			Service.newStub(channel).backTranslate(controller, request, cb);
			BackTranslateResponse response = cb.response();
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};
}
