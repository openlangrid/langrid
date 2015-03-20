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
package jp.go.nict.langrid.composite.backtranslation;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
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
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.servicecontainer.service.composite.Invocation;
import jp.go.nict.langrid.servicecontainer.service.composite.ServiceLoadingFailedException;

/**
 * BackTranslation composite service implementation.
 * @author Takao Nakaguchi
 */
public class BackTranslation
extends AbstractCompositeService
implements BackTranslationService{
	public static class Components{
		@Invocation(name="ForwardTranslationPL", required=true)
		private TranslationService ft;
		@Invocation(name="BackwardTranslationPL", required=true)
		private TranslationService bt;
	}

	public BackTranslation() {
		super(Components.class);
	}

	@Override
	public BackTranslationResult backTranslate(String sourceLang, String intermediateLang, String source)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ServiceNotActiveException
		, ProcessFailedException, ServerBusyException
		, ServiceNotFoundException, UnsupportedLanguagePairException
	{
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("intermediateLang", intermediateLang)
				).notNull().trim().notEmpty().getLanguagePair();
		String src = new StringValidator("source", source)
				.notNull().trim().notEmpty().getValue();
		String sl = pair.getSource().getCode();
		String tl = pair.getTarget().getCode();

		try{
			Components c = loadServices();
			String fwTranslationResult = c.ft.translate(sl, tl, src);
			String bwTranslationResult = c.bt.translate(tl, sl, fwTranslationResult);
			return new BackTranslationResult(fwTranslationResult, bwTranslationResult);
		} catch(InvalidParameterException e){
			throw e;
		} catch(ProcessFailedException e){
			throw e;
		} catch(ServiceLoadingFailedException e){
			throw new ServiceNotFoundException(e.getServiceId());
		} catch(Throwable th){
			severe("unknown error occurred.", th);
			throw new ProcessFailedException(
					ExceptionUtil.getMessageWithStackTrace(th)
					);
		}
	}
}
