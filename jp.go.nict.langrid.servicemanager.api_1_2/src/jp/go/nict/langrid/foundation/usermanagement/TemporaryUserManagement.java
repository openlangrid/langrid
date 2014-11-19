package jp.go.nict.langrid.foundation.usermanagement;

import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.ADMINONLY;
import static jp.go.nict.langrid.foundation.util.validation.AccessRightValidationPolicy.PARENT_OR_ADMIN;

import java.util.Calendar;

import jp.go.nict.langrid.commons.beanutils.ConversionException;
import jp.go.nict.langrid.commons.security.MessageDigestUtil;
import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.commons.validator.annotation.EachElement;
import jp.go.nict.langrid.commons.validator.annotation.IntInRange;
import jp.go.nict.langrid.commons.validator.annotation.IntNotNegative;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.NotNull;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.TemporaryUserDao;
import jp.go.nict.langrid.dao.entity.TemporaryUser;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.Log;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidMatchingCondition;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidOrder;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidUserId;
import jp.go.nict.langrid.management.logic.TemporaryUserLogic;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.MatchingCondition;
import jp.go.nict.langrid.service_1_2.foundation.Order;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.InvalidUserIdException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.TemporaryUserEntrySearchResult;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.TemporaryUserManagementService;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserAlreadyExistsException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserNotFoundException;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;

/**
 * 
 * Temporary user administration service.  The temporary user inherits language service execution privileges from the parent user.
 * When a temporary user calls a service, access control operates on the parent user's ID.
 * Temporary users are valid only within 24 hours of the time of their activation. Thereafter they can no longer call services.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class TemporaryUserManagement
extends AbstractLangridService
implements TemporaryUserManagementService
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public TemporaryUserManagement(){
		try{
			logic = new TemporaryUserLogic();
		} catch(DaoException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * Constructor.
	 * @param serviceContext Service context
	 * 
	 */
	public TemporaryUserManagement(ServiceContext serviceContext){
		super(serviceContext);
		try{
			logic = new TemporaryUserLogic();
		} catch(DaoException e){
			throw new RuntimeException(e);
		}
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@Log
	public void clear()
			throws AccessLimitExceededException, NoAccessPermissionException
			, ServiceConfigurationException, UnknownException
	{
		try{
			logic.clear();
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod(policy=ADMINONLY)
	@TransactionMethod
	public void clearExpiredUsers()
			throws AccessLimitExceededException, NoAccessPermissionException
			, ServiceConfigurationException, UnknownException
	{
		try{
			logic.clearExpiredUsers();
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Searchable only by the temporary user registered by the user calling this API.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public TemporaryUserEntrySearchResult searchTemporaryUsers(
			@IntNotNegative int startIndex
			, @IntInRange(minimum=0, maximum=100) int maxCount
			, @NotNull @EachElement @ValidMatchingCondition MatchingCondition[] conditions
			, @NotNull @EachElement @ValidOrder Order[] orders
			)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			UnknownException
	{
		String callerUserId = null;
		try{
			callerUserId = getUserChecker().getUserId();
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}

		jp.go.nict.langrid.dao.Order[] o = null;
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
					logic.searchTemporaryUsers(startIndex, maxCount
							, getGridId(), callerUserId, conds, o
							)
					, TemporaryUserEntrySearchResult.class
					);
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>If a user ID already used by a user or temporary user is specified, throw InvalidUserIdException.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	@Log(maskParameters="password")
	public void addTemporaryUser(
			@NotEmpty @ValidUserId String userId
			, @NotEmpty String password
			, @NotNull Calendar beginAvailableDateTime
	)
			throws AccessLimitExceededException, InvalidParameterException,
			InvalidUserIdException, NoAccessPermissionException,
			ServiceConfigurationException, UnknownException,
			UserAlreadyExistsException
	{
		try{
			String callerUserId = getUserChecker().getUserId();
			logic.addUser(new TemporaryUser(
					getGridId(), userId, MessageDigestUtil.digestBySHA512(password)
					, callerUserId, beginAvailableDateTime
					, CalendarUtil.cloneAndAdd(beginAvailableDateTime, Calendar.DATE, 1)
					));
		} catch(jp.go.nict.langrid.dao.UserAlreadyExistsException e){
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
	 * <p>Can be executed only by the temporary user registered by the user calling this API.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod(policy=PARENT_OR_ADMIN, argNames="userId")
	@TransactionMethod
	@Log
	public void deleteTemporaryUser(
			@NotEmpty @ValidUserId String userId
			)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			UnknownException, UserNotFoundException {
		String callerUserId = "--unknown--";
		try{
			callerUserId = getUserChecker().getUserId();
			getUserDao().getUser(getGridId(), callerUserId);
			TemporaryUser u = getTemporaryUserDao().getUser(getGridId(), userId);
			if(!u.getParentUserId().equals(callerUserId)){
				throw new NoAccessPermissionException(callerUserId);
			}
			logic.deleteUser(getGridId(), userId);
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(NoAccessPermissionException e){
			throw e;
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Can be executed only by the temporary user registered by the user calling this API.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public Calendar getBeginAvailableDateTime(
			@NotEmpty @ValidUserId String userId
	)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			UnknownException, UserNotFoundException
	{
		try{
			String callerUserId = getUserChecker().getUserId();
			TemporaryUser u = getTemporaryUserDao().getUser(getGridId(), userId);
			if(!u.getParentUserId().equals(callerUserId)){
				throw new NoAccessPermissionException(callerUserId);
			}
			return u.getBeginAvailableDateTime();
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(NoAccessPermissionException e){
			throw e;
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Can be executed only by the temporary user registered by the user calling this API.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public Calendar getEndAvailableDateTime(
			@NotEmpty @ValidUserId String userId
	)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			UnknownException, UserNotFoundException
	{
		try{
			String callerUserId = getUserChecker().getUserId();
			TemporaryUserDao dao = getTemporaryUserDao();
			TemporaryUser u = dao.getUser(getGridId(), userId);
			if(!u.getParentUserId().equals(callerUserId)){
				throw new NoAccessPermissionException(callerUserId);
			}
			return u.getEndAvailableDateTime();
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(NoAccessPermissionException e){
			throw e;
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Can be executed only by the temporary user registered by the user calling this API.</p>
	 * 
	 */
	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	@Log
	public void setAvailableDateTimes(
			@NotEmpty @ValidUserId String userId
			, @NotNull Calendar beginAvailableDateTime
			, @NotNull Calendar endAvailableDateTime
			)
			throws AccessLimitExceededException, InvalidParameterException,
			NoAccessPermissionException, ServiceConfigurationException,
			UnknownException, UserNotFoundException
	{
		if(endAvailableDateTime.before(beginAvailableDateTime)){
			throw new InvalidParameterException(
					"beginAvailableDateTime,endAvailableDateTime"
					, "beginAvailableDateTime must earlier than endAvailableDateTime.");
		}
		try{
			String callerUserId = getUserChecker().getUserId();
			TemporaryUser u = getTemporaryUserDao().getUser(getGridId(), userId);
			if(!u.getParentUserId().equals(callerUserId)){
				throw new NoAccessPermissionException(callerUserId);
			}
			getTemporaryUserDao().setAvailableDateTime(
					u
					, beginAvailableDateTime
					, endAvailableDateTime
					);
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			throw convertException(e);
		} catch(DaoException e){
			throw convertException(e);
		} catch(NoAccessPermissionException e){
			throw e;
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	private TemporaryUserLogic logic;
}
