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
package jp.go.nict.langrid.composite.translationselection;

import jp.go.nict.langrid.composite.commons.test.ComponentServiceFactoryImpl;
import jp.go.nict.langrid.composite.commons.test.SimilarityCalculationClientAdapter;
import jp.go.nict.langrid.composite.commons.test.TranslationClientAdapter;
import jp.go.nict.langrid.composite.qualityestimation.QualityEstimationBasedOnBackTranslation;
import jp.go.nict.langrid.service_1_2.qualityestimation.QualityEstimationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.translationselection.SelectionResult;
import junit.framework.Assert;

import org.junit.Test;


public class BestTranslationSelectionUsingQualityEstimationWithAllResultsTest {
	@Test
	public void testSelect() throws Exception {
		BestTranslationSelectionUsingQualityEstimationWithAllResults selection =
			new BestTranslationSelectionUsingQualityEstimationWithAllResults();

		selection.setComponentServiceFactory(
				new ComponentServiceFactoryImpl()
				.add("TranslationPL1", new MockTranslation("This is a sample sentence."))
				.add("TranslationPL2", new MockTranslation("This is not a sample sentence."))
				.add("TranslationPL3", new MockTranslation("These are sample sentences."))
				.add("QualityEstimationPL", new MockQualityEstimation())
		);

		SelectionResult result = selection.select("ja", "en", "これはサンプルの文ではありません。");
		Assert.assertEquals(1, result.getSelectedIndex());
	}

	@Test
	public void testJServerGoogleBLEU() throws Exception {
		BestTranslationSelectionUsingQualityEstimationWithAllResults selection =
			new BestTranslationSelectionUsingQualityEstimationWithAllResults();
		QualityEstimationBasedOnBackTranslation estimation = new QualityEstimationBasedOnBackTranslation();

		selection.setComponentServiceFactory(
				new ComponentServiceFactoryImpl()
				.add("TranslationPL1", new TranslationClientAdapter("KyotoUJServer"))
				.add("TranslationPL2", new TranslationClientAdapter("GoogleTranslate"))
				.add("QualityEstimationPL",estimation)
		);
		estimation.setComponentServiceFactory(
				new ComponentServiceFactoryImpl()
				.add("TranslationPL", new TranslationClientAdapter("KyotoUJServer"))
				.add("SimilarityCalculationPL", new SimilarityCalculationClientAdapter("BLEU"))
		);

		SelectionResult result = selection.select("ja", "en", "これはサンプルの文ではありません。");
		Assert.assertEquals(1, result.getSelectedIndex());
	}

	static class MockTranslation implements TranslationService{
		public MockTranslation(String result){
			this.result = result;
		}

		@Override
		public String translate(String sourceLang, String targetLang, String source) {
			return result;
		}
		private String result;
	}

	static class MockQualityEstimation implements QualityEstimationService
	{

		@Override
		public double estimate(String sourceLang, String targetLang,
				String source, String target) {

			return 0.0;
		}
	}
}
