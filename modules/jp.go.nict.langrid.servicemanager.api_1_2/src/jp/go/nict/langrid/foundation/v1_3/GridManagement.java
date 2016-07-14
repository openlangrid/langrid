package jp.go.nict.langrid.foundation.v1_3;

import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.foundation.AbstractLangridService;
import jp.go.nict.langrid.foundation.annotation.TransactionMethod;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;
import jp.go.nict.langrid.service_1_2.ServiceConfigurationException;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.UserEntry;
import jp.go.nict.langrid.service_1_3.foundation.GridEntry;
import jp.go.nict.langrid.service_1_3.foundation.GridManagementService;

public class GridManagement
extends AbstractLangridService
implements GridManagementService{
	public GridManagement() {
	}
	public GridManagement(ServiceContext sc) {
		super(sc);
	}
	
	@Override
	@TransactionMethod
	public GridEntry getGridEntry()
	throws ProcessFailedException, ServiceConfigurationException{
		try{
			String self = getGridId();
			Grid g = getGridDao().getGrid(self);
			User u = getUserDao().getUser(g.getGridId(), g.getOperatorUserId());
			Converter c = new Converter();
			UserEntry ue = c.convert(u, UserEntry.class);
			ue.setRegisteredDate(u.getCreatedDateTime());
			ue.setUpdatedDate(u.getUpdatedDateTime());
			GridEntry ge = c.convert(g, GridEntry.class);
			ge.setRegisteredDate(g.getCreatedDateTime());
			ge.setUpdatedDate(g.getUpdatedDateTime());
			ge.setOperator(ue);
			return ge;
		} catch(DaoException e){
			throw new ProcessFailedException(e);
		}
	}
}
