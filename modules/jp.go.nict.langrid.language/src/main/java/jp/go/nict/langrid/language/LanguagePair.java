/*
 * $Id:LanguagePair.java 5274 2007-09-10 06:03:14Z nakaguchi $
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
package jp.go.nict.langrid.language;

import java.io.Serializable;

import jp.go.nict.langrid.commons.util.Pair;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:5274 $
 */
public class LanguagePair implements Serializable{
	/**
	 * 
	 * 
	 */
	public LanguagePair(Language sourceLang, Language targetLang) {
		pair = Pair.create(sourceLang, targetLang);
	}

	/**
	 * 
	 * 
	 */
	public Language getSource(){
		return pair.getFirst();
	}

	/**
	 * 
	 * 
	 */
	public Language getTarget(){
		return pair.getSecond();
	}

	/**
	 * 
	 * 
	 */
	public LanguagePair reverse(){
		return new LanguagePair(pair.getSecond(), pair.getFirst());
	}

	/**
	 * 
	 * 
	 */
	@Override
	public int hashCode(){
		return pair.hashCode();
	}

	/**
	 * 
	 * 
	 */
	@Override
	public boolean equals(Object aValue){
		return equals((LanguagePair)aValue);
	}

	/**
	 * 
	 * 
	 */
	public boolean equals(LanguagePair aValue){
		return pair.equals(aValue.pair);
	}

	/**
	 * 
	 * 
	 */
	public String toCodeString(String separator){
		return pair.getFirst().getCode() + separator + pair.getSecond().getCode();
	}

	/**
	 * 
	 * 
	 */
	@Override
	public String toString(){
		return pair.getFirst() + ":" + pair.getSecond();
	}

	private Pair<Language, Language> pair;
	private static final long serialVersionUID = 3615005769730665967L;
}
