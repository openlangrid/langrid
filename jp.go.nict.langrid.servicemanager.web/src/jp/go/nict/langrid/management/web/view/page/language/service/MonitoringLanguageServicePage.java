/*
 * $Id: MonitoringLanguageServicePage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service;

import java.util.Calendar;

import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.ListType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.model.LangridSearchCondition;
import jp.go.nict.langrid.management.web.view.model.provider.AtomicServiceSortableDataProvider;
import jp.go.nict.langrid.management.web.view.model.provider.CompositeServiceSortableDataProvider;
import jp.go.nict.langrid.management.web.view.model.provider.LangridSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceAccessRightRadioPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.ServiceListPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.MonitoringLanguageServicesListRowPanel;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class MonitoringLanguageServicePage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public MonitoringLanguageServicePage() {
		try {
			LangridSearchCondition conditions = getCondition();
			listContainer = new WebMarkupContainer("listContainer");
			String gridId = getSelfGridId();
			// add public atomic service list
			listContainer.add(makeList(gridId, "atomicList"
				, ListType.PUBLICATOMICSERVICE, conditions));
			listContainer.add(getAccessRightPanel("atomicAccessRightPanel", "atomicList"
				, ListType.PUBLICATOMICSERVICE));
			// add composite service list
			listContainer.add(makeList(gridId, "compositeList"
				, ListType.PUBLICCOMPOSITESERVICE, conditions));
			listContainer.add(getAccessRightPanel("compositeAccessRightPanel"
				, "compositeList", ListType.PUBLICCOMPOSITESERVICE));
			add(listContainer);
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

	private Component makeList(
		String gridId, String listId, ListType type, LangridSearchCondition conditions)
	throws ServiceManagerException {
		Component component = null;
		if(type.equals(ListType.PUBLICATOMICSERVICE)) {
			AtomicServiceSortableDataProvider provider = new AtomicServiceSortableDataProvider(
				gridId, gridId, getSessionUserId());
			component = createListPanel(listId, provider, conditions);
			component.setOutputMarkupId(true);
		} else if(type.equals(ListType.PUBLICCOMPOSITESERVICE)) {
			CompositeServiceSortableDataProvider provider = new CompositeServiceSortableDataProvider(
				gridId, gridId, getSessionUserId());
			component = createListPanel(listId, provider, conditions);
			component.setOutputMarkupId(true);
		}
		listContainer.addOrReplace(component);
		return component;
	}

	protected <T extends ServiceModel>ServiceListPanel<T> createListPanel(
			String panelId, LangridSortableDataProvider<T> provider,
		LangridSearchCondition conditions)
	throws ServiceManagerException {
		provider.setConditions(conditions.getConditions(), conditions.getOrders(),
			conditions.getScope());
		return new ServiceListPanel<T>(getSelfGridId(), panelId, provider) {
			@Override
			protected Panel getRowPanel(String gridId, Item<T> item, String uniqueId)
			throws ServiceManagerException {
				return getListRowPanel(item, uniqueId);
			}

			private static final long serialVersionUID = 1L;
		};
	}

	protected <T extends ServiceModel>Panel getListRowPanel(Item<T> item, String uniqueId)
	throws ServiceManagerException {
		return new MonitoringLanguageServicesListRowPanel("row", item.getModelObject(),
			uniqueId) {
			@Override
			protected String getOrganizationName(ServiceModel entry)
			throws ServiceManagerException {
				UserModel ue = ServiceFactory.getInstance()
					.getUserService(entry.getGridId()).get(entry.getOwnerUserId());
				return ue == null ? "" : ue.getOrganization();
			}

			@Override
			protected Page getResponsePage(String serviceGridId, String serviceId,
				String serviceName, Calendar start, Calendar end) {
				return getMonitarPage(serviceGridId, serviceId, serviceName, start, end);
			}

			private static final long serialVersionUID = 1L;
		};
	}

	protected Page getMonitarPage(String serviceGridId, String serviceId,
		String serviceName, Calendar start, Calendar end) {
		return new MonitoringLanguageServiceStatisticPage(serviceGridId, serviceId,
			serviceName, start, end);
	}

	protected ServiceAccessRightRadioPanel getAccessRightPanel(
		String panelId, final String listId, final ListType listType) {
		return new ServiceAccessRightRadioPanel(panelId) {
			@Override
			protected void doUpdate(boolean isMembersOnly, AjaxRequestTarget target) {
				try {
					LangridSearchCondition conditions = getCondition();
					conditions.putOrReplaceCondition("membersOnly", isMembersOnly);
					target.addComponent(makeList(getSelfGridId(), listId, listType,
						conditions));
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
			}
		};
	}

	protected LangridSearchCondition getCondition() {
		LangridSearchCondition conditions = new LangridSearchCondition();
		conditions.setScope(Scope.MINE);
		conditions.putOrReplaceCondition("approved", true);
		conditions.putOrReplaceCondition("membersOnly", false);
		return conditions;
	}

	//	private WebMarkupContainer rewriteContainer;
	private WebMarkupContainer listContainer;
	//	private WebMarkupContainer membersContainer;
}
