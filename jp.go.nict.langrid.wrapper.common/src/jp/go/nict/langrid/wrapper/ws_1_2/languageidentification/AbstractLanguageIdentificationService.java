/*
 * $Id: AbstractLanguageIdentificationService.java 265 2010-10-03 10:25:32Z t-nakaguchi $
 *
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
package jp.go.nict.langrid.wrapper.ws_1_2.languageidentification;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.languageidentification.LanguageAndEncoding;
import jp.go.nict.langrid.service_1_2.languageidentification.LanguageIdentificationService;
import jp.go.nict.langrid.service_1_2.util.validator.ObjectValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguageService;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 265 $
 */
public abstract class AbstractLanguageIdentificationService
extends AbstractLanguageService
implements LanguageIdentificationService{
	@Override
	public LanguageAndEncoding identifyLanguageAndEncoding(byte[] textBytes)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		textBytes= (byte[])new ObjectValidator("textBytes", textBytes)
				.notNull().getValue();
		processStart();
		try{
			acquireSemaphore();
			try {
				return doIdentifyLanguageAndEncoding(textBytes);
			} finally{
				releaseSemaphore();
			}
		} finally{
			processEnd();
		}
	}

	@Override
	public String identify(String text, String originalEncoding)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, NoValidEndpointsException,
			ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException {
		text = new StringValidator("text", text).notNull().notEmpty().getValue();
		originalEncoding = new StringValidator("originalEncoding", originalEncoding)
				.notNull().getValue();
//		processStart();
		try{
//			acquireSemaphore();
			try {
				return doIdentify(text, originalEncoding);
			} finally{
//				releaseSemaphore();
			}
		} finally{
//			processEnd();
		}
	}

	@Override
	public String[] getSupportedEncodings()
	throws NoValidEndpointsException, ProcessFailedException
	, ServerBusyException, ServiceNotActiveException
	, ServiceNotFoundException {
		return supportedEncodings;
	}

	protected void setSupportedEncodings(String[] supportedEncodings){
		this.supportedEncodings = supportedEncodings;
	}

	protected abstract LanguageAndEncoding doIdentifyLanguageAndEncoding(
			byte[] textBytes)
	throws InvalidParameterException, ProcessFailedException;

	protected abstract String doIdentify(
			String text, String originalEncoding)
	throws InvalidParameterException, ProcessFailedException;

	private String[] supportedEncodings = {};
}
