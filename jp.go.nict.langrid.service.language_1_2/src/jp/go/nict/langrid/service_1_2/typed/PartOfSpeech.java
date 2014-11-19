/*
 * $Id: PartOfSpeech.java 224 2010-10-03 00:17:47Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.typed;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * Displays the part of speech.
 * Because in Java '.' cannot be used as an identifier, we create a separate field that displays expressions.
 * accompanying this, the getExpression method returning an expression,
 * and the valueOfExpression method searching objects from expressions, are implemented.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 224 $
 */
public enum PartOfSpeech {
	/**
	 * 
	 * Constant indicating a common noun.
	 * 
	 */
	noun_common("noun.common"),

	/**
	 * 
	 * Constant indicating a pronoun.
	 * 
	 */
	noun_pronoun("noun.pronoun"),

	/**
	 * 
	 * Constant indicating a proper noun.
	 * 
	 */
	noun_proper("noun.proper"),

	/**
	 * 
	 * Constant indicating a noun classified as other than a common noun or proper noun.
	 * Pronouns, count nouns, etc.
	 * 
	 */
	noun_other("noun.other"),

	/**
	 * 
	 * Constant indicating that it is some sort of noun, but proper analysis hasn't been run on it.
	 * When we only have "noun" as a language resource.
	 * 
	 */
	noun,

	/**
	 * 
	 * Constant indicating a verb.
	 * 
	 */
	verb,

	/**
	 * 
	 * Constant indicating an adjective.
	 * 
	 */
	adjective,

	/**
	 * 
	 * Constant indicating an adverb.
	 * 
	 */
	adverb,

	/**
	 * 
	 * There is classification data, but it can't be applied to the value of this enumerated type.
	 * 
	 */
	other,

	/**
	 * 
	 * Things having no classification data.
	 * 
	 */
	unknown,

	;

	/**
	 * 
	 * Constructor.
	 * 
	 */
	PartOfSpeech(){
		this.expression = null;
	}

	/**
	 * 
	 * Constructor.
	 * Takes the string used in the actual expression as a parameter.
	 * @param expression String used in the description
	 * 
	 */
	PartOfSpeech(String expression){
		this.expression = expression;
	}

	/**
	 * 
	 * Gets this object's array expression.
	 * @return This object's string expression
	 * 
	 */
	@Override
	public String toString(){
		return getExpression();
	}

	/**
	 * 
	 * Gets this object's expression.
	 * @return This object's expression
	 * 
	 */
	public String getExpression(){
		if(expression != null){
			return expression;
		} else{
			return name();
		}
	}

	/**
	 * 
	 * Seeks an object from the expression.
	 * @param expression expression
	 * @return Object corresponding with expression
	 * 
	 */
	public static PartOfSpeech valueOfExpression(String expression){
		return expressionToValue.get(expression);
	}

	/**
	 * 
	 * Returns a collection of values.
	 * When initializing an instance, using Collections.unmodifiableCollection it is only created once,
	 * so it operates faster than values().
	 * @return Collection of values
	 * 
	 */
	public static Collection<PartOfSpeech> valuesCollection(){
		return values;
	}

	private final String expression;

	private static final Map<String, PartOfSpeech> expressionToValue;
	private static final Collection<PartOfSpeech> values;

	static{
		expressionToValue = new LinkedHashMap<String, PartOfSpeech>();
		for(PartOfSpeech i : values()){
			expressionToValue.put(i.getExpression(), i);
		}
		values = Collections.unmodifiableCollection(
			expressionToValue.values()
			);
	}
}
