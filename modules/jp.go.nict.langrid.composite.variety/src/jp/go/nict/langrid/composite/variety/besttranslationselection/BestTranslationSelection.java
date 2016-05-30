package jp.go.nict.langrid.composite.variety.besttranslationselection;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.composite.commons.thread.Task;
import jp.go.nict.langrid.composite.commons.thread.TaskGroup;
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
import jp.go.nict.langrid.service_1_2.backtranslation.BackTranslationService;
import jp.go.nict.langrid.service_1_2.similaritycalculation.SimilarityCalculationService;
import jp.go.nict.langrid.service_1_2.translation.TranslationService;
import jp.go.nict.langrid.service_1_2.util.validator.LanguagePairValidator;
import jp.go.nict.langrid.service_1_2.util.validator.LanguageValidator;
import jp.go.nict.langrid.service_1_2.util.validator.StringValidator;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;
import jp.go.nict.langrid.servicecontainer.service.composite.AbstractCompositeService;

public class BestTranslationSelection
extends AbstractCompositeService
implements TranslationService{
	static class ResultListener{
		public String getCurrentResult() {
			return currentResult;
		}
		public double getCurrentSimilarity() {
			return currentSimilarity;
		}
		public LangridException getFirstException() {
			return firstException;
		}
		synchronized void onResult(String source, String fwTrans, String bwTrans
				, double similarity, long dt){
			if(similarity > currentSimilarity){
				currentResult = fwTrans;
				currentSimilarity = similarity;
			}
		}
		synchronized void onException(LangridException e){
			if(firstException != null){
				firstException = e;
			}
		}
		private String currentResult;
		private double currentSimilarity = -1;
		private LangridException firstException;
	}

	@Override
	public String translate(String sourceLang, String targetLang,
			String source)
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

		ComponentServiceFactory csf = getComponentServiceFactory();
		List<Pair<BackTranslationService, SimilarityCalculationService>>
			services = new ArrayList<Pair<BackTranslationService, SimilarityCalculationService>>();
		for(int i = 1; i <= 5; i++){
			BackTranslationService backTrans = csf.getService(
					"BackTranslationPL" + i, BackTranslationService.class);
			SimilarityCalculationService simiCalc = csf.getService(
					"SimilarityCalculationPL" + i, SimilarityCalculationService.class);
			if(backTrans == null || simiCalc == null){
				continue;
			}
			services.add(Pair.create(backTrans, simiCalc));
		}
		final ResultListener listener = newResultListener();
		TaskGroup group = new TaskGroup();
		for(Pair<BackTranslationService, SimilarityCalculationService> s : services){
			final BackTranslationService backTrans = s.getFirst();
			final SimilarityCalculationService simiCalc = s.getSecond();
			group.add(new Task<Double>(){
				@Override
				protected void doWork() throws LangridException {
					BackTranslationResult r = backTrans.backTranslate(
							pair.getSource().getCode(), pair.getTarget().getCode(), src);
					String forward = r.getIntermediate();
					String backward = r.getTarget();
					double sim = simiCalc.calculate(
							pair.getSource().getCode(), src, backward);
					listener.onResult(src, forward, backward, sim
							, System.currentTimeMillis() - getStartTime());
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
