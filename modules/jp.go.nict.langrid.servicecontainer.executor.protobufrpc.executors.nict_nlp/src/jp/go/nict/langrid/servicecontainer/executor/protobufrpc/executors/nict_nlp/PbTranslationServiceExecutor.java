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
package jp.go.nict.langrid.servicecontainer.executor.protobufrpc.executors.nict_nlp;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.client.protobuf.proto.TranslationProtos;
import jp.go.nict.langrid.client.protobuf.proto.TranslationProtos.TranslateRequest;
import jp.go.nict.langrid.client.protobuf.proto.TranslationProtos.TranslateResponse;
import jp.go.nict.langrid.commons.protobufrpc.DefaultRpcController;
import jp.go.nict.langrid.commons.protobufrpc.RpcSyncCallback;
import jp.go.nict.langrid.commons.protobufrpc.URLRpcChannel;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.servicecontainer.executor.protobufrpc.AbstractPbServiceExecutor;

/**
 * 
 * 
 */
public class PbTranslationServiceExecutor
extends AbstractPbServiceExecutor
implements TranslationService{
	public PbTranslationServiceExecutor(String invocationName){
		super(invocationName);
	}

	public PbTranslationServiceExecutor(String invocationName, long invocationId, Endpoint endpoint){
		super(invocationName, invocationId, endpoint);
	}

	@Override
	public String translate(String sourceLang, String targetLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException
	{
		List<RpcHeader> reqHeaders = new ArrayList<RpcHeader>();
		Pair<URLRpcChannel, Long> r = preprocessPb(reqHeaders);
		TranslationProtos.Service.Stub stub = TranslationProtos.Service.newStub(r.getFirst());
		DefaultRpcController c = new DefaultRpcController();
		List<Header> resHeaders = new ArrayList<Header>();
		long s = System.currentTimeMillis();
		RpcSyncCallback<TranslateResponse> callback = new RpcSyncCallback<TranslateResponse>();
		try{
			TranslateRequest.Builder b = TranslateRequest.newBuilder();
			for(RpcHeader h : reqHeaders){
				b.addHeaders(Header.newBuilder().setName(h.getNamespace()).setValue(h.getValue()));
			}
			TranslateRequest req = b
					.setSourceLang(sourceLang)
					.setTargetLang(targetLang)
					.setSource(source)
					.build();
			stub.translate(c, req, callback);
		} finally{
			postprocessPb(
					r.getSecond(), System.currentTimeMillis( ) - s
					, c, r.getFirst()
					, resHeaders, callback.response().getFault()
					);
		}
		return callback.response().getResult();
	}
}
