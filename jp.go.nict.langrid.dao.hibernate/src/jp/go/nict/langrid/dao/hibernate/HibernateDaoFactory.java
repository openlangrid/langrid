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
package jp.go.nict.langrid.dao.hibernate;

import java.util.List;

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
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 389 $
 */
public class HibernateDaoFactory extends DaoFactory{
	@Override
	public DaoContext getDaoContext() {
		return context;
	}

	public SystemPropertyDao createSystemPropertyDao()
	throws DaoException{
		return new HibernateSystemPropertyDao(context);
	}

	public GridDao createGridDao()
	throws DaoException{
		return new HibernateGridDao(context);
	}

	public FederationDao createFederationDao()
	throws DaoException{
		return new HibernateFederationDao(context);
	}

	public ServiceDao createServiceDao()
	throws DaoException{
		return new HibernateServiceDao(context);
	}

	public ServiceDeploymentDao createServiceDeploymentDao()
	throws DaoException{
		return new HibernateServiceDeploymentDao(context);
	}

	public AccessLimitDao createAccessLimitDao() throws DaoException {
		return new HibernateAccessLimitDao(context);
	}

	public AccessLogDao createAccessLogDao()
		throws DaoException
	{
		return new HibernateAccessLogDao(context);
	}

	public AccessRightDao createAccessRightDao()
		throws DaoException
	{
		return new HibernateAccessRightDao(context);
	}

	public AccessStatDao createAccessStateDao() throws DaoException {
		return new HibernateAccessStatDao(context);
	}

	public UserDao createUserDao()
		throws DaoException
	{
		return new HibernateUserDao(context);
	}

	public TemporaryUserDao createTemporaryUserDao()
		throws DaoException
	{
		return new HibernateTemporaryUserDao(context);
	}

	public NodeDao createNodeDao()
	throws DaoException{
		return new HibernateNodeDao(context);
	}

	public OverUseLimitDao createOverUseLimitDao() throws DaoException {
		return new HibernateOverUseLimitDao(context);
	}

	public OverUseStateDao createOverUseStateDao() throws DaoException {
		return new HibernateOverUseStateDao(context);
	}

	public ResourceDao createResourceDao() throws DaoException{
		return new HibernateResourceDao(context);
	}

	public AcceptableRemoteAddressDao createAcceptableRemoteAddressDao() throws DaoException{
		return new HibernateAcceptableRemoteAddressDao(context);
	}

	public void initialize(List<Class<?>> additionalEntities) throws DaoException{
		HibernateDaoContext.addEntityClass(additionalEntities);
	}

	@Override
	public NewsDao createNewsDao() throws DaoException {
		return new HibernateNewsDao(context);
	}

	@Override
	public ServiceActionScheduleDao createServiceActionScheduleDao() throws DaoException {
		return new HibernateServiceActionScheduleDao(context);
	}

	@Override
	public OperationRequestDao createOperationRequestDao() throws DaoException {
		return new HibernateOperationRequestDao(context);
	}

	public DomainDao createDomainDao() throws DaoException {
		return new HibernateDomainDao(context);
	}

	public ProtocolDao createProtocolDao() throws DaoException {
		return new HibernateProtocolTypeDao(context);
	}

	public ResourceTypeDao createResourceTypeDao() throws DaoException {
		return new HibernateResourceTypeDao(context);
	}

	public ServiceTypeDao createServiceTypeDao() throws DaoException {
		return new HibernateServiceTypeDao(context);
	}
	
	@Override
	public InvocationDao createInvocationDao() throws DaoException {
		return new HibernateInvocationDao(context);
	}

	private HibernateDaoContext context = new HibernateDaoContext();
}
