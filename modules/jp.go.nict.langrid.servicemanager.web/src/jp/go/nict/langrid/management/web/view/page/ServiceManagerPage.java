/*
 * $Id: ServiceManagerPage.java 1506 2015-03-02 16:03:34Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page;

import java.util.Set;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.enumeration.UserRole;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.menu.SideMenuMaker;
import jp.go.nict.langrid.management.web.view.component.menu.panel.SessionOperatePanel;
import jp.go.nict.langrid.management.web.view.page.other.ExpiredPasswordChangePage;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 *  サービスマネージャの基本となるページ。<br/>
 *  BasePageにヘッダ、ログイン用パネル、サイドメニュー、フッタ、フィードバックパネルが追加される
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public class ServiceManagerPage extends BasePage {
	/**
	 * 
	 * 
	 */
	public ServiceManagerPage() {
		FeedbackPanel fp = new FeedbackPanel("feedback");
		fp.setEscapeModelStrings(false);
		fp.setOutputMarkupId(true);
		add(fp);
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		Set<UserRole> userRoles = session.getUserRoles();

		try {
			add(new SideMenuMaker().makeMenu(
					"nestedMenu", getPageClass().getName(), userRoles));
		} catch(ServiceManagerException e) {
			LogWriter.writeError("System", e, getClass(), "Menu component can't be maked.");
			e.printStackTrace();
		}

		add(new SessionOperatePanel(
				"SessionOperate", !userRoles.isEmpty(), session.getUserId()));
		add(new Label("ServiceManagerCopyright"
			, MessageUtil.getServiceManagerCopyright()).setEscapeModelStrings(false));
		add(new Label("Version", MessageUtil.VERSION));
		ServiceFactory sf =ServiceFactory.getInstance();
		
		try {
			GridModel g = sf.getGridService().get(getSelfGridId());
			String userId = g.getOperatorUserId();
			String org = sf.getUserService(getSelfGridId()).get(userId).getOrganization();
			add(new Label("operatingOrganization", org));
			add(new Label("pageHeaderLabel", g.getGridName()).setRenderBodyOnly(true));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}

		if(session.isExpiredPassword()
			&& !ExpiredPasswordChangePage.class.isAssignableFrom(this.getClass()))
		{
			throw new RestartResponseException(new ExpiredPasswordChangePage());
		}
	}

	public FeedbackPanel getFeedbackPanel() {
		return (FeedbackPanel)this.get(feedbackPanelName);
	}

	private String feedbackPanelName = "feedback";
}
