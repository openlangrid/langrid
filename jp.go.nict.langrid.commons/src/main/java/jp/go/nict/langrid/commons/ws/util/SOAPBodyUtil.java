/*
 * $Id: SOAPBodyUtil.java 1122 2014-02-07 00:33:01Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.ws.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import jp.go.nict.langrid.commons.util.Pair;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1122 $
 */
public class SOAPBodyUtil {
	/**
	 * 
	 * 
	 */
	public static Pair<String, String> extractSoapFaultString(InputStream inputstream)
	throws IOException
	{
		@SuppressWarnings("resource")
		Scanner s = new Scanner(inputstream, "UTF-8").useDelimiter("");
		String first = null;
		try{
			MatchResult r = s.skip(faultCodePattern).match();
			if(r.groupCount() > 0){
				first = r.group(1);
			}
		} catch(IllegalStateException e){
		} catch(NoSuchElementException e){
		}
		String second = null;
		try{
			MatchResult r = s.skip(faultStringPattern).match();
			if(r.groupCount() > 0){
				second = r.group(1);
			}
		} catch(IllegalStateException e){
		} catch(NoSuchElementException e){
		}
		return Pair.create(first, second);
	}

	private static final Pattern faultCodePattern;
	private static final Pattern faultStringPattern;
	static{
		faultCodePattern = Pattern.compile(
				".*\\<faultcode.*?\\>(.*?)\\<\\/faultcode\\>"
				, Pattern.DOTALL);
		faultStringPattern = Pattern.compile(
				".*\\<faultstring.*?\\>(.*?)\\<\\/faultstring\\>"
				, Pattern.DOTALL);
	}
}
