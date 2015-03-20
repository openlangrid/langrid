/*
 * $Id: WS2Pb_Morpheme_Transformer.java 435 2011-12-20 07:35:31Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.protobuf.transformer;

import jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos;
import jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos.Morpheme.Builder;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;

public class WS2Pb_Morpheme_Transformer
implements Transformer<Morpheme, jp.go.nict.langrid.client.protobuf.proto.MorphologicalAnalysisProtos.Morpheme>{
	@Override
	public MorphologicalAnalysisProtos.Morpheme transform(
			Morpheme value) throws TransformationException {
		Builder b = MorphologicalAnalysisProtos.Morpheme.newBuilder();
		return b.setLemma(value.getLemma())
			.setWord(value.getWord())
			.setPartOfSpeech(value.getPartOfSpeech())
			.build();
	}
}
