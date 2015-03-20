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
package jp.go.nict.langrid.dao.hibernate.searchsupport;

import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.entity.Attribute;
import jp.go.nict.langrid.dao.entity.AttributedElement;
import jp.go.nict.langrid.language.InvalidLanguagePathException;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.language.util.LanguagePathMatcher;
import jp.go.nict.langrid.language.util.LanguagePathUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class LanguagePathArrayPathListSearchSupport
<T extends AttributedElement<U>, U extends Attribute>
extends AbstractLanguagePathArraySearchSupport<T, U>
implements SearchSupport<T>{
	public void filterResults(MatchingCondition condition, List<T> results) {
		LanguagePath[] containedPaths = null;
		try{
			containedPaths = LanguagePathUtil.decodeLanguagePathArray(
				condition.getMatchingValue().toString());
		} catch(InvalidLanguageTagException e){
			logger.log(Level.WARNING, "invalid tag found. ignore this condition.", e);
			return;
		} catch(InvalidLanguagePathException e){
			logger.log(Level.WARNING, "invalid path found. ignore this condition.", e);
			return;
		}
		
		ListIterator<T> i = results.listIterator();
		while(i.hasNext()){
			T s = i.next();
			LanguagePath[] paths = null;
			try{
				paths = LanguagePathUtil.decodeLanguagePathArray(
						s.getAttributeValue(condition.getFieldName())
						);
			} catch(InvalidLanguagePathException e){
				continue;
			} catch(InvalidLanguageTagException e){
				continue;
			}
			if(!isMatched(containedPaths, paths)){
				i.remove();
			}
		}
	}

	static boolean isMatched(LanguagePath[] conditionPaths, LanguagePath[] servicePaths){
		for(LanguagePath cp : conditionPaths){
			for(LanguagePath sp : servicePaths){
				if(LanguagePathMatcher.matches(cp, sp)) return true;
			}
		}
		return false;
	}

	private static Logger logger = Logger.getLogger(
			LanguagePathArrayPathListSearchSupport.class.getName());
}
