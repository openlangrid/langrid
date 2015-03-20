/*
 * $Id: UserSignupResultPage.java Trang $
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
package jp.go.nict.langrid.management.web.view.page.other;

import jp.go.nict.langrid.management.web.annotation.RequireLogin;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.markup.html.basic.Label;

/**
 * 
 * @author Trang
 *
 */
@RequireLogin(false)
public class UserSignupResultPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public UserSignupResultPage(String userId){
		try {
			UserModel model = ServiceFactory.getInstance().getUserService(getSelfGridId()).get(userId);
			add(new Label("userId", userId));
			add(new Label("organization", model.getOrganization()));
			add(new Label("representative", model.getRepresentative()));
			add(new Label("address", model.getAddress()));
			add(new Label("emailAddress", model.getEmailAddress()));
			add(new Label("homepageUrl", model.getHomepageUrl() == null ? "" : model.getHomepageUrl() == null ? "" : model.getHomepageUrl().getValue().toString()));
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}

}
