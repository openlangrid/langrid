/*
 * $Id: AccessRightValidationPolicy.java 1506 2015-03-02 16:03:34Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation.util.validation;

import jp.go.nict.langrid.foundation.UserChecker;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.ServiceNotFoundException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserNotFoundException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public enum AccessRightValidationPolicy {
	/**
	 * 
	 * 
	 */
	ADMINONLY{
		public void validate(UserChecker checker, Object[] arg)
			throws NoAccessPermissionException, ServiceConfigurationException
			, UserNotFoundException
		{
			checker.checkAuthUserExists();
			checker.checkAuthUserAdmin();
		}
	},
	/**
	 * 
	 * 
	 */
	SERVICEOWNER_OR_ADMIN{
		public void validate(UserChecker checker, Object[] args)
			throws NoAccessPermissionException, ServiceConfigurationException
			, UserNotFoundException
		{
			checker.checkAuthUserExists();
			if(!checker.isAuthUserAdmin()){
				if((args.length > 0) && (args[0].toString().length() > 0)){
					try{
						checker.checkAuthUserIsServiceOwner(args[0].toString());
					} catch(ServiceNotFoundException e){
						throw new NoAccessPermissionException(checker.getUserId());
					}
				}
			}
		}
	},

	/**
	 * 
	 * 
	 */
	RESOURCEOWNER_OR_ADMIN{
		public void validate(UserChecker checker, Object[] args)
		throws NoAccessPermissionException, ServiceConfigurationException
		, UserNotFoundException
		{
			checker.checkAuthUserExists();
			if(!checker.isAuthUserAdmin()){
				if((args.length > 0) && (args[0].toString().length() > 0)){
					checker.checkAuthUserIsResourceOwner(args[0].toString());
				}
			}
		}
	},

	/**
	 * 
	 * 
	 */
	SELF_OR_ADMIN{
		public void validate(UserChecker checker, Object[] args)
			throws NoAccessPermissionException, ServiceConfigurationException
			, UserNotFoundException
		{
			checker.checkAuthUserExists();
			if(!checker.isAuthUserAdmin()){
				if((args.length > 0) && (args[0].toString().length() > 0)){
					checker.checkAuthUserEquals(args[0].toString());
				}
			}
		}
	},
	/**
	 * 
	 * 
	 */
	PARENT_OR_ADMIN{
		public void validate(UserChecker checker, Object[] args)
		throws NoAccessPermissionException, ServiceConfigurationException
		, UserNotFoundException
		{
			checker.checkAuthUserExists();
			if(checker.isAuthUserAdmin()) return;
			if((args.length > 0) && (args[0].toString().length() > 0)){
				checker.checkAuthUserIsParent(args[0].toString());
			} else{
				throw new ServiceConfigurationException("parameter needed.");
			}
		}
	},
	/**
	 * 
	 * 
	 */
	PARENT_OR_SELF_OR_ADMIN{
		public void validate(UserChecker checker, Object[] args)
		throws NoAccessPermissionException, ServiceConfigurationException
		, UserNotFoundException
		{
			checker.checkAuthUserExists();
			if(checker.isAuthUserAdmin()) return;
			if((args.length > 0) && (args[0].toString().length() > 0)){
				if(checker.getUserId().equals(args[0])) return;
				checker.checkAuthUserIsParent(args[0].toString());
			} else{
				throw new ServiceConfigurationException("parameter needed.");
			}
		}
	},
	/**
	 * 
	 * 
	 */
	AUTHORIZEDONLY{
		public void validate(UserChecker checker, Object[] args)
			throws NoAccessPermissionException, ServiceConfigurationException
			, UserNotFoundException
		{
			checker.checkAuthorized();
		}
	}
	;

	/**
	 * 
	 * 
	 */
	public abstract void validate(UserChecker checker, Object[] args)
		throws NoAccessPermissionException, ServiceConfigurationException
		, UserNotFoundException;
}
