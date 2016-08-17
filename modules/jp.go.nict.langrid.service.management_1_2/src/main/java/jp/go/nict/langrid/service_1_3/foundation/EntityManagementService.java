package jp.go.nict.langrid.service_1_3.foundation;

import java.util.Calendar;
import java.util.Collection;

import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;

public interface EntityManagementService {
	NewerAndOlderKeys getNewerAndOlderKeys(
			String entityType, Calendar standardDateTime, MatchingCondition... conditions)
	throws ProcessFailedException, ServiceConfigurationException;

	Object getEntity(String entityType, Object entityId)
	throws ProcessFailedException, ServiceConfigurationException;

	Collection<KeyAndUpdate> getKeysAndUpdates(String entityType, MatchingCondition... conditions)
	throws ProcessFailedException, ServiceConfigurationException;

	void setEntity(String entityType, Object entity)
	throws ProcessFailedException, ServiceConfigurationException;
}
