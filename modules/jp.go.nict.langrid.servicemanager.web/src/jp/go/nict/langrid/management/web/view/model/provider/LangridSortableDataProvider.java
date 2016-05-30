package jp.go.nict.langrid.management.web.view.model.provider;

import java.io.Serializable;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.DataService;
import jp.go.nict.langrid.management.web.model.service.LangridList;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public abstract class LangridSortableDataProvider<T extends Serializable>
extends AbstractLangridSortableDataProvider<T> 
{
	/**
	 * 
	 * 
	 */
	public LangridSortableDataProvider() {
	}

	@Override
	protected LangridList<T> getList(int first, int count) throws ServiceManagerException {
		return getService().getList(first, count, getConditions(), getOrders(), getScope());
	}
	
	@Override
	protected int getTotalCount() throws ServiceManagerException {
		return getService().getTotalCount(getConditions(), getScope());
	}

	protected abstract DataService<T> getService() throws ServiceManagerException;
}
