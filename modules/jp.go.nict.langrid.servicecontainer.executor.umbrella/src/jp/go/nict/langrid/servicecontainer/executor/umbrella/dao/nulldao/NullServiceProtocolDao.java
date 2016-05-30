package jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.nulldao;

import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoException;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.ServiceProtocolDao;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.entity.ServiceProtocol;

public class NullServiceProtocolDao implements ServiceProtocolDao {
	@Override
	public ServiceProtocol getServiceProtocol(String gridId, String serviceId)
			throws DaoException {
		return null;
	}
}
