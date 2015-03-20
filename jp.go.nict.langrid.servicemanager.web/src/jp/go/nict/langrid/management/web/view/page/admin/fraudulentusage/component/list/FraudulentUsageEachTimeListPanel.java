/*
 * $Id: FraudulentUsageEachTimeListPanel.java 406 2011-08-25 02:12:29Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage.component.list;

import java.util.Calendar;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.web.model.IndividualExecutionInformationModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.ServiceInformationService;
import jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage.FraudulentUsagePage;
import jp.go.nict.langrid.management.web.view.page.admin.fraudulentusage.component.list.row.FraudulentUsageEachTimeListRowPanel;
import jp.go.nict.langrid.management.web.view.page.error.ErrorInternalPage;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class FraudulentUsageEachTimeListPanel extends Panel {
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public FraudulentUsageEachTimeListPanel(
		String gridId, String componentId, int limitCount, final int index, Calendar start, Calendar end)
	throws ServiceManagerException
	{
		super(componentId);
		setOutputMarkupId(true);
		this.selfGridId = gridId;
		this.limitCount = limitCount;
		startDate = start;
		endDate = end;
		
		int totalCount = setListView(index);
		setNavigateLink(index, totalCount);
	}
	
	private int setListView(int index) throws ServiceManagerException {
		ServiceInformationService service = ServiceFactory.getInstance().getServiceInformationService(
			selfGridId, selfGridId, "");
		
		LangridList<IndividualExecutionInformationModel> list = service.getLimitOverVerboseList(
			index * pagingCount, pagingCount, "", "", startDate, endDate
			, new MatchingCondition[]{new MatchingCondition("callNest", 0)}, new Order[]{}, limitCount);
		ListView<IndividualExecutionInformationModel> lv = new ListView<IndividualExecutionInformationModel>(
		"logList", new WildcardListModel<IndividualExecutionInformationModel>(list))
		{
			@Override
			protected void populateItem(ListItem<IndividualExecutionInformationModel> item) {
				try {
					item.add(getRowPanel(selfGridId, item, ""));
				} catch(ServiceManagerException e) {
					throw new RestartResponseException(new ErrorInternalPage(e));
				}
			}
			
			private static final long serialVersionUID = 1L;
		};
		addOrReplace(lv);
		WebMarkupContainer wmc = new WebMarkupContainer("hasNoList");
		addOrReplace(wmc);
		if(list.size() == 0) {
			lv.setVisible(false);
		} else {
			wmc.setVisible(false);
		}
		return list.getTotalCount();
	}
	
	private void setNavigateLink(int index, int totalCount){
		boolean isFixed = (totalCount - 1) < ((index + 1) * pagingCount);
		Link previewLink = createLink("topPreviewLink", index - 1);
		previewLink.add(new Label("topPreview", "< Newer").setRenderBodyOnly(true));
		previewLink.setEnabled(0 <= index - 1);
		addOrReplace(previewLink);
		Link nextLink = createLink("topNextLink", index + 1);
		nextLink.add(new Label("topNext", "Older >").setRenderBodyOnly(true));
		nextLink.setEnabled(!isFixed);
		addOrReplace(nextLink);
		Link underPreviewLink = createLink("underPreviewLink", index - 1);
		underPreviewLink.add(new Label("underPreview", "< Newer").setRenderBodyOnly(true));
		underPreviewLink.setEnabled(0 <= index - 1);
		addOrReplace(underPreviewLink);
		Link underNextLink = createLink("underNextLink", index + 1);
		underNextLink.add(new Label("underNext", "Older >").setRenderBodyOnly(true));
		underNextLink.setEnabled(!isFixed);
		addOrReplace(underNextLink);
	}

	private Panel getRowPanel(String nowGridId, ListItem<IndividualExecutionInformationModel> item, String uniqueId)
	throws ServiceManagerException
	{
		return new FraudulentUsageEachTimeListRowPanel("row", item.getModelObject(), uniqueId, limitCount);
	}

	private Link createLink(String linkId, final int index)
	{
		return new Link(linkId) {
			@Override
			@SuppressWarnings("unchecked")
			public void onClick() {
				setResponsePage(new FraudulentUsagePage(index, startDate, endDate));
			}
		};
	}

	private String selfGridId;
	private int limitCount;
	private int pagingCount = 10;
	
	private Calendar startDate;
	private Calendar endDate;
}
