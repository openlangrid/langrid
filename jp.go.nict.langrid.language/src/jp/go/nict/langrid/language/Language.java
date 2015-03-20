/*
 * $Id: Language.java 217 2010-10-02 14:45:56Z t-nakaguchi $
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

import jp.go.nict.langrid.commons.lang.ClassResourceBundle;

/**
 * 
 * 
 * <pre>
 * RFC 3066: Tags for the Identification of Languages
 * http://www.ietf.org/rfc/rfc3066.txt
 * ISO 639: Codes for the Representation of Names of Languages
 * http://www.loc.gov/standards/iso639-2/englangn.html
 * ISO 3166: Codes for Country Names
 * http://www.iso.org/iso/en/prods-services/iso3166ma/02iso-3166-code-lists/list-en1.html
 * IANA LANGUAGE TAGS
 * http://www.iana.org/assignments/language-tags
 * </pre>
 * @author $Author: t-nakaguchi $
 * @version $Revision: 217 $
 * @see jp.go.nict.langrid.language.ISO639_1
 * @see jp.go.nict.langrid.language.ISO639_2
 * @see jp.go.nict.langrid.language.ISO3166
 * @see jp.go.nict.langrid.language.IANALanguageTags
 * @see jp.go.nict.langrid.language.LangridLanguageTags
 */
public class Language implements Serializable{
	/**
	 * 
	 * 
	 */
	public Language(String expression)
		throws InvalidLanguageTagException
	{
		tags = parseToTags(expression);
	}

	/**
	 * 
	 * 
	 */
	@Override
	public int hashCode(){
		return getCode().hashCode();
	}

	/**
	 * 
	 * 
	 */
	public boolean equals(Language value){
		assert value != null;
		return getCode().toUpperCase().equals(value.getCode().toUpperCase());
	}

	/**
	 * 
	 * 
	 */
	@Override
	public boolean equals(Object value){
		return equals((Language)value);
	}

	/**
	 * 
	 * 
	 */
	public boolean isDeprecated(){
		return false;
	}
	
	/**
	 * 
	 * 
	 */
	public String getCode(){
		if(code != null) return code;

		StringBuilder b = new StringBuilder();
		int i = 0;
		for(String tag : tags){
			if(i == 0){
				b.append(tag.toLowerCase());
			} else if(i == 1){
				switch(tag.length()){
					case 2:
					case 3:
						b.append(tag.toUpperCase());
						break;
					default:
						b.append(tag);
						break;
				}
			} else{
				b.append(tag);
			}
			b.append("-");
			i++;
		}
		code = b.deleteCharAt(b.length() - 1).toString();
		return code;
	}

	/**
	 * 
	 * 
	 */
	public String getLocalizedName(Locale locale){
		try{
			return ClassResourceBundle.getString(locale, getClass(), getCode());
		} catch(MissingResourceException e){
			return getCode();
		}
	}

	/**
	 * 
	 * 
	 */
	@Override
	public String toString(){
		return getCode();
	}

