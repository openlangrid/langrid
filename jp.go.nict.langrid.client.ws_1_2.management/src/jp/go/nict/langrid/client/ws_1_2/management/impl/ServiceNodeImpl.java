/*
 * $Id: ServiceNodeImpl.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
import localhost.wrapper_1_2.services.ServiceNode.ServiceNodeServiceLocator;

import org.apache.axis.client.Stub;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class ServiceNodeImpl
extends ServiceClientImpl
{
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 5461637599397016438L;

	/**
	 * 
	 * 
	 */
	public ServiceNodeImpl(URL serviceUrl){
		super(serviceUrl);
	}

	public String[] listServices()
	throws LangridException{
		return (String[])invoke();
	}

	public String getEndpointUrl(String serviceName)
	throws LangridException{
		return (String)invoke(serviceName);
	}

	public boolean hasService(String serviceName)
	throws LangridException{
		return (Boolean)invoke(serviceName);
	}
	
	@Override
	protected Stub createStub(URL url) throws ServiceException {
		ServiceNodeServiceLocator locator = new ServiceNodeServiceLocator();
		setUpService(locator);
		return (Stub)locator.getServiceNode(url);
	}
}
