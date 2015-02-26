/*
 * $Id: EditUserProfileResultAdminPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.user.admin;

import java.util.Collections;
import java.util.Set;

import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.UserRole;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.label.CalendarLabel;
import jp.go.nict.langrid.management.web.view.component.label.EmailAddressLabel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class EditUserProfileResultAdminPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public EditUserProfileResultAdminPage(String userId, UserModel model)
	throws ServiceManagerException{
		setDefaultModel(new CompoundPropertyModel<UserModel>(model));
		add(new CalendarLabel("updatedDate", DateUtil.STR_YMD_SLASH,  model.getUpdatedDateTime()));
		add(new Label("userId"));
		add(new Label("organization"));
		add(new Label("representative"));
		add(new EmailAddressLabel("emailAddress"));
		add(new Label("homepageUrl", new Model<String>(model.getHomepageUrl() == null ? "" : model.getHomepageUrl().getValue().toString())));
		add(new Label("address"));
		add(new Label("defaultUseType", MessageManager.getMessage("UseType." + model.getDefaultUseType(), getLocale())));
		add(new Label("defaultAppProvisionType", MessageManager.getMessage("AppProvisionType." + model.getDefaultAppProvisionType(), getLocale())));
		@SuppressWarnings("unchecked")
		Set<UserRole> roles = Collections.EMPTY_SET;
		if(model.getUserId() != null){
			roles = ServiceFactory.getInstance().getUserService(model.getGridId())
					.getUserRoles(model.getUserId());
		}
		add(new CheckBox("providerRole", new Model<Boolean>(roles.contains(UserRole.SERVICEPROVIDER))));
		add(new CheckBox("userRole", new Model<Boolean>(roles.contains(UserRole.SERVICEUSER))));
	}
}
