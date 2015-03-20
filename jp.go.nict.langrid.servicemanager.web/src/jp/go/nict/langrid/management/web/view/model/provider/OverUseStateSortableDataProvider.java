package jp.go.nict.langrid.management.web.view.model.provider;

import java.util.Calendar;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.management.web.model.OverUseStateModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.OveruseLimitControlService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

public class OverUseStateSortableDataProvider extends
	AbstractLangridSortableDataProvider<OverUseStateModel> {
	public OverUseStateSortableDataProvider(String gridId, Calendar before, Calendar now, Period period) {
		this.before = before;
		this.now = now;
		this.gridId = gridId;
		this.period = period;
		this.count = -1;
	}

	@Override
	protected LangridList<OverUseStateModel> getList(int first, int count)
	throws ServiceManagerException {
		OveruseLimitControlService service = ServiceFactory.getInstance().getOveruseLimitControlService(gridId, gridId);
		LangridList<OverUseStateModel> list = service.getStatList(first, count, before, now
			, new Order[]{new Order("lastAccessDateTime", OrderDirection.DESCENDANT)}, period);
		return list;
	}

	@Override
	protected int getTotalCount() throws ServiceManagerException {
		if(count != -1){
			return count;
		}
		OveruseLimitControlService service = ServiceFactory.getInstance().getOveruseLimitControlService(gridId, gridId);
		count = service.getStatTotalCount(before, now
			, new Order[]{}, period); 
		return count;
	}

	private String gridId;
	private Calendar now;
	private Calendar before;
	private Period period;
	private int count = -1;
}
