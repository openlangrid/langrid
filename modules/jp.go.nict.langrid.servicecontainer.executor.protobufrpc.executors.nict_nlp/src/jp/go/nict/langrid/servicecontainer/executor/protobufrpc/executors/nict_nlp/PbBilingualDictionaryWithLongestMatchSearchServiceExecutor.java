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
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryProtos;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryProtos.SearchRequest;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryProtos.SearchResponse;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos.SearchLongestMatchingTermsRequest;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos.SearchLongestMatchingTermsResponse;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos.TranslationWithPosition;
import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.client.ws_1_2.protobuf.transformer.Pb2WS_TranslationWithPosition_Transformer;
import jp.go.nict.langrid.client.ws_1_2.protobuf.transformer.Pb2WS_Translation_Transformer;
import jp.go.nict.langrid.client.ws_1_2.protobuf.transformer.WS2Pb_Morpheme_Transformer;
import jp.go.nict.langrid.commons.protobufrpc.DefaultRpcController;
import jp.go.nict.langrid.commons.protobufrpc.RpcSyncCallback;
import jp.go.nict.langrid.commons.protobufrpc.URLRpcChannel;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePair;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.servicecontainer.executor.protobufrpc.AbstractPbServiceExecutor;


/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class PbBilingualDictionaryWithLongestMatchSearchServiceExecutor
extends AbstractPbServiceExecutor
implements BilingualDictionaryWithLongestMatchSearchService{
	public PbBilingualDictionaryWithLongestMatchSearchServiceExecutor(String invocationName){
		super(invocationName);
	}

	public PbBilingualDictionaryWithLongestMatchSearchServiceExecutor(String invocationName, long invocationId, Endpoint endpoint){
		super(invocationName, invocationId, endpoint);
	}

	@Override
	public jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition[] searchLongestMatchingTerms(
			String headLang, String targetLang, Morpheme[] morphemes)
	throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException,
			UnsupportedMatchingMethodException
	{
		List<RpcHeader> reqHeaders = new ArrayList<RpcHeader>();
		Pair<URLRpcChannel, Long> r = preprocessPb(reqHeaders);
		BilingualDictionaryWithLongestMatchSearchProtos.Service.Stub stub = BilingualDictionaryWithLongestMatchSearchProtos.Service.newStub(r.getFirst());
		SearchLongestMatchingTermsRequest.Builder b = SearchLongestMatchingTermsRequest.newBuilder( ) ;
		DefaultRpcController c = new DefaultRpcController();
		List<Header> resHeaders = new ArrayList<Header>();
		RpcSyncCallback<SearchLongestMatchingTermsResponse> callback = new RpcSyncCallback<SearchLongestMatchingTermsResponse>( ) ;
		long s = System.currentTimeMillis();
		try{
			for(RpcHeader h : reqHeaders){
				b.addHeaders(Header.newBuilder().setName(h.getNamespace()).setValue(h.getValue()));
			}
			b.setHeadLang(headLang).setTargetLang(targetLang);
			WS2Pb_Morpheme_Transformer trans = new WS2Pb_Morpheme_Transformer();
			for(jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme m : morphemes){
				b.addMorphemes(trans.transform(m));
			}
			SearchLongestMatchingTermsRequest req = b.build();
			stub.searchLongestMatchingTerms(c, req, callback);
		} finally{
			postprocessPb(
					r.getSecond(), System.currentTimeMillis( ) - s
					, c, r.getFirst()
					, resHeaders, callback.response().getFault()
					);
		}
		return ArrayUtil.collect(
				callback.response().getResultList().toArray(new TranslationWithPosition[]{})
				, new Pb2WS_TranslationWithPosition_Transformer()
				);
	}

	@Override
	public Translation[] search(String headLang, String targetLang,
			String headWord, String matchingMethod)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException,
			UnsupportedMatchingMethodException {
		List<RpcHeader> reqHeaders = new ArrayList<RpcHeader>();
		Pair<URLRpcChannel, Long> r = preprocessPb(reqHeaders);
		BilingualDictionaryProtos.Service.Stub stub = BilingualDictionaryProtos.Service.newStub(r.getFirst());
		DefaultRpcController c = new DefaultRpcController();
		List<Header> resHeaders = new ArrayList<Header>();
		RpcSyncCallback<SearchResponse> callback = new RpcSyncCallback<SearchResponse>();
		long s = System.currentTimeMillis();
		try{
			SearchRequest.Builder b = SearchRequest.newBuilder();
			for(RpcHeader h : reqHeaders){
				b.addHeaders(Header.newBuilder().setName(h.getNamespace()).setValue(h.getValue()));
			}
			SearchRequest req = b.setHeadLang(headLang)
				.setTargetLang(targetLang)
				.setHeadWord(headWord)
				.setMatchingMethod(matchingMethod)
				.build();
			stub.search(c, req, callback);
		} finally{
			postprocessPb(
					r.getSecond(), System.currentTimeMillis( ) - s
					, c, r.getFirst()
					, resHeaders, callback.response().getFault()
					);
		}
		return ArrayUtil.collect(
				callback.response().getResultList().toArray(new BilingualDictionaryProtos.Translation[]{})
				, new Pb2WS_Translation_Transformer()
				);
	}

	@Override
	public Calendar getLastUpdate() throws AccessLimitExceededException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		throw new UnsupportedOperationException();
	}

	@Override
	public LanguagePair[] getSupportedLanguagePairs()
			throws AccessLimitExceededException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String[] getSupportedMatchingMethods()
			throws AccessLimitExceededException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException {
		throw new UnsupportedOperationException();
	}
}
