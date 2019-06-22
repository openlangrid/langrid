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
package jp.go.nict.langrid.composite.translation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.composite.util.CompositeTranslationUtil;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationResult;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.servicecontainer.service.composite.Invocation;
import jp.go.nict.langrid.servicecontainer.service.composite.ServiceLoadingFailedException;
import jp.go.nict.langrid.wrapper.workflowsupport.ConstructSourceAndMorphemesAndCodes;
import jp.go.nict.langrid.wrapper.workflowsupport.DefaultMorphologicalAnalysis;
import jp.go.nict.langrid.wrapper.workflowsupport.ReplacementTerm;
import jp.go.nict.langrid.wrapper.workflowsupport.TemporalBilingualDictionaryWithLongestMatchSearch;

/**
 * Translation combined with bilingual dictionary with longest match search service implementation.
 * @author Takao Nakaguchi
 */
public class MultihopTranslationCombinedWithBilingualDictionaryWithLongestMatchSearch
extends AbstractCompositeService
implements MultihopTranslationWithTemporalDictionaryService{
	public static class Services{
		@Invocation(name="BilingualDictionaryWithLongestMatchSearchPL")
		private BilingualDictionaryWithLongestMatchSearchService bdict;
		@Invocation(name="MorphologicalAnalysisPL")
		private MorphologicalAnalysisService morph;
		// TranslationPL? (? = 1 - intermediateLangs.length)
	}

	public MultihopTranslationCombinedWithBilingualDictionaryWithLongestMatchSearch() {
		super(Services.class);
	}

	private <T> T validateArrayLengthEQ(String parameterName, T values, int expectedLength)
	throws InvalidParameterException{
		int n = Array.getLength(values);
		if(n != expectedLength){
			throw new InvalidParameterException(parameterName, String.format(
					" must have %d elements. actual: %d",
					expectedLength, n));
		}
		return values;
	}

	private <T> T validateArrayLengthGE(String parameterName, T values, int expectedLength)
	throws InvalidParameterException{
		int n = Array.getLength(values);
		if(n < expectedLength){
			throw new InvalidParameterException(parameterName, String.format(
					" must have %d or more elements. actual: %d",
					expectedLength, n));
		}
		return values;
	}

	private String validateLang(String parameterName, String value)
	throws InvalidParameterException{
		return new LanguageValidator(parameterName, value)
			.notNull().trim().notEmpty().getLanguage().getCode();
	}

	private String[] validateLangs(String parameterName, String[] values)
	throws InvalidParameterException{
		for(int i = 0; i < values.length; i++){
			values[i] = new LanguageValidator(parameterName + "[" + i + "]", values[i])
				.notNull().trim().notEmpty().getLanguage().getCode();
		}
		return values;
	}

	private String validateLangOrElse(String parameterName, String value, String elseValue)
	throws InvalidParameterException{
		if(value == null || value.trim().length() == 0) return elseValue;
		return new LanguageValidator(parameterName, value)
			.notNull().trim().notEmpty().getLanguage().getCode();
	}

	private String validateText(String parameterName, String value)
	throws InvalidParameterException{
		return new StringValidator(parameterName, value).notNull().trim().notEmpty().getValue();
	}

	private String[] validateTexts(String parameterName, String[] values)
	throws InvalidParameterException{
		for(int i = 0; i < values.length; i++){
			values[i] = new StringValidator(parameterName + "[" + i + "]", values[i])
				.notNull().trim().notEmpty().getValue();
		}
		return values;
	}

	private String[][] validateTextss(String parameterName, String[][] values)
	throws InvalidParameterException{
		for(int i = 0; i < values.length; i++){
			values[i] = validateArrayLengthGE(parameterName + "[" + i + "]", values[i], 1);
			values[i] = validateTexts(parameterName, values[i]);
		}
		return values;
	}

	private String[][][] validateTemporalDictTargets(String parameterName, String[][][] values,
			int intermediateLangLength, int temporalDictLength)
	throws InvalidParameterException{
		values = validateArrayLengthEQ(parameterName, values, intermediateLangLength);
		for(int i = 0; i < values.length; i++){
			values[i] = validateArrayLengthEQ(parameterName + "[" + i + "]", values[i], temporalDictLength);
			values[i] = validateTextss(parameterName + "[" + i + "]", values[i]);
		}
		return values;
	}

	public MultihopTranslationResult multihopTranslate(
			String sourceLang, String[] intermediateLangs, String targetLang,
			String source,
			Translation[] temporalDict, String dictTargetLang,
			String[][][] intermediateTemporalDictTargets,
			String[] intermediateDictTargetLangs
			)
	throws AccessLimitExceededException,
	InvalidParameterException, LanguagePairNotUniquelyDecidedException,
	NoAccessPermissionException, ProcessFailedException,
	NoValidEndpointsException, ServerBusyException,
	ServiceNotActiveException, ServiceNotFoundException,
	UnsupportedLanguagePairException
	{
		/**
		 * 形態素解析して一時辞書と対訳辞書結果をマージして中間コード入り元文を作り、マルチホップ翻訳していく。
		 * 各中間言語毎に、翻訳結果に一時辞書と対訳辞書検索結果をリプレースして中間結果を作る。
		 * 最終結果も作る。
		 * 辞書は訳語が無ければ中間コードのままにする。
		 */
		sourceLang = validateLang("sourceLang", sourceLang);
		intermediateLangs = validateLangs("intermediateLangs", intermediateLangs);
		targetLang = validateLang("targetLang", targetLang);
		source = validateText("source", source);
		temporalDict = (temporalDict != null) ? temporalDict : new Translation[]{};
		dictTargetLang = validateLangOrElse("dictTargetLang", dictTargetLang, targetLang);
		intermediateTemporalDictTargets = validateTemporalDictTargets(
				"intermediateTemporalDictTargets",
				intermediateTemporalDictTargets,
				intermediateLangs.length,
				temporalDict.length);
		intermediateDictTargetLangs = validateLangs(
				"intermediateDictTargetLangs",
				validateArrayLengthEQ("intermediateDictTargetLangs", intermediateDictTargetLangs, intermediateLangs.length)
				);
		try{
			Services s = loadServices();
			ConstructSourceAndMorphemesAndCodes csmcService = getConstructSourceAndMorphemesAndCodes();
			ReplacementTerm replService = new ReplacementTerm();

			Morpheme[] analyzeResult = doMorphologicalAnalyze(
					sourceLang, source, s.morph);
			Translation[] indexedDict = makeIndexedDict(temporalDict);
			Collection<TranslationWithPosition> tempDictResult = doTempDictMatch(
					sourceLang, analyzeResult, indexedDict);
			SourceAndMorphemesAndCodes firstSmc = csmcService.doConstructSMC(
					sourceLang, analyzeResult, tempDictResult.toArray(emptyTranslation));
			TranslationWithPosition[] bdictResult = doDictMatch(
					sourceLang, dictTargetLang, firstSmc.getMorphemes(), s.bdict);
			SourceAndMorphemesAndCodes secondSmc = csmcService.doConstructSMC(
					sourceLang, firstSmc.getMorphemes(), bdictResult);
			String[] codes = ArrayUtil.append(firstSmc.getCodes(), secondSmc.getCodes());

			// intermediate translations
			int c = 0;
			String srct = secondSmc.getSource();
			String srcl = sourceLang;
			List<String> intermResults = new ArrayList<String>();
			for(String l : intermediateLangs){
				TranslationWithPosition[] dictResult = doDictMatch(
						sourceLang, intermediateDictTargetLangs[c], firstSmc.getMorphemes(), s.bdict);
				String[] targetWords = getIntermediateDictTargetWords(dictResult);
				String r = getService("TranslationPL" + (c + 1), TranslationService.class).translate(srcl, l, srct);
				if(intermediateTemporalDictTargets.length > c){
					targetWords = ArrayUtil.append(
							getTargetWordsByIndex(intermediateTemporalDictTargets[c], firstSmc.getTargetWords()),
							targetWords);
				}
				String replaced = replService.doReplace(
						l, r, codes, targetWords
						);
				intermResults.add(replaced);
				srct = r;
				srcl = l;
				c++;
			}
			// translation for last lang
			String r = getService("TranslationPL" + (intermediateLangs.length + 1), TranslationService.class)
					.translate(srcl, targetLang, srct);
			r = replService.doReplace(
					targetLang, r, codes,
					ArrayUtil.append(
							getTargetWordsByIndex(temporalDict, firstSmc.getTargetWords()),
							secondSmc.getTargetWords())
					);

			return new MultihopTranslationResult(intermResults.toArray(new String[]{}), r);
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			warning("process failed.", e);
			throw e;
		} catch(ServiceLoadingFailedException e){
			throw new ServiceNotFoundException(e.getServiceId());
		} catch(Throwable t){
			warning("unknown error occurred.", t);
			throw new ProcessFailedException(t);
		}
	}

	private static Morpheme[] doMorphologicalAnalyze(String sourceLang, String source, MorphologicalAnalysisService service)
	throws LanguageNotUniquelyDecidedException, UnsupportedLanguageException, AccessLimitExceededException,
	InvalidParameterException, NoAccessPermissionException, NoValidEndpointsException, ProcessFailedException,
	ServerBusyException, ServiceNotActiveException, ServiceNotFoundException{
		Morpheme[] ret = null;
		try{
			if(service != null){
				ret = service.analyze(sourceLang, source);
			}
		} catch(ServiceNotActiveException e){
			if(!e.getServiceId().equals("AbstractService")){
			}
		} catch(Exception e){
		}
		if(ret == null){
			ret = new DefaultMorphologicalAnalysis().analyze(sourceLang, source);
		}
		return ret;
	}

	private static Collection<TranslationWithPosition> doTempDictMatch(
			String sourceLang, Morpheme[] morphs, Translation[] dict){
		Collection<TranslationWithPosition> ret = new ArrayList<TranslationWithPosition>();
		try{
			ret = new TemporalBilingualDictionaryWithLongestMatchSearch().doSearchAllLongestMatchingTerms(
				Language.parse(sourceLang), morphs, dict);
		} catch(Exception e){
		}
		return ret;
	}

	private static TranslationWithPosition[] doDictMatch(String sourceLang, String targetLang, Morpheme[] morphs,
			BilingualDictionaryWithLongestMatchSearchService service){
		TranslationWithPosition[] ret = new TranslationWithPosition[]{};
		try{
			if(service != null){
				ret = service.searchLongestMatchingTerms(sourceLang, targetLang, morphs);
				ret = CompositeTranslationUtil.dropInvalidEntries(ret, morphs);
			}
		} catch(Exception e){
		}
		return ret;
	}

	private <T> T getService(String name, Class<T> clazz) throws ProcessFailedException{
		T s = getComponentServiceFactory().getService(name, clazz);
		if(s == null){
			throw new ProcessFailedException(name + " is not binded.");
		}
		return s;
	}

	private Translation[] makeIndexedDict(Translation[] dict){
		int n = dict.length;
		Translation[] ret = new Translation[n];
		for(int i = 0; i < n; i++){
			ret[i] = new Translation(dict[i].getHeadWord(), new String[]{Integer.toString(i)});
		}
		return ret;
	}

	private static String[] getTargetWordsByIndex(String[][] targets, String[] indexes){
		int n = indexes.length;
		String[] ret = new String[n];
		for(int i = 0; i < n; i++){
			int idx = Integer.parseInt(indexes[i]);
			ret[i] = idx < targets.length ? targets[idx][0] : "";
		}
		return ret;
	}

	private static String[] getTargetWordsByIndex(Translation[] translations, String[] indexes){
		int n = indexes.length;
		String[] ret = new String[n];
		for(int i = 0; i < n; i++){
			ret[i] = translations[Integer.parseInt(indexes[i])].getTargetWords()[0];
		}
		return ret;
	}

	private static String[] getIntermediateDictTargetWords(TranslationWithPosition[] dictResult){
		List<String> result = new ArrayList<String>();
		Map<Integer, TranslationWithPosition> translations = new TreeMap<Integer, TranslationWithPosition>();
		for(TranslationWithPosition t : dictResult){
			TranslationWithPosition old = translations.get(t.getStartIndex());
			if(old != null && t.getNumberOfMorphemes() > old.getNumberOfMorphemes()){
				translations.put(t.getStartIndex(), t);
			}
		}
		for(TranslationWithPosition t: translations.values()){
			result.add(t.getTranslation().getTargetWords()[0]);
		}
		return result.toArray(new String[]{});
	}

	protected ConstructSourceAndMorphemesAndCodes getConstructSourceAndMorphemesAndCodes(){
		return new ConstructSourceAndMorphemesAndCodes();
	}
	private static final TranslationWithPosition[] emptyTranslation = {};
}
