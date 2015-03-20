package jp.go.nict.langrid.serviceexecutor.mocks;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.LanguageNotUniquelyDecidedException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.NoValidEndpointsException;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServerBusyException;
import jp.go.nict.langrid.service_1_2.ServiceNotActiveException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.UnsupportedLanguageException;
import jp.go.nict.langrid.service_1_2.similaritycalculation.SimilarityCalculationService;

public class SimilarityCalculation
implements SimilarityCalculationService{
	@Override
	public double calculate(String language, String text1, String text2)
			throws AccessLimitExceededException, InvalidParameterException,
			LanguageNotUniquelyDecidedException, NoAccessPermissionException,
			NoValidEndpointsException, ProcessFailedException,
			ServerBusyException, ServiceNotActiveException,
			ServiceNotFoundException, UnsupportedLanguageException {
		// TODO Auto-generated method stub
		return 0;
	}
}
