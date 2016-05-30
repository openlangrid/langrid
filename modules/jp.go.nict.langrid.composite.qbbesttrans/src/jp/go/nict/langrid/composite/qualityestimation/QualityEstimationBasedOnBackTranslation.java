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
package jp.go.nict.langrid.composite.qualityestimation;

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.qualityestimation.QualityEstimationService;
import jp.go.nict.langrid.service_1_2.similaritycalculation.SimilarityCalculationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;

public class QualityEstimationBasedOnBackTranslation extends
AbstractCompositeService implements QualityEstimationService{

	@Override
	public double estimate(String sourceLang, String targetLang, String source,
			String target) throws LanguagePairNotUniquelyDecidedException,
			UnsupportedLanguagePairException, InvalidParameterException,
			ProcessFailedException {


		TranslationService translation = getComponentServiceFactory().getService(
				"TranslationPL", TranslationService.class);
		if(translation == null)  throw new ProcessFailedException("TranslationPL not bound.");
		//
		SimilarityCalculationService similarityCalculation = getComponentServiceFactory().getService(
				"SimilarityCalculationPL", SimilarityCalculationService.class);
		if(similarityCalculation == null)  throw new ProcessFailedException("SimilarityCalculationPL not bound.");

		double similarity=0;;
		try {
			String backtransResult  = translation.translate(targetLang, sourceLang, target);
			similarity = similarityCalculation.calculate(sourceLang, source, backtransResult);
		} catch(LanguagePairNotUniquelyDecidedException e){
			throw e;
		} catch(UnsupportedLanguagePairException e){
			throw e;
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e) {
			throw e;
		} catch(Throwable t){
			throw new ProcessFailedException(t);
		}

		return similarity;
	}

}
