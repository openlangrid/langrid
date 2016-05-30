/*
 * $Id: ExceptionConverter.java 369 2011-08-19 09:35:21Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.error;

import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.ws_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.ws_1_2.InvalidParameterException;
import jp.go.nict.langrid.ws_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.ws_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.ws_1_2.LanguagePathNotUniquelyDecidedException;
import jp.go.nict.langrid.ws_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.ws_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.ws_1_2.ProcessFailedException;
import jp.go.nict.langrid.ws_1_2.ServerBusyException;
import jp.go.nict.langrid.ws_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.ws_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.ws_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.ws_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.ws_1_2.UnsupportedLanguagePathException;
import jp.go.nict.langrid.ws_1_2.UnsupportedMatchingMethodException;


import org.apache.axis.AxisFault;

/**
 * 
 * 
 * @author  $Author: t-nakaguchi $
 * @version  $Revision: 369 $
 */
public class ExceptionConverter {
	/**
	 * 
	 * 
	 */
	public static LangridException convert(
		InvocationTargetException exception
		, URL serviceUrl
		, String operationName
		, Object... parameters
		)
	{
		Throwable cause = exception.getCause();
		if(cause != null){
			return convert(cause, serviceUrl
					, operationName, parameters
					);
		}

		return new LangridException(
				exception
				, serviceUrl
				, operationName
				, parameters
				, LangridError.E000
				);
	}

	/**
	 * 
	 * 
	 */
	public static LangridException convertToE000(
			Throwable exception
			, URL serviceUrl
			, String operationName
			, Object... parameters
			){
		return new LangridException(
				exception
				, serviceUrl
				, operationName
				, parameters
				, LangridError.E000
				);
	}

	/**
	 * 
	 * 
	 */
	public static LangridException convertToE000(
			String message
			, URL serviceUrl
			, String operationName
			, Object... parameters
			){
		return new LangridException(
				message
				, serviceUrl
				, operationName
				, parameters
				, LangridError.E000
				);
	}

	/**
	 * 
	 * 
	 */
	static LangridException convert(
		Throwable exception
		, URL serviceUrl
		, String operationName
		, Object... parameters
		)
	{
		if(exception instanceof jp.go.nict.langrid.ws_1_2.LangridException){
			return convert(
					(jp.go.nict.langrid.ws_1_2.LangridException)exception
					, serviceUrl
					, operationName
					, parameters
					);
		} else if(exception instanceof RemoteException){
			return convert(
					(RemoteException)exception, serviceUrl
					, operationName, parameters
					);
		} else if(exception instanceof InvocationTargetException){
			return convert(
					(InvocationTargetException)exception, serviceUrl
					, operationName, parameters
					);
		}

		return new LangridException(
				exception
				, serviceUrl
				, operationName
				, parameters
				, LangridError.E000
				);
	}

	/**
	 * 
	 * 
	 */
	private static LangridException convert(
		jp.go.nict.langrid.ws_1_2.LangridException exception
		, URL serviceUrl
		, String operationName
		, Object... parameters
		)
	{
		LangridError error = errors.get(exception.getClass());
		if(error == null){
			error = LangridError.E000;
		}
		return new LangridException(
			exception
			, serviceUrl
			, operationName
			, parameters
			, error
			);
	}

	/**
	 * 
	 * 
	 */
	private static LangridException convert(
		RemoteException exception
		, URL serviceUrl
		, String operationName
		, Object... parameters
		)
	{
		if(exception instanceof jp.go.nict.langrid.ws_1_2.LangridException){
			return convert(
					(jp.go.nict.langrid.ws_1_2.LangridException)exception
					, serviceUrl
					, operationName
					, parameters
					);
		} else if(exception instanceof AxisFault){
			AxisFault af = (AxisFault)exception;
			String fs = af.getFaultString();
			LangridError error = LangridError.E050;
			if(fs.equals("(401)Unauthorized") || fs.equals("(403)Forbidden")){
				error = LangridError.E002;
			} else if(fs.matches("\\(4\\d\\d\\)[\\w ]+")){
				error = LangridError.E001;
			} else if(fs.startsWith("(503)") || fs.startsWith("(504)")){
				error = LangridError.E063;
			} else if(fs.matches("\\(5\\d\\d\\)[\\w ]+")){
				error = LangridError.E050;
			} else if(af.detail instanceof SocketException){
				error = LangridError.E001;
			}
			return new LangridException(
					exception, serviceUrl, operationName
					, parameters, error 
					);
		}
		return new LangridException(
			exception
			, serviceUrl
			, operationName
			, parameters
			, LangridError.E000
			);
	}

	public static void addErrorMapping(
			Class<? extends jp.go.nict.langrid.ws_1_2.LangridException> clazz
			, LangridError error){
		errors.put(clazz, error);
	}
	protected static Map<Class<? extends jp.go.nict.langrid.ws_1_2.LangridException>, LangridError> errors
		= new HashMap<Class<? extends jp.go.nict.langrid.ws_1_2.LangridException>, LangridError>();
	static{
		errors.put(InvalidParameterException.class, LangridError.E052);
		errors.put(LanguageNotUniquelyDecidedException.class, LangridError.E053);
		errors.put(UnsupportedLanguageException.class, LangridError.E054);
		errors.put(LanguagePairNotUniquelyDecidedException.class, LangridError.E055);
		errors.put(UnsupportedLanguagePairException.class, LangridError.E056);
		errors.put(LanguagePathNotUniquelyDecidedException.class, LangridError.E057);
		errors.put(UnsupportedLanguagePathException.class, LangridError.E058);
		errors.put(NoAccessPermissionException.class, LangridError.E059);
		errors.put(ServiceNotActiveException.class, LangridError.E060);
		errors.put(AccessLimitExceededException.class, LangridError.E061);
		errors.put(UnsupportedMatchingMethodException.class, LangridError.E062);
		errors.put(ServerBusyException.class, LangridError.E063);
		errors.put(ServiceNotFoundException.class, LangridError.E150);
		errors.put(ProcessFailedException.class, LangridError.E450);
		errors.put(NoValidEndpointsException.class, LangridError.E451);
	}
}
