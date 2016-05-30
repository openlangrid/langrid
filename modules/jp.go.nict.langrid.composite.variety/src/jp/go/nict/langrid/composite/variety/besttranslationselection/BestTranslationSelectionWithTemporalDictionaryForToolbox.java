package jp.go.nict.langrid.composite.variety.besttranslationselection;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.commons.transformer.PassthroughTransformer;
import jp.go.nict.langrid.commons.transformer.TransformationException;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.util.Trio;
import jp.go.nict.langrid.composite.commons.thread.Task;
import jp.go.nict.langrid.composite.commons.thread.TaskGroup;
import jp.go.nict.langrid.composite.variety.besttranslationselection.BestTranslationSelectionForToolbox.ResultListener;
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
import jp.go.nict.langrid.service_1_2.bilingualdictionary.Translation;
import jp.go.nict.langrid.service_1_2.similaritycalculation.SimilarityCalculationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationWithTemporalDictionaryService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;

public class BestTranslationSelectionWithTemporalDictionaryForToolbox
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
		List<Trio<TranslationWithTemporalDictionaryService, TranslationWithTemporalDictionaryService, SimilarityCalculationService>> services
				= new ArrayList<Trio<TranslationWithTemporalDictionaryService, TranslationWithTemporalDictionaryService, SimilarityCalculationService>>();
		for(int i = 1; i <= 5; i++){
			TranslationWithTemporalDictionaryService forwardTrans = csf.getService(
					"ForwardTranslationWithTemporalDictionaryPL" + i, TranslationWithTemporalDictionaryService.class);
			TranslationWithTemporalDictionaryService backwardTrans = csf.getService(
					"BackwardTranslationWithTemporalDictionaryPL" + i, TranslationWithTemporalDictionaryService.class);
			SimilarityCalculationService sim = csf.getService(
					"SimilarityCalculationForTranslationWithTemporalDictionaryPL" + i, SimilarityCalculationService.class);
			if(forwardTrans == null || backwardTrans == null || sim == null){
				continue;
			}
			services.add(Trio.create(forwardTrans, backwardTrans, sim));
		}
		final ResultListener listener = newResultListener();
		TaskGroup group = new TaskGroup();
		for(Trio<TranslationWithTemporalDictionaryService, TranslationWithTemporalDictionaryService, SimilarityCalculationService> s : services){
			final TranslationWithTemporalDictionaryService forwardTrans = s.getFirst();
			final TranslationWithTemporalDictionaryService backwardTrans = s.getSecond();
			final SimilarityCalculationService simiCalc = s.getThird();
			group.add(new Task<Double>(){
				@Override
				protected void doWork() throws LangridException {
					String sl = pair.getSource().getCode();
					String tl = pair.getTarget().getCode();
					String f = forwardTrans.translate(sl, tl, src, tempDic, dictLang);
					Translation[] backwardTempDic = ArrayUtil.collect(
							tempDic, Translation.class, new PassthroughTransformer<Translation>(){
								@Override
								public Translation transform(Translation value)
										throws TransformationException {
									return new Translation(
											value.getTargetWords()[0]
									        , new String[]{value.getHeadWord()});
								}
							});
					String b = backwardTrans.translate(tl, sl, f, backwardTempDic, sl);
					double sim = simiCalc.calculate(sl, src, b);
					listener.onResult(src, f, b
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

	protected ResultListener newResultListener(){
		return new ResultListener();
	}
}
