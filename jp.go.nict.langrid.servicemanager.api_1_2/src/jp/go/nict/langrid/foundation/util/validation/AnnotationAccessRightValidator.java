/*
 * $Id: AnnotationAccessRightValidator.java 404 2011-08-25 01:40:39Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.UserChecker;
import jp.go.nict.langrid.foundation.annotation.AccessRightValidatedMethod;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.UnknownException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserNotFoundException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 404 $
 */
public class AnnotationAccessRightValidator {
	/**
	 * 
	 * 
	 */
	public static void validate(
			AbstractLangridService service, AccessRightValidatedMethod annotation
			, String[] parameterNames, Object[] parameters
			)
		throws NoAccessPermissionException, ServiceConfigurationException
		, UserNotFoundException, UnknownException
	{
		List<Object> args = new ArrayList<Object>();
		String[] names = annotation.argNames();
		if(names != null) for(String name : annotation.argNames()){
			for(int i = 0; i < parameterNames.length; i++){
				if(name.equals(parameterNames[i])){
					args.add(parameters[i]);
				}
			}
		}
		try{
			UserChecker checker = UserChecker.get(service);
			DaoContext c = checker.getDaoContext();
			c.beginTransaction();
			try{
				annotation.policy().validate(checker, args.toArray());
				c.commitTransaction();
			} catch(NoAccessPermissionException e){
				c.rollbackTransaction();
				throw e;
			} catch(ServiceConfigurationException e){
				c.rollbackTransaction();
				throw e;
			} catch(UserNotFoundException e){
				c.rollbackTransaction();
				throw e;
			} catch(DaoException e){
				c.rollbackTransaction();
				throw new ServiceConfigurationException(e);
			} catch(RuntimeException e){
				c.rollbackTransaction();
				throw new UnknownException(e);
			} catch(Error e){
				c.rollbackTransaction();
				throw new UnknownException(e);
			}
		} catch(DaoException e){
			logger.log(Level.SEVERE, "failed to get UserChecker", e);
			throw new ServiceConfigurationException(e);
		}
	}

	private static Logger logger = Logger.getLogger(
			AnnotationAccessRightValidator.class.getName());
}
