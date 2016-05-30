/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.servicecontainer.service.composite.Invocation;
import jp.go.nict.langrid.servicecontainer.service.composite.ServiceLoadingFailedException;

/**
 * @author Takao Nakaguchi
 */
public class CompositeBackTranslationService
extends AbstractCompositeService
implements jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService{
	public static class Services{
		@Invocation(name="ForwardTranslationPL", required=true)
		public jp.go.nict.langrid.service_1_2.translation.TranslationService forwardTrans;
		@Invocation(name="BackwardTranslationPL", required=true)
		public jp.go.nict.langrid.service_1_2.translation.TranslationService backwardTrans;
	}
	public CompositeBackTranslationService() {
		super(Services.class);
	}
	@Override
	public BackTranslationResult backTranslate(String sourceLang,
			String intermediateLang, String source)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		try {
			Services s = loadServices();
			String f = s.forwardTrans.translate(sourceLang, intermediateLang, source);
			return new BackTranslationResult(
					f, s.backwardTrans.translate(intermediateLang, sourceLang, f)
					);
		} catch (ServiceLoadingFailedException e) {
			throw new ProcessFailedException(e);
		}
	}
}
