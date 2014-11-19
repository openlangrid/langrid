package jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.web.view.component.choice.IndicatingAjaxDropDownChoice;
import jp.go.nict.langrid.management.web.view.model.ServiceSortOrder;
import jp.go.nict.langrid.management.web.view.model.ServiceSortValue;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.renderer.SortChoiceRenderer;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.WildcardListModel;

public class ServiceListSortFormPanel extends AbstractSortFormPanel{
	public ServiceListSortFormPanel(String componentId){
		super(componentId);
		setOutputMarkupId(true);
	}

	@Override
	public ServiceSortOrder getSortOrder(){
		return order;
	}

	@Override
	protected void initialize(){
		order = new ServiceSortOrder(){
			@Override
			protected void setDefaultOrder(){
				setOrReplaceOrder(getServiceNameId(), OrderDirection.ASCENDANT);
			}

			private static final long serialVersionUID = 1L;
		};
		sortColumn = new ArrayList<ServiceSortValue>();
		sortColumn.add(new ServiceSortValue(order.getServiceNameId(), "Ascending order of Service Name"
				, OrderDirection.ASCENDANT));
		sortColumn.add(new ServiceSortValue(order.getServiceNameId(), "Descending order of Service Name"
				, OrderDirection.DESCENDANT));
//		sortColumn.add(new ServiceSortValue(order.getServiceTypeId(), "Ascending order of Service Type"
//				, OrderDirection.ASCENDANT));
//		sortColumn.add(new ServiceSortValue(order.getServiceTypeId(), "Descending order of Service Type"
//				, OrderDirection.DESCENDANT));
		sortColumn.add(new ServiceSortValue(order.getRegisteredDateId(), "Ascending order of Registered Date"
				, OrderDirection.ASCENDANT));
		sortColumn.add(new ServiceSortValue(order.getRegisteredDateId(), "Descending order of Registered Date"
				, OrderDirection.DESCENDANT));
		choice = new IndicatingAjaxDropDownChoice<ServiceSortValue>(
				"sortType", new Model<ServiceSortValue>(), new WildcardListModel<ServiceSortValue>())
		{
			@Override
			protected void doUpdate(AjaxRequestTarget target){
				ServiceSortValue ssv = choice.getModelObject();
				order.initialize();
				order.deleteOrder(ssv.getColumnName());
				order.setOrReplaceOrder(ssv.getColumnName(), ssv.getDirection());
				onSortRequested(target);
			}
			private static final long serialVersionUID = 1L;
		};
		choice.setChoiceRenderer(new SortChoiceRenderer());
		choice.setChoices(sortColumn);
		form.add(choice);
		addOrReplace(form);
	}

	/**
	 * 
	 * 
	 */
	protected void onSortRequested(AjaxRequestTarget target){
	}

	private DropDownChoice<ServiceSortValue> choice;
	private ServiceSortOrder order;
	private List<ServiceSortValue> sortColumn;

	private static final long serialVersionUID = 1L;
}
