/*
 * $Id: AbstractTextToSpeechService.java 308 2010-12-01 06:12:19Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.wrapper.ws_1_2.texttospeech;

import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
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
import jp.go.nict.langrid.service_1_2.speech.TextToSpeechService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 308 $
 */
public abstract class AbstractTextToSpeechService
extends AbstractLanguageService
implements TextToSpeechService
{
	/**
	 * 
	 * 
	 */
	public AbstractTextToSpeechService() {
	}

	@Override
	public Speech speak(
			String language, String text, String voiceType, String audioType)
	throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguageException
	{
		checkStartupException();
		Language l = new LanguageValidator("language", language)
			.notNull().trim().notEmpty().getUniqueLanguage(
				getSupportedLanguageCollection());
		String txt = new StringValidator("text", text)
			.notNull().trim().notEmpty().getValue();
		String vt = new StringValidator("voiceType", voiceType)
			.notNull().trim().getValue();
		String at = new StringValidator("audioType", audioType)
			.notNull().trim().getValue();

		acquireSemaphore();
		try {
			return doSpeak(l, txt, vt, at);
		} catch(Throwable t) {
			logger.log(Level.SEVERE, "unknown error occurred.", t);
			throw new ProcessFailedException(
					ExceptionUtil.getMessageWithStackTrace(t)
					);
		} finally{
			releaseSemaphore();
		}
	}

	public String[] getSupportedVoiceTypes() {
		return supportedVoiceTypes;
	}

	public String[] getSupportedAudioTypes() {
		return supportedAudioTypes;
	}

	protected abstract Speech doSpeak(
			Language language, String text, String voiceType, String audioType)
	throws InvalidParameterException, ProcessFailedException;

	protected void setSupportedVoiceTypes(String... voiceTypes){
		this.supportedVoiceTypes = voiceTypes;
	}

	protected void setSupportedAudioTypes(String... audioTypes){
		this.supportedAudioTypes = audioTypes;
	}

	private String[] supportedVoiceTypes = new String[]{};
	private String[] supportedAudioTypes = new String[]{};

	private static Logger logger = Logger.getLogger(
			AbstractTextToSpeechService.class.getName()
			);
}
