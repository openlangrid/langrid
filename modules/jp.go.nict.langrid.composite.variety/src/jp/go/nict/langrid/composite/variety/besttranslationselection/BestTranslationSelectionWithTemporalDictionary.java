package jp.go.nict.langrid.composite.variety.besttranslationselection;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.composite.commons.thread.Task;
import jp.go.nict.langrid.composite.commons.thread.TaskGroup;
import jp.go.nict.langrid.composite.commons.thread.TaskManager;
import jp.go.nict.langrid.composite.variety.besttranslationselection.BestTranslationSelection.ResultListener;
import jp.go.nict.langrid.language.LanguagePair;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LangridException;
import jp.go.nict.langrid.service_1_2.LanguagePairNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguagePairException;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationResult;
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.similaritycalculation.SimilarityCalculationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;

public class BestTranslationSelectionWithTemporalDictionary
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
		final LanguagePair pair = new LanguagePairValidator(
				new LanguageValidator("sourceLang", sourceLang)
				, new LanguageValidator("targetLang", targetLang)
				).notNull().trim().notEmpty().getLanguagePair();
		final String src = new StringValidator("source", source)
				.notNull().trim().notEmpty().getValue();
		final Translation[] tempDic = (temporalDict != null) ? temporalDict : new Translation[]{};
		final String dictLang = (dictTargetLang != null)
			?  new LanguageValidator("dictTargetLang", dictTargetLang).getLanguage().getCode()
			: pair.getTarget().getCode();

		ComponentServiceFactory csf = getComponentServiceFactory();
		List<Pair<BackTranslationWithTemporalDictionaryService, SimilarityCalculationService>> services
				= new ArrayList<Pair<BackTranslationWithTemporalDictionaryService, SimilarityCalculationService>>();
		for(int i = 1; i <= 5; i++){
			BackTranslationWithTemporalDictionaryService backTrans = csf.getService(
					"BackTranslationWithTemporalDictionaryPL" + i, BackTranslationWithTemporalDictionaryService.class);
			SimilarityCalculationService sim = csf.getService(
					"SimilarityCalculationPL" + i, SimilarityCalculationService.class);
			if(backTrans != null && sim != null){
				services.add(Pair.create(backTrans, sim));
			}
		}
		final ResultListener listener = newResultListener();
		TaskGroup group = new TaskGroup();
		for(Pair<BackTranslationWithTemporalDictionaryService, SimilarityCalculationService> s : services){
			final BackTranslationWithTemporalDictionaryService backTrans = s.getFirst();
			final SimilarityCalculationService simiCalc = s.getSecond();
			group.add(new Task<Double>(){
				@Override
				protected void doWork() throws LangridException {
					BackTranslationResult r = backTrans.backTranslate(
							pair.getSource().getCode(), pair.getTarget().getCode()
							, src, tempDic, dictLang);
					double sim = simiCalc.calculate(
							pair.getSource().getCode(), src, r.getTarget());
					listener.onResult(src, r.getIntermediate(), r.getTarget()
							, sim, System.currentTimeMillis() - this.getStartTime());
				}
			});
		}
		try{
			group.runAndWait();
		} catch(InterruptedException e){
			throw new ProcessFailedException(e);
		}
		String r = listener.getCurrentResult();
		if(r != null){
			return r;
		}
		if(listener.getFirstException() != null){
			throw new ProcessFailedException(listener.getFirstException());
		}
		throw new ProcessFailedException("no translation succeeded");	
	}

	public TaskManager getTaskManager(){
		return manager;
	}

	protected ResultListener newResultListener(){
		return new ResultListener();
	}

	private static TaskManager manager = new TaskManager();
}
