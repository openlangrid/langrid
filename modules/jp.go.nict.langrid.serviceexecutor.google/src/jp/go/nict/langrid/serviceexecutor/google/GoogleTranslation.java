/*
 * This is a program to wrap language resources as Web services.
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
package jp.go.nict.langrid.serviceexecutor.google;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.translation.MultistatementTranslationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;

import org.apache.commons.lang.StringEscapeUtils;

import com.opensymphony.oscache.base.Cache;
import com.opensymphony.oscache.base.NeedsRefreshException;

/**
 * Google AJAX Translation API Wrapper.
 * @author Takao Nakaguchi
 * @author Masaaki Kamiya
 */
public class GoogleTranslation
implements TranslationService, MultistatementTranslationService{
	public void setTimeoutMillis(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public void setMaxQueryCount(int maxQueryCount) {
		this.maxQueryCount = maxQueryCount;
	}

	public void setMaxTotalQueryLength(int maxTotalQueryLength) {
		this.maxTotalQueryLength = maxTotalQueryLength;
	}

	@Override
	public String translate(String sourceLang, String targetLang, String source)
	throws InvalidParameterException, ProcessFailedException
	, UnsupportedLanguagePairException{
		sourceLang = getOrDefault(languageCodeConversionMap, sourceLang, sourceLang);
		targetLang = getOrDefault(languageCodeConversionMap, targetLang, targetLang);

		LanguagePair pair = validateLanguagePair(new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				));
		return doTranslation(pair.getSource(), pair.getTarget(), source);
	}

	@Override
	public String[] multistatementTranslate(String sourceLang, String targetLang, String[] sources)
	throws InvalidParameterException, ProcessFailedException
	, UnsupportedLanguagePairException{
		sourceLang = getOrDefault(languageCodeConversionMap, sourceLang, sourceLang);
		targetLang = getOrDefault(languageCodeConversionMap, targetLang, targetLang);

		LanguagePair pair = validateLanguagePair(new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				));
		return doMultistatementTranslation(pair.getSource(), pair.getTarget(), sources);
	}

	@SuppressWarnings("unchecked")
	private LanguagePair validateLanguagePair(
			LanguagePairValidator languagePair)
	throws InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			UnsupportedLanguagePairException
	{
		LanguagePair pair = languagePair.notNull().trim().notEmpty()
			.getLanguagePair();
		String key = pair.toCodeString(":");
		try {
			boolean supported = (Boolean)langPairCache.getFromCache(
					key, 60 * 60 * 24);
			if(supported){
				return pair;
			} else{
				languagePair.getUniqueLanguagePair(Collections.EMPTY_LIST);
			}
		} catch (NeedsRefreshException e) {
			languagePairValidator.set(languagePair);
			langPairCache.cancelUpdate(key);
		}
		return pair;
	}

	private String doTranslation(
			Language sourceLang, Language targetLang, String source)
	throws InvalidParameterException, ProcessFailedException
	{
		source = source.replaceAll("xxx(\\w+)xxx", "XXX$1XXX");
		String result = invokeTranslation(sourceLang, targetLang, source);
		result = result.replaceAll("XXX(\\w+)XXX", "xxx$1xxx");
		return result;
	}

	protected String[] doMultistatementTranslation(
			Language sourceLang, Language targetLang, String[] sources)
	throws InvalidParameterException, ProcessFailedException
	{
		for(int i=0; i < sources.length; i++) {
			sources[i] = sources[i].replaceAll("xxx(\\w+)xxx", "XXX$1XXX");
		}

		ArrayList<String[]> separatedSources =  separateSourcesConsideredBatchAPILimitation(sources);
		ArrayList <String> res = new ArrayList<String>();
		for (String[] srcs : separatedSources) {
			if(srcs.length == 1) {
				res.add(invokeTranslation(sourceLang, targetLang, srcs[0]));
			} else {
				res.addAll(invokeBatchTranslation(sourceLang, targetLang, srcs));
			}
		}

		String[] results = res.toArray(new String[]{});
		for(int i=0; i < results.length; i++) {
			results[i] = results[i].replaceAll("XXX(\\w+)XXX", "xxx$1xxx");
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private String invokeTranslation(
			Language sourceLang, Language targetLang, String source)
	throws InvalidParameterException, ProcessFailedException
	{
		InputStream is = null;
		try{
			URLConnection con = new URL(TRANSLATE_URL).openConnection();
			con.setDoOutput(true);
			con.setReadTimeout(timeoutMillis);
			con.addRequestProperty("Referer", referer);
			writeParameters(con, sourceLang, targetLang, source);

			is = con.getInputStream();
			String json = StreamUtil.readAsString(
					is, CharsetUtil.newUTF8Decoder()
					);
			try{
				TranslationResult result = JSON.decode(json, TranslationResult.class);
				ResponseData responseData = result.responseData;
				int status = result.responseStatus;
				String key = sourceLang.getCode() + ":" + targetLang.getCode();
				if(status == 200){
					langPairCache.putInCache(key, true);
					return StringEscapeUtils.unescapeHtml(
							responseData.translatedText.replaceAll("&#39;", "'"));
				} else{
					String details = result.responseDetails;
					if(details.equals("invalid translation language pair")){
						langPairCache.putInCache(key, false);
						languagePairValidator.get().getUniqueLanguagePair(
								Collections.EMPTY_LIST)
								;
					}
					throw new ProcessFailedException(result.responseStatus + ": " + details);
				}
			} catch(JSONException e){
				String message = "failed to read translated text: " + json;
				logger.log(Level.WARNING, message, e);
				throw new ProcessFailedException(message);
			}
		} catch(IOException e){
			logger.log(
					Level.SEVERE, "failed to execute service.", e
					);
			throw new ProcessFailedException(e);
		} finally{
			if(is != null){
				try{
					is.close();
				} catch(IOException e){
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<String> invokeBatchTranslation(
			Language sourceLang, Language targetLang, String[] sources)
	throws InvalidParameterException, ProcessFailedException
	{
		InputStream is = null;
		try{
			URLConnection con = new URL(TRANSLATE_URL).openConnection();
			con.setDoOutput(true);
			con.setReadTimeout(timeoutMillis);
			con.addRequestProperty("Referer", referer);
			writeParameters(con, sourceLang, targetLang, sources);

			is = con.getInputStream();
			String json = StreamUtil.readAsString(
					is, CharsetUtil.newUTF8Decoder()
					);
			try{
				BatchTranslationResult result = JSON.decode(json, BatchTranslationResult.class);
				int status = result.responseStatus;
				String key = sourceLang.getCode() + ":" + targetLang.getCode();

				if(status == 200){
					langPairCache.putInCache(key, true);
					ArrayList<String> resultArray = new ArrayList<String>();
					for(TranslationResult r : result.responseData) {
						resultArray.add(StringEscapeUtils.unescapeHtml(
								r.responseData.translatedText.replaceAll("&#39;", "'")));
					}
					return resultArray;
				} else{
					String details = result.responseDetails;
					if(details.equals("invalid translation language pair")){
						langPairCache.putInCache(key, false);
						languagePairValidator.get().getUniqueLanguagePair(Collections.EMPTY_LIST);
					}
					throw new ProcessFailedException(result.responseStatus + ": " + details);
				}
			} catch(JSONException e){
				String message = "failed to read translated text: " + json;
				logger.log(Level.WARNING, message, e);
				throw new ProcessFailedException(message);
			}
		} catch(IOException e){
			logger.log(
					Level.SEVERE, "failed to execute service.", e
					);
			throw new ProcessFailedException(e);
		} finally{
			if(is != null){
				try{
					is.close();
				} catch(IOException e){
				}
			}
		}
	}

	static void writeParameters(
			URLConnection c, Language sourceLang, Language targetLang, String... sources)
	throws IOException{
		StringBuilder b = new StringBuilder();
		b.append("langpair=").append(sourceLang.getCode())
			.append("|").append(targetLang.getCode());
		for(String s : sources){
			b.append("&q=").append(URLEncoder.encode(s, "UTF-8"));
		}
		c.addRequestProperty(
				"Content-Type"
				, "application/x-www-form-urlencoded; charset=UTF-8"
				);
		c.getOutputStream().write(b.toString().getBytes("UTF-8"));
	}

	private ArrayList<String[]> separateSourcesConsideredBatchAPILimitation(String[] sources) {
		ArrayList<String[]> result = new ArrayList<String[]>();
		ArrayList<String> sourceArray = new ArrayList<String>(Arrays.asList(sources));

		int totalLength = 0;
		int index = 0;
		int count = 0;
		for (String source: sourceArray) {
			totalLength += source.length();
			if(totalLength >= maxTotalQueryLength || (count - index) >= maxQueryCount) {
				result.add(sourceArray.subList(index, count).toArray(new String[]{}));
				index = count;
				totalLength = source.length();
			}
			count++;
		}

		if (index < sourceArray.size()) {
			result.add(sourceArray.subList(index, sourceArray.size()).toArray(new String[]{}));
		}

		return result;
	}

	private static <T,U> U getOrDefault(Map<T,U> map, T key, U defaultValue){
		U v = map.get(key);
		if(v != null) return v;
		else return defaultValue;
	}

	private static class ResponseData{
		public String translatedText;
	}
	private static class TranslationResult{
		public ResponseData responseData;
		public String responseDetails;
		public int responseStatus;
	}
	private static class BatchTranslationResult{
		public TranslationResult[] responseData;
		public String responseDetails;
		public int responseStatus;
	}

	private int timeoutMillis = 10000;
	private String referer = "";
	private int maxQueryCount = 128;
	private int maxTotalQueryLength = 5120;
	private static final String TRANSLATE_URL =
			"http://ajax.googleapis.com/ajax/services/language/translate?v=1.0";
	private static Cache langPairCache = new Cache(
			true, false, false, true, null, 10000);
	private static Map<String, String> languageCodeConversionMap = new HashMap<String, String>();
	private static ThreadLocal<LanguagePairValidator> languagePairValidator
			= new ThreadLocal<LanguagePairValidator>();
	private static Logger logger = Logger.getLogger(GoogleTranslation.class.getName());

	static {
		languageCodeConversionMap.put("he", "iw");
		languageCodeConversionMap.put("pt", "pt-PT");
	}
}
