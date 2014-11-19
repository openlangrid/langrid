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

import jp.go.nict.langrid.commons.net.proxy.pac.PacUtil;
import jp.go.nict.langrid.composite.commons.test.ComponentServiceFactoryImpl;
import jp.go.nict.langrid.composite.commons.test.SimilarityCalculationClientAdapter;
import jp.go.nict.langrid.composite.commons.test.TranslationClientAdapter;
import jp.go.nict.langrid.service_1_2.similaritycalculation.SimilarityCalculationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import junit.framework.Assert;

import org.junit.Test;


public class QualityEstimationBasedOnBackTranslationTest {

	@Test
	public void testEstimate() throws Exception {
		QualityEstimationBasedOnBackTranslation estimation =
			new QualityEstimationBasedOnBackTranslation();

		estimation.setComponentServiceFactory(
				new ComponentServiceFactoryImpl()
				.add("TranslationPL", new MockTranslation("This is a sample sentence."))
				.add("SimilarityCalculationPL", new MockSimilarityCalculation())
		);

		double result = estimation.estimate("ja", "en", "これはサンプルの文です．", "This is a sample sentence.");
		Assert.assertEquals(0.0, result);
	}

	@Test
	public void testEstimateJServerBLEU() throws Exception {
		QualityEstimationBasedOnBackTranslation estimation =
			new QualityEstimationBasedOnBackTranslation();

		estimation.setComponentServiceFactory(
				new ComponentServiceFactoryImpl()
				.add("TranslationPL", new TranslationClientAdapter("KyotoUJServer"))
				.add("SimilarityCalculationPL", new SimilarityCalculationClientAdapter("BLEU"))
		);

		double result = estimation.estimate("ja", "en", "これはサンプルの文です．", "This is a sample sentence.");
		Assert.assertTrue(result > 0.0);
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

	static class MockSimilarityCalculation implements SimilarityCalculationService{

		@Override
		public double calculate(String language, String text1, String text2) {
			if(text1.equals(text2)) return 1.0;

			return 0.0;
		}
	}

	static{
		PacUtil.setupDefaultProxySelector();
	}
}
