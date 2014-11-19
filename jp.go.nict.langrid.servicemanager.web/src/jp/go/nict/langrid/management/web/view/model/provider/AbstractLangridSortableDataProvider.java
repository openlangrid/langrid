package jp.go.nict.langrid.management.web.view.model.provider;

import java.util.Iterator;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.view.utility.RequestResponseUtil;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public abstract class AbstractLangridSortableDataProvider<T>
extends SortableDataProvider<T>
{
	public Iterator<T> iterator(int first, int count){
		LangridList<T> list = new LangridList<T>();
		try{
			list = getList(first, count);
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}
		return list.iterator();
	}

	public IModel<T> model(T obj) {
		return new LangridDetachableModel(obj);
	}

	public int size(){
	   try {
         return getTotalCount();
      } catch(ServiceManagerException e) {
         doErrorProcess(e);
      }
      return 0;
	}

	/**
	 * 
	 * 
	 */
	public void setConditions(MatchingCondition[] conditions, Order[] orders, Scope scope) {
		this.conditions = conditions;
		this.orders = orders;
		this.scope = scope;
	}

	/**
	 * 
	 * 
	 */
	protected abstract LangridList<T> getList(int first, int count)
	throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	protected abstract int getTotalCount() throws ServiceManagerException;

	/**
	 * 
	 * 
	 */
	protected void doErrorProcess(ServiceManagerException e){
		LogWriter.writeError("LangridDataProvider", e, this.getClass());
		throw new RestartResponseException(RequestResponseUtil.getPageForErrorRequest(e));
	}
	
	protected MatchingCondition[] getConditions() {
		return conditions;
	}
	
	protected Order[] getOrders() {
		return orders;
	}

	protected Scope getScope() {
		return scope;
	}
	
	private MatchingCondition[] conditions = new MatchingCondition[]{};
	private Order[] orders = new Order[]{};
	private Scope scope = Scope.ALL;
	
	private class LangridDetachableModel extends LoadableDetachableModel<T> {
		public LangridDetachableModel(T obj) {
			setObject(obj);
		}
		@Override
		protected T load() {
			return getObject();
		}

		private static final long serialVersionUID = 7658665570098729410L;
	}
}
