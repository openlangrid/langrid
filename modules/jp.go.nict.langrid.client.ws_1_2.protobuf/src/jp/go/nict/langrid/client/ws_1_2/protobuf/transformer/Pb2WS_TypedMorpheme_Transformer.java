/*
 * $Id: Pb2WS_TypedMorpheme_Transformer.java 435 2011-12-20 07:35:31Z t-nakaguchi $
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
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.transformer.Transformer;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.typed.Morpheme;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;

public class Pb2WS_TypedMorpheme_Transformer
implements Transformer<MorphologicalAnalysisProtos.Morpheme, Morpheme>{
	@Override
	public Morpheme transform(
			MorphologicalAnalysisProtos.Morpheme value) throws TransformationException {
		PartOfSpeech pos = null;
		try{
			pos = PartOfSpeech.valueOf(value.getPartOfSpeech().replace('.', '_'));
		} catch(IllegalArgumentException e){
			throw new TransformationException(e);
		}
		return new Morpheme(value.getWord(), value.getLemma(), pos);
	}
}
