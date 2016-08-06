package jp.go.nict.langrid.service_1_3.foundation;

import java.util.Calendar;

import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;

public interface EntityManagementService {
	NewerAndOlderKeys getNewerAndOlderIds(
			String entityType, Calendar standardDateTime)
	throws ServiceConfigurationException, UnknownException;

	Object getEntity(String entityType, Object entityId)
	throws ServiceConfigurationException, UnknownException;

	Object getGridEntity();
}
