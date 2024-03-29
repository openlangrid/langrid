/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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

import static jp.go.nict.langrid.service_1_2.typed.MatchingMethod.PREFIX;

import java.util.ArrayList;
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
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;
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
 * Translation combined with bilingual dictionary service implementation.
 * @author Takao Nakaguchi
 */
public class TranslationCombinedWithBilingualDictionary
extends AbstractCompositeService
implements TranslationWithTemporalDictionaryService{
	public static class Services{
		@Invocation(name="BilingualDictionaryPL")
		private BilingualDictionaryService bdict;
		@Invocation(name="MorphologicalAnalysisPL")
		private MorphologicalAnalysisService morph;
		@Invocation(name="TranslationPL")
		private TranslationService trans;
	}

	public TranslationCombinedWithBilingualDictionary() {
		super(Services.class);
	}

	@Override
	public String translate(String sourceLang, String targetLang,
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
				, new LanguageValidator("targetLang", targetLang)
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
			Services s = loadServices();
			TemporalBilingualDictionaryWithLongestMatchSearch tempDictMatch = new TemporalBilingualDictionaryWithLongestMatchSearch();
			ConstructSourceAndMorphemesAndCodes csmc = new ConstructSourceAndMorphemesAndCodes();
			GetLongestMatchingTerm glmt = new GetLongestMatchingTerm();
			ReplacementTerm rt = new ReplacementTerm();
	
			Morpheme[] analyzeResult = null;
			try{
				if(s.morph != null){
					analyzeResult = s.morph.analyze(sl, src);
				}
			} catch(ServiceNotActiveException e){
				log("service is not active: " + e.getServiceId());
			} catch(Exception e){
				log("exception occurred: " + ExceptionUtil.getMessageWithStackTrace(e));
			}
			if(analyzeResult == null || analyzeResult.length == 0){
				analyzeResult = new DefaultMorphologicalAnalysis().analyze(sl, src);
			}

			Collection<TranslationWithPosition> tempDictResult = new ArrayList<TranslationWithPosition>();
			try{
				tempDictResult = tempDictMatch.doSearchAllLongestMatchingTerms(
					pair.getSource(), analyzeResult, tempDict);
			} catch(Exception e){
				log("exception occurred: " + ExceptionUtil.getMessageWithStackTrace(e));
			}

			SourceAndMorphemesAndCodes firstSmc = csmc.doConstructSMC(
					sl, analyzeResult, tempDictResult.toArray(emptyTranslation));

			TranslationWithPosition[] dictResult = new TranslationWithPosition[]{};
			if(s.bdict != null){
				List<TranslationWithPosition> bdictResult = new ArrayList<TranslationWithPosition>();
				try{
					int n = firstSmc.getMorphemes().length;
					for(int i = 0; i < n; i++){
						Morpheme m = firstSmc.getMorphemes()[i];
						log("invoke BilingualDictionary.search(" + m.getWord() + "morphs)");
						Translation[] ret = s.bdict.search(
								sl, dl, m.getWord(), PREFIX.name()
								);
						Collection<TranslationWithPosition> glmret = glmt.doGetLongestMatchingTerm(
								pair.getSource(), firstSmc.getMorphemes(), i, ret);
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

			SourceAndMorphemesAndCodes secondSmc = csmc.doConstructSMC(sl, firstSmc.getMorphemes(), dictResult);

			SourceAndMorphemesAndCodes joinedSmc = new SourceAndMorphemesAndCodes(
					secondSmc.getSource()
					, secondSmc.getMorphemes()
					, ArrayUtil.append(firstSmc.getCodes(), secondSmc.getCodes())
					, ArrayUtil.append(firstSmc.getHeadWords(), secondSmc.getHeadWords())
					, ArrayUtil.append(firstSmc.getTargetWords(), secondSmc.getTargetWords())
			);

			String fwTranslationResult = null;
			boolean doParaphrase = false;
			if(s.trans == null){
				doParaphrase = true;
			} else{
				try{
					fwTranslationResult = s.trans.translate(sl, tl, joinedSmc.getSource());
				} catch(ServiceNotActiveException e){
					if(e.getServiceId().equals("AbstractService") && sl.equals(tl)){
						doParaphrase = true;
					}
				}
			}
			if(doParaphrase){
				fwTranslationResult = joinedSmc.getSource();
			}
			String fwFinalResult = rt.doReplace(tl, fwTranslationResult
					, joinedSmc.getCodes(), joinedSmc.getTargetWords());

			return fwFinalResult;
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

	private static final TranslationWithPosition[] emptyTranslation = {};
}
