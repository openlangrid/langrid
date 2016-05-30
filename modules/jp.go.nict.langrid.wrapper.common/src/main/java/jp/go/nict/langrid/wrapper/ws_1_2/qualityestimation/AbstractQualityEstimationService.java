/*
 * $Id: AbstractQualityEstimationService.java 409 2011-08-25 03:12:59Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
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
package jp.go.nict.langrid.wrapper.ws_1_2.qualityestimation;

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.qualityestimation.QualityEstimationService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 409 $
 */
public abstract class AbstractQualityEstimationService extends AbstractLanguagePairService
implements QualityEstimationService
{
	@Override
	public double estimate(String sourceLang, String targetLang, String source, String target)
	throws LanguagePairNotUniquelyDecidedException, UnsupportedLanguagePairException
		, InvalidParameterException, ProcessFailedException
	{
		checkStartupException();
		jp.go.nict.langrid.language.LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("headLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection());
		String hw = new StringValidator("source", source)
				.notNull().trim().getValue();
		hw = new StringValidator("target", target)
		.notNull().trim().getValue();
		
		return doEstimate(sourceLang, targetLang, source, target);
	}
	
	protected abstract double doEstimate(String sourceLang, String targetLang, String source,
			String target);
}
