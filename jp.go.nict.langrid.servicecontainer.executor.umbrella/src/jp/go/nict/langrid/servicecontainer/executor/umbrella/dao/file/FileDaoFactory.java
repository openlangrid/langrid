/*
 * $Id:HibernateDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.file;

import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoContext;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoException;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoFactory;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.EndpointAddressProtocolDao;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.ServiceProtocolDao;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 496 $
 */
public class FileDaoFactory extends DaoFactory{
	public FileDaoFactory(String path){
		this.context = new FileDaoContext(path);
	}

	@Override
	public DaoContext getDaoContext() {
		return context;
	}
	
	@Override
	public ServiceProtocolDao createServiceProtocolDao() throws DaoException {
		return new FileServiceProtocolDao(context);
	}

	@Override
	public EndpointAddressProtocolDao createEndpointAddressProtocolDao() throws DaoException {
		return new FileEndpointAddressProtocolDao(context);
	}

	private FileDaoContext context;
}
