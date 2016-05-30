/*
 * $Id: MorphologicalAnalysisClientImpl.java 435 2011-12-20 07:35:31Z t-nakaguchi $
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
import jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos.AnalyzeRequest;
import jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos.AnalyzeResponse;
import jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos.Service;
import jp.go.nict.langrid.client.ws_1_2.MorphologicalAnalysisClient;
import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.protobuf.util.MorphologicalAnalysisProtosUtil;
import jp.go.nict.langrid.commons.protobufrpc.RpcSyncCallback;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.typed.Morpheme;

import com.google.protobuf.RpcChannel;
import com.google.protobuf.RpcController;

public class MorphologicalAnalysisClientImpl
extends ServiceClientImpl
implements MorphologicalAnalysisClient{
	/**
	 * Constructor.
	 * @param url The invocation url of the service.
	 */
	public MorphologicalAnalysisClientImpl(URL url){
		super(url);
	}

	@Override
	public Morpheme[] analyze(Language language, String text)
	throws LangridException {
		AnalyzeRequest req = MorphologicalAnalysisProtosUtil.buildAnalyzeRequest(
				getRequestHeaders()
				, language.getCode(), text
				);
		return convert(execute(
				executor, req, "analyze"
				, language, text
				).getResultList().toArray(), Morpheme[].class);
	}

	private static ServiceExecutor<AnalyzeRequest, AnalyzeResponse> executor
			= new ServiceExecutor<AnalyzeRequest, AnalyzeResponse>(){
		public Trio<AnalyzeResponse, List<Header>, Fault> execute(
				RpcController controller, RpcChannel channel, AnalyzeRequest request){
			RpcSyncCallback<AnalyzeResponse> cb = new RpcSyncCallback<AnalyzeResponse>();
			Service.newStub(channel).analyze(controller, request, cb);
			AnalyzeResponse response = cb.response();
			return Trio.create(response, response.getHeadersList()
					, response.hasFault() ? response.getFault() : null);
		}
		};
}
