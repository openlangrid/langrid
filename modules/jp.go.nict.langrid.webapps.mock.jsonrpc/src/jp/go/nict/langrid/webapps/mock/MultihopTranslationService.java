/*
 * $Id: MultihopTranslationService.java 15143 2011-10-05 01:34:28Z Takao Nakaguchi $
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

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePathNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePathException;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationResult;

/**
 * テスト用のマルチホップ翻訳実装を提供する。
 * メソッドの戻り値はパラメータをあらわす文字列。
 * @author $Author: Takao Nakaguchi $
 * @version $Revision: 15143 $
 */
public class MultihopTranslationService
implements jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationService{
	@Override
	public MultihopTranslationResult multihopTranslate(String sourceLang,
			String[] intermediateLangs, String targetLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePathNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePathException {
		return new MultihopTranslationResult(new String[]{source}, source);
	}
}
