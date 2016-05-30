/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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

import java.util.Arrays;

import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.translation.AsyncTranslationResult;

/**
 * @author Takao Nakaguchi
 */
public class AsyncTranslationService
implements jp.go.nict.langrid.service_1_2.translation.AsyncTranslationService{
	@Override
	public String startTranslation(String sourceLang, String targetLang,
			String[] sources) throws InvalidParameterException,
			ProcessFailedException {
		return sourceLang + ":" + targetLang + ":" + Arrays.toString(sources);
	}

	@Override
	public AsyncTranslationResult getCurrentResult(String token)
			throws InvalidParameterException, ProcessFailedException {
		return new AsyncTranslationResult(true, new String[]{token});
	}

	@Override
	public void terminate(String token) throws InvalidParameterException,
			ProcessFailedException {
	}
}
