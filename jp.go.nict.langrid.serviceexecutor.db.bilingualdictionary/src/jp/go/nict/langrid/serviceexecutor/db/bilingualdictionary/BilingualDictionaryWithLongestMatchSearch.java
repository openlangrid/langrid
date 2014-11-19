package jp.go.nict.langrid.serviceexecutor.db.bilingualdictionary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.transformer.CodeStringToLanguageTransformer;
import jp.go.nict.langrid.language.util.LanguagePairUtil;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePair;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.BilingualDictionaryWithLongestMatchSearchService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.TranslationWithPosition;
import jp.go.nict.langrid.service_1_2.morphologicalanalysis.Morpheme;
import jp.go.nict.langrid.service_1_2.transformer.LanguagePair_LanguageToWITransformer;
import jp.go.nict.langrid.service_1_2.typed.MatchingMethod;
import jp.go.nict.langrid.serviceexecutor.db.ConnectionManager;
import jp.go.nict.langrid.serviceexecutor.db.ConnectionParameters;
import jp.go.nict.langrid.serviceexecutor.db.DataBaseUtil;
import jp.go.nict.langrid.serviceexecutor.db.DictionaryDataBase;

public class BilingualDictionaryWithLongestMatchSearch
implements BilingualDictionaryWithLongestMatchSearchService{
	public void setConnectionParameters(ConnectionParameters p){
		this.conParams = p;
	}

	public void setTableName(String tableName){
		this.tableName = tableName;
	}

	public void setLanguageColumnNames(String languageColumnNames){
		String[] languages = languageColumnNames.split("\\s");
		Collection<jp.go.nict.langrid.language.LanguagePair> langs
				= new ArrayList<jp.go.nict.langrid.language.LanguagePair>();
		LanguagePairUtil.addBidirectionalRoundrobinformedPairs(
				langs
				, ArrayUtil.collect(
						languages
						, new CodeStringToLanguageTransformer())
				);
		this.supportedLanguagePairs = ArrayUtil.collect(
				langs.toArray(new jp.go.nict.langrid.language.LanguagePair[]{})
				, new LanguagePair_LanguageToWITransformer()
				);
	}

	public void setDateColumnName(String dateColumnName){
		this.dateColumnName = dateColumnName;
	}

	@Override
	public Translation[] search(
			String headLang, String targetLang
			, String headword, String matchingMethod)
	throws InvalidParameterException, ProcessFailedException {
		checkConnectionValid();
		try {
			DictionaryDataBase ddb = new DictionaryDataBase(
					tableName, manager.get()
					, conParams.getDbDictionary(), maxResults
					);
			Collection<Translation> ret = ddb.getTranslation(
					Language.parse(headLang)
					, Language.parse(targetLang)
					, headword
					, MatchingMethod.valueOf(matchingMethod)
					);
			return ret.toArray(new Translation[]{});
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "failed to sql execute: " , e);
			throw new ProcessFailedException(e);
		} catch (InvalidLanguageTagException e) {
			throw new InvalidParameterException(
					"headLang", "or targetLang is invalid language expression.");
		}
	}

	@Override
	public TranslationWithPosition[] searchLongestMatchingTerms(
			String headLang, String targetLang, Morpheme[] morphemes)
			throws InvalidParameterException, ProcessFailedException {
		DictionaryDataBase ddb = new DictionaryDataBase(
				tableName, manager.get()
				, conParams.getDbDictionary(), maxResults
				);
		try{
			return QueryCentricSearch.search(
					ddb
					, Language.parse(headLang)
					, Language.parse(targetLang)
					, morphemes
					, cacheSearchResults).toArray(
							new TranslationWithPosition[]{}
							);
		} catch(SQLException e){
			throw new ProcessFailedException(e);
		} catch (InvalidLanguageTagException e) {
			throw new InvalidParameterException(
					"headLang", "or targetLang is invalid language expression.");
		}
	}

	@Override
	public Calendar getLastUpdate() throws ProcessFailedException {
		checkConnectionValid();
		Date date = DataBaseUtil.getLastUpdate(
				tableName, dateColumnName, manager.get()
				);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	@Override
	public LanguagePair[] getSupportedLanguagePairs()
			throws AccessLimitExceededException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException {
		return supportedLanguagePairs;
	}

	@Override
	public String[] getSupportedMatchingMethods()
			throws AccessLimitExceededException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException {
		return supportedMatchingMethods;
	}

	private void checkConnectionValid() throws ProcessFailedException{
		if(manager == null){
			throw new ProcessFailedException("failed to connect database.");
		}
	}

	private ThreadLocal<ConnectionManager> manager = new ThreadLocal<ConnectionManager>(){
		protected ConnectionManager initialValue(){
			try{
				return new ConnectionManager(conParams);
			} catch(NamingException e){
				logger.log(Level.SEVERE, "failed to create ConnectionManager.", e);
				return null;
			}
		};
	};
	private ConnectionParameters conParams = new ConnectionParameters();
	private String tableName;
	private String dateColumnName = "date";
	private LanguagePair[] supportedLanguagePairs = {new LanguagePair("*", "*")};
	private String[] supportedMatchingMethods = {"COMPLETE", "PARTIAL", "PREFIX", "SUFFIX", "REGEX"};
	private int maxResults = 200;
	private boolean cacheSearchResults = true;

	private static Logger logger = Logger.getLogger(
			BilingualDictionaryWithLongestMatchSearch.class.getName()
			);
}
