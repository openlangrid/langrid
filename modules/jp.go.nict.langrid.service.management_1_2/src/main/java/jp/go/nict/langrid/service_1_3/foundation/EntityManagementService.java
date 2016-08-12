package jp.go.nict.langrid.service_1_3.foundation;

import java.util.Calendar;

import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;

public interface EntityManagementService {
	NewerAndOlderKeys getNewerAndOlderKeys(
			String entityType, Calendar standardDateTime, MatchingCondition... conditions)
	throws ServiceConfigurationException, UnknownException;

	Object getEntity(String entityType, Object entityId)
	throws ServiceConfigurationException, UnknownException;

	Object getGridEntity();
}
