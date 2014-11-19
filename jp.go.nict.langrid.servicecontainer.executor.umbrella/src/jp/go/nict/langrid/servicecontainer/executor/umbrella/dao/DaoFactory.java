package jp.go.nict.langrid.servicecontainer.executor.umbrella.dao;

import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.file.FileDaoFactory;

public abstract class DaoFactory {
	/**
	 * 
	 * 
	 */
	public abstract DaoContext getDaoContext();

	/**
	 * 
	 * 
	 */
	public abstract ServiceProtocolDao createServiceProtocolDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public abstract EndpointAddressProtocolDao createEndpointAddressProtocolDao() throws DaoException;

	/**
	 * 
	 * 
	 */
	public static synchronized DaoFactory createInstance(String path) throws DaoException{
		if(daoFactory == null){
			daoFactory = new FileDaoFactory(path);
		}
		return daoFactory;
	}

	private static DaoFactory daoFactory;
}
