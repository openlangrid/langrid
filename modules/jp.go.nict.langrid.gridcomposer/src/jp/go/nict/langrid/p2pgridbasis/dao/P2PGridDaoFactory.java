/*
 * $Id: P2PGridDaoFactory.java 1522 2015-03-11 02:20:42Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jp.go.nict.langrid.dao.hibernate.HibernateDaoFactory;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisAccessLimitDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisAccessLogDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisAccessRightDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisAccessStateDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisDomainDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisFederationDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisGridDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisNewsDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisNodeDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisOverUseLimitDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisProtocolDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisResourceDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisResourceMetaAttributeDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisResourceTypeDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisServiceDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisServiceMetaAttributeDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisServiceTypeDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisTemporaryUserDao;
import jp.go.nict.langrid.p2pgridbasis.dao.langrid.P2PGridBasisUserDao;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessLimitData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessLogData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessRightData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessStateData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DomainData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.FederationData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.GridData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.NewsData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.NodeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.OverUseLimitData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ProtocolData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceMetaAttributeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceTypeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceMetaAttributeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceTypeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.TemporaryUserData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.UserData;
import net.jxta.peer.PeerID;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1522 $
 */
public class P2PGridDaoFactory extends DaoFactory {
	@Override
	public AccessRightDao createAccessRightDao() throws DaoException {
		return new P2PGridBasisAccessRightDao(daoFactory.createAccessRightDao(), getDaoContext());
	}

	@Override
	public ResourceDao createResourceDao() throws DaoException {
		return new P2PGridBasisResourceDao(daoFactory.createResourceDao(), getDaoContext());
	}

	@Override
	public ServiceDao createServiceDao() throws DaoException {
		return new P2PGridBasisServiceDao(
				daoFactory.createServiceDao(), getDaoContext()
				, activeBpelServicesUrl, activeBpelDeployBinding
				);
	}

	@Override
	public UserDao createUserDao() throws DaoException {
		return new P2PGridBasisUserDao(daoFactory.createUserDao(), getDaoContext());
	}

	@Override
	public TemporaryUserDao createTemporaryUserDao() throws DaoException {
		return new P2PGridBasisTemporaryUserDao(daoFactory.createTemporaryUserDao(), getDaoContext());
	}

	@Override
	public AccessLimitDao createAccessLimitDao() throws DaoException {
		return new P2PGridBasisAccessLimitDao(daoFactory.createAccessLimitDao(), getDaoContext());
	}

	@Override
	public OverUseLimitDao createOverUseLimitDao() throws DaoException {
		return new P2PGridBasisOverUseLimitDao(daoFactory.createOverUseLimitDao(), getDaoContext());
	}

	@Override
	public AccessLogDao createAccessLogDao() throws DaoException {
		return new P2PGridBasisAccessLogDao(daoFactory.createAccessLogDao(), getDaoContext());
	}

	@Override
	public AccessStatDao createAccessStateDao() throws DaoException {
		return new P2PGridBasisAccessStateDao(daoFactory.createAccessStateDao(), getDaoContext());
	}

	@Override
	public DomainDao createDomainDao() throws DaoException {
		return new P2PGridBasisDomainDao(daoFactory.createDomainDao(), getDaoContext());
	}

	@Override
	public FederationDao createFederationDao() throws DaoException {
		return new P2PGridBasisFederationDao(
				daoFactory.createFederationDao(),
				daoFactory.createGridDao(),
				getDaoContext());
	}

	@Override
	public GridDao createGridDao() throws DaoException {
		return new P2PGridBasisGridDao(daoFactory.createGridDao(), getDaoContext());
	}

	@Override
	public NodeDao createNodeDao() throws DaoException {
		return new P2PGridBasisNodeDao(daoFactory.createNodeDao(), getDaoContext());
	}

	@Override
	public ProtocolDao createProtocolDao() throws DaoException {
		return new P2PGridBasisProtocolDao(daoFactory.createProtocolDao(), getDaoContext());
	}

	@Override
	public ResourceTypeDao createResourceTypeDao() throws DaoException {
		return new P2PGridBasisResourceTypeDao(
				daoFactory.createDomainDao(),
				daoFactory.createResourceTypeDao(),
				getDaoContext());
	}

