/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.service_1_2.translationselection;

import jp.go.nict.langrid.commons.rpc.intf.Parameter;
import jp.go.nict.langrid.commons.rpc.intf.Service;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;

/**
 * 
 * Defines the interface for the translation selection service.
 * If you're providing the translation selection service, then if you implement this class,
 * you can access it with a client provided by langrid.
 * 
 * @author Masaaki Kamiya
 * @author Takao Nakaguchi
 */
@Service(namespace="servicegrid:servicetype:nict.nlp:TranslationSelection")
public interface TranslationSelectionService {
	/**
	 * 
	 * Run translation.
	 * @param sourceLang Translation source language (RFC3066 compliant)
	 * @param targetLang Target language of the translation (RFC3066 compliant)
	 * @param source String to be translated
	 * @throws ProcessFailedException Translation process failed
	 * @throws LanguagePairNotUniquelyDecidedException Multiple candidate language pairs exist
	 * @throws UnsupportedLanguagePairException An unsupported language pair was specified
	 * @throws InvalidParameterException An invalid parameter was passed
	 * 
	 */
	SelectionResult select(
		@Parameter(name="sourceLang") String sourceLang
		, @Parameter(name="targetLang") String targetLang
		, @Parameter(name="source") String source)
	throws ProcessFailedException, LanguagePairNotUniquelyDecidedException, UnsupportedLanguagePairException, InvalidParameterException;
}
