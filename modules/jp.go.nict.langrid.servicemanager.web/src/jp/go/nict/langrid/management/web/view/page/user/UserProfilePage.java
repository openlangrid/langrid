/*
 * $Id: UserProfilePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.user;

import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.view.component.label.CalendarLabel;
import jp.go.nict.langrid.management.web.view.component.label.EmailAddressLabel;
import jp.go.nict.langrid.management.web.view.page.PopupPage;
import jp.go.nict.langrid.management.web.view.page.user.component.link.ExternalHomePageLink;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class UserProfilePage extends PopupPage {
	/**
	 * 
	 * 
	 */
	public UserProfilePage(PageParameters pp) {
		try {
			String gridId = pp.getString("gridId");
			String userId = pp.getString("id");
			if(gridId == null || userId == null) {
				redirectTop();
				return;
			}
			UserModel user = ServiceFactory.getInstance().getUserService(gridId).get(userId);
			if(user == null) {
				redirectTop();
				return;
			}
			setDefaultModel(new CompoundPropertyModel<UserModel>(user));
			add(new Label("organization"));
			add(new Label("representative"));
			add(new EmailAddressLabel("emailAddress"));
			String url = user.getHomepageUrl() != null ? user.getHomepageUrl().getValue()
				.toExternalForm() : "";
			add(new ExternalHomePageLink("linkUrl", url, user.getUserId()));
			add(new Label("address"));
			add(new CalendarLabel("registeredDate", DateUtil.STR_YMD_SLASH,
				user.getCreatedDateTime()));
		} catch(ServiceManagerException e) {
			doErrorProcessForPopup(e);
		}
	}
}
