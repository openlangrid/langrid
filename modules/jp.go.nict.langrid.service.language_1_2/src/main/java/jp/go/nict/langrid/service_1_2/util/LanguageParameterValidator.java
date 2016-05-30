package jp.go.nict.langrid.service_1_2.util;

import java.util.Collection;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.language.util.LanguageUtil;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.dictionary.typed.DictMatchingMethod;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.service_1_2.typed.PartOfSpeech;

public class LanguageParameterValidator {
	/**
	 * 
	 * 
	 */
	public static DictMatchingMethod getValidDictSearchMethod(
		String parameterName, String value
		)
		throws InvalidParameterException
	{
		if(value == null) return null;
		if(value.length() == 0) return null;

		DictMatchingMethod method = DictMatchingMethod.valueOfExpression(value);
		if(method == null){
			throw new InvalidParameterException(
				parameterName, "invalid searchMethod: \"" + value + "\""
				);
		}
		return method;
	}

	/**
	 * 
	 * 
	 */
	public static MatchingMethod getValidMatchingMethod(
		String aParameterName, String aValue
		)
		throws InvalidParameterException
	{
		if(aValue == null) return null;
		if(aValue.length() == 0) return null;

		try{
			return MatchingMethod.valueOf(aValue.toUpperCase());
		} catch(IllegalArgumentException e){
			throw new InvalidParameterException(
				aParameterName, "invalid searchMethod: \"" + aValue + "\""
				);
		}
	}

	/**
	 * 
	 * 
	 */
	public static PartOfSpeech getValidPartOfSpeech(
		String aParameterName, String aValue
		)
		throws InvalidParameterException
	{
		if(aValue == null) return null;
		if(aValue.length() == 0) return null;

		PartOfSpeech pos = PartOfSpeech.valueOfExpression(aValue);
		if(pos == null){
			throw new InvalidParameterException(
				aParameterName, "invalid partOfSpeech: \"" + aValue + "\""
				);
		}
		return pos;
	}

	/**
	 * 
	 * 
	 */
	public static Language getValidLanguage(String aParametername, String aValue)
		throws InvalidParameterException
	{
		// 修正する場合は村上さんに連絡！
		if(aValue == null) return null;
		if(aValue.length() == 0) return null;

		try{
			return Language.parse(aValue);
		} catch(InvalidLanguageTagException e){
			throw LanguageExceptionConverter.convertException(e, aParametername);
		}
	}

	/**
	 * 
	 * 
	 */
	public static Language getUniqueLanguage(String parameterName
		, Language language, Collection<Language> candidates)
		throws LanguageNotUniquelyDecidedException, UnsupportedLanguageException
	{
		switch(candidates.size()){
			case 0:
				throw new UnsupportedLanguageException(
						parameterName
						, language.getCode()
						);
			case 1:
				return candidates.iterator().next();
			default:
				throw new LanguageNotUniquelyDecidedException(
					parameterName
					, LanguageUtil.arrayToCodeArray(candidates.toArray(new Language[]{}))
					);
		}
	}

	/**
	 * 
	 * 
	 */
	public static LanguagePair getUniqueLanguagePair(
		String parameterName1, String parameterName2
		, Language language1, Language language2
		, Collection<LanguagePair> candidates)
		throws LanguagePairNotUniquelyDecidedException
		, UnsupportedLanguagePairException
	{
		switch(candidates.size()){
			case 0:
				throw new UnsupportedLanguagePairException(
						parameterName1, parameterName2
						, language1.getCode(), language2.getCode()
						);
			default:
				return candidates.iterator().next();
		}
	}
}
