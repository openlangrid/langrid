package jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.file;

import java.io.IOException;

import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoException;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.EndpointAddressProtocolDao;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.entity.EndpointAddressProtocol;

public class FileEndpointAddressProtocolDao
implements EndpointAddressProtocolDao{
	public FileEndpointAddressProtocolDao(FileDaoContext context){
		this.context = context;
	}

	@Override
	public EndpointAddressProtocol getEndpointAddressProtocol(String invocationName)
			throws DaoException {
		try{
			String value = context.load(EndpointAddressProtocol.class, invocationName);
			return new EndpointAddressProtocol(invocationName, value);
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	private FileDaoContext context;
}
