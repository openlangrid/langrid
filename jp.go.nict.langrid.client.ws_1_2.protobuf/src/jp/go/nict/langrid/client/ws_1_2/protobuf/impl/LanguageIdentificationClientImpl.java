/*
 * $Id: LanguageIdentificationClientImpl.java 435 2011-12-20 07:35:31Z t-nakaguchi $
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
import java.util.List;

import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Fault;
import jp.go.nict.langrid.client.protobuf.proto.CommonProtos.Header;
import jp.go.nict.langrid.client.protobuf.proto.LanguageIdentificationProtos.GetSupportedEncodingsRequest;
import jp.go.nict.langrid.client.protobuf.proto.LanguageIdentificationProtos.GetSupportedEncodingsResponse;
import jp.go.nict.langrid.client.protobuf.proto.LanguageIdentificationProtos.GetSupportedLanguagesRequest;
import jp.go.nict.langrid.client.protobuf.proto.LanguageIdentificationProtos.GetSupportedLanguagesResponse;
import jp.go.nict.langrid.client.protobuf.proto.LanguageIdentificationProtos.IdentifyLanguageAndEncodingRequest;
import jp.go.nict.langrid.client.protobuf.proto.LanguageIdentificationProtos.IdentifyLanguageAndEncodingResponse;
import jp.go.nict.langrid.client.protobuf.proto.LanguageIdentificationProtos.IdentifyRequest;
import jp.go.nict.langrid.client.protobuf.proto.LanguageIdentificationProtos.IdentifyResponse;
import jp.go.nict.langrid.client.protobuf.proto.LanguageIdentificationProtos.Service;
import jp.go.nict.langrid.client.ws_1_2.LanguageIdentificationClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.commons.protobufrpc.RpcSyncCallback;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.service_1_2.languageidentification.LanguageAndEncoding;

import com.google.protobuf.ByteString;
import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;

/**
 * 
 * 
 */
public class LanguageIdentificationClientImpl
extends ServiceClientImpl
implements LanguageIdentificationClient{
	/**
	 * Constructor.
	 * @param url The invocation url of the service.
	 */
	public LanguageIdentificationClientImpl(URL url){
		super(url);
	}

	@Override
	public String identify(String text, String originalEncoding)
	throws LangridException {
		IdentifyRequest req = IdentifyRequest.newBuilder()
			.addAllHeaders(getRequestHeaders())
			.setText(text)
			.setOriginalEncoding(originalEncoding)
			.build()
			;
		return execute(
				identifyExecutor, req, "identify"
				, text, originalEncoding
				).getResult();
	}

	@Override
	public LanguageAndEncoding identifyLanguageAndEncoding(byte[] textBytes)
	throws LangridException {
		IdentifyLanguageAndEncodingRequest req = IdentifyLanguageAndEncodingRequest.newBuilder()
				.addAllHeaders(getRequestHeaders())
				.setTextBytes(ByteString.copyFrom(textBytes))
				.build()
				;
		IdentifyLanguageAndEncodingResponse response = execute(
				identifyLanguageAndEncodingExecutor
				, req, "identifyLanguageAndEncoding", textBytes);
		return new LanguageAndEncoding(
				response.getResult().getLanguage()
				, response.getResult().getEncoding()
				);
	}

	@Override
	public String[] getSupportedLanguages() throws LangridException {
		GetSupportedLanguagesRequest req = GetSupportedLanguagesRequest.newBuilder()
				.addAllHeaders(getRequestHeaders())
				.build();
		return execute(getSupportedLanguagesExecutor, req, "getSupportedLanguages")
				.getResultsList().toArray(new String[]{});
	}

	@Override
	public String[] getSupportedEncodings() throws LangridException {
		GetSupportedEncodingsRequest req = GetSupportedEncodingsRequest.newBuilder()
				.addAllHeaders(getRequestHeaders())
				.build();
		return execute(getSupportedEncodingsExecutor, req, "getSupportedEncodings")
				.getResultsList().toArray(new String[]{});
	}

	private static ServiceExecutor<IdentifyRequest, IdentifyResponse> identifyExecutor
			= new ServiceExecutor<IdentifyRequest, IdentifyResponse>(){
		public Trio<IdentifyResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, IdentifyRequest request){
			RpcSyncCallback<IdentifyResponse> cb = new RpcSyncCallback<IdentifyResponse>();
			Service.newStub(channel).identify(controller, request, cb);
			IdentifyResponse response = cb.response();
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};
	private static ServiceExecutor<IdentifyLanguageAndEncodingRequest, IdentifyLanguageAndEncodingResponse> identifyLanguageAndEncodingExecutor
			= new ServiceExecutor<IdentifyLanguageAndEncodingRequest, IdentifyLanguageAndEncodingResponse>(){
		public Trio<IdentifyLanguageAndEncodingResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, IdentifyLanguageAndEncodingRequest request){
			RpcSyncCallback<IdentifyLanguageAndEncodingResponse> cb = new RpcSyncCallback<IdentifyLanguageAndEncodingResponse>();
			Service.newStub(channel).identifyLanguageAndEncoding(controller, request, cb);
			IdentifyLanguageAndEncodingResponse response = cb.response();
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};
	private static ServiceExecutor<GetSupportedLanguagesRequest, GetSupportedLanguagesResponse> getSupportedLanguagesExecutor
			= new ServiceExecutor<GetSupportedLanguagesRequest, GetSupportedLanguagesResponse>(){
		public Trio<GetSupportedLanguagesResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, GetSupportedLanguagesRequest request){
			RpcSyncCallback<GetSupportedLanguagesResponse> cb = new RpcSyncCallback<GetSupportedLanguagesResponse>();
			Service.newStub(channel).getSupportedLanguages(controller, request, cb);
			GetSupportedLanguagesResponse response = cb.response();
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};
	private static ServiceExecutor<GetSupportedEncodingsRequest, GetSupportedEncodingsResponse> getSupportedEncodingsExecutor
			= new ServiceExecutor<GetSupportedEncodingsRequest, GetSupportedEncodingsResponse>(){
		public Trio<GetSupportedEncodingsResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, GetSupportedEncodingsRequest request){
			RpcSyncCallback<GetSupportedEncodingsResponse> cb = new RpcSyncCallback<GetSupportedEncodingsResponse>();
			Service.newStub(channel).getSupportedEncodings(controller, request, cb);
			GetSupportedEncodingsResponse response = cb.response();
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};
}
