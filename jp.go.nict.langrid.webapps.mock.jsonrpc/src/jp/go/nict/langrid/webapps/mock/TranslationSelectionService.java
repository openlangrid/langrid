/*
 * $Id: TranslationSelectionService.java 29669 2012-07-25 10:08:51Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or (at 
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
package jp.go.nict.langrid.webapps.mock;

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.translationselection.EstimationResult;
import jp.go.nict.langrid.service_1_2.translationselection.SelectionResult;

/**
 * @author $Author: nakaguchi $
 * @version $Revision: 29669 $
 */
public class TranslationSelectionService
implements jp.go.nict.langrid.service_1_2.translationselection.TranslationSelectionService{
	@Override
	public SelectionResult select(String sourceLang, String targetLang,
			String source) throws ProcessFailedException,
			LanguagePairNotUniquelyDecidedException,
			UnsupportedLanguagePairException, InvalidParameterException {
		return new SelectionResult(new EstimationResult[]{new EstimationResult(source, 1.0)}, 0);
	}
}
