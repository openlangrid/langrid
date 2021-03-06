/*
 * $Id: EditUserProfileAdminPage.java 1506 2015-03-02 16:03:34Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.admin.component.form.EditUserProfileAdminForm;

import org.apache.wicket.Page;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public class EditUserProfileAdminPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public EditUserProfileAdminPage(final UserModel model){
		@SuppressWarnings("serial")
		EditUserProfileAdminForm form = new EditUserProfileAdminForm(
				"form", model.getUserId(), getSelfGridId(), model)
		{
			@Override
			protected Page getCancelPageClass(){
				return new AllLanguageGridUsersPage();
			}

			@Override
			protected boolean hasCancel(){
				return true;
			}

			@Override
			protected void setResultPage(UserModel resultParameter){
				try {
					setResponsePage(new EditUserProfileResultAdminPage(model.getUserId(),
							resultParameter));
				} catch (ServiceManagerException e) {
					throw new RuntimeException(e);
				}
			}
		};
		add(form);
	}
}
