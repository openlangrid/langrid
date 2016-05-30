/*
 * $Id: AbstractLogic.java 405 2011-08-25 01:43:27Z t-nakaguchi $
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
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class AbstractLogic {
	/**
	 * 
	 * 
	 */
	public AbstractLogic() throws DaoException{
		daoContext = factory.getDaoContext();
		systemPropertyDao = factory.createSystemPropertyDao();
		gridDao = factory.createGridDao();
		federationDao = factory.createFederationDao();
		userDao = factory.createUserDao();
		temporaryUserDao = factory.createTemporaryUserDao();
		resourceDao = factory.createResourceDao();
		resourceTypeDao = factory.createResourceTypeDao();
		serviceDeploymentDao = factory.createServiceDeploymentDao();
		serviceDao = factory.createServiceDao();
		nodeDao = factory.createNodeDao();
		accessLogDao = factory.createAccessLogDao();
		accessLimitDao = factory.createAccessLimitDao();
		accessRightDao = factory.createAccessRightDao();
		accessStateDao = factory.createAccessStateDao();
		overUseLimitDao = factory.createOverUseLimitDao();
		overUseStateDao = factory.createOverUseStateDao();
		scheduleDao = factory.createServiceActionScheduleDao();
		newsDao = factory.createNewsDao();
		operationRequestDao = factory.createOperationRequestDao();
		protocolDao = factory.createProtocolDao();
		serviceTypeDao = factory.createServiceTypeDao();
		domainDao = factory.createDomainDao();
		invocationDao = factory.createInvocationDao();
	}

	protected DaoContext getDaoContext(){
		return daoContext;
	}

	protected DaoFactory getDaoFactory(){
		return factory;
	}

	protected DomainDao getDomainDao(){
		return domainDao;
	}

	protected SystemPropertyDao getSystemPropertyDao(){
		return systemPropertyDao;
	}

	protected GridDao getGridDao(){
		return gridDao;
	}

	protected FederationDao getFederationDao(){
		return federationDao;
	}

	protected UserDao getUserDao(){
		return userDao;
	}

	protected TemporaryUserDao getTemporaryUserDao(){
		return temporaryUserDao;
	}

	protected ResourceDao getResourceDao(){
		return resourceDao;
	}

	protected ResourceTypeDao getResourceTypeDao(){
		return resourceTypeDao;
	}

	protected ServiceDao getServiceDao(){
		return serviceDao;
	}

	protected ServiceDeploymentDao getServiceDeploymentDao(){
		return serviceDeploymentDao;
	}

	protected NodeDao getNodeDao(){
		return nodeDao;
	}

	protected AccessLogDao getAccessLogDao(){
		return accessLogDao;
	}

	protected AccessRightDao getAccessRightDao(){
		return accessRightDao;
	}

	protected AccessLimitDao getAccessLimitDao(){
		return accessLimitDao;
	}

	protected AccessStatDao getAccessStateDao(){
		return accessStateDao;
	}

	protected OverUseLimitDao getOverUseLimitDao(){
		return overUseLimitDao;
	}

	protected OverUseStateDao getOverUseStateDao(){
		return overUseStateDao;
	}

	protected ServiceActionScheduleDao getScheduleDao() {
		return scheduleDao;
	}

	protected NewsDao getNewsDao(){
		return newsDao;
	}

	protected OperationRequestDao getOperationRequestDao(){
		return operationRequestDao;
	}

	protected ProtocolDao getProtocolDao() {
      return protocolDao;
   }
	
	protected ServiceTypeDao getServiceTypeDao() {
	   return serviceTypeDao;
	}
	
	protected InvocationDao getInvocationDao(){
		return invocationDao;
	}

	private DaoContext daoContext;
	private SystemPropertyDao systemPropertyDao;
	private GridDao gridDao;
	private FederationDao federationDao;
	private UserDao userDao;
	private TemporaryUserDao temporaryUserDao;
	private ServiceDao serviceDao;
	private ServiceDeploymentDao serviceDeploymentDao;
	private NodeDao nodeDao;
	private AccessLogDao accessLogDao;
	private AccessRightDao accessRightDao;
	private AccessLimitDao accessLimitDao;
	private AccessStatDao accessStateDao;
	private OverUseLimitDao overUseLimitDao;
	private OverUseStateDao overUseStateDao;
	private ResourceDao resourceDao;
	private ResourceTypeDao resourceTypeDao;
	private ServiceActionScheduleDao scheduleDao;
	private NewsDao newsDao;
	private OperationRequestDao operationRequestDao;
	private ProtocolDao protocolDao;
	private DomainDao domainDao;
	private ServiceTypeDao serviceTypeDao;
	private InvocationDao invocationDao;
	private static DaoFactory factory;
	static{
		try{
			factory = DaoFactory.createInstance();
		} catch(DaoException e){
			throw new RuntimeException(e);
		}
	}
}
