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
package jp.go.nict.langrid.wrapper.ws_1_2.speechrecognition;

import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.speech.Speech;
import jp.go.nict.langrid.service_1_2.speech.SpeechRecognitionService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.ObjectValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public abstract class AbstractSpeechRecognitionService
extends AbstractLanguageService
implements SpeechRecognitionService{
	public AbstractSpeechRecognitionService() {
	}

	public AbstractSpeechRecognitionService(ServiceContext context){
		super(context);
	}

	@Override
	public String recognize(String language, Speech speech)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException
	, NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException, UnsupportedLanguageException
	{
		checkStartupException();
		Language l = new LanguageValidator("language", language)
			.notNull().trim().notEmpty().getUniqueLanguage(
					getSupportedLanguageCollection());
		new ObjectValidator("speech", speech).notNull();

		processStart();
		try{
			acquireSemaphore();
			try{
				return doRecognize(l, speech);
			} catch(InvalidParameterException e){
				throw e;
			} catch(ProcessFailedException e){
				throw e;
			} catch(Throwable t){
				logger.log(Level.SEVERE, "unknown error occurred.", t);
				throw new ProcessFailedException(
						ExceptionUtil.getMessageWithStackTrace(t)
						);
			} finally{
				releaseSemaphore();
			}
		} finally{
			processEnd();
		}
	}

	@Override
	public String[] getSupportedVoiceTypes()
	throws NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException, ServiceNotFoundException{
		return supportedVoiceTypes;
	}

	@Override
	public String[] getSupportedAudioTypes()
	throws NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException{
		return supportedAudioTypes;
	}

	protected abstract String doRecognize(Language language, Speech speech)
	throws InvalidParameterException, ProcessFailedException;

	protected void setSupportedVoiceTypes(String[] supportedVoiceTypes){
		this.supportedVoiceTypes = supportedVoiceTypes;
	}

	protected void setSupportedAudioTypes(String[] supportedAudioTypes){
		this.supportedAudioTypes = supportedAudioTypes;
	}

	private String[] supportedVoiceTypes;
	private String[] supportedAudioTypes;
	private static Logger logger = Logger.getLogger(AbstractSpeechRecognitionService.class.getName());
}
