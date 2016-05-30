/*
 * $Id: LanguagePathMatcher.java 217 2010-10-02 14:45:56Z t-nakaguchi $
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
package jp.go.nict.langrid.language.util;

import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePath;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 */
public class LanguagePathMatcher {
	/**
	 * 
	 * 
	 */
	public static boolean matchesPrefix(LanguagePath src, LanguagePath dst){
		Language[] sp = src.getPath();
		Language[] dp = dst.getPath();

		if(sp.length > dp.length) return false;
		if(sp.length == 0 && dp.length != 0) return false;

		for(int i = 0; i < sp.length; i++){
			if(!matchesInNoDirection(sp[i], dp[i])) return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	public static boolean matchesSuffix(LanguagePath src, LanguagePath dst){
		Language[] sp = src.getPath();
		Language[] dp = dst.getPath();

		if(sp.length > dp.length) return false;
		if(sp.length == 0 && dp.length != 0) return false;

		int d = dp.length - sp.length;
		for(int i = (sp.length - 1); i >= 0; i--){
			if(!matchesInNoDirection(sp[i], dp[i + d])) return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	public static boolean matchesPartial(LanguagePath src, LanguagePath dst){
		Language[] sp = src.getPath();
		Language[] dp = dst.getPath();

		if(sp.length > dp.length) return false;
		if(sp.length == 0 && dp.length != 0) return false;

		int si = 0;
		for(int di = 0; di < dp.length; di++){
			if(di == dp.length) return false;
			if(matchesInNoDirection(sp[si], dp[di])){
				si++;
				if(si == sp.length) return true;
			} else if(si != 0){
				return false;
			}
		}
		return false;
	}

	/**
	 * 
	 * 
	 */
	public static boolean matchesComplete(LanguagePath src, LanguagePath dst){
		Language[] sp = src.getPath();
		Language[] dp = dst.getPath();

		if(sp.length != dp.length) return false;

		for(int i = 0; i < sp.length; i++){
			if(!matchesInNoDirection(sp[i], dp[i])){
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	public static boolean matches(LanguagePath src, LanguagePath dst){
		Language[] sp = src.getPath();
		Language[] dp = dst.getPath();

		if(sp.length > dp.length) return false;
		if((sp.length == 1) && (dp.length != 1)) return false;
		if((sp.length == 0) || (dp.length == 0)) return false;

		if(!matchesInNoDirection(sp[0], dp[0])) return false;
		if(!matchesInNoDirection(sp[sp.length - 1], dp[dp.length - 1])) return false;

		int di = 1;
		for(int i = 1; i < (sp.length - 1); i++){
			while(!matchesInNoDirection(sp[i], dp[di++])){
				if(di == (dp.length - 1)) return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	public static boolean matchesInCombination(LanguagePath src, LanguagePath dstCombination){
		Language[] sp = src.getPath();
		Language[] dp = dstCombination.getPath();
		for(Language sl : sp){
			boolean matched = false;
			for(Language dl : dp){
				if(matchesInNoDirection(sl, dl)){
					matched = true;
					break;
				}
			}
			if(!matched) return false;
		}
		return true;
	}

	private static boolean matchesInNoDirection(Language src, Language dst){
		return src.matches(dst) || dst.matches(src);
	}
}
