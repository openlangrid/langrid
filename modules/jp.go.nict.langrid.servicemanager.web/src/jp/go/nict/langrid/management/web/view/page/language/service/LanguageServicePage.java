/*
 * $Id: LanguageServicePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.FederationService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.model.LangridSearchCondition;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceSearchFormPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.ServiceListFullOptionTabPanel;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class LanguageServicePage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public LanguageServicePage() {
      try {
         String gridId = getSelfGridId();
         // add search form
         searchPanel = new ServiceSearchFormPanel(gridId, "searchPanel");
         searchPanel.setSubmitComponent(new IndicatingAjaxButton("search") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
               try {
                  ServiceListFullOptionTabPanel panel = tabPanelList.get(tabPanel.getSelectedTab());
                  panel.rewriteAllList(searchPanel.getSearchCondition());
                  target.addComponent(panel);
               } catch(ServiceManagerException e) {
                  doErrorProcess(e);
               }
            }
         });
         
         searchPanel.setOutputMarkupId(true);
         searchPanel.setVisibleLogin(((ServiceManagerSession)getSession()).isLogin());
         add(searchPanel);
         
         List<ITab> tabList = new ArrayList<ITab>();
         setTabPanel(gridId, tabList);
         
         FederationService fs = ServiceFactory.getInstance().getFederationService(gridId);
         for(String targetId : fs.getConnectedTargetGridIdList(gridId, new Order("tagetGridName", OrderDirection.ASCENDANT))) {
            setTabPanel(targetId, tabList);
         }
         add(tabPanel = new AjaxTabbedPanel("serviceListPanel", tabList));
      } catch(ServiceManagerException e) {
         doErrorProcess(e);
      }
	}
	
	protected ServiceListFullOptionTabPanel getTabPanel(
	   String panelId, String gridId, LangridSearchCondition condition)
	throws ServiceManagerException
	{
//	   LangridSearchCondition lsc = new LangridSearchCondition();
      condition.addNoChangeCondition("approved", true);
		return new ServiceListFullOptionTabPanel(panelId, gridId
		   , getSelfGridId()
		   , isLogin() ? getSessionUserId() : ""
		   , condition);
	}
	
	private void setTabPanel(final String gridId, List<ITab> tabList) throws ServiceManagerException {
		String gridName = ServiceFactory.getInstance().getGridService().get(gridId).getGridName();
		tabList.add(new AbstractTab(new Model<String>(gridName)) {
			@Override
			public Panel getPanel(String panelId) {
				try {
					if(tabPanel.getSelectedTab() < tabPanelList.size()){
					   tabPanelList.remove(tabPanel.getSelectedTab());
					}
					LangridSearchCondition condition = searchPanel.getSearchCondition();
					ServiceListFullOptionTabPanel panel = getTabPanel(panelId, gridId, condition);
					tabPanelList.put(tabPanel.getSelectedTab(), panel);
					return panel;
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
				return new Panel(panelId);
			}
		});
	}
	
	private Map<Integer, ServiceListFullOptionTabPanel> tabPanelList = new LinkedHashMap<Integer, ServiceListFullOptionTabPanel>();
	private AjaxTabbedPanel tabPanel;
	private ServiceSearchFormPanel searchPanel;
}
