package jp.go.nict.langrid.wrapper.ws_1_2.translationselection;

import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.translationselection.SelectionResult;
import jp.go.nict.langrid.service_1_2.translationselection.TranslationSelectionService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.wrapper.ws_1_2.AbstractLanguagePairService;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 409 $
 */
public abstract class AbstractTranslationSelectionService extends AbstractLanguagePairService 
implements TranslationSelectionService
{
	@Override
	public SelectionResult select(String sourceLang, String targetLang, String source)
	throws ProcessFailedException, LanguagePairNotUniquelyDecidedException
		, UnsupportedLanguagePairException, InvalidParameterException
	{
		// validate input parameter
		checkStartupException();
		jp.go.nict.langrid.language.LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("headLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getUniqueLanguagePair(
						getSupportedLanguagePairCollection());
		String s = new StringValidator("source", source).notNull().trim().getValue();
		return doSelect(pair.getSource(), pair.getTarget(), s);
	}
	
	protected abstract SelectionResult doSelect(Language sourceLang, Language targetLang,
			String source);
}
