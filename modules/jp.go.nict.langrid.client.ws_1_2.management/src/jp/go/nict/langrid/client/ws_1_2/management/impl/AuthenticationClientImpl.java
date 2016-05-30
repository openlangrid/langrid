/*
 * $Id: AuthenticationClientImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.client.ws_1_2.management.impl;

import java.net.URL;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.client.ws_1_2.error.LangridException;
import jp.go.nict.langrid.client.ws_1_2.impl.ServiceClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.AuthenticationClient;
import localhost.langrid_1_2_N.services.Authentication.AuthenticationServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class AuthenticationClientImpl
extends ServiceClientImpl
implements AuthenticationClient
{
	/**
	 * 
	 * 
	 */
	public AuthenticationClientImpl(URL serviceUrl){
		super(serviceUrl);
	}

	@Override
	protected Stub createStub(URL url) throws ServiceException {
		AuthenticationServiceLocator locator = new AuthenticationServiceLocator();
		setUpService(locator);
		return (Stub)locator.getAuthentication(url);
	}

	public boolean authenticate(String userId, String password)
			throws LangridException {
		return (Boolean)invoke(userId, password);
	}

	public void nop() throws LangridException {
		invoke();
	}

	public boolean isAdministrator() throws LangridException {
		return (Boolean)invoke();
	}

	private static final long serialVersionUID = -4765914845576338749L;
}
