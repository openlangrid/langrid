/*
 * $Id: TextToSpeechClientImpl.java 435 2011-12-20 07:35:31Z t-nakaguchi $
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
import jp.go.nict.langrid.client.protobuf.proto.TextToSpeechProtos.GetSupportedAudioTypesRequest;
import jp.go.nict.langrid.client.protobuf.proto.TextToSpeechProtos.GetSupportedAudioTypesResponse;
import jp.go.nict.langrid.client.protobuf.proto.TextToSpeechProtos.GetSupportedLanguagesRequest;
import jp.go.nict.langrid.client.protobuf.proto.TextToSpeechProtos.GetSupportedLanguagesResponse;
import jp.go.nict.langrid.client.protobuf.proto.TextToSpeechProtos.GetSupportedVoiceTypesRequest;
import jp.go.nict.langrid.client.protobuf.proto.TextToSpeechProtos.GetSupportedVoiceTypesResponse;
import jp.go.nict.langrid.client.protobuf.proto.TextToSpeechProtos.Service;
import jp.go.nict.langrid.client.protobuf.proto.TextToSpeechProtos.SpeakRequest;
import jp.go.nict.langrid.client.protobuf.proto.TextToSpeechProtos.SpeakResponse;
import jp.go.nict.langrid.client.ws_1_2.TextToSpeechClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.commons.protobufrpc.RpcSyncCallback;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.speech.Speech;

import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;

public class TextToSpeechClientImpl
extends ServiceClientImpl
implements TextToSpeechClient{
	/**
	 * Constructor.
	 * @param url The invocation url of the service.
	 */
	public TextToSpeechClientImpl(URL url){
		super(url);
	}

	@Override
	public Speech speak(Language language, String text, String voiceType,
			String audioType) throws LangridException {
		SpeakRequest req = SpeakRequest.newBuilder()
			.addAllHeaders(getRequestHeaders())
			.setLanguage(language.getCode())
			.setText(text)
			.setVoiceType(voiceType)
			.setAudioTypeType(audioType)
			.build()
			;
		SpeakResponse response = execute(
				speakExecutor, req, "speak", language, text, voiceType, audioType
				);
		return new Speech(
				response.getVoiceType()
				, response.getAudioType()
				, response.getAudio().toByteArray()
				);
	}

	@Override
	public String[] getSupportedLanguages() throws LangridException {
		GetSupportedLanguagesRequest req = GetSupportedLanguagesRequest.newBuilder()
				.addAllHeaders(getRequestHeaders())
				.build();
		return execute(
				getSupportedLanguagesExecutor, req, "getSupportedLanguages"
				).getResultsList().toArray(new String[]{});
	}

	@Override
	public String[] getSupportedVoiceTypes() throws LangridException {
		GetSupportedVoiceTypesRequest req = GetSupportedVoiceTypesRequest.newBuilder()
				.addAllHeaders(getRequestHeaders())
				.build();
		return execute(
				getSupportedVoiceTypesExecutor, req
				, "getSupportedVoiceTypes"
				).getResultsList().toArray(new String[]{});
	}

	@Override
	public String[] getSupportedAudioTypes() throws LangridException {
		GetSupportedAudioTypesRequest req = GetSupportedAudioTypesRequest.newBuilder()
				.addAllHeaders(getRequestHeaders())
				.build();
		return execute(
				getSupportedAudioTypesExecutor, req
				, "getSupportedAudioTypes"
				).getResultsList().toArray(new String[]{});
	}

	private static ServiceExecutor<SpeakRequest, SpeakResponse> speakExecutor
			= new ServiceExecutor<SpeakRequest, SpeakResponse>(){
		public Trio<SpeakResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, SpeakRequest request){
			RpcSyncCallback<SpeakResponse> cb = new RpcSyncCallback<SpeakResponse>();
			Service.newStub(channel).speak(controller, request, cb);
			SpeakResponse response = cb.response();
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
	private static ServiceExecutor<GetSupportedVoiceTypesRequest, GetSupportedVoiceTypesResponse> getSupportedVoiceTypesExecutor
			= new ServiceExecutor<GetSupportedVoiceTypesRequest, GetSupportedVoiceTypesResponse>(){
		public Trio<GetSupportedVoiceTypesResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, GetSupportedVoiceTypesRequest request){
			RpcSyncCallback<GetSupportedVoiceTypesResponse> cb = new RpcSyncCallback<GetSupportedVoiceTypesResponse>();
			Service.newStub(channel).getSupportedVoiceTypes(controller, request, cb);
			GetSupportedVoiceTypesResponse response = cb.response();
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};
	private static ServiceExecutor<GetSupportedAudioTypesRequest, GetSupportedAudioTypesResponse> getSupportedAudioTypesExecutor
			= new ServiceExecutor<GetSupportedAudioTypesRequest, GetSupportedAudioTypesResponse>(){
		public Trio<GetSupportedAudioTypesResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, GetSupportedAudioTypesRequest request){
			RpcSyncCallback<GetSupportedAudioTypesResponse> cb = new RpcSyncCallback<GetSupportedAudioTypesResponse>();
			Service.newStub(channel).getSupportedAudioTypes(controller, request, cb);
			GetSupportedAudioTypesResponse response = cb.response();
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};

}
