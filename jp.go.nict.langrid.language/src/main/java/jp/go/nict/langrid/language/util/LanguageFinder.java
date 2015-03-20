/*
 * $Id: LanguageFinder.java 217 2010-10-02 14:45:56Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.Collection;

import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.language.LanguagePath;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 */
public class LanguageFinder {
	/**
	 * 
	 * 
	 */
	public static Collection<Language> find(
		Iterable<Language> languages, Language target)
	{
		Collection<Language> candidates = new ArrayList<Language>();
		for(Language l : languages){
			if(target.equals(l)){
				candidates.clear();
				candidates.add(l);
				return candidates;
			}
			if(target.matches(l)){
				candidates.add(l);
			}
		}
		return candidates;
	}

	/**
	 * 
	 * 
	 */
	public static Collection<LanguagePair> find(
		Iterable<LanguagePair> languagePairs, LanguagePair pair)
	{
		Language sl = pair.getSource();
		Language tl = pair.getTarget();
		Collection<LanguagePair> candidates = new ArrayList<LanguagePair>();
		for(LanguagePair p : languagePairs){
			if((sl != null) && !sl.matches(p.getSource())) continue;
			if((tl != null) && !tl.matches(p.getTarget())) continue;
			if((sl != null) && sl.equals(p.getSource())
					&& (tl != null) && tl.equals(p.getTarget())){
				candidates.clear();
				candidates.add(p);
				return candidates;
			}
			candidates.add(p);
		}
		return candidates;
	}

	/**
	 * 
	 * 
	 */
	public static boolean containsMatched(
		Iterable<LanguagePair> languagePairs, LanguagePair pair)
	{
		Language sl = pair.getSource();
		Language tl = pair.getTarget();
		for(LanguagePair p : languagePairs){
			if((sl != null) && !sl.matches(p.getSource())) continue;
			if((tl != null) && !tl.matches(p.getTarget())) continue;
			return true;
		}
		return false;
	}

	/**
	 * 
	 * 
	 */
	public static Collection<LanguagePath> find(
		Iterable<LanguagePath> languagePathes, LanguagePath path)
	{
		Language[] srcPath = path.getPath();
		Collection<LanguagePath> candidates = new ArrayList<LanguagePath>();
		for(LanguagePath p : languagePathes){
			Language[] dstPath = p.getPath();
			if(srcPath.length != dstPath.length) continue;
			boolean pathMatched = true;
			for(int i = 0; i < srcPath.length; i++){
				if(!srcPath[i].matches(dstPath[i])){
					pathMatched = false;
					break;
				}
			}
			if(pathMatched) continue;
			candidates.add(p);
		}
		return candidates;
	}
}
