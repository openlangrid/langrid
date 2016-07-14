/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.foundation.usermanagement;

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.ADMINONLY;
import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.SELF_OR_ADMIN;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.validator.annotation.EachElement;
import jp.go.nict.langrid.commons.validator.annotation.IntInRange;
import jp.go.nict.langrid.commons.validator.annotation.IntNotNegative;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.NotNull;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.UserUtil;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.entity.UserRole;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.AttributedElementUpdater;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.Log;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidAttribute;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidAttributeName;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidMatchingCondition;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidOrder;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidPassword;
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
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.InvalidUserIdException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserAlreadyExistsException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserProfile;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;
import jp.go.nict.langrid.service_1_2.util.ParameterValidator;
import jp.go.nict.langrid.service_1_3.foundation.NewerAndOlderUserIds;
import jp.go.nict.langrid.service_1_3.foundation.UserEntry;
import jp.go.nict.langrid.service_1_3.foundation.UserManagementService;

/**
 * 
 * User administration service.
 * 
 * @author Takao Nakaguchi
 */
public class UserManagement
extends AbstractLangridService
implements UserManagementService{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public UserManagement(){
	}

	/**
	 * 
	 * Constructor.
	 * @param serviceContext Service context
	 * 
	 */
	public UserManagement(ServiceContext serviceContext){
		super(serviceContext);
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@Log
	public void clear()
			throws AccessLimitExceededException, NoAccessPermissionException
			, ServiceConfigurationException, UnknownException
	{
		try{
			getServiceLogic().clear();
			getUserLogic().clear();
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@Log
	public void initialize()
			throws AccessLimitExceededException, NoAccessPermissionException
			, ServiceConfigurationException, UnknownException
	{
		try{
			getUserLogic().initialize();
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			UnknownException ex = ExceptionConverter.convertException(e);
			ex.printStackTrace();
			throw ex;
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	public UserEntrySearchResult searchUsers(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @EachElement @ValidMatchingCondition MatchingCondition[] conditions
			, @NotNull @EachElement @ValidOrder Order[] orders
			)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UnsupportedMatchingMethodException
	{
		jp.go.nict.langrid.dao.Order[] o = null;
		adjustDateFieldName(orders);
		try{
			o = convert(orders, jp.go.nict.langrid.dao.Order[].class);
		} catch(ConversionException e){
			throw new InvalidParameterException(
					"orders", e.getMessage()
					);
		}

		jp.go.nict.langrid.dao.MatchingCondition[] conds = null;
		adjustDateFieldName(conditions);
		try{
			conds = convert(conditions
					, jp.go.nict.langrid.dao.MatchingCondition[].class);
		} catch(ConversionException e){	
			throw new InvalidParameterException(
					"conditions", e.getMessage()
			);
		}

		try{
			return convert(
					getUserLogic().searchUsers(startIndex, maxCount, getGridId(), conds, o)
					, UserEntrySearchResult.class
					);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	public UserEntrySearchResult searchUsersShouldChangePassword(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @IntNotNegative int days
			, @NotNull @EachElement @ValidOrder Order[] orders
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException
	{
		jp.go.nict.langrid.dao.Order[] o = null;
		adjustDateFieldName(orders);
		try{
			o = convert(orders, jp.go.nict.langrid.dao.Order[].class);
		} catch(ConversionException e){
			throw new InvalidParameterException(
					"orders", e.getMessage()
					);
		}

		try{
			return convert(
					getUserLogic().searchUsersShouldChangePassword(
							startIndex, maxCount, getGridId(), days, o)
					, UserEntrySearchResult.class
					);
		} catch(DaoException e){
			throw new ServiceConfigurationException(e);
		} catch(Throwable e){
			throw new UnknownException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@Log(maskParameters="password")
	public void addUser(
			@NotEmpty @ValidUserId String userId
			, @NotEmpty String password
			, boolean canCallServices 
			, @NotNull UserProfile profile
			, @NotNull @EachElement @ValidAttribute Attribute[] attributes
			)
			throws AccessLimitExceededException, InvalidParameterException
			, InvalidUserIdException, NoAccessPermissionException
			, ServiceConfigurationException
			, UnknownException, UserAlreadyExistsException
	{
		try{
			User user = new User();
			user.setGridId(getGridId());
			user.setUserId(userId);
			user.setCanCallServices(canCallServices);
			user.setPassword(password);
			copyProperties(user, profile);
			copyAttributes(user, attributes, UserUtil.getUserProperties().keySet().toArray(new String[]{}));
			getUserLogic().addUser(user);
		} catch(jp.go.nict.langrid.dao.UserAlreadyExistsException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@Log
	public void deleteUser(
			@NotEmpty @ValidUserId String userId
			)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException
	{
		try{
			String gid = getGridId();
			getServiceLogic().deleteServicesOfUser(gid, userId);
			getUserLogic().deleteUser(getGridId(), userId);
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public UserProfile getUserProfile(
			@NotEmpty @ValidUserId String userId
			)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException
	{
		String gridId;
		String[] ids = userId.split(":", 2);
		if(ids.length == 2){
			gridId = ids[0];
			userId = ids[1];
		} else{
			gridId = getGridId();
			userId = ids[0];
		}
		try{
			return getUserLogic().transactRead(gridId, userId, user -> convert(user, UserProfile.class));
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=SELF_OR_ADMIN, argNames="userId")
	@ValidatedMethod
	@Log
	public void setUserProfile(
			@NotEmpty @ValidUserId String userId
			, final @NotNull UserProfile profile
			)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException
	{
		try{
			getUserLogic().transactUpdate(getGridId(), userId, user -> copyProperties(user, profile));
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>When an attribute name is specified that does not exist in attributeNames, that attribute name is ignored.
	 * When an empty string is set, gets all attributes which can be acquired.</p>
	 * 
	 */
	@AccessRightValidatedMethod(policy=SELF_OR_ADMIN, argNames="userId")
	@ValidatedMethod
	@TransactionMethod
	public Attribute[] getUserAttributes(
			final @NotEmpty @ValidUserId String userId
			, @NotNull @EachElement @ValidAttributeName String[] attributeNames)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException, UserNotFoundException
	{
		final Set<String> names = new HashSet<String>();
		for(String n : attributeNames){
			names.add(n);
		}
		try{
			return getUserLogic().transactRead(getGridId(), userId, user -> {
					List<Attribute> attrs = new ArrayList<Attribute>();
					if(names.size() == 0){
						for(jp.go.nict.langrid.dao.entity.Attribute a : user.getAttributes()){
							attrs.add(new Attribute(a.getName(), a.getValue()));
						}
					} else{
						for(jp.go.nict.langrid.dao.entity.Attribute a : user.getAttributes()){
							if(names.contains(a.getName())){
								attrs.add(new Attribute(a.getName(), a.getValue()));
							}
						}
					}
					return attrs.toArray(new Attribute[]{});
			});
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>When the specified attribute's value field is an empty string, that attribute is deleted.</p>
	 * 
	 */
	@AccessRightValidatedMethod(policy=SELF_OR_ADMIN, argNames="userId")
	@ValidatedMethod
	@TransactionMethod
	@Log
	public void setUserAttributes(
			@NotEmpty @ValidUserId String userId
			, final @NotNull @EachElement @ValidAttribute Attribute[] attributes
			)
	throws AccessLimitExceededException, InvalidParameterException
	, NoAccessPermissionException, ServiceConfigurationException
	, UnknownException, UserNotFoundException
	{
		validateInputAttribute("attributes", attributes);
		try{
			getUserLogic().transactUpdate(getGridId(), userId, user ->
					AttributedElementUpdater.updateAttributes(
							user, attributes, UserUtil.getUserProperties()
							)
			);
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw convertException(e);
		} catch(jp.go.nict.langrid.dao.DaoException e){
			throw convertException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SELF_OR_ADMIN, argNames="userId")
	@TransactionMethod
	@Log(maskParameters="password")
	public void setUserPassword(
			@NotEmpty @ValidUserId String userId
			, @NotEmpty @ValidPassword String password
			)
			throws AccessLimitExceededException, InvalidParameterException
			, NoAccessPermissionException, ServiceConfigurationException
			, UnknownException, UserNotFoundException
	{
		try{
			getUserLogic().setUserPassword(getGridId(), userId, password);
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw new UserNotFoundException(userId);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SELF_OR_ADMIN, argNames="userId")
	@TransactionMethod
	public Calendar getPasswordChangedDate(
			@NotEmpty @ValidUserId String userId
			)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			UnknownException, UserNotFoundException {
		try{
			return getUserLogic().transactRead(getGridId(), userId,
					user -> user.getPasswordChangedDate()
			);
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw new UserNotFoundException(userId);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	public String getUserPassword(
			@NotEmpty @ValidUserId String userId
			)
			throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException,
			UserNotFoundException {
		try{
			String gridId;
			String[] ids = userId.split(":", 2);
			if(ids.length == 2){
				gridId = ids[0];
				userId = ids[1];
			} else{
				gridId = getGridId();
				userId = ids[0];
			}
			return getUserLogic().transactRead(gridId, userId,
					user -> user.getPassword());
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw new UserNotFoundException(userId);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@ValidatedMethod
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	public String getUserDigestedPassword(
			@NotEmpty @ValidUserId String userId
			)
			throws AccessLimitExceededException,
			InvalidParameterException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException,
			UserNotFoundException {
		try{
			return getUserLogic().transactRead(getGridId(), userId,
					user -> user.getPassword());
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw new UserNotFoundException(userId);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>This data is always prioritized over access privilege and access restriction settings.
	 * If false, even with access permission and the access count and transfer size within permissible range, will not call the service.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=SELF_OR_ADMIN)
	@TransactionMethod
	public boolean getUserCanCallServices(
			@NotEmpty @ValidUserId String userId
			)
	throws AccessLimitExceededException, InvalidParameterException,
	NoAccessPermissionException, ServiceConfigurationException,
	UnknownException, UserNotFoundException {
		try{
			return getUserLogic().transactRead(getGridId(), userId,
					user -> user.isAbleToCallServices());
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw new UserNotFoundException(userId);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>This data is always prioritized over access privilege and access restriction settings.
	 * If false, even with access permission and the access count and transfer size within permissible range, will not call the service.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	@Log
	public void setUserCanCallServices(
			@NotEmpty @ValidUserId String userId
			, final boolean canCallServices
			)
	throws AccessLimitExceededException, InvalidParameterException,
	NoAccessPermissionException, ServiceConfigurationException,
	UnknownException, UserNotFoundException
	{
		try{
			getUserLogic().transactUpdate(getGridId(), userId,
					user -> user.setCanCallServices(canCallServices));
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw new UserNotFoundException(userId);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@Override
	public NewerAndOlderUserIds getNewerAndOlderUserIds(Calendar standardDateTime)
	throws ServiceConfigurationException, UnknownException{
		try{
			Pair<String[], String[]> ret = getUserLogic().getNewerAndOlderUserIds(
					getGridId(), standardDateTime);
			return new NewerAndOlderUserIds(ret.getFirst(), ret.getSecond());
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@Override
	@TransactionMethod
	public UserEntry getUserEntry(String userId) throws ServiceConfigurationException, UnknownException {
		try{
			User u = getUserLogic().getUser(getGridId(), userId);
			UserEntry ret = new Converter().convert(u, UserEntry.class);
			ret.setRoles(new HashSet<>());
			for(UserRole ur : u.getRoles()){
				ret.getRoles().add(ur.getRoleName());
			}
			return ret;
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	private static void validateInputAttribute(
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
	}
}
