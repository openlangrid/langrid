package org.langrid.wrapper.chatgpt;

import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.text.StringSubstitutor;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
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
import jp.go.nict.langrid.service_1_2.translation.TranslationService;

public class ChatGPTTranslation extends ChatGPTService implements TranslationService {
	public void setPromptTemplate(String promptTemplate){
		this.promptTemplate = promptTemplate;
	}

	@Override
	public String translate(String sourceLang, String targetLang, String source)
			throws AccessLimitExceededException, InvalidParameterException, LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, ProcessFailedException, NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException, UnsupportedLanguagePairException {
		try {
			var srcLang = Language.parse(sourceLang).getLocalizedName(Locale.US);
			var tgtLang = Language.parse(targetLang).getLocalizedName(Locale.US);
			var params = new HashMap<String, Object>();
			params.put("sourceLang", srcLang);
			params.put("targetLang", tgtLang);
			params.put("source", source);
			var prompt = StringSubstitutor.replace(promptTemplate, params, "${", "}");
			System.out.println(prompt);
			return execute(prompt);
		} catch(InvalidLanguageTagException e) {
			throw new ProcessFailedException(e);
		}
	}

	private String promptTemplate = "Translate the sentence from ${sourceLang} to ${targetLang}. "
			+ "Response only translated sentence.\n\n${source}";
}
