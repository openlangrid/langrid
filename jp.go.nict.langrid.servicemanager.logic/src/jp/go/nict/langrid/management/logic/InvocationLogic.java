/*
 * $Id: InvocationLogic.java 405 2011-08-25 01:43:27Z t-nakaguchi $
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

import java.util.List;
import java.util.logging.Logger;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.Invocation;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 405 $
 */
public class InvocationLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public InvocationLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getInvocationDao().clear();
	}

	@DaoTransaction
	public void addInvocation(Invocation invocation)
		throws DaoException
	{
	   getInvocationDao().addInvocation(invocation);
	}
	
	@DaoTransaction
	public void deleteInvocation(String gridId, String serviceId, String invocationName)
	throws DaoException
	{
	   getInvocationDao().deleteInvocation(gridId, serviceId, invocationName);
	}

	@DaoTransaction
	public List<Invocation> listAll(String gridId, String serviceId) throws DaoException{
	   return getInvocationDao().listInvocation(gridId, serviceId);
	}

	private static Logger logger = Logger.getLogger(
			InvocationLogic.class.getName());
}
