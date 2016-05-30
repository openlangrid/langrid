/*
 * $Id: BilingualDictionaryWithLongestMatchSearchClientImpl.java 435 2011-12-20 07:35:31Z t-nakaguchi $
 *
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos.SearchLongestMatchingTermsRequest;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos.SearchLongestMatchingTermsResponse;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos.SearchRequest;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos.SearchResponse;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos.Service;
import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Fault;
import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.client.ws_1_2.BilingualDictionaryWithLongestMatchSearchClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.protobuf.util.BilingualDictionaryWithLongestMatchSearchProtosUtil;
import jp.go.nict.langrid.commons.protobufrpc.RpcSyncCallback;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.LanguagePair;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;

import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;

public class BilingualDictionaryWithLongestMatchSearchClientImpl
extends ServiceClientImpl
implements BilingualDictionaryWithLongestMatchSearchClient{
	/**
	 * Constructor.
	 * @param url The invocation url of the service.
	 */
	public BilingualDictionaryWithLongestMatchSearchClientImpl(URL url){
		super(url);
	}

	@Override
	public Translation[] search(Language headLang, Language targetLang,
			String headWord, MatchingMethod matchingMethod)
	throws LangridException {
		SearchRequest req = BilingualDictionaryWithLongestMatchSearchProtos.SearchRequest.newBuilder()
				.addAllHeaders(getRequestHeaders())
				.setHeadLang(headLang.getCode())
				.setTargetLang(targetLang.getCode())
				.setHeadWord(headWord)
				.setMatchingMethod(matchingMethod.name())
				.build();
		return convert(
				execute(
						searchExecutor, req
						, "search", headLang, targetLang, headWord, matchingMethod
						).getResultList().toArray()
				, Translation[].class);
	}

	@Override
	public TranslationWithPosition[] searchLongestMatchingTerms(
			Language headLang, Language targetLang, Morpheme[] morphemes)
	throws LangridException {
		SearchLongestMatchingTermsRequest req = BilingualDictionaryWithLongestMatchSearchProtosUtil.buildSearchLongestMatchingTermsRequest(
				getRequestHeaders()
				, headLang.getCode(), targetLang.getCode()
				, Arrays.asList(convert(
						morphemes
						, jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos.Morpheme[].class))
				);
		return convert(
				execute(
						searchLongestMatchingTermsExecutor, req
						, "searchLongestMatchingTerms", headLang, targetLang, morphemes
						).getResultList().toArray(new jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos.TranslationWithPosition[]{})
				, TranslationWithPosition[].class);
	}

	@Override
	public LanguagePair[] getSupportedLanguagePairs() throws LangridException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String[] getSupportedMatchingMethods() throws LangridException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Calendar getLastUpdate() throws LangridException {
		throw new UnsupportedOperationException();
	}

	private static ServiceExecutor<SearchRequest, SearchResponse> searchExecutor
			= new ServiceExecutor<SearchRequest, SearchResponse>(){
		public Trio<SearchResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, SearchRequest request){
			RpcSyncCallback<SearchResponse> cb = new RpcSyncCallback<SearchResponse>();
			Service.newStub(channel).search(controller, request, cb);
			SearchResponse response = cb.response();
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};
	private static ServiceExecutor<SearchLongestMatchingTermsRequest, SearchLongestMatchingTermsResponse> searchLongestMatchingTermsExecutor
			= new ServiceExecutor<SearchLongestMatchingTermsRequest, SearchLongestMatchingTermsResponse>(){
		public Trio<SearchLongestMatchingTermsResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, SearchLongestMatchingTermsRequest request){
			RpcSyncCallback<SearchLongestMatchingTermsResponse> cb = new RpcSyncCallback<SearchLongestMatchingTermsResponse>();
			Service.newStub(channel).searchLongestMatchingTerms(controller, request, cb);
			SearchLongestMatchingTermsResponse response = cb.response();
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};
}
