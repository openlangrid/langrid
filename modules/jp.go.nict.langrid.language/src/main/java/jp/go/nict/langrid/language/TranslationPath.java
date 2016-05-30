/*
 * $Id: TranslationPath.java 217 2010-10-02 14:45:56Z t-nakaguchi $
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

import java.util.ArrayList;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 */
@Deprecated
public class TranslationPath {
	/**
	 * 
	 * 
	 */
	public TranslationPath(Language... path) {
		this.path = path;
	}

	/**
	 * 
	 * 
	 */
	@Override
	public int hashCode(){
		int h = 0;
		for(Language l : path){
			h = h * 31 + l.hashCode();
		}
		return h;
	}

	/**
	 * 
	 * 
	 */
	@Override
	public boolean equals(Object value)
	{
		return equals((TranslationPath)value);
	}

	/**
	 * 
	 * 
	 */
	public boolean equals(TranslationPath value)
	{
		if(path.length != value.path.length){
			return false;
		}
		for(int i = 0; i < path.length; i++){
			if(!path[i].equals(value.path[i])){
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		boolean first = true;
		for(Language language : path){
			if(!first){
				builder.append(" ");
			} else{
				first = false;
			}
			builder.append(language.getCode());
		}
		builder.append(")");
		return builder.toString();
	}

	/**
	 * 
	 * 
	 */
	public Language[] getLanguages(){
		return path;
	}

	/**
	 * 
	 * 
	 */
	public Language getSourceLanguage(){
		return path[0];
	}

	/**
	 * 
	 * 
	 */
	public Language getTargetLanguage(){
		return path[path.length - 1];
	}

	/**
	 * 
	 * 
	 */
	public static TranslationPath[] parse(String aPathExpression)
		throws InvalidLanguageTagException, InvalidLanguagePathException
	{
		int len = aPathExpression.length();
		if(aPathExpression.charAt(0) != '('){
			throw new InvalidLanguagePathException(
				"Translation path must start with '(': " + aPathExpression);
		}
		if(aPathExpression.charAt(1) != '('){
			throw createInvalidPathException(aPathExpression);
		}
		if(aPathExpression.charAt(len - 1) != ')'){
			throw new InvalidLanguagePathException("Translation path must end with ')'.");
		}
		if(aPathExpression.charAt(len - 2) != ')'){
			throw createInvalidPathException(aPathExpression);
		}
		String expression = aPathExpression.substring(2, len - 2);

		ArrayList<TranslationPath> list = new ArrayList<TranslationPath>();
		for(String path : expression.split("\\) ?\\(")){
			ArrayList<Language> languages = new ArrayList<Language>();
			for(String language : path.split(" ")){
				languages.add(Language.parse(language));
			}
			list.add(new TranslationPath(languages.toArray(new Language[0])));
		}
		return list.toArray(new TranslationPath[0]);
	}

	/**
	 * 
	 * 
	 */
	private static InvalidLanguagePathException createInvalidPathException(
		String anExpression)
	{
		return new InvalidLanguagePathException(
			"Translation path contains invalid expression: " + anExpression
			);
	}

	private Language[] path;
}
