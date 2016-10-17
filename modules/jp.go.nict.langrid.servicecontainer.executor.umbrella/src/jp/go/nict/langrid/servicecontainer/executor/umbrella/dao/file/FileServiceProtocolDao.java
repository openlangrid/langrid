package jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.file;

import java.io.IOException;

import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoException;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.ServiceProtocolDao;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.entity.ServiceProtocol;

public class FileServiceProtocolDao
implements ServiceProtocolDao{
	public FileServiceProtocolDao(FileDaoContext context){
		this.context = context;
	}

	@Override
	public ServiceProtocol getServiceProtocol(String gridId, String serviceId)
			throws DaoException {
		try{
			String value = context.load(ServiceProtocol.class, gridId + ":" + serviceId);
			if(value == null){
				value = context.load(ServiceProtocol.class, "*:" + serviceId);
			}
			if(value == null){
				value = context.load(ServiceProtocol.class, gridId + ":*");
			}
			if(value == null){
				value = context.load(ServiceProtocol.class, "*:*");
			}
			return new ServiceProtocol(gridId, serviceId, value);
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	private FileDaoContext context;
}
