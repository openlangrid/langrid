package jp.go.nict.langrid.service_1_3.foundation;

import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;

public interface GridManagementService {
	GridEntry getGridEntry()
	throws ProcessFailedException, ServiceConfigurationException;
}
