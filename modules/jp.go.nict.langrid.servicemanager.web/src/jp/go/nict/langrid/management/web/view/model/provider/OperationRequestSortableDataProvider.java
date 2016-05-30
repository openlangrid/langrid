package jp.go.nict.langrid.management.web.view.model.provider;

import jp.go.nict.langrid.management.web.model.OperationRequestModel;
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
public class OperationRequestSortableDataProvider
extends LangridSortableDataProvider<OperationRequestModel> {
	/**
	 * 
	 * 
	 */
	public OperationRequestSortableDataProvider(String gridId, String userId)
	throws ServiceManagerException {
		this.gridId = gridId;
		this.userId = userId;
	}

	@Override
	protected DataService<OperationRequestModel> getService()
	throws ServiceManagerException {
		return ServiceFactory.getInstance().getOperationService(gridId, gridId, userId);
	}

	private String gridId;
	private String userId;
}
