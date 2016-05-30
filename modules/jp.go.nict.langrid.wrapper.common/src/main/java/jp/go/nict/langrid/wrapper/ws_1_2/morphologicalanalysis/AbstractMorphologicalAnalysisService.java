/*
 * $Id: AbstractMorphologicalAnalysisService.java 409 2011-08-25 03:12:59Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.wrapper.ws_1_2.morphologicalanalysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.language.Language;
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
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.MorphologicalAnalysisService;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.typed.Morpheme;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * This is the base class for the morpheme analysis service.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 409 $
 */
public abstract class AbstractMorphologicalAnalysisService
	extends AbstractLanguageService
	implements MorphologicalAnalysisService
{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractMorphologicalAnalysisService(){
	}

	/**
	 * 
	 * Constructor that takes the set of supported languages as a parameter.
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractMorphologicalAnalysisService(Collection<Language> supportedLanguages){
		setSupportedLanguageCollection(supportedLanguages);
	}

	/**
	 * 
	 * Constructor that takes the service context and supported language pair(s) as a parameter(s).
	 * @param serviceContext Service context
	 * @param supportedLanguages Set of supported languages
	 * 
	 */
	public AbstractMorphologicalAnalysisService(
		ServiceContext serviceContext
		, Collection<Language> supportedLanguages
		)
	{
		super(serviceContext);
		setSupportedLanguageCollection(supportedLanguages);
	}

	public jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme[] analyze(
			String language, String text)
			throws AccessLimitExceededException, InvalidParameterException
			, LanguageNotUniquelyDecidedException, NoAccessPermissionException
			, NoValidEndpointsException, ProcessFailedException
			, ServerBusyException, ServiceNotActiveException
			, ServiceNotFoundException, UnsupportedLanguageException
	{
		checkStartupException();
		Language l = new LanguageValidator("language", language)
			.notNull().trim().notEmpty().getUniqueLanguage(
					getSupportedLanguageCollection());
		String txt = new StringValidator("text", text)
			.notNull().trim().notEmpty().getValue();

		processStart();
		try{
			acquireSemaphore();
			try{
				Collection<Morpheme> morphs = doAnalyze(l, txt);
				int n = Math.min(morphs.size(), getMaxResults());
				Iterator<Morpheme> it = morphs.iterator();
				ArrayList<jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme> mList =
					new ArrayList<jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme>();

				ArrayList<String> patterns = new ArrayList<String>();
				patterns.add("*$%*");
				patterns.add("*%$*");
				while(it.hasNext() && mList.size() < n){
					concatenateDelimiterReflexive(mList, patterns, it, 0);
				}
				return mList.toArray(new jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme[]{});
			} catch(InvalidParameterException e){
				throw e;
			} catch(ProcessFailedException e){
				throw e;
			} catch(Throwable t){
				logger.log(Level.SEVERE, "unknown error occurred.", t);
				if(t instanceof RuntimeException){
					logger.severe("language: " + language + ", text: " + text);
				}
				throw new ProcessFailedException(t);
			} finally{
				releaseSemaphore();
			}
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			throw e;
		} catch(Throwable t){
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			if(t instanceof RuntimeException){
				logger.severe("language: " + language + ", text: " + text);
			}
			throw new ProcessFailedException(t);
		} finally{
			processEnd();
		}
	}

	/**
	 * 
	 */
	private boolean concatenateDelimiterReflexive(
			final ArrayList<jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme> morphemeList,
			final ArrayList<String> patterns, final Iterator<Morpheme> it, final int now) {

		if(!it.hasNext()) {
			return false;
		}

		Morpheme m = it.next();

		// 条件チェックおよび、パターン候補の削減
		// patterns[0] = "abcde" , now = 3 の時に
		// m.getWord()が"c","cd","cde"のいずれかである場合はnextPatternsにpatterns[0]を追加
		ArrayList<String> nextPatterns = new ArrayList<String>();
		for(int i = 0; i < patterns.size(); i++) {
			int index = patterns.get(i).indexOf(m.getWord(), now);

			if(now == index) {
				nextPatterns.add(patterns.get(i));
			}
		}

		// どのpatternsとも一致しないパターンの場合
		if(nextPatterns.size() == 0) {
			morphemeList.add(new jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme(
					m.getWord()
					, m.getLemma()
					, m.getPartOfSpeech().getExpression()
					));
			return false;
		}

		int nextNow = now + m.getWord().length();

		// patternsのいずれかに完全一致するパターンが現れた場合
		for(int i = 0; i < nextPatterns.size(); i++) {
			if(nextNow == nextPatterns.get(i).length()) {
				morphemeList.add(new jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme(
						nextPatterns.get(i)
						, m.getLemma()
						, m.getPartOfSpeech().getExpression()
						));
				return true;
			}
		}

		boolean ret = concatenateDelimiterReflexive(morphemeList, nextPatterns, it, nextNow);

		// 条件に合致していない場合(falseが返る)、イテレータから得た情報をそのままリストに追加する
		if(!ret) {
			morphemeList.add(new jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme(
					m.getWord()
					, m.getLemma()
					, m.getPartOfSpeech().getExpression()
					));
		}

		// patternsのいずれかに完全一致するパターンが現れた場合
		for(int i = 0; i < nextPatterns.size(); i++) {
			if(nextNow == nextPatterns.get(i).length()) {
				morphemeList.add(new jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme(
						nextPatterns.get(i)
						, m.getLemma()
						, m.getPartOfSpeech().getExpression()
						));
				return true;
			}
		}

		return ret;
	}

	/**
	 * 
	 * Execute morphological analysis process.
	 * @param language Language
	 * @param text Sentence to analyze
	 * @return Analysis results
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Process failed
	 * 
	 */
	protected abstract Collection<Morpheme> doAnalyze(
			Language language, String text
			)
	throws InvalidParameterException, ProcessFailedException;

	private static Logger logger = Logger.getLogger(
			AbstractMorphologicalAnalysisService.class.getName()
			);
}