	/**
	 * 
	 * 
	 */
	public boolean matches(Language value){
		if(value.getCode().equals("*")){
			return true;
		}
		for(int i = 0; i < tags.length; i++){
			// RFC3066 2.5 Language-range
			if(i == (tags.length - 1) && tags[i].equals("*")) return true;

			if(i >= value.tags.length) return false;
			if(!tags[i].equalsIgnoreCase(value.tags[i])) return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 */
	public static Language get(LanguageTag language)
	{
		return new Language(new String[]{language.getCode()});
	}

	/**
	 * 
	 * 
	 */
	public static Language get(
		LanguageTag language, CountryName country, String... additionals)
		throws InvalidLanguageTagError
	{
		String[] args = new String[additionals.length + 2];
		args[0] = language.getCode();
		args[1] = country.getCode();
		try{
			for(int i = 0; i < additionals.length; i++){
				String s = additionals[i];
				assertValidSubTag(s);
				args[i + 2] = s;
			}
		} catch(InvalidLanguageTagException e){
			throw new InvalidLanguageTagError(e);
		}
		return new Language(args);
	}

	/**
	 * 
	 * 
	 */
	public static Language get(
		LanguageTag language, String countryOrCharset, String... additionals)
		throws InvalidLanguageTagError
	{
		try{
			String[] args = new String[additionals.length + 2];
			args[0] = language.getCode();
			assertValidSubTag(countryOrCharset);
			args[1] = countryOrCharset;
			for (int i = 0; i < additionals.length; i++) {
				String s = additionals[i];
				assertValidSubTag(s);
				args[i + 2] = s;
			}
			return new Language(args);
		} catch(InvalidLanguageTagException e){
			throw new InvalidLanguageTagError(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static Language parse(String expression)
		throws InvalidLanguageTagException
	{
		return new Language(parseToTags(expression));
	}

	private static String[] parseToTags(String expression)
		throws InvalidLanguageTagException
	{
		{	// IANAに登録されていれば有効
			Language lang = IANALanguageTags.get(expression);
			if(lang != null){
				return lang.tags;
			}
		}

		// 全言語を表す範囲"*"は有効
		if(expression.equals("*")){
			return new String[]{expression};
		}

		if(expression.endsWith("-")){
			throw createException(
					"\"%s\" is not valid expression.", expression);
		}
		String[] tags = expression.split("-");

		// 登録されていない場合、内容を検証
		do{
			// primary-subtag
			String tag = tags[0];
			if (tag.equals("x")) {
				if(tags.length == 1){
					throw createException(
						"\"%s\" is not valid expression.", expression);
				} else{
					break;
				}
			}

			if(tag.length() == 2){
				try{
					tags[0] = ISO639_1.valueOf(tag.toUpperCase()).getCode();
				} catch(IllegalArgumentException e){
					throw createException("\"%s\" is not in ISO639_1.", tag);
				}
			} else if(tag.length() == 3){
				try{
					ISO639_2 tag2 = ISO639_2.valueOf(tag.toUpperCase());
					if(tag2.getTwoLetterCode() != null){
						// ISO3066 2.3 2
						throw createException(
								"\"%s\" must be used instead of \"%s\"."
								, tag2.getTwoLetterCode().getCode(), tag);
					}
					String term = tag2.getTerminologyCode();
					if((term != null) && !term.equals(tag)){
						// ISO3066 2.3 3
						throw createException(
								"The terminology code \"%s\" must be used instead of \"%s\"."
								, tag2.getTerminologyCode(), tag);
					}
					tags[0] = tag2.getCode();
				} catch(IllegalArgumentException e){
					throw createException("\"%s\" is not in ISO639_2.", tag);
				}
			} else{
				throw createException("\"%s\" is not a valid primary subtag.", tag);
			}
			if(tags.length == 1) break;

			// secondary-subtag
			tag = tags[1];
			try{
				if(tag.equals("*")){
					if(tags.length != 2){
						throw createException("\"*\" is only allowed in end of the tags.");
					}
				} else{
					tags[1] = ISO3166.valueOf(tag.toUpperCase()).getCode();
				}
			} catch(IllegalArgumentException e){
				throw createException("\"%s\" is not in ISO3166.", tag);
			}
		} while(false);

		boolean first = true;
		for(String t : tags){
			if(first){
				assertValidPrimarySubTag(t);
				first = false;
			}
			else assertValidSubTag(t);
		}

		return tags;
	}

	/**
	 * 
	 * 
	 */
	private static InvalidLanguageTagException createException(
		String format, String... args)
	{
		return new InvalidLanguageTagException(
			String.format(format, (Object[]) args)
			);
	}

	/**
	 * 
	 * 
	 */
	private static void assertValidPrimarySubTag(String tag)
		throws InvalidLanguageTagException
	{
		if(tag.equals("*")) return;
		for(char c : tag.toCharArray()){
			if((c < 127) && (Character.isLetterOrDigit(c))) continue;
			throw new InvalidLanguageTagException(
				"'" + c + "' is not a valid character.");
		}
		if(tag.length() > 8){
			throw createException("sub-tag is too long. \"%s\"", tag);
		}
		if(invalidTags.contains(tag.toUpperCase())){
			throw createException("tag %s is reserved.", tag);
		}
	}

	/**
	 * 
	 * 
	 */
	private static void assertValidSubTag(String tag)
		throws InvalidLanguageTagException
	{
		assertValidPrimarySubTag(tag);
		if(tag.length() < 2 && !tag.equals("*")){
			throw createException("sub-tag is too short. \"%s\"", tag);
		}
	}

	/**
	 * 
	 * 
	 */
	private Language(String[] tags) {
		this.tags = tags;
	}

	private String[] tags;
	private transient String code;
	
	private static final long serialVersionUID = 2327769078848039242L;
	private static Set<String> invalidTags;
	static{
		invalidTags = new HashSet<String>(Arrays.asList(new String[]{
				"UND", "MUL"
		}));
	}
}
