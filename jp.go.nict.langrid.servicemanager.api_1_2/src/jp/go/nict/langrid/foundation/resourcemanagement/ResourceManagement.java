package jp.go.nict.langrid.foundation.resourcemanagement;

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.ADMINONLY;
import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.RESOURCEOWNER_OR_ADMIN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.validator.annotation.EachElement;
import jp.go.nict.langrid.commons.validator.annotation.IntInRange;
import jp.go.nict.langrid.commons.validator.annotation.IntNotNegative;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.NotNull;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.ResourceDao;
import jp.go.nict.langrid.dao.ResourceSearchResult;
import jp.go.nict.langrid.dao.ResourceUtil;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.AttributedElementUpdater;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.Log;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidAttribute;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidEnum;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidMatchingCondition;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidOrder;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidResourceId;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidUserId;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.UnsupportedMatchingMethodException;
import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceAlreadyExistsException;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceEntry;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceInstance;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceManagementService;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceNotInactiveException;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.ResourceProfile;
import jp.go.nict.langrid.service_1_2.foundation.resourcemanagement.typed.ResourceAddTimeOnlyAttribute;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.AttributeName;
import jp.go.nict.langrid.service_1_2.foundation.typed.Scope;
import jp.go.nict.langrid.service_1_2.foundation.util.ProfileKeyUtil;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;
import jp.go.nict.langrid.service_1_2.util.ParameterValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public class ResourceManagement 
extends AbstractLangridService
implements ResourceManagementService
{
	public ResourceManagement() {
	}

	public ResourceManagement(ServiceContext context) {
		super(context);
	}
	
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	public void clear()
	throws AccessLimitExceededException, NoAccessPermissionException
		, ServiceConfigurationException, UnknownException 
	{
		try {
			getResourceDao().clear();
		} catch(DaoException e) {
			e.printStackTrace();
		} catch(Throwable t) {
			t.printStackTrace();
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod
	@ValidatedMethod
	@TransactionMethod
	public ResourceEntrySearchResult searchResources(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @EachElement @ValidMatchingCondition MatchingCondition[] conditions
			, @NotNull @EachElement @ValidOrder Order[] orders
			, @NotEmpty @ValidEnum(Scope.class) String scope)
	throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException, UnknownException
		, UnsupportedMatchingMethodException 
	{
		jp.go.nict.langrid.dao.MatchingCondition[] conds = null;
		try{
			conds = convert(
					conditions
					, jp.go.nict.langrid.dao.MatchingCondition[].class
					);
			for(jp.go.nict.langrid.dao.MatchingCondition c : conds){
				if(c.getFieldName().equals("registeredDate")){
					c.setFieldName("createdDateTime");
				} else if(c.getFieldName().equals("updatedDate")){
					c.setFieldName("updatedDateTime");
				}
			}
		} catch(ConversionException e){
			// 
			// Always return emtpy results for weird search conditions.
			// 
			logger.log(Level.INFO, "illegal matching condition specified: " + Arrays.toString(conditions), e);
			return new ResourceEntrySearchResult();
		}
		jp.go.nict.langrid.dao.Order[] ords = null;
		try{
			ords = convert(orders, jp.go.nict.langrid.dao.Order[].class);
			for(jp.go.nict.langrid.dao.Order o : ords){
				if(o.getFieldName().equals("registeredDate")){
					o.setFieldName("createdDateTime");
				} else if(o.getFieldName().equals("updatedDate")){
					o.setFieldName("updatedDateTime");
				}
			}
		} catch(ConversionException e){
			throw new InvalidParameterException(
					"orders", e.getMessage()
					);
		}
		
		if(scope.equalsIgnoreCase("ALL")){
			return searchAllResources(startIndex, maxCount, conds, ords);
		} else if(scope.equalsIgnoreCase("MINE")){
			return searchMyResources(startIndex, maxCount, conds, ords);
		} else if(scope.equalsIgnoreCase("ACCESSIBLE")){
			return searchAllResources(startIndex, maxCount, conds, ords);
		}
		return new ResourceEntrySearchResult(new ResourceEntry[]{}, 0, true);
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void addResource(
			@NotEmpty @ValidResourceId String resourceId
			, @NotNull ResourceProfile profile
			, @NotNull ResourceInstance instance
			, @NotNull @EachElement @ValidAttribute Attribute[] attributes)
	throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ResourceAlreadyExistsException, ServiceConfigurationException,
			UnknownException 
	{
		String authUserId = getServiceContext().getAuthUser();
		doAddResource(authUserId, getGridId(), resourceId, profile, instance, attributes);
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void addResourceAs(
			@NotEmpty @ValidUserId String ownerUserId
			, @NotEmpty @ValidResourceId String resourceId
			, @NotNull ResourceProfile profile
			, @NotNull ResourceInstance instance
			, @NotNull @EachElement @ValidAttribute Attribute[] attributes)
	throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ResourceAlreadyExistsException,
			ServiceConfigurationException, UnknownException 
	{
		try{
			getUserDao().getUser(getGridId(), ownerUserId);
		} catch(UserNotFoundException e){
			throw new InvalidParameterException(
					"ownerUserId"
					, "User \"" + ownerUserId + "\" not exist.");
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
		doAddResource(ownerUserId, getGridId(), resourceId, profile, instance, attributes);
	}

	@AccessRightValidatedMethod(policy=RESOURCEOWNER_OR_ADMIN)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void deleteResource(
			@NotEmpty @ValidResourceId String resourceId)
	throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			ResourceNotFoundException, ResourceNotInactiveException,
			UnknownException 
	{
		try {
			Resource resource = getResourceDao().getResource(getGridId(), resourceId);
			if(resource.isActive()) {
				throw new ResourceNotInactiveException(resourceId);
			}
			getResourceDao().deleteResource(getGridId(), resourceId);
		} catch(jp.go.nict.langrid.dao.ResourceNotFoundException e) {
			throw convertException(e);
		} catch(DaoException e) {
			throw convertException(e);
		} catch(Throwable t) {
			throw ExceptionConverter.convertException(t);
		}
	}

	@AccessRightValidatedMethod
	@ValidatedMethod
	@TransactionMethod
	public ResourceProfile getResourceProfile(
			@NotEmpty @ValidResourceId String resourceId)
	throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			ResourceNotFoundException, UnknownException
	{
		try {
			return convert(
				getResourceDao().getResource(getGridId(), resourceId)
				, ResourceProfile.class
			);
		} catch(jp.go.nict.langrid.dao.ResourceNotFoundException e) {
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=RESOURCEOWNER_OR_ADMIN)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void setResourceProfile(
			@NotEmpty @ValidResourceId String resourceId
			, @NotNull ResourceProfile profile)
	throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, ResourceNotFoundException,
			UnknownException 
	{
		Resource resource;
		try {
			resource = getResourceDao().getResource(getGridId(), resourceId);
			copyProperties(resource, profile);
		} catch(jp.go.nict.langrid.dao.ResourceNotFoundException e) {
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(ConversionException e){
			Throwable t = e;
			if(e.getCause() != null){
				t = e.getCause();
			}
			throw new InvalidParameterException(
					"profile", t.toString());
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod
	@ValidatedMethod
	@TransactionMethod
	public ResourceInstance getResourceInstance(String resourceId)
	throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException 
	{
		try {
			return convert(
				getResourceDao().getResource(getGridId(), resourceId)
				, ResourceInstance.class
			);
		} catch(jp.go.nict.langrid.dao.ResourceNotFoundException e) {
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=RESOURCEOWNER_OR_ADMIN)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void setResourceInstance(String resourceId, ResourceInstance instance)
	throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException 
	{
		Resource resource;
		try {
			resource = getResourceDao().getResource(getGridId(), resourceId);
			copyProperties(resource, instance);
		} catch(jp.go.nict.langrid.dao.ResourceNotFoundException e) {
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(ConversionException e){
			Throwable t = e;
			if(e.getCause() != null){
				t = e.getCause();
			}
			throw new InvalidParameterException(
					"instance", t.toString());
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod
	@ValidatedMethod
	@TransactionMethod
	public Attribute[] getResourceAttributes(
			String resourceId
			, String[] attributeNames)
	throws AccessLimitExceededException
		, InvalidParameterException, NoAccessPermissionException
		, ServiceConfigurationException, ResourceNotFoundException
		, UnknownException 
	{
		Set<String> names = new HashSet<String>();
		for(String n : attributeNames){
			names.add(n);
		}
		try{
			ResourceDao dao = getResourceDao();
			Resource resource = dao.getResource(getGridId(), resourceId);
			List<Attribute> attrs = new ArrayList<Attribute>();
			if(names.size() == 0){
				for(jp.go.nict.langrid.dao.entity.Attribute a : resource.getAttributes()){
					attrs.add(new Attribute(a.getName(), a.getValue()));
				}
			} else{
				for(jp.go.nict.langrid.dao.entity.Attribute a : resource.getAttributes()){
					if(names.contains(a.getName())){
						attrs.add(new Attribute(a.getName(), a.getValue()));
					}
				}
			}
			return attrs.toArray(new Attribute[]{});
		} catch(jp.go.nict.langrid.dao.ResourceNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=RESOURCEOWNER_OR_ADMIN)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void setResourceAttributes(
			@NotEmpty @ValidResourceId String resourceId
			, @EachElement @ValidAttribute Attribute[] attributes)
	throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			ResourceNotFoundException, UnknownException 
	{
		validateInputAttribute("attributes", attributes);
		try{
			ResourceDao dao = getResourceDao();
			Resource lr = dao.getResource(getGridId(), resourceId);
			AttributedElementUpdater.updateAttributes(
					lr, attributes 
					, ResourceUtil.getProperties());
		} catch(jp.go.nict.langrid.dao.ResourceAlreadyExistsException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}
	
	@AccessRightValidatedMethod(policy=RESOURCEOWNER_OR_ADMIN)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void activateResource(
			@NotEmpty @ValidResourceId String resourceId)
	throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException 
	{
		setActive(resourceId, true);
	}
	
	@AccessRightValidatedMethod(policy=RESOURCEOWNER_OR_ADMIN)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void deactivateResource(
			@NotEmpty @ValidResourceId String resourceId)
	throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException 
	{
		setActive(resourceId, false);
	}

	@AccessRightValidatedMethod
	@ValidatedMethod
	@TransactionMethod
	public boolean isResourceActive(
			@NotEmpty @ValidResourceId String resourceId)
	throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException 
	{
		try {
			return getResourceDao().getResource(getGridId(), resourceId).isActive();
		} catch(jp.go.nict.langrid.dao.ResourceNotFoundException e) {
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=RESOURCEOWNER_OR_ADMIN)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void authorizeResource(
			@NotEmpty @ValidResourceId String resourceId)
	throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException
	{
		setAuthorize(resourceId, true);
	}

	@AccessRightValidatedMethod(policy=RESOURCEOWNER_OR_ADMIN)
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void deauthorizeResource(
			@NotEmpty @ValidResourceId String resourceId)
	throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, ResourceNotFoundException, UnknownException 
	{
		setAuthorize(resourceId, false);
	}

	/**
	 * 
	 * 
	 */
	protected static void validateInputAttribute(
			String parameterName, Attribute[] attributes)
		throws InvalidParameterException
	{
		ParameterValidator.objectNotNull(parameterName, attributes);

		Map<String, String> attrs = new HashMap<String, String>();
		for(Attribute e : attributes){
			if(e.getName() == null){
				throw new InvalidParameterException(
					parameterName, "has null key");
			}
			if(e.getName().length() == 0){
				throw new InvalidParameterException(
					parameterName, "has 0 length key");
			}
			attrs.put(e.getName(), e.getValue());
		}

		for(AttributeName key : ProfileKeyUtil.readonlyKeys){
			assertUnreservedKey(parameterName, attrs, key);
		}
	}
	
	private ResourceEntrySearchResult searchAllResources(
			int startIndex, int maxCount
			, jp.go.nict.langrid.dao.MatchingCondition[] conditions
			, jp.go.nict.langrid.dao.Order[] orders
			)
		throws AccessLimitExceededException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException
	{
		try{
			ResourceSearchResult result = getResourceDao().searchResources(
					startIndex, maxCount, getGridId(), conditions, orders
			);
			return convert(result, ResourceEntrySearchResult.class);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}
	
	private ResourceEntrySearchResult searchMyResources(
			int startIndex
			, int maxCount
			, jp.go.nict.langrid.dao.MatchingCondition[] conditions
			, jp.go.nict.langrid.dao.Order[] orders
			)
		throws AccessLimitExceededException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException
	{
		try{
			conditions = ArrayUtil.append(
					conditions
					, new jp.go.nict.langrid.dao.MatchingCondition(
							"ownerUserId"
							, getUserChecker().getUser().getUserId()
							, MatchingMethod.COMPLETE
					)
					);
			ResourceSearchResult result = getResourceDao().searchResources(
					startIndex, maxCount, getGridId(), conditions, orders
					);
			return convert(result, ResourceEntrySearchResult.class);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			e.printStackTrace();
			throw ExceptionConverter.convertException(e);
		}
	}

	private void doAddResource(
			String ownerUserId, String gridId, String resourceId, ResourceProfile profile
			, ResourceInstance instance, Attribute[] attributes)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ResourceAlreadyExistsException
	, ServiceConfigurationException, UnknownException
	{
		Map<String, String> attrs = new HashMap<String, String>();
		try{
			// attributes check
			for(Attribute a : attributes){
				if(a.getName() == null){
					throw new InvalidParameterException("attributes[].name", "is null");
				}
				String name = a.getName().trim();
				if(name.length() == 0){
					throw new InvalidParameterException("attributes[].name", "is empty");
				}
				if(a.getValue() == null){
					throw new InvalidParameterException("attributes[\"" + name + "\"].value", "is null");
				}
				String value = a.getValue().trim();
				if(value == null){
					throw new InvalidParameterException("attributes[\"" + name + "\"].value", "is empty");
				}
				attrs.put(name, value);
			}
		} catch(InvalidParameterException e){
			throw e;
		} catch(Throwable t){
			t.printStackTrace();
			throw ExceptionConverter.convertException(t);
		}
		try{
			ResourceDao dao = getResourceDao();
			Resource resource = new Resource();
			resource.setGridId(gridId);
			resource.setResourceId(resourceId);
			resource.setOwnerUserId(ownerUserId);
			copyProperties(resource, profile);
			copyProperties(resource, instance);
			copyAttributes(resource, attributes, ResourceAddTimeOnlyAttribute.values());
			dao.addResource(resource);
			
			// 
			// 
			String activeString = attrs.get(
					ResourceAddTimeOnlyAttribute.defaultActive.name()
					);
			if(activeString != null && activeString.equalsIgnoreCase("true")){
				resource.setActive(true);
			}
		} catch(jp.go.nict.langrid.dao.ResourceAlreadyExistsException e){
			throw new ResourceAlreadyExistsException(e.getResourceId());
		} catch(jp.go.nict.langrid.dao.DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			e.printStackTrace();
			throw ExceptionConverter.convertException(e);
		}
	}

	private void setActive(String resourceId, boolean isActive) throws UnknownException {
		Resource lr = null;
		try {
			lr = getResourceDao().getResource(getGridId(), resourceId);
			if(lr == null || lr.isActive() == isActive){
				return;
			}
			lr.setActive(isActive);
			lr.touchUpdatedDateTime();
		} catch(jp.go.nict.langrid.dao.ResourceNotFoundException e) {
			convertException(e);
		} catch(DaoException e) {
			throw ExceptionConverter.convertException(e);
		} catch(Throwable t) {
			throw ExceptionConverter.convertException(t);
		}
	}
	
	private void setAuthorize(String resourceId, boolean isAuthorize) throws UnknownException {
		Resource lr = null;
		try {
			lr = getResourceDao().getResource(getGridId(), resourceId);
			if(lr == null || lr.isApproved() == isAuthorize){
				return;
			}
			lr.setApproved(isAuthorize);
			lr.touchUpdatedDateTime();
		} catch(jp.go.nict.langrid.dao.ResourceNotFoundException e) {
			convertException(e);
		} catch(DaoException e) {
			throw ExceptionConverter.convertException(e);
		} catch(Throwable t) {
			throw ExceptionConverter.convertException(t);
		}
	}
	
	/**
	 * 
	 * 
	 */
	static void assertUnreservedKey(
		String parameterName, Map<String, String> attributes, AttributeName key
		)
		throws InvalidParameterException
	{
		if(attributes.get(key.getAttributeName()) != null){
			throwReservedKeyException(parameterName, key);
		}
	}

	/**
	 * 
	 * 
	 */
	static void throwReservedKeyException(
		String parameterName, AttributeName key)
		throws InvalidParameterException
	{
		throw new InvalidParameterException(
			String.format("%s.key[\"%s\"]", parameterName, key.getAttributeName())
			, "is reserved"
			);
	}

	private static Logger logger = Logger.getLogger(
			ResourceManagement.class.getName());
}
