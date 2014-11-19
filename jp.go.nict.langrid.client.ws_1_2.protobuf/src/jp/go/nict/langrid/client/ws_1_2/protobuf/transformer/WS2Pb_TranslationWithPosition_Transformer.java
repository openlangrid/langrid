/*
 * $Id: WS2Pb_TranslationWithPosition_Transformer.java 371 2011-08-23 02:22:52Z t-nakaguchi $
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

import java.util.Arrays;

import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryProtos.Translation;
import jp.go.nict.langrid.client.protobuf.proto.BilingualDictionaryWithLongestMatchSearchProtos.TranslationWithPosition;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;

public class WS2Pb_TranslationWithPosition_Transformer
implements Transformer<jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition, TranslationWithPosition>{
	@Override
	public TranslationWithPosition transform(
			jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition value) throws TransformationException {
		return TranslationWithPosition.newBuilder()
				.setTranslation(
						Translation.newBuilder()
							.setHeadWord(value.getTranslation().getHeadWord())
							.addAllTargetWords(Arrays.asList(value.getTranslation().getTargetWords()))
							.build()
							)
				.setStartIndex(value.getStartIndex())
				.setNumberOfMorphemes(value.getNumberOfMorphemes())
				.build();
	}
}
