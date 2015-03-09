/*
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
package jp.go.nict.langrid.service_1_2.backtranslation;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;

/**
 * 
 * The interface for asynchronous back translation using a temporary dictionary.
 * 
 * @author Takao Nakaguchi
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:AsyncBackTranslationWithTemporalDictionary")
public interface AsyncBackTranslationWithTemporalDictionaryService {
	/**
	 * 
	 * Run asynchronous back translation.
	 * @param sourceLang Translation source language (RFC3066 compliant)
	 * @param intermediateLang Intermediate language of the translation (RFC3066 compliant)
	 * @param array of source String to be translated
	 * @param temporalDict Bilingual dictionary data used during translation
	 * @param dictTargetLang Target language of dictionary data(targetLang)
	 * @return The token to get result later
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Translation process failed
	 * 
	 */
	String startBackTranslation(
			@Parameter(name="sourceLang") String sourceLang
			, @Parameter(name="intermediateLang") String intermediateLang
			, @Parameter(name="sources") String[] sources
			, @Parameter(name="temporalDict") Translation[] temporalDict
			, @Parameter(name="dictTargetLang") String dictTargetLang)
	throws InvalidParameterException, ProcessFailedException;

	/**
	 * 
	 * @param token
	 * @return
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Translation process failed
	 * 
	 */
	AsyncBackTranslationResult getCurrentResult(
			@Parameter(name="token") String token)
	throws InvalidParameterException, ProcessFailedException;
	
	/**
	 * 
	 * @param token
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Translation process failed
	 * 
	 */
	void terminate(
			@Parameter(name="token") String token)
	throws InvalidParameterException, ProcessFailedException;
}
