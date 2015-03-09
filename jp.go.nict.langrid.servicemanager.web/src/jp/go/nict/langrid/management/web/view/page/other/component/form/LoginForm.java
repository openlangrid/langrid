/*
 * $Id: LoginForm.java 1506 2015-03-02 16:03:34Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.other.component.form;

import java.util.HashMap;

import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.form.AbstractStatelessForm;
import jp.go.nict.langrid.management.web.view.component.text.RequiredPasswordTextField;
import jp.go.nict.langrid.management.web.view.page.other.SignupPage;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredUserIdField;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public abstract class LoginForm extends AbstractStatelessForm<HashMap<String, String>>{
	/**
	 * 
	 * 
	 */
	public LoginForm(String gridId, String formId){
		super(formId);
		this.gridId = gridId;
	}

	@Override
	protected void addComponents(HashMap<String, String> initialParameter){
		add(userId = new RequiredUserIdField("userId"));
		add(password = new RequiredPasswordTextField("password"));
		MarkupContainer c = new WebMarkupContainer("createAccountContainer").add(
				new Link("createAccount"){
				private static final long serialVersionUID = 1L;
				
				@Override
				public void onClick(){
					setResponsePage(new SignupPage());
				}
			});
		c.setVisible(MessageUtil.isOpenLangrid());
		add(c);
	}

	@Override
	protected void doErrorProcess(ServiceManagerException e){
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		session.clearAuthenticatedParameter();
		super.doErrorProcess(e);
	}

	@Override
	protected void onSubmit(){
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		try{
			if(!session.authenticate(gridId, userId.getModelObject(), password.getModelObject())){
				isValidateError = true;
				errorMessage = MessageManager.getMessage("Login.error.invaild.Inputs",
						getLocale());
				LogWriter.writeWarn(getSessionUserId(), "Can't authenticate session.",
						getPage().getClass(), "LoginUser:" + userId.getModelObject());
				return;
			}
			LogWriter.writeInfo(getSessionUserId(), "Authenticate session", getPage().getClass());
		}catch(ServiceManagerException e){
		   if(e.getParentException() instanceof UserNotFoundException){
		      error(MessageManager.getMessage("Login.error.invaild.Inputs", getLocale()));
		      isCancel = true;
		      LogWriter.writeWarn(getSessionUserId(), "Can't authenticate session.",
		         getPage().getClass(), "LoginUser:" + userId.getModelObject());
		      return;
		   }
			raisedException = e;
		}
	}
	
	private RequiredPasswordTextField password;
	private RequiredUserIdField userId;
	private String gridId;

	private static final long serialVersionUID = 1L;
}
