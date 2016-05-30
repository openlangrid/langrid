/*
 * $Id: LanguagePathArrayConverter.java 318 2010-12-03 03:10:29Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.data.langrid.converter;

import jp.go.nict.langrid.language.InvalidLanguagePathException;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.language.util.LanguagePathUtil;

import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;

public class LanguagePathArrayConverter implements Converter {
	static private Logger logger = Logger.getLogger(LanguagePathArrayConverter.class);
	
	public Object convert(Class type, Object value) {
		logger.debug("type:" + type + " value:" + value);
		if (value == null) {
			logger.error("value is null");
			return null;
		}
		if (String.class.isAssignableFrom(type)) {
			if (LanguagePath[].class.isInstance(value)) {
				return LanguagePathUtil.encodeLanguagePathArray((LanguagePath[])value);
			}
			return value.toString();
		} else if (LanguagePath[].class.equals(type) && String.class.isInstance(value)) {
			try {
				return LanguagePathUtil.decodeLanguagePathArray((String)value);
			} catch (InvalidLanguageTagException e) {
				logger.error(e.getMessage());
				return null;
			} catch (InvalidLanguagePathException e) {
				logger.error(e.getMessage());
				return null;
			}
		}
		logger.error("invalid type: type->" + type + " value->" + value);
		return null;
	}

}
