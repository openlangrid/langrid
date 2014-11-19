/*
 * $Id: ChangePasswordAdminPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import java.util.HashMap;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.text.RequiredPasswordTextField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.form.ChangePasswordForm;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ChangePasswordAdminPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public ChangePasswordAdminPage(UserModel userEntry) {
		entry = userEntry;
		ChangePasswordForm form;
		add(form = new ChangePasswordForm("form", getSelfGridId()) {
			@Override
			protected void addComponents(HashMap<String, String> initialParameter) {
				add(newPass = new RequiredPasswordTextField("newPassword"));
				add(newPassConfirm = new RequiredPasswordTextField("newPasswordConfirm"));
				add(new Button("change") {
					@Override
					public void onSubmit() {
						try {
						   String gridId = getSelfGridId();
							ServiceFactory.getInstance().getUserService(gridId).changePassword(entry.getUserId(), newPass.getModelObject());
							LogWriter.writeInfo(getSessionUserId(), "Password for \"" + entry.getUserId()
							+ "\" has been changed.", getPageClass());
						} catch(ServiceManagerException e) {
							raisedException = e;
						}
					}

				});
			}
			
			@Override
			protected void setResultPage(HashMap<String, String> resultParameter) {
				setResponsePage(new ChangePasswordResultAdminPage());
			}
		});
		
		form.add(new Link("cancel"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AllLanguageGridUsersPage());
			}
		});
		
		Label label = new Label("message", MessageManager.getMessage(
				"UserSettings.table.headline.password.change", getLocale(),  userEntry.getUserId()));
		add(label);
	}
	
	private UserModel entry;
}
