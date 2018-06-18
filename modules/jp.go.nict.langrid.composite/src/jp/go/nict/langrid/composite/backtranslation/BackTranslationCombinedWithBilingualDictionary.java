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
package jp.go.nict.langrid.composite.backtranslation;

import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.PREFIX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.service_1_2.workflowsupport.SourceAndMorphemesAndCodes;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.servicecontainer.service.composite.Invocation;
import jp.go.nict.langrid.servicecontainer.service.composite.ServiceLoadingFailedException;
import jp.go.nict.langrid.wrapper.workflowsupport.ConstructSourceAndMorphemesAndCodes;
import jp.go.nict.langrid.wrapper.workflowsupport.DefaultMorphologicalAnalysis;
import jp.go.nict.langrid.wrapper.workflowsupport.GetLongestMatchingTerm;
import jp.go.nict.langrid.wrapper.workflowsupport.ReplacementTerm;
import jp.go.nict.langrid.wrapper.workflowsupport.TemporalBilingualDictionaryWithLongestMatchSearch;

/**
 * Back translation combined with bilingual dictionary service implementation.
 * @author Takao Nakaguchi
 */
public class BackTranslationCombinedWithBilingualDictionary
extends AbstractCompositeService
implements BackTranslationWithTemporalDictionaryService{
	public static class Components{
		@Invocation(name="BilingualDictionaryPL")
		private BilingualDictionaryService bdict;
		@Invocation(name="MorphologicalAnalysisPL")
		private MorphologicalAnalysisService morph;
		@Invocation(name="ForwardTranslationPL", required=true)
		private TranslationService ft;
		@Invocation(name="BackwardTranslationPL", required=true)
		private TranslationService bt;
	}

	public BackTranslationCombinedWithBilingualDictionary() {
		super(Components.class);
	}

	@Override
	public BackTranslationResult backTranslate(String sourceLang, String intermediateLang,
			String source, Translation[] temporalDict,
			String dictTargetLang)
	throws AccessLimitExceededException,
	InvalidParameterException, LanguagePairNotUniquelyDecidedException,
	NoAccessPermissionException, ProcessFailedException,
	NoValidEndpointsException, ServerBusyException,
	ServiceNotActiveException, ServiceNotFoundException,
	UnsupportedLanguagePairException
	{
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("intermediateLang", intermediateLang)
				).notNull().trim().notEmpty().getLanguagePair();
		String src = new StringValidator("source", source)
				.notNull().trim().notEmpty().getValue();
		Translation[] tempDict = temporalDict;
		if(tempDict == null){
			tempDict = new Translation[]{};
		}
		Language dictLang = null;
		if(dictTargetLang != null){
			dictLang = new LanguageValidator("dictTargetLang", dictTargetLang)
					.getLanguage();
		} else{
			dictLang = pair.getTarget();
		}
		String sl = pair.getSource().getCode();
		String tl = pair.getTarget().getCode();
		String dl = dictLang.getCode();

		try{
			Components s = loadServices();
			TemporalBilingualDictionaryWithLongestMatchSearch tempDictMatch = new TemporalBilingualDictionaryWithLongestMatchSearch();
			ConstructSourceAndMorphemesAndCodes csmc = new ConstructSourceAndMorphemesAndCodes();
			GetLongestMatchingTerm glmt = new GetLongestMatchingTerm();
			ReplacementTerm rt = new ReplacementTerm();
	
			Morpheme[] analyzeResult = null;
			{
				log("invoke MorphologicalAnalysis.analyze");
				try{
					if(s.morph != null){
						analyzeResult = s.morph.analyze(sl, src);
					}
				} catch(ServiceNotActiveException e){
					log("service is not active: " + e.getServiceId());
				} catch(Exception e){
					log("exception occurred: " + ExceptionUtil.getMessageWithStackTrace(e));
				}
				if(analyzeResult == null){
					log("analyze result is null. calling DefaultMorphologicalAnalysis.");
					analyzeResult = new DefaultMorphologicalAnalysis().analyze(sl, src);
				}
				log("invoke MorphologicalAnalysis.analyze done(" + analyzeResult.length + "morphs)");
			}

			Collection<TranslationWithPosition> tempDictResult = new ArrayList<TranslationWithPosition>();
			{
				log("invoke TemporalBilingualDictionaryWithLongestMatchSearch.doSearchAllLongestMatchingTerms("
						+ analyzeResult.length + "morphes["
						+ (analyzeResult.length < 10
								? Arrays.toString(analyzeResult)
								: analyzeResult[0])
						+ "], "
						+ tempDict.length + "translations["
						+ (tempDict.length < 10
								? Arrays.toString(tempDict)
								: tempDict[0])
						+ "])");
				try{
					tempDictResult = tempDictMatch.doSearchAllLongestMatchingTerms(
						pair.getSource(), analyzeResult, tempDict);
				} catch(Exception e){
					log("exception occurred: " + ExceptionUtil.getMessageWithStackTrace(e));
				}
				log("invoke TemporalBilingualDictionaryWithLongestMatchSearch.doSearchAllLongestMatchingTerms done("
						+ tempDictResult.size() + "translations)");
			}
			SourceAndMorphemesAndCodes firstSmc = null;
			{
				log("prepare for ConstructSourceAndMorphemesAndCodes.doConstructSMC()");
				TranslationWithPosition[] bdictResult = tempDictResult.toArray(emptyTranslation);
				log("invoke ConstructSourceAndMorphemesAndCodes.doConstructSMC("
						+ analyzeResult.length + "morphs,"
						+ ((tempDict != null) ? tempDict.length : "null ") + "translations)");
				firstSmc = csmc.doConstructSMC(sl, analyzeResult, bdictResult);
				log("invoke ConstructSourceAndMorphemesAndCodes.doConstructSMC done(" +
						+ firstSmc.getMorphemes().length + "morphs," +
						+ firstSmc.getCodes().length + "codes,"
						+ firstSmc.getTargetWords().length + "targetWords)");
			}

			TranslationWithPosition[] dictResult = new TranslationWithPosition[]{};
			{
				int n = firstSmc.getMorphemes().length;
				List<TranslationWithPosition> bdictResult = new ArrayList<TranslationWithPosition>();
				log("morpheme scan loop start.");
				if(s.bdict != null){
					try{
						for(int i = 0; i < n; i++){
							Morpheme m = firstSmc.getMorphemes()[0];
							log("invoke BilingualDictionary.search(" + m.getWord() + "morphs)");
							Translation[] ret = s.bdict.search(
									sl, dl, m.getWord(), PREFIX.name()
									);
							log("invoke BilingualDictionary.search done(" +
									ret.length + "translations)");
							log("invoke GetLongestMatchingTerm.getLongestMatchingTerm(" +
									"sourceLang:" + sl
									+ ",morphemes.length:" + firstSmc.getMorphemes().length
									+ ",startIndex:" + i
									+ ",translations.length:" + ret.length
									+ ")");
							Collection<TranslationWithPosition> glmret = glmt.doGetLongestMatchingTerm(
									pair.getSource(), firstSmc.getMorphemes(), i, ret);
							log("invoke GetLongestMatchingTerm.getLongestMatchingTerm done (" +
									glmret.size() + "translations)");
							if(glmret.size() > 0){
								TranslationWithPosition r = glmret.iterator().next();
								bdictResult.add(r);
								if(r.getNumberOfMorphemes() > 0){
									i += r.getNumberOfMorphemes() - 1;
								}
								log(r.getNumberOfMorphemes() + "morphemes consumed.");
							}
						}
						dictResult = bdictResult.toArray(new TranslationWithPosition[]{});
					} catch(ServiceNotActiveException e){
						log("service is not active: " + e.getServiceId());
					} catch(Exception e){
						log("exception occurred: " + ExceptionUtil.getMessageWithStackTrace(e));
					}
				}
				log("morpheme scan loop end.");
			}

			SourceAndMorphemesAndCodes secondSmc = null;
			{
				log("invoke ConstructSourceAndMorphemesAndCodes.doConstructSMC("
						+ firstSmc.getMorphemes().length + "morphs," + dictResult.length + "translations)"
						);
				secondSmc = csmc.doConstructSMC(sl, firstSmc.getMorphemes(), dictResult);
				log("invoke ConstructSourceAndMorphemesAndCodes.doConstructSMC done(" +
						+ secondSmc.getMorphemes().length + "morphs," +
						+ secondSmc.getCodes().length + "codes,"
						+ secondSmc.getTargetWords().length + "targetWords)"
						);
			}

			log("joining mrophemes and codes");
			SourceAndMorphemesAndCodes joinedSmc = new SourceAndMorphemesAndCodes(
					secondSmc.getSource()
					, secondSmc.getMorphemes()
					, ArrayUtil.append(firstSmc.getCodes(), secondSmc.getCodes())
					, ArrayUtil.append(firstSmc.getHeadWords(), secondSmc.getHeadWords())
					, ArrayUtil.append(firstSmc.getTargetWords(), secondSmc.getTargetWords())
			);
			log("joining mrophemes and codes done(" +
					+ joinedSmc.getMorphemes().length + "morphs," +
					+ joinedSmc.getCodes().length + "codes,"
					+ joinedSmc.getTargetWords().length + "targetWords)");

			log("invoke TranslationService.translate(" + joinedSmc.getSource().length() + "chars)");
			String fwTranslationResult = s.ft.translate(
							sl, tl, joinedSmc.getSource()
							);
			log("invoke TranslationService.translate done");
			log("replacing codes(" + joinedSmc.getCodes().length + "codes" +
					"," + joinedSmc.getTargetWords().length + "words)");
			String fwFinalResult = rt.doReplace(tl, fwTranslationResult
					, joinedSmc.getCodes(), joinedSmc.getTargetWords());
			log("replacing codes done");

			log("invoke TranslationService.translate(" + fwTranslationResult.length() + "chars)");
			String bwTranslationResult = s.bt.translate(
							tl, sl, fwTranslationResult
							);
			log("invoke TranslationService.translate done");

			String bwFinalResult = null;
			{
				log("prepare for replacing codes()");
				List<String> headWordList = new ArrayList<String>();
				for(TranslationWithPosition t : tempDictResult){
					headWordList.add(t.getTranslation().getHeadWord());
				}
				for(TranslationWithPosition t : dictResult){
					headWordList.add(t.getTranslation().getHeadWord());
				}
				String[] headWords = headWordList.toArray(emptyStringArray);
				log("replacing codes(" + joinedSmc.getCodes().length + "codes" +
						"," + headWords.length + "words)");
				bwFinalResult = rt.doReplace(sl, bwTranslationResult
						, joinedSmc.getCodes(), headWords);
				log("replacing codes done");
			}

			return new BackTranslationResult(fwFinalResult, bwFinalResult);
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			warning("process failed.", e);
			throw e;
		} catch(ServiceLoadingFailedException e){
			throw new ServiceNotFoundException(e.getServiceId());
		} catch(Throwable t){
			severe("unknown error occurred.", t);
			throw new ProcessFailedException(t);
		}
	}

	private static final String[] emptyStringArray = {};
	private static final TranslationWithPosition[] emptyTranslation = {};
}
