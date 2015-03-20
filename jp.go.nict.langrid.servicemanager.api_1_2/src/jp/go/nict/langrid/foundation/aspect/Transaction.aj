/*
 * $Id: Transaction.aj 302 2010-12-01 02:49:42Z t-nakaguchi $
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

package jp.go.nict.langrid.foundation.aspect;

import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;

/**
 * Daoトランザクション処理を行うアスペクト。
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public aspect Transaction {
	pointcut pc()
		: execution(@TransactionMethod * (jp.go.nict.langrid.foundation..* && AbstractLangridService+).*(..)
				throws ServiceConfigurationException)
		;

	before(AbstractLangridService self)
		throws ServiceConfigurationException
		: pc() && this(self)
	{
		try{
			getDaoContext().beginTransaction();
		} catch(DaoException e){
			logger.log(Level.SEVERE, "failed to start transaction", e);
			throw new ServiceConfigurationException(e.toString());
		}
	}
	
	after(AbstractLangridService self)
		returning (Object ret)
		throws ServiceConfigurationException
		: pc() && this(self)
	{
		try{
			DaoContext c = getDaoContext();
			c.commitTransaction();
			if(c.getTransactionNestCount() != 0){
				logger.log(Level.SEVERE
						, "transaction nest is not zero ("
						+ c.getTransactionNestCount()
						+ ")"
						, new Exception());
				while(c.getTransactionNestCount() > 0)
					c.rollbackTransaction();
			}
		} catch(DaoException e){
			logger.log(Level.SEVERE, "failed to commit transaction", e);
			throw new ServiceConfigurationException(e.toString());
		}
	}
	
	after(AbstractLangridService self)
		throwing (Throwable t)
		throws ServiceConfigurationException
		: pc() && this(self)
	{
		try{
			DaoContext c = getDaoContext();
			c.rollbackTransaction();
			if(c.getTransactionNestCount() != 0){
				logger.log(Level.SEVERE
						, "transaction nest is not zero ("
						+ c.getTransactionNestCount()
						+ ")"
						, new Exception());
				while(c.getTransactionNestCount() > 0)
					c.rollbackTransaction();
			}
		} catch(DaoException e){
			logger.log(Level.SEVERE, "failed to rollback transaction", e);
			logger.log(Level.SEVERE, "exception that caused rollback is " + t.toString(), t);
		}
	}

	private static DaoContext getDaoContext(){
		if(daoContext.get() == null){
			try{
				daoContext.set(DaoFactory.createInstance().getDaoContext());
			} catch(DaoException e){
				logger.log(Level.SEVERE, "can't get DaoContext", e);
				throw new RuntimeException(e);
			}
		}
		return daoContext.get();
	}
	
	private static ThreadLocal<DaoContext> daoContext
		= new ThreadLocal<DaoContext>();

	private static Logger logger = Logger.getLogger(
			Transaction.class.getName());
}
