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
import jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos;
import jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos.AnalyzeRequest;
import jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos.AnalyzeResponse;
import jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos.Morpheme;
import jp.go.nict.langrid.client.ws_1_2.protobuf.transformer.Pb2WS_Morpheme_Transformer;
import jp.go.nict.langrid.commons.protobufrpc.DefaultRpcController;
import jp.go.nict.langrid.commons.protobufrpc.RpcSyncCallback;
import jp.go.nict.langrid.commons.protobufrpc.URLRpcChannel;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.servicecontainer.executor.protobufrpc.AbstractPbServiceExecutor;


/**
 * 
 * 
 */
public class PbMorphologicalAnalysisServiceExecutor
extends AbstractPbServiceExecutor
implements MorphologicalAnalysisService{
	public PbMorphologicalAnalysisServiceExecutor(String invocationName){
		super(invocationName);
	}

	public PbMorphologicalAnalysisServiceExecutor(String invocationName, long invocationId, Endpoint endpoint){
		super(invocationName, invocationId, endpoint);
	}

	@Override
	public jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme[] analyze(
			String language, String text) throws AccessLimitExceededException,
			InvalidParameterException, LanguageNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException {
		List<RpcHeader> reqHeaders = new ArrayList<RpcHeader>();
		Pair<URLRpcChannel, Long> r = preprocessPb(reqHeaders);
		MorphologicalAnalysisProtos.Service.Stub stub = MorphologicalAnalysisProtos.Service.newStub(r.getFirst());
		DefaultRpcController c = new DefaultRpcController();
		List<Header> resHeaders = new ArrayList<Header>();
		long s = System.currentTimeMillis( ) ;
		RpcSyncCallback<AnalyzeResponse> callback = new RpcSyncCallback<AnalyzeResponse>();
		try{
			AnalyzeRequest.Builder b = AnalyzeRequest.newBuilder();
			for(RpcHeader h : reqHeaders){
				b.addHeaders(Header.newBuilder().setName(h.getNamespace()).setValue(h.getValue()));
			}
			AnalyzeRequest req = b
					.setLanguage(language)
					.setText(text)
					.build();
			stub.analyze(c, req, callback);
		} finally{
			postprocessPb(
					r.getSecond(), System.currentTimeMillis( ) - s
					, c, r.getFirst()
					, resHeaders, callback.response().getFault()
					);
		}
		return ArrayUtil.collect(
				callback.response().getResultList().toArray(new Morpheme[]{})
				, new Pb2WS_Morpheme_Transformer()
				);
	}
}
