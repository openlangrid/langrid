/*
 * $Id: MonitoringUserLogLimitedPagingListPanel.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.user.component.list;

import java.util.Calendar;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.management.web.model.ExecutionInformationStatisticsModel;
import jp.go.nict.langrid.management.web.model.IndividualExecutionInformationModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.ServiceInformationService;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.view.page.language.service.MonitoringLanguageServiceVerbosePage;
import jp.go.nict.langrid.management.web.view.page.user.component.list.row.MonitoringUserLogListRowPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class MonitoringUserLogLimitedPagingListPanel extends Panel {
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public MonitoringUserLogLimitedPagingListPanel(
		String componentId, ExecutionInformationStatisticsModel model, int index,
		Calendar start, Calendar end)
	throws ServiceManagerException {
		super(componentId);
		this.organization = model.getUserOrganization();
		ServiceInformationService service = ServiceFactory.getInstance()
			.getServiceInformationService(
				model.getServiceGridId(), model.getUserGridId(), model.getUserId());
		LangridList<IndividualExecutionInformationModel> list = service.getVerboseList(
			index * pagingCount, pagingCount, model.getUserId(), model.getServiceId(),
			start, end
			, new MatchingCondition[]{}, new Order[]{});
		ListView<IndividualExecutionInformationModel> lv = new ListView<IndividualExecutionInformationModel>(
				"logList", new WildcardListModel<IndividualExecutionInformationModel>(
					list))
		{
			@Override
			protected void populateItem(ListItem<IndividualExecutionInformationModel> item) {
				item.add(getRowPanel(item));
			}

			private static final long serialVersionUID = 1L;
		};
		add(lv);
		WebMarkupContainer wmc = new WebMarkupContainer("hasNoList");
		add(wmc);
		if(list.size() == 0) {
			lv.setVisible(false);
		} else {
			wmc.setVisible(false);
		}
		boolean isFixed = (list.getTotalCount() - 1) < ((index + 1) * pagingCount);
		Link previewLink = createLink("topPreviewLink", index - 1, start, end, model);
		previewLink.add(new Label("topPreview", "< Newer").setRenderBodyOnly(true));
		previewLink.setEnabled(0 <= index - 1);
		add(previewLink);
		Link nextLink = createLink("topNextLink", index + 1, start, end, model);
		nextLink.add(new Label("topNext", "Older >").setRenderBodyOnly(true));
		nextLink.setEnabled(!isFixed);
		add(nextLink);
		Link underPreviewLink = createLink("underPreviewLink", index - 1, start, end,
			model);
		underPreviewLink
			.add(new Label("underPreview", "< Newer").setRenderBodyOnly(true));
		underPreviewLink.setEnabled(0 <= index - 1);
		add(underPreviewLink);
		Link underNextLink = createLink("underNextLink", index + 1, start, end, model);
		underNextLink.add(new Label("underNext", "Older >").setRenderBodyOnly(true));
		underNextLink.setEnabled(!isFixed);
		add(underNextLink);
		add(new Label("timeZone", "(" + DateUtil.defaultTimeZone() + ")"));
	}

	protected Page getRequestPage(int index, Calendar start, Calendar end
		, ExecutionInformationStatisticsModel model) {
		return new MonitoringLanguageServiceVerbosePage(model, start, end, index);
	}

	protected Panel getRowPanel(ListItem<IndividualExecutionInformationModel> item) {
		return new MonitoringUserLogListRowPanel("row", item.getModelObject(), "",
			organization);
	}

	private Link createLink(String linkId, final int index, final Calendar start,
		final Calendar end
		, ExecutionInformationStatisticsModel model) {
		return new Link<ExecutionInformationStatisticsModel>(
			linkId, new Model<ExecutionInformationStatisticsModel>(model)) {
			@Override
			@SuppressWarnings("unchecked")
			public void onClick() {
				setResponsePage(getRequestPage(index, start, end, getModelObject()));
			}
		};
	}

	protected String organization;
	private int pagingCount = 50;
	private static final long serialVersionUID = 1L;
}
