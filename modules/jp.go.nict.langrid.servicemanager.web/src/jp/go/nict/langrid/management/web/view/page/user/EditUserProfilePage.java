/*
 * $Id: EditUserProfilePage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.form.EditUserProfileForm;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class EditUserProfilePage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public EditUserProfilePage(){
		try{
			UserModel user = ServiceFactory.getInstance().getUserService(getSelfGridId()).get(getSessionUserId());
			EditUserProfileForm form = new EditUserProfileForm("form", getSessionUserId(), getSelfGridId(), user){
					@Override
					protected void setResultPage(UserModel resultParameter){
						setResponsePage(new EditUserProfileResultPage(resultParameter));
					}

				private static final long serialVersionUID = 0L;
			};
			add(form);
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}
	}
}
