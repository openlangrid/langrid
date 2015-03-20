/*
 * $Id: DaoTransactionAspect.aj 405 2011-08-25 01:43:27Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.management.logic;

import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.management.logic.DaoTransaction;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 405 $
 */
public aspect DaoTransactionAspect {
	pointcut pc()
		: execution(@DaoTransaction * (jp.go.nict.langrid.management.logic..* && AbstractLogic+).*(..)
				throws DaoException+, ServiceLogicException+)
		;

	Object around(AbstractLogic self)
		throws DaoException, ServiceLogicException
		: pc() && this(self)
	{
		DaoContext c = self.getDaoContext();
		c.beginTransaction();
		try{
			Object result = proceed(self);
			c.commitTransaction();
			return result;
		} catch(DaoException e){
			try{
				c.rollbackTransaction();
			} catch(Throwable ex){
				logger.log(Level.WARNING, "Exception when rollbacking transaction", e);
			}
			throw e;
		} catch(ServiceLogicException e){
			try{
				c.rollbackTransaction();
			} catch(Throwable ex){
				logger.log(Level.WARNING, "Exception when rollbacking transaction", e);
			}
			throw e;
		} catch(RuntimeException e){
			try{
				c.rollbackTransaction();
			} catch(Throwable ex){
				logger.log(Level.WARNING, "Exception when rollbacking transaction", e);
			}
			throw e;
		} catch(Error e){
			try{
				c.rollbackTransaction();
			} catch(Throwable ex){
				ex.printStackTrace();
			}
			throw e;
		}
	}

	private static Logger logger = Logger.getLogger(DaoTransactionAspect.class.getName());
}
