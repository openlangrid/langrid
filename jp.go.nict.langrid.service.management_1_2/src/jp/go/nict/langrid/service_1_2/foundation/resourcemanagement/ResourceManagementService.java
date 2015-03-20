package jp.go.nict.langrid.service_1_2.foundation.resourcemanagement;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public interface ResourceManagementService {
	/**
	 * 
	 * 
	 */
	void clear()
		throws AccessLimitExceededException, NoAccessPermissionException
		, ServiceConfigurationException
		, UnknownException;

	/**
	 * 
	 * 
	 */
	ResourceEntrySearchResult searchResources(
			int startIndex, int maxCount, MatchingCondition[] conditions
			, Order[] orders, String scope)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException, UnsupportedMatchingMethodException;

	/**
	 * 
	 * 
	 */
	void addResource(String resourceId, ResourceProfile profile, ResourceInstance instance
			, Attribute[] attributes)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ResourceAlreadyExistsException
		, ServiceConfigurationException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void addResourceAs(String ownerUserId, String resourceId, ResourceProfile profile
			, ResourceInstance instance, Attribute[] attributes)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ResourceAlreadyExistsException
		, ServiceConfigurationException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void deleteResource(String resourceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, ResourceNotInactiveException
		, UnknownException
		;

	/**
	 * 
	 * 
	 */
	ResourceProfile getResourceProfile(String resourceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void setResourceProfile(String resourceId, ResourceProfile profile)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	ResourceInstance getResourceInstance(String resourceId)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, ResourceNotFoundException, UnknownException
	;

	/**
	 * 
	 * 
	 */
	void setResourceInstance(String resourceId, ResourceInstance instance)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, ResourceNotFoundException, UnknownException
	;

	/**
	 * 
	 * 
	 */
	Attribute[] getResourceAttributes(String resourceId, String[] attributeNames)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void setResourceAttributes(String resourceId, Attribute[] attributes)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void activateResource(String resourceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	void deactivateResource(String resourceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException
		;

	/**
	 * 
	 * 
	 */
	boolean isResourceActive(String resourceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException
		;
	
	/**
	 * 
	 * 
	 */
	void authorizeResource(String resourceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException
		;
	
	/**
	 * 
	 * 
	 */
	void deauthorizeResource(String resourceId)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException
		;
}
