/*
 * $Id: ConstructSourceAndMorphemesAndCodesForRich.java 409 2011-08-25 03:12:59Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2011 NICT Language Grid Project.
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

package jp.go.nict.langrid.wrapper.workflowsupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.workflowsupport.ConstructSourceAndMorphemesAndCodesService;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes;
import jp.go.nict.langrid.wrapper.workflowsupport.util.StringUtil;

/**
 * @author Takao Nakaguchi
 */
public class ConstructSourceAndMorphemesAndCodesForRich
extends ConstructSourceAndMorphemesAndCodes
implements ConstructSourceAndMorphemesAndCodesService{
	public SourceAndMorphemesAndCodes doConstructSMC(String sourceLang,
			Morpheme[] morphemes, TranslationWithPosition[] translations)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		Map<Integer, TranslationWithPosition> positionMap = new HashMap<Integer, TranslationWithPosition>();
		for(TranslationWithPosition t : translations) {
			if (t == null) continue;
			if (t.getTranslation() == null) continue;
			String[] tWords = t.getTranslation().getTargetWords();
			if (tWords == null || tWords.length == 0) {
				continue;
			}
			int index = t.getStartIndex();
			TranslationWithPosition exist = positionMap.get(index);
			if(exist != null){
				if(t.getNumberOfMorphemes() > exist.getNumberOfMorphemes()){
					positionMap.put(index, t);
				}
			} else{
				positionMap.put(index, t);
			}
		}
		List<String> sourceTerms = new ArrayList<String>();
		boolean insertBlank = true;
		if (sourceLang.startsWith("ja")) {
			insertBlank = false;
		} else if (sourceLang.startsWith("ko")) {
			insertBlank = true;
		} else if (sourceLang.startsWith("zh")) {
			insertBlank = false;
		} else if (sourceLang.startsWith("en")) {
			insertBlank = true;
		} else if (sourceLang.startsWith("th")) {
			insertBlank = false;
		} else {
			insertBlank = true;
		}
		for (int i = 0; i < morphemes.length; i++) {
			TranslationWithPosition translation = positionMap.get(Integer.valueOf(i));
			if (translation != null) {
				String term = StringUtil.createWord(
						insertBlank, morphemes
						, translation.getStartIndex(), translation.getNumberOfMorphemes());
				sourceTerms.add(term);
				i = i + translation.getNumberOfMorphemes() - 1;
			}
		}
		SourceAndMorphemesAndCodes ret = super.doConstructSMC(sourceLang, morphemes, translations);

		String[] targetWords = ret.getTargetWords();
		int n = Math.min(sourceTerms.size(), ret.getTargetWords().length);
		for(int i = 0; i < n; i++){
			targetWords[i] = targetWords[i] + " (" + sourceTerms.get(i) + ")";
		}
		return ret;
	}
}
