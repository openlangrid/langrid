/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao.jsonic;

import java.util.List;

import jp.go.nict.langrid.commons.lang.reflect.UnsupportedOperationInvocationHandler;
import jp.go.nict.langrid.dao.AcceptableRemoteAddressDao;
import jp.go.nict.langrid.dao.AccessLimitDao;
import jp.go.nict.langrid.dao.AccessLogDao;
import jp.go.nict.langrid.dao.AccessRightDao;
import jp.go.nict.langrid.dao.AccessStatDao;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.InvocationDao;
import jp.go.nict.langrid.dao.NewsDao;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.OperationRequestDao;
import jp.go.nict.langrid.dao.OverUseLimitDao;
import jp.go.nict.langrid.dao.OverUseStateDao;
import jp.go.nict.langrid.dao.ProtocolDao;
import jp.go.nict.langrid.dao.ResourceDao;
import jp.go.nict.langrid.dao.ResourceTypeDao;
import jp.go.nict.langrid.dao.ServiceActionScheduleDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceDeploymentDao;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.SystemPropertyDao;
import jp.go.nict.langrid.dao.TemporaryUserDao;
import jp.go.nict.langrid.dao.UserDao;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicDaoFactory extends DaoFactory{
	@Override
	public DaoContext getDaoContext() {
		return context;
	}

	@Override
	public SystemPropertyDao createSystemPropertyDao() throws DaoException {
		return new JsonicSystemPropertyDao(context);
	}

	@Override
	public GridDao createGridDao() throws DaoException {
		return new JsonicGridDao(context);
	}

	@Override
	public FederationDao createFederationDao() throws DaoException {
		return new JsonicFederationDao(context);
	}

	@Override
	public UserDao createUserDao() throws DaoException {
		return new JsonicUserDao(context);
	}

	@Override
	public TemporaryUserDao createTemporaryUserDao() throws DaoException {
		return new JsonicTemporaryUserDao(context);
	}

	@Override
	public ServiceDao createServiceDao() throws DaoException {
		return new JsonicServiceDao(context);
	}
	
	@Override
	public ServiceDeploymentDao createServiceDeploymentDao()
			throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(ServiceDeploymentDao.class);
	}

	@Override
	public NodeDao createNodeDao() throws DaoException {
		return new JsonicNodeDao(context);
	}

	@Override
	public AccessLimitDao createAccessLimitDao() throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(AccessLimitDao.class);
	}

	@Override
	public AccessLogDao createAccessLogDao() throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(AccessLogDao.class);
	}

	@Override
	public AccessRightDao createAccessRightDao() throws DaoException {
		return new JsonicAccessRightDao(context);
	}

	@Override
	public AccessStatDao createAccessStateDao() throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(AccessStatDao.class);
	}

	@Override
	public OverUseLimitDao createOverUseLimitDao() throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(OverUseLimitDao.class);
	}

	@Override
	public OverUseStateDao createOverUseStateDao() throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(OverUseStateDao.class);
	}

	@Override
	public ResourceDao createResourceDao() throws DaoException {
		return new JsonicResourceDao();
	}

	@Override
	public AcceptableRemoteAddressDao createAcceptableRemoteAddressDao()
			throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(AcceptableRemoteAddressDao.class);
	}

	@Override
	public ServiceActionScheduleDao createServiceActionScheduleDao()
			throws DaoException {
		return new JsonicServiceActionScheduleDao(context);
	}

	@Override
	public OperationRequestDao createOperationRequestDao() throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(OperationRequestDao.class);
	}

	@Override
	public NewsDao createNewsDao() throws DaoException {
		return new JsonicNewsDao();
	}

	@Override
	public DomainDao createDomainDao() throws DaoException {
		return new JsonicDomainDao(context);
	}

	@Override
	public ProtocolDao createProtocolDao() throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(ProtocolDao.class);
	}

	@Override
	public ResourceTypeDao createResourceTypeDao() throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(ResourceTypeDao.class);
	}

	@Override
	public ServiceTypeDao createServiceTypeDao() throws DaoException {
		return new JsonicServiceTypeDao(context);
	}
	
	@Override
	public InvocationDao createInvocationDao() throws DaoException {
		return UnsupportedOperationInvocationHandler.newProxy(InvocationDao.class);
	}

	@Override
	public void initialize(List<Class<?>> additionalEntities)
	throws DaoException {
	}

	private JsonicDaoContext context = new JsonicDaoContext();
}
