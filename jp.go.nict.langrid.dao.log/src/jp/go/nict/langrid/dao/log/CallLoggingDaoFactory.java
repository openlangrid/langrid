package jp.go.nict.langrid.dao.log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.logging.Logger;

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

public class CallLoggingDaoFactory extends DaoFactory{
	public DaoFactory getOriginalDaoFactory() {
		return originalDaoFactory;
	}

	public void setOriginalDaoFactory(DaoFactory originalDaoFactory) {
		this.originalDaoFactory = originalDaoFactory;
	}

	private DaoFactory originalDaoFactory;

	@Override
	public DaoContext getDaoContext() {
		return originalDaoFactory.getDaoContext();
	}

	@Override
	public SystemPropertyDao createSystemPropertyDao() throws DaoException {
		return newProxyInstance(SystemPropertyDao.class, originalDaoFactory.createSystemPropertyDao());
	}

	@Override
	public GridDao createGridDao() throws DaoException {
		return newProxyInstance(GridDao.class, originalDaoFactory.createGridDao());
	}

	@Override
	public FederationDao createFederationDao() throws DaoException {
		return newProxyInstance(FederationDao.class, originalDaoFactory.createFederationDao());
	}

	@Override
	public UserDao createUserDao() throws DaoException {
		return newProxyInstance(UserDao.class, originalDaoFactory.createUserDao());
	}

	@Override
	public TemporaryUserDao createTemporaryUserDao() throws DaoException {
		return newProxyInstance(TemporaryUserDao.class, originalDaoFactory.createTemporaryUserDao());
	}

	@Override
	public ServiceDao createServiceDao() throws DaoException {
		return newProxyInstance(ServiceDao.class, originalDaoFactory.createServiceDao());
	}

	@Override
	public ServiceDeploymentDao createServiceDeploymentDao()
			throws DaoException {
		return newProxyInstance(ServiceDeploymentDao.class, originalDaoFactory.createServiceDeploymentDao());
	}

	@Override
	public NodeDao createNodeDao() throws DaoException {
		return newProxyInstance(NodeDao.class, originalDaoFactory.createNodeDao());
	}

	@Override
	public AccessLimitDao createAccessLimitDao() throws DaoException {
		return newProxyInstance(AccessLimitDao.class, originalDaoFactory.createAccessLimitDao());
	}

	@Override
	public AccessLogDao createAccessLogDao() throws DaoException {
		return newProxyInstance(AccessLogDao.class, originalDaoFactory.createAccessLogDao());
	}

	@Override
	public AccessRightDao createAccessRightDao() throws DaoException {
		return newProxyInstance(AccessRightDao.class, originalDaoFactory.createAccessRightDao());
	}

	@Override
	public AccessStatDao createAccessStateDao() throws DaoException {
		return newProxyInstance(AccessStatDao.class, originalDaoFactory.createAccessStateDao());
	}

	@Override
	public OverUseLimitDao createOverUseLimitDao() throws DaoException {
		return newProxyInstance(OverUseLimitDao.class, originalDaoFactory.createOverUseLimitDao());
	}

	@Override
	public OverUseStateDao createOverUseStateDao() throws DaoException {
		return newProxyInstance(OverUseStateDao.class, originalDaoFactory.createOverUseStateDao());
	}

	@Override
	public ResourceDao createResourceDao() throws DaoException {
		return newProxyInstance(ResourceDao.class, originalDaoFactory.createResourceDao());
	}

	@Override
	public AcceptableRemoteAddressDao createAcceptableRemoteAddressDao()
			throws DaoException {
		return newProxyInstance(AcceptableRemoteAddressDao.class, originalDaoFactory.createAcceptableRemoteAddressDao());
	}

	@Override
	public ServiceActionScheduleDao createServiceActionScheduleDao()
			throws DaoException {
		return newProxyInstance(ServiceActionScheduleDao.class, originalDaoFactory.createServiceActionScheduleDao());
	}

	@Override
	public OperationRequestDao createOperationRequestDao() throws DaoException {
		return newProxyInstance(OperationRequestDao.class, originalDaoFactory.createOperationRequestDao());
	}

	@Override
	public NewsDao createNewsDao() throws DaoException {
		return newProxyInstance(NewsDao.class, originalDaoFactory.createNewsDao());
	}

	@Override
	public DomainDao createDomainDao() throws DaoException {
		return newProxyInstance(DomainDao.class, originalDaoFactory.createDomainDao());
	}

	@Override
	public ProtocolDao createProtocolDao() throws DaoException {
		return newProxyInstance(ProtocolDao.class, originalDaoFactory.createProtocolDao());
	}

	@Override
	public ResourceTypeDao createResourceTypeDao() throws DaoException {
		return newProxyInstance(ResourceTypeDao.class, originalDaoFactory.createResourceTypeDao());
	}

	@Override
	public ServiceTypeDao createServiceTypeDao() throws DaoException {
		return newProxyInstance(ServiceTypeDao.class, originalDaoFactory.createServiceTypeDao());
	}

	@Override
	public InvocationDao createInvocationDao() throws DaoException {
		return newProxyInstance(InvocationDao.class, originalDaoFactory.createInvocationDao());
	}

	@Override
	public void initialize(List<Class<?>> additionalEntities)
			throws DaoException {
		originalDaoFactory.initialize(additionalEntities);
	}

	private <T> T newProxyInstance(Class<T> clazz, final T dao){
		return clazz.cast(Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader()
				, new Class<?>[]{clazz}
				, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						logger.info(method.getName());
						try{
							return method.invoke(dao, args);
						} catch(InvocationTargetException e){
							throw e.getCause();
						}
					}
				}));
	}

	private static Logger logger = Logger.getLogger(
			CallLoggingDaoFactory.class.getName()
			);
}
