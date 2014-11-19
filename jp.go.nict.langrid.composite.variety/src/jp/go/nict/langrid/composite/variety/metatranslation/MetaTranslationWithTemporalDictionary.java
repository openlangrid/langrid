package jp.go.nict.langrid.composite.variety.metatranslation;

import jp.go.nict.langrid.language.LanguagePair;
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
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;
import jp.go.nict.langrid.wrapper.workflowsupport.ConstructSPForMTResult;
import jp.go.nict.langrid.wrapper.workflowsupport.ConstructSourceAndTaggedPartsForMetaTranslation;
import jp.go.nict.langrid.wrapper.workflowsupport.TaggedPart;

public class MetaTranslationWithTemporalDictionary
extends AbstractCompositeService
implements TranslationWithTemporalDictionaryService{
	@Override
	public String translate(String sourceLang, String targetLang,
			String source, Translation[] temporalDict, String dictTargetLang)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguagePairNotUniquelyDecidedException,
			NoAccessPermissionException, ProcessFailedException,
			NoValidEndpointsException, ServerBusyException,
			ServiceNotActiveException, ServiceNotFoundException,
			UnsupportedLanguagePairException {
		LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getLanguagePair();
		String src = new StringValidator("source", source)
				.notNull().trim().getValue();
		if(src.length() == 0) return "";
		Translation[] dict = temporalDict != null ? temporalDict : new Translation[]{};
		final String dictLang = (dictTargetLang != null)
				?  new LanguageValidator("dictTargetLang", dictTargetLang).getLanguage().getCode()
				: pair.getTarget().getCode();

		return doMetaTranslation(
				pair.getSource().getCode(), pair.getTarget().getCode(), src
				, dict, dictLang);
	}

	private String doMetaTranslation(String sourceLang, String targetLang, String source
			, Translation[] dict, String dictLang)
	throws AccessLimitExceededException, InvalidParameterException,
	LanguagePairNotUniquelyDecidedException,
	NoAccessPermissionException, ProcessFailedException,
	NoValidEndpointsException, ServerBusyException,
	ServiceNotActiveException, ServiceNotFoundException,
	UnsupportedLanguagePairException {
		ConstructSPForMTResult r = new ConstructSourceAndTaggedPartsForMetaTranslation()
			.constructSPForMT(sourceLang, source);
		TranslationWithTemporalDictionaryService trans = getComponentServiceFactory().getService(
				"TranslationWithTemporalDictionaryPL", TranslationWithTemporalDictionaryService.class);
		String transResult = trans.translate(
				sourceLang, targetLang, r.getText()
				, dict, dictLang);
		for(TaggedPart p : r.getParts()){
			transResult = transResult.replace(
					p.getCode()
					, String.format("<%s>%s</%1$s>(%s)"
							, p.getLang(), p.getText()
							, p.getLang().equals(targetLang)
									? p.getText()
									: doMetaTranslation(p.getLang(), targetLang, p.getText()
											, dict, dictLang)
							)
							);
		}
		return transResult;
	}
}
