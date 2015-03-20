/*
 * $Id: SessionOperatePanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.component.menu.panel;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.link.NoSessionIdBookmarkablePageLink;
import jp.go.nict.langrid.management.web.view.page.other.LoginPage;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class SessionOperatePanel extends Panel{
	/**
	 * 
	 * 
	 */
	public SessionOperatePanel(String componentId, boolean isLogin, String userId){
		super(componentId, new Model());
		WebMarkupContainer container;
		add(container = new WebMarkupContainer("container"));
		if(isLogin){
			Link link = new Link(operateLinkId){
				@Override
				public void onClick(){
					// セッションの破棄
					getSession().invalidateNow();
					// クッキーの削除
					HttpServletRequest request = ((WebRequest)RequestCycle.get()
							.getRequest()).getHttpServletRequest();
					HttpServletResponse response = ((WebResponse)RequestCycle.get()
							.getResponse()).getHttpServletResponse();
					Cookie[] cookies = request.getCookies() == null ? new Cookie[]{}
							: request.getCookies();
					for(Cookie cookie : cookies){
						if(cookie.getName().equals("JSESSIONID")){
							cookie.setMaxAge(0);
							response.addCookie(cookie);
						}
					}
					setResponsePage(LoginPage.class);
					ServiceManagerSession session = (ServiceManagerSession)getSession();
					LogWriter.writeInfo(session.getUserId()
							, session.getUserId() + " has been Logout", getPage().getPageClass());
				}

				@Override
				protected CharSequence getURL(){
					String url = new StringBuffer(super.getURL()).toString();
					String temp = url.replaceAll(";jsessionid=(.+\\?|.+)", "?");
					return temp.endsWith("?")
								? temp.substring(0, temp.length() - 1) : temp;
				}

				private static final long serialVersionUID = 1L;
			};
			link.add(new Label("operateLabel"
					, MessageManager.getMessage("Login.label.Logout", getLocale()))
						.setRenderBodyOnly(true));
			container.add(link);
			container.add(new AttributeAppender(
					"class", new Model<String>("btn-account-area"), " "));
		}else{
			Link link = new NoSessionIdBookmarkablePageLink(operateLinkId,
					LoginPage.class);
			link.add(new Label("operateLabel"
					, MessageManager.getMessage("Login.label.Login", getLocale()))
						.setRenderBodyOnly(true));
			container.add(link);
		}
		container.add(new Label("userId", new Model<String>(userId)).setVisible(isLogin));
		container.add(new Label("userLabel", new Model<String>(
				MessageManager.getMessage("Login.label.User", getLocale())))
					.setVisibilityAllowed(isLogin));
	}

	private static final String operateLinkId = "operateLink";
	private static final long serialVersionUID = 1L;
}
