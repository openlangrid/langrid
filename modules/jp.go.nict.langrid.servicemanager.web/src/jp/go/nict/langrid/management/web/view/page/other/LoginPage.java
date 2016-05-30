/*
 * $Id: LoginPage.java 1506 2015-03-02 16:03:34Z t-nakaguchi $
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

import java.util.HashMap;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.RegistrationOfAtomicServiceListPage;
import jp.go.nict.langrid.management.web.view.page.other.component.form.LoginForm;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;

import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public class LoginPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public LoginPage(){
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		session.setLoginedAccess(true);
	}

	@Override
	protected void initialize() throws ServiceManagerException{
		add(new LoginForm(getSelfGridId(), "form"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void setResultPage(HashMap<String, String> resultParameter){
				setResponsePage(getApplication().getHomePage());
			}
		});
	}
}
