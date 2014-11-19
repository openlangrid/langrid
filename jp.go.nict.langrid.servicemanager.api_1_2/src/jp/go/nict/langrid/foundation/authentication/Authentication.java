/*
 * $Id: Authentication.java 302 2010-12-01 02:49:42Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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
package jp.go.nict.langrid.foundation.authentication;

import jp.go.nict.langrid.commons.security.MessageDigestUtil;
import jp.go.nict.langrid.commons.validator.annotation.NotEmpty;
import jp.go.nict.langrid.commons.validator.annotation.ValidatedMethod;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.foundation.util.validation.annotation.ValidUserId;
import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.InvalidParameterException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.AuthenticationService;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserNotFoundException;
import jp.go.nict.langrid.service_1_2.util.ExceptionConverter;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class Authentication
extends AbstractLangridService
implements AuthenticationService
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public Authentication(){}

	/**
	 * 
	 * 
	 */
	public Authentication(ServiceContext serviceContext){
		super(serviceContext);
	}

	@ValidatedMethod
	@AccessRightValidatedMethod
	@TransactionMethod
	public boolean authenticate(
			@NotEmpty @ValidUserId String userId
			, @NotEmpty String password
			)
		throws AccessLimitExceededException, InvalidParameterException
		, NoAccessPermissionException, ServiceConfigurationException
		, UnknownException, UserNotFoundException
	{
		String gridId = getGridId();
		userId = userId.trim();
		try{
			if(!getUserChecker().getUser().getUserId().equals(userId)){
				throw new NoAccessPermissionException(userId);
			}
			return getUserDao().getUser(getGridId(), userId).getPassword().equals(
					MessageDigestUtil.digestBySHA512(password)
					);
		} catch(jp.go.nict.langrid.dao.UserNotFoundException e){
			return false;
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}

	@AccessRightValidatedMethod
	public void nop(){
	}

	@AccessRightValidatedMethod()
	@TransactionMethod
	public boolean isAdministrator() throws AccessLimitExceededException,
			NoAccessPermissionException, ServiceConfigurationException,
			UnknownException {
		try{
			return getUserChecker().isAuthUserAdmin();
		} catch(DaoException e){
			throw ExceptionConverter.convertToServiceConfigurationException(e);
		} catch(Throwable e){
			throw ExceptionConverter.convertException(e);
		}
	}
}
