package jp.go.nict.langrid.management.web.view.model.provider;

import jp.go.nict.langrid.management.web.model.ProtocolModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DataService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class ProtocolSortableDataProvider
extends LangridSortableDataProvider<ProtocolModel> {
	/**
	 * 
	 * 
	 */
	public ProtocolSortableDataProvider(String gridId, String userId)
	throws ServiceManagerException {
		this.gridId = gridId;
		this.userId = userId;
	}

	@Override
	protected DataService<ProtocolModel> getService() throws ServiceManagerException {
		return ServiceFactory.getInstance().getProtocolService(gridId, gridId, userId);
	}

	private String gridId;
	private String userId;
}
