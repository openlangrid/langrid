package jp.go.nict.langrid.management.web.view.model.provider;

import jp.go.nict.langrid.management.web.model.NewsModel;
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
public class NewsSortableDataProvider extends LangridSortableDataProvider<NewsModel> {
	/**
	 * 
	 * 
	 */
	public NewsSortableDataProvider(String gridId, String userId)
	throws ServiceManagerException {
		this.gridId = gridId;
		this.userId = userId;
	}

	@Override
	protected DataService<NewsModel> getService() throws ServiceManagerException {
		return ServiceFactory.getInstance().getNewsService(gridId, gridId, userId);
	}

	private String gridId;
	private String userId;
}
