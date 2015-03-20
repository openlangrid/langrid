/*
 * $Id: ChangePasswordForm.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.user.component.form;

import java.util.HashMap;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.form.AbstractStatelessForm;
import jp.go.nict.langrid.management.web.view.component.text.RequiredPasswordTextField;
import jp.go.nict.langrid.management.web.view.page.user.component.form.validator.ChangePasswordFormValidator;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public abstract class ChangePasswordForm extends AbstractStatelessForm<HashMap<String, String>>{
	/**
	 * 
	 * 
	 */
	public ChangePasswordForm(String formId, String gridId) {
		super(formId);
		this.gridId = gridId;
	}
	
	@Override
	protected void addComponents(HashMap<String, String> initialParameter) {
		add(oldPass = new RequiredPasswordTextField("oldPassword"));
		add(newPass = new RequiredPasswordTextField("newPassword"));
		add(newPassConfirm = new RequiredPasswordTextField("newPasswordConfirm"));
		add(new ChangePasswordFormValidator(oldPass, newPass, newPassConfirm, getSessionPassword()));
	}
	
	@Override
	protected void onSubmit() {
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		try {
			ServiceFactory.getInstance().getUserService(gridId).changePassword(getSessionUserId(), newPass.getModelObject());
			session.setPassword(newPass.getModelObject());
			session.setIsExpiredPassword(false);
			setResponsePage(getApplication().getHomePage());
			LogWriter.writeInfo(getSessionUserId(), "Current user password has been changed.", getPage().getPageClass());
		} catch(ServiceManagerException e) {
			raisedException = e;
		}
	}
	
	protected RequiredPasswordTextField newPass;
	protected RequiredPasswordTextField newPassConfirm;
	private RequiredPasswordTextField oldPass;
	private String gridId;
}
