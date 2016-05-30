/*
 * $Id: ExceptionUtil.java 182 2010-10-02 03:16:36Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.lang;

import java.io.IOException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class ExceptionUtil {
	/**
	 * 
	 * 
	 */
	public static String getMessageWithStackTrace(Throwable e){
		return getMessageWithStackTrace(e, 3, 9);
	}

	/**
	 * 
	 * 
	 */
	public static String getMessageWithStackTrace(Throwable e, int maxStackDepth, int maxTotalStackDepth){
		StringBuilder b = new StringBuilder();
		try{
			appendMessage(e, b);
			b.append(". stack trace\"\"\"\n");
			appendStackTrace(e, b, maxStackDepth, maxTotalStackDepth);
			b.append("\n\"\"\"");
		} catch(IOException ex){
			// IOException must not be thrown
			throw new RuntimeException(ex);
		}
		return b.toString();
	}

	/**
	 * 
	 * 
	 */
	public static void appendMessage(
			Throwable e, Appendable buff)
	throws IOException
	{
		buff.append(e.toString());
	}

	/**
	 * 
	 * 
	 */
	public static void appendStackTrace(
			Throwable e, Appendable buff
			, int maxStackDepth, int maxTotalStackDepth)
	throws IOException
	{
		boolean first = true;
		int stackCount = 0;
		while(e != null){
			if(first){
				first = false;
			} else{
				buff.append("Caused by: ");
				appendMessage(e, buff);
				buff.append("\n");
			}
			StackTraceElement[] sts = e.getStackTrace();
			maxStackDepth = Math.min(maxStackDepth, sts.length);
			if(maxStackDepth > 0){
				buff.append("\tat ");
				buff.append(sts[0].toString());
				buff.append("\n");
				for(int si = 1; si < maxStackDepth; si++){
					if(stackCount < maxTotalStackDepth){
						buff.append("\tat ");
						buff.append(sts[si].toString());
						buff.append("\n");
						stackCount++;
					}
				}
				if(sts.length > maxStackDepth){
					buff.append("\t... ");
					buff.append(Integer.toString(sts.length - maxStackDepth));
					buff.append(" more");
					buff.append("\n");
				}
			}
			e = e.getCause();
			if(e == null) break;
		}
	}
}
