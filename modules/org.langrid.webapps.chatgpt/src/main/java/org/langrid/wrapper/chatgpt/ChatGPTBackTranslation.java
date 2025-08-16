package org.langrid.wrapper.chatgpt;

import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.text.StringSubstitutor;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;

public class ChatGPTBackTranslation
extends ChatGPTService
implements BackTranslationService{
	public void setPromptTemplate(String promptTemplate){
		this.promptTemplate = promptTemplate;
	}

	@Override
	public BackTranslationResult backTranslate(String sourceLang, String intermediateLang, String source)
			throws AccessLimitExceededException, InvalidParameterException, LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, NoValidEndpointsException, ProcessFailedException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException, UnsupportedLanguagePairException {
		try {
			var key = resolveApikey();
			var params = new HashMap<String, Object>();
			params.put("sourceLang", Language.parse(sourceLang).getLocalizedName(Locale.US));
			params.put("intermediateLang", Language.parse(intermediateLang).getLocalizedName(Locale.US));
			params.put("source", source);
			var prompt = StringSubstitutor.replace(promptTemplate, params, "${", "}");
			var ret = JSON.decode(execute(key, prompt), Result.class);
			return new BackTranslationResult(ret.getTranslation(), ret.getBacktranslation());
		} catch(InvalidLanguageTagException e) {
			throw new ProcessFailedException(e);
		}
	}

	private String promptTemplate = "Translate and back translate the sentence from ${sourceLang} to ${intermediateLang}. "
			+ "Return the results in JSON format like"
			+ " {\"translation\": \"translation result\", \"backtranslation\": \"back translation result\"}\n\n${source}";
}

class Result{
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	public String getBacktranslation() {
		return backtranslation;
	}
	public void setBacktranslation(String backtranslation) {
		this.backtranslation = backtranslation;
	}
	private String translation;
	private String backtranslation;
}
