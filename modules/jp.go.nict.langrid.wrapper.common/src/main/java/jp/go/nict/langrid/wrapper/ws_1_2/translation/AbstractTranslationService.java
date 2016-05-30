/*
 * $Id: AbstractTranslationService.java 507 2012-05-24 04:34:29Z t-nakaguchi $
 *
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.wrapper.ws_1_2.translation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.LanguagePath;
import jp.go.nict.langrid.service_1_2.LanguagePathNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePathException;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationResult;
import jp.go.nict.langrid.service_1_2.multihoptranslation.MultihopTranslationService;
import jp.go.nict.langrid.service_1_2.transformer.LanguagePath_LanguageToWITransformer;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePathValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;
import jp.go.nict.langrid.wrapper.ws_1_2.util.TextParser;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * Base class for the translation service.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 507 $
 */
public abstract class AbstractTranslationService
extends AbstractLanguagePairService
implements TranslationService, BackTranslationService
	, MultihopTranslationService
{
	/**
	 * 
	 * Constructor that doesn't take parameter(s).
	 * 
	 */
	public AbstractTranslationService(){
		init();
	}

	/**
	 * 
	 * Constructor that takes the service context as a parameter(s).
	 * @param serviceContext Service context
	 * 
	 */
	public AbstractTranslationService(ServiceContext serviceContext){
		super(serviceContext);
		init();
	}

	/**
	 * 
	 * Constructor that takes the service context and supported language pair(s) as a parameter(s).
	 * @param serviceContext Service context
	 * @param supportedPairs Supported language pairs
	 * 
	 */
	public AbstractTranslationService(
		ServiceContext serviceContext
		, Collection<LanguagePair> supportedPairs
		)
	{
		super(serviceContext);
		init();
		setSupportedLanguagePairs(supportedPairs);
	}

	public void setMaxSourceLength(int maxSourceLength) {
		this.maxSourceLength = maxSourceLength;
	}

	public void setSentenceDivision(DivisionType sentenceDivision) {
		this.sentenceDivision = sentenceDivision;
	}

	private void init(){
		maxHop = getInitParameterInt("langrid.multihopTranslation.maxHop", 20);
		maxSourceLength = getInitParameterInt("langrid.maxSourceLength", 5000);
		try {
			String parameter = getInitParameter("langrid.translation.sentenceDivision");
			if (parameter == null || parameter.length() == 0) {
				sentenceDivision = DivisionType.NONE;
			} else {
				sentenceDivision = DivisionType.valueOf(parameter);
			}
		} catch (IllegalArgumentException e) {
			sentenceDivision = DivisionType.NONE;
		}
	}

	public String translate(String sourceLang, String targetLang, String source)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ProcessFailedException
		, ServerBusyException, ServiceNotActiveException
		, ServiceNotFoundException, UnsupportedLanguagePairException
	{
		checkStartupException();
		return invokeDoTranslation(sourceLang, targetLang, source);
	}

	public final BackTranslationResult backTranslate(
			String sourceLang, String interMediateLang, String source)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ProcessFailedException
		, ServerBusyException, ServiceNotActiveException
		, UnsupportedLanguagePairException
	{
		checkStartupException();
		String intermediate = invokeDoTranslation(
				sourceLang, interMediateLang, source);
		String r = invokeDoTranslation(interMediateLang, sourceLang, intermediate);
		return new BackTranslationResult(intermediate, r);
	}

	/**
	 * 
	 * <p>When the LanguagePathNotUnizuelyDecidedException occurs, only one language path data is returned as a candidate.
	 * (It is not a formal interface, so the constructor of the language path data is omitted).</p>
	 * 
	 */
	public final MultihopTranslationResult multihopTranslate(
			String sourceLang, String[] intermediateLangs, String targetLang
			, String source)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePathNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ProcessFailedException
		, ServerBusyException, ServiceNotActiveException
		, UnsupportedLanguagePathException
	{
		checkStartupException();
		if(intermediateLangs.length >= maxHop){
			throw new InvalidParameterException(
					"intermediateLangs", "too many hop: "
					+ intermediateLangs.length + "(max: " + maxHop + ")"
					);
		}
		jp.go.nict.langrid.language.LanguagePath path = new LanguagePathValidator(
				"sourceLang", sourceLang
				, "intermediateLangs", intermediateLangs
				, "targetLang", targetLang
				).notNull().trim().notEmpty().getLanguagePath();
		Language[] langs = path.getPath();
		String[] intermediates = new String[langs.length - 2];
		int n = intermediates.length;
		String src = source;
		acquireSemaphore();
		try{
			for(int i = 0; i < n; i++){
				Language sl = langs[i];
				Language tl = langs[i + 1];
				src = invokeDoTranslation(
						sl.getCode(), tl.getCode(), src
						);
				intermediates[i] = src;
			}
			String target = invokeDoTranslation(
					langs[n].getCode(), langs[n + 1].getCode(), src
					);
			return new MultihopTranslationResult(intermediates, target);
		} catch(LanguagePairNotUniquelyDecidedException e){
			throw new LanguagePathNotUniquelyDecidedException(
					new String[]{"sourceLang", "intermediateLangs", "targetLang"}
					, ArrayUtil.collect(
						new jp.go.nict.langrid.language.LanguagePath[]{path}
						, LanguagePath.class
						, new LanguagePath_LanguageToWITransformer())
					);
		} finally{
			releaseSemaphore();
		}
	}

	/**
	 * 
	 * 
	 */
	public final String multistatementTranslate(
			String sourceLang, String targetLang,
			String source, String delimiterRegx)
		throws AccessLimitExceededException, InvalidParameterException
		, LanguagePairNotUniquelyDecidedException, NoAccessPermissionException
		, NoValidEndpointsException, ProcessFailedException
		, ServerBusyException, ServiceNotActiveException
		, ServiceNotFoundException, UnsupportedLanguagePairException
	{
		checkStartupException();
		if (StringUtils.isBlank(delimiterRegx)) {
			throw new InvalidParameterException("delimiterRegx", "is Blank.");
		}
		StringBuilder sb = new StringBuilder();
		Scanner s = new Scanner(source).useDelimiter(delimiterRegx);
		int i = 0;
		while(s.hasNext()){
			String text = s.next();
			MatchResult m = s.match();
			if(i != m.start()){
				String tag = source.substring(i, m.start());
				sb.append(tag);
			}
			i = m.end();
			sb.append(invokeDoTranslation(sourceLang, targetLang, text));
		}
		if(source.length() != i){
			String tag = source.substring(i);
			sb.append(tag);
		}

		return sb.toString();
	}

	protected String[] doMultistatementTranslation(
		Language sourceLang, Language targetLang, String[] sources
		)
		throws InvalidParameterException, ProcessFailedException
	{
		String[] ret = new String[sources.length];
		int n = ret.length;
		for(int i = 0; i < n; i++){
			ret[i] = doTranslation(sourceLang, targetLang, sources[i]);
		}
		return ret;
	}

	/**
	 * 
	 * Template method supporting transtlate.
	 * @param sourceLang Source language
	 * @param targetLang Target language
	 * @param source String to be translated
	 * @return The translated string
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws ProcessFailedException Process failed
	 * 
	 */
	protected abstract String doTranslation(
		Language sourceLang, Language targetLang, String source
		)
	throws InvalidParameterException, ProcessFailedException;

	/**
	 * 
	 * @param languagePair
	 * @return
	 * @throws InvalidParameterException An invalid parameter was passed
	 * @throws LanguagePairNotUniquelyDecidedException Multiple candidate language pairs exist
	 * @throws UnsupportedLanguagePairException An unsupported language pair was specified
	 * 
	 */
	protected LanguagePair validateLanguagePair(
			LanguagePairValidator languagePair)
	throws InvalidParameterException, LanguagePairNotUniquelyDecidedException
	, UnsupportedLanguagePairException
	{
		return languagePair.notNull().trim().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection()
						);
	}

	public enum DivisionType{
		NONE,
		WORD,
		FULL,
		FULL_WITH_PUNCTUATION,
		;
	}

	private String invokeDoTranslation(String sourceLang, String targetLang, String source)
		throws InvalidParameterException
		, LanguagePairNotUniquelyDecidedException
		, ProcessFailedException
		, UnsupportedLanguagePairException
	{
		LanguagePair pair = validateLanguagePair(new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				));
		String src = new StringValidator("source", source)
				.notNull().trim().notEmpty().getValue();
		src = toUpperCaseCharacterBehindDelimiter(src);

		processStart();
		try{
			acquireSemaphore();
			try{
				Language s = pair.getSource();
				Language t = pair.getTarget();
				String ret = null;
				switch(sentenceDivision) {
					default:
					case NONE:
						ret = translateByNone(s, t, src);
						break;
					case WORD:
						ret = translateByWord(s, t, src);
						break;
					case FULL:
						ret = translateByFull(s, t, src);
						break;
					case FULL_WITH_PUNCTUATION:
						ret = translateByFullWithPunctuation(s, t, src);
						break;
				}
				if(ret != null){
					ret = toLowerCaseInternalCode(ret);
					ret = convertDelimiter(t.getCode(), ret);
				}
				return ret;
			} catch(InvalidParameterException e){
				throw e;
			} catch(ProcessFailedException e){
				logger.log(Level.WARNING, "process failed.", e);
				throw e;
			} catch(Throwable t){
				logger.log(Level.SEVERE, "unknown error occurred.", t);
				throw new ProcessFailedException(t);
			} finally{
				releaseSemaphore();
			}
		} finally{
			processEnd();
		}
	}

	private String translateByNone(Language s, Language t, String src)
		throws InvalidParameterException
		, LanguagePairNotUniquelyDecidedException
		, ProcessFailedException
		, UnsupportedLanguagePairException
	{
		if(src.length() > this.maxSourceLength) {
			String[] sources = divideSource(src, this.maxSourceLength);
			String[] results = doMultistatementTranslation(s, t, sources);
			StringBuilder sb = new StringBuilder();
			for(String result : results) {
				sb.append(result);
			}
			return  sb.toString();
		} else {
			return  doTranslation(s, t, src);
		}
	}

	private String translateByWord(Language s, Language t, String src)
		throws InvalidParameterException
		, LanguagePairNotUniquelyDecidedException
		, ProcessFailedException
		, UnsupportedLanguagePairException
	{
		return divideByDelimiterAndTranslation(s, t, src, java.util.regex.Pattern.compile(" *\\Q*$%*\\E[.。] *"));
	}

	private String translateByFull(Language s, Language t, String src)
		throws InvalidParameterException
		, LanguagePairNotUniquelyDecidedException
		, ProcessFailedException
		, UnsupportedLanguagePairException
	{
		return divideByDelimiterAndTranslation(s, t, src, java.util.regex.Pattern.compile(" *((\\Q*$%*\\E)|(\\Q*%$*\\E))[.。] *"));
	}
	/**
	 * セパレータで分割して翻訳する。<br/>
	 * このメソッドは translateByFull と translateByWord から呼び出される。<br/>
	 * @author Hitoshi Sugihara
	 * @param s 元テキストの言語
	 * @param t 翻訳先の言語
	 * @param src 元テキスト
	 * @param deli 分割対象のセパレータ記号を表すPatternオブジェクト
	 * @return 翻訳結果
	 * @throws InvalidParameterException
	 * @throws ProcessFailedException
	 */
	private String divideByDelimiterAndTranslation(Language s, Language t, String src, java.util.regex.Pattern deli) throws InvalidParameterException, ProcessFailedException {
		java.util.List<String> delimiters; // 分割時にセパレータ記号を保存しておくリスト
		String[] sources; // 分割された元テキスト
		String[] results; // 分割翻訳された結果
		delimiters = new java.util.LinkedList<String>();
		sources = divideSourceByDelimiter(src, deli, delimiters);
		results = doMultistatementTranslation(s, t, sources);
		return combineResult(results, delimiters, t);
	}

	private String translateByFullWithPunctuation(Language s, Language t, String src)
		throws InvalidParameterException
		, LanguagePairNotUniquelyDecidedException
		, ProcessFailedException
		, UnsupportedLanguagePairException
	{
		src = TextParser.preprocessOriginal(s.getCode(), src);
		ArrayList<String> sentences = new ArrayList<String>();
		while (src.length() != 0) {
			HashMap<String, String> parsed = TextParser.getFirstSentence(s.getCode(), src);
			sentences.add(parsed.get("first"));
			src = parsed.get("remain");
		}

		String[] retCodes = new String[sentences.size()];
		ArrayList<String> contents = new ArrayList<String>();
		for (int i = 0; i < retCodes.length; i++) {
			String aStr = sentences.get(i);
			if(aStr.equals(TextParser.getRetSymbol("\r"))) {
				retCodes[i] = "\r";
			} else if (aStr.equals(TextParser.getRetSymbol("\n"))) {
				retCodes[i] = "\n";
			} else if (aStr.equals(TextParser.getRetSymbol("\r\n"))) {
				retCodes[i] = "\r\n";
			} else {
				contents.add(aStr);
			}
		}

		String[] res = doMultistatementTranslation(s, t, contents.toArray(new String[]{}));

		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (int i = 0; i < retCodes.length; i++) {
			if (retCodes[i] == null) {
				sb.append(res[index]);
				index++;
			} else {
				sb.append(retCodes[i]);
			}
		}

		return sb.toString();
	}

	private String[] divideSource(String source, int maxSourceLength) {
		ArrayList<String> resultArray = new ArrayList<String>();

		Pattern p = Pattern.compile("(\\Q*$%*\\E|\\Q*%$*\\E)");
		Matcher m = p.matcher(source);

		int index = 0;
		int end = 0;
		while(index + maxSourceLength < source.length()) {
			Matcher region = m.region(index, index + maxSourceLength);
			while (region.find()) {
				end = m.end();
			}
			if (end > index) {
				resultArray.add(source.substring(index, end + 1 ));
				index = end + 2;
				end = index;
			} else {
				break;
			}
		}

		if (index < source.length()) {
			resultArray.add(source.substring(index));
		}

		return resultArray.toArray(new String[]{});
	}

	/**
	 * セパレータ文字列でテキストを分割する。<br/>
	 * このメソッドは、divideByDelimiterAndTranslation から呼び出される。<br/>
	 * @author Hitoshi Sugihara
	 * @param source 分割する文字列
	 * @param deli 分割対象のセパレータ文字列を表す Pattern オブジェクト
	 * @param delis セパレータ文字列を保存しておくリストへの参照
	 * @return セパレータによって配列に分割された文字列
	 */
	private String[] divideSourceByDelimiter(String source, java.util.regex.Pattern deli, java.util.List<String> delis) {
		java.util.List<String> sources = new java.util.ArrayList<String>(); // このリストに、分割された文字列が貯められていく。
		java.util.regex.Matcher mth = deli.matcher(source); // 分割処理に利用する Matcher オブジェクト
		StringBuffer hoge; // 分割された文字列を一時的に保存するための変数
		while (mth.find()) {
			delis.add(mth.group());
			hoge = new StringBuffer();
			mth.appendReplacement(hoge, "");
			sources.add(hoge.toString());
		}
		hoge = new StringBuffer();
		mth.appendTail(hoge);
		if (hoge.length() > 0) {
			sources.add(hoge.toString());
		}
		return sources.toArray(new String[]{});
	}
	/**
	 * 分割して翻訳された結果をひとつのテキストに戻す。<br/>
	 * このメソッドは、divideByDelimiterAndTranslation から呼び出される。
	 * @author Hitoshi Sugihara
	 * @param results 分割翻訳の結果
	 * @param delis セパレータ文字列が保存されたリストへの参照
	 * @param t 翻訳結果の言語
	 * @return 結合された文字列
	 */
	private String combineResult(String[] results,java.util.List<String> delis, Language t) {
		StringBuilder sb = new StringBuilder();// 翻訳結果を結合する StringBuilder
		java.util.Iterator<String> deli = delis.iterator();// 保存されたセパレータ文字列を取り出すためのIterator
		for (int i = 0; i < results.length; i++) {
			sb.append(results[i]);
			if(deli.hasNext()) {
				sb.append(deli.next());
			}
		}
		return sb.toString();
	}

	private String toUpperCaseCharacterBehindDelimiter(String source) {
		StringBuilder sb = new StringBuilder(source);
		Pattern p = Pattern.compile("(\\Q*$%*\\E|\\Q*%$*\\E)\\. *[a-z]");
		Matcher m = p.matcher(sb);

		while(m.find()) {
			String sub = sb.substring(m.start(),m.end());
			sb.replace(m.start(), m.end(), sub.toUpperCase());
		}

		return sb.toString();
	}

	private String toLowerCaseInternalCode(String source) {
		return source.replaceAll("Xxx([a-z]+)xxx", "xxx$1xxx");
	}

	private String convertDelimiter(String targetLang, String src) {
		// 翻訳先言語が'ja'または'zh'の場合は区切り文字の終端記号を"。"に統一、それ以外の場合は、"."に統一
		if (targetLang.equals("ja") || targetLang.equals("zh") || targetLang.startsWith("zh-")) {
			src = src.replace("*$%*.", "*$%*。");
			src = src.replace("*%$*.", "*%$*。");
		} else {
			src = src.replace("*$%*。", "*$%*.");
			src = src.replace("*%$*。", "*%$*.");
		}

		// 区切り文字の終端記号の直後に半角スペースが含まれない場合、半角スペースを挿入
		if (targetLang.equals("ja") || targetLang.equals("zh") || targetLang.startsWith("zh-")) {
			src = src.replaceAll("(\\Q*$%*\\E。)(\\S)", "$1 $2");
			src = src.replaceAll("(\\Q*%$*\\E。)(\\S)", "$1 $2");
		} else {
			src = src.replaceAll("(\\Q*$%*\\E.)(\\S)", "$1 $2");
			src = src.replaceAll("(\\Q*%$*\\E.)(\\S)", "$1 $2");
		}

		// 区切り文字の直前に「終端文字 + 半角スペース」が存在しない場合、それらを挿入
		if (targetLang.equals("ja") || targetLang.equals("zh") || targetLang.startsWith("zh-")) {
			src = src.replaceAll("([^。\\s])\\s*(\\Q*$%*\\E。)", "$1。 $2");
			src = src.replaceAll("([^。．.？！?!\\s])\\s*(\\Q*%$*\\E。)", "$1。 $2");
		} else {
			src = src.replaceAll("([^.\\s])\\s*(\\Q*$%*\\E.)", "$1. $2");
			src = src.replaceAll("([^.?!\\s])\\s*(\\Q*%$*\\E.)", "$1. $2");
		}

		// 区切り文字と直前の終端文字の間に半角スペースが存在しない場合、半角スペースを挿入
		if (targetLang.equals("ja") || targetLang.equals("zh") || targetLang.startsWith("zh-")) {
			src = src.replaceAll("([。])(\\Q*$%*\\E。)", "$1 $2");
			src = src.replaceAll("([。．.？！?!])(\\Q*%$*\\E。)", "$1 $2");
		} else {
			src = src.replaceAll("([.])(\\Q*$%*\\E.)", "$1 $2");
			src = src.replaceAll("([.?!])(\\Q*%$*\\E.)", "$1 $2");
		}

		return src;
	}

	private int maxHop;
	private int maxSourceLength;
	private DivisionType sentenceDivision;
	private static Logger logger = Logger.getLogger(
			AbstractTranslationService.class.getName()
			);
}
