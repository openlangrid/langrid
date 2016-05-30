/*
 * $Id: LangridException.java 199 2010-10-02 12:33:18Z t-nakaguchi $
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

import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;

/**
 * 
 * 
 * @author  $Author: t-nakaguchi $
 * @version  $Rev: 199 $
 */
public class LangridException extends Exception {
	/**
	 * 
	 * 
	 */
	public LangridException(
		Throwable cause
		, URL serviceUrl
		, String operationName
		, Object[] operationParameters
		, LangridError error
		)
	{
		super(cause);
		this.serviceUrl = serviceUrl;
		this.operationName = operationName;
		this.operationParameters = operationParameters;
		this.error = error;
	}

	/**
	 * 
	 * 
	 */
	public LangridException(
		String message
		, URL serviceUrl
		, String operationName
		, Object[] operationParameters
		, LangridError error
		)
	{
		super(message);
		this.serviceUrl = serviceUrl;
		this.operationName = operationName;
		this.operationParameters = operationParameters;
		this.error = error;
	}

	/**
	 * 
	 * 
	 */
	public LangridError getError(){
		return error;
	}

	/**
	 * 
	 * 
	 */
	public URL getServiceUrl(){
		return serviceUrl;
	}

	/**
	 * 
	 * 
	 */
	public String getOperationName(){
		return operationName;
	}

	/**
	 * 
	 * 
	 */
	public String getErrorString(){
		if(getCause() != null){
			return String.format(
				"%s: %s"
				, error.name()
				, error.getMessage(getCause())
				);
		} else{
			return String.format(
					"%s: %s"
					, error.name()
					, super.getMessage()
					);
		}
	}

	/**
	 * 
	 * 
	 */
	public String getErrorString(Locale locale){
		return String.format(
			"%s: %s"
			, error.name()
			, error.getMessage(getCause(), locale)
			);
	}

	/**
	 * 
	 * 
	 */
	public String getOperationString(){
		String url = serviceUrl.toString();
		int qi = url.indexOf('?');
		if(qi != -1){
			url = url.substring(0, qi);
		}
		StringBuilder b = new StringBuilder(String.format(
			"%s#%s(", url, operationName
			));

		boolean first = true;
		for(Object p : operationParameters){
			if(first){
				first = false;
			} else{
				b.append(", ");
			}
			if(p == null){
				b.append("null");
				continue;
			}
			if(p instanceof String){
				b.append("\"");
				b.append(p);
				b.append("\"");
			} else if(p instanceof Calendar){
				b.append("\"");
				b.append(CalendarUtil.formatToDefault((Calendar)p));
				b.append("\"");
			} else if(p.getClass().isArray()){
				b.append(ArrayUtil.toString(p));
			} else{
				b.append(p);
			}
		}
		b.append(")");

		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	@Override
	public String getMessage(){
		return getErrorString() + " [" + getOperationString() + "]";
	}

	/**
	 * 
	 * 
	 */
	public String getMessage(Locale locale){
		return getErrorString(locale) + " [" + getOperationString() + "]";
	}

	private URL serviceUrl;
	private String operationName;
	private Object[] operationParameters;
	private LangridError error;

	private static final long serialVersionUID = -2554093624509897933L;
}