	private static P2PGridBasisResourceMetaAttributeDao createResourceMetaAttributeDao() {
		HibernateDaoFactory factory = new HibernateDaoFactory();
		try {
			return new P2PGridBasisResourceMetaAttributeDao(
					factory.createDomainDao(),
					factory.createResourceTypeDao(),
					factory.getDaoContext());
		} catch (DaoException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static P2PGridBasisServiceMetaAttributeDao createServiceMetaAttributeDao() {
		HibernateDaoFactory factory = new HibernateDaoFactory();
		try {
			return new P2PGridBasisServiceMetaAttributeDao(
					factory.createDomainDao(),
					factory.createServiceTypeDao(),
					factory.getDaoContext());
		} catch (DaoException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public NewsDao createNewsDao() throws DaoException {
		return new P2PGridBasisNewsDao(daoFactory.createNewsDao(), getDaoContext());
	}

	@Override
	public ServiceTypeDao createServiceTypeDao() throws DaoException {
		return new P2PGridBasisServiceTypeDao(
				daoFactory.createDomainDao(),
				daoFactory.createServiceTypeDao(), getDaoContext());
	}

	@Override
	public InvocationDao createInvocationDao() throws DaoException {
		return daoFactory.createInvocationDao();
	}

	@Override
	public DaoContext getDaoContext() {
		return daoFactory.getDaoContext();
	}

	@Override
	public OverUseStateDao createOverUseStateDao() throws DaoException {
		return daoFactory.createOverUseStateDao();
	}

	@Override
	public ServiceDeploymentDao createServiceDeploymentDao() throws DaoException {
		return daoFactory.createServiceDeploymentDao();
	}

	@Override
	public AcceptableRemoteAddressDao createAcceptableRemoteAddressDao() throws DaoException {
		return daoFactory.createAcceptableRemoteAddressDao();
	}

	@Override
	public SystemPropertyDao createSystemPropertyDao() throws DaoException {
		return daoFactory.createSystemPropertyDao();
	}

	@Override
	public OperationRequestDao createOperationRequestDao() throws DaoException {
		return daoFactory.createOperationRequestDao();
	}

	@Override
	public ServiceActionScheduleDao createServiceActionScheduleDao() throws DaoException {
		return daoFactory.createServiceActionScheduleDao();
	}

	public static DataDao getDataDao(String dataType) throws DataTypeNotFoundException{
		if(dataDaoMap.containsKey(dataType) == false) {
			throw new DataTypeNotFoundException(dataType);
		}
		return dataDaoMap.get(dataType);
	}

	public static DaoFactory setupDataDaoMap(PeerID id
			, String activeBpelServicesUrl, String activeBpelDeployBinding)
	throws DaoException{
		P2PGridDaoFactory.activeBpelServicesUrl = activeBpelServicesUrl;
		P2PGridDaoFactory.activeBpelDeployBinding = activeBpelDeployBinding;
		DaoFactory factory = DaoFactory.createInstance();
		dataDaoMap.put(AccessLimitData.getDataType()          , (P2PGridBasisAccessLimitDao)factory.createAccessLimitDao());
		dataDaoMap.put(AccessLogData.getDataType()            , (P2PGridBasisAccessLogDao)factory.createAccessLogDao());
		dataDaoMap.put(AccessRightData.getDataType()          , (P2PGridBasisAccessRightDao)factory.createAccessRightDao());
		dataDaoMap.put(AccessStateData.getDataType()          , (P2PGridBasisAccessStateDao)factory.createAccessStateDao());
		dataDaoMap.put(DomainData.getDataType()               , (P2PGridBasisDomainDao)factory.createDomainDao());
		dataDaoMap.put(FederationData.getDataType()           , (P2PGridBasisFederationDao)factory.createFederationDao());
		dataDaoMap.put(GridData.getDataType()                 , (P2PGridBasisGridDao)factory.createGridDao());
		dataDaoMap.put(NewsData.getDataType()                 , (P2PGridBasisNewsDao)factory.createNewsDao());
		dataDaoMap.put(NodeData.getDataType()                 , (P2PGridBasisNodeDao)factory.createNodeDao());
		dataDaoMap.put(OverUseLimitData.getDataType()         , (P2PGridBasisOverUseLimitDao)factory.createOverUseLimitDao());
		dataDaoMap.put(ProtocolData.getDataType()             , (P2PGridBasisProtocolDao)factory.createProtocolDao());
		dataDaoMap.put(ResourceData.getDataType()             , (P2PGridBasisResourceDao)factory.createResourceDao());
		dataDaoMap.put(ResourceTypeData.getDataType()         , (P2PGridBasisResourceTypeDao)factory.createResourceTypeDao());
		dataDaoMap.put(ResourceMetaAttributeData.getDataType(), (P2PGridBasisResourceMetaAttributeDao)createResourceMetaAttributeDao());
		dataDaoMap.put(ServiceData.getDataType()              , (P2PGridBasisServiceDao)factory.createServiceDao());
		dataDaoMap.put(ServiceTypeData.getDataType()          , (P2PGridBasisServiceTypeDao)factory.createServiceTypeDao());
		dataDaoMap.put(ServiceMetaAttributeData.getDataType() , (P2PGridBasisServiceMetaAttributeDao)createServiceMetaAttributeDao());
		dataDaoMap.put(TemporaryUserData.getDataType()        , (P2PGridBasisTemporaryUserDao)factory.createTemporaryUserDao());
		dataDaoMap.put(UserData.getDataType()                 , (P2PGridBasisUserDao)factory.createUserDao());
		return factory;
	}

	@Override
	public void initialize(List<Class<?>> additionalEntities)
			throws DaoException {
		daoFactory.initialize(additionalEntities);
	}

	private DaoFactory daoFactory = new HibernateDaoFactory();
	private static Map<String, DataDao> dataDaoMap = new HashMap<String, DataDao>();
	private static String activeBpelServicesUrl;
	private static String activeBpelDeployBinding;
}
