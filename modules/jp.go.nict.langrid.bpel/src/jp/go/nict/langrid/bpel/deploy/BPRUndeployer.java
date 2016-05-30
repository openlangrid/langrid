/*
 * $Id: BPRUndeployer.java 184 2010-10-02 10:49:08Z t-nakaguchi $
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
package jp.go.nict.langrid.bpel.deploy;

import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;

import localhost.active_bpel.services.UndeployBPRService.ProcessUndeployer;
import localhost.active_bpel.services.UndeployBPRService.ProcessUndeployerService;
import localhost.active_bpel.services.UndeployBPRService.ProcessUndeployerServiceLocator;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 184 $
 */
public class BPRUndeployer {
	/**
	 * 
	 * 
	 */
	public static boolean undeploy(
			String processId, URL serviceUrl, String username, String password)
		throws RemoteException, ServiceException
	{
		ProcessUndeployerService service = new ProcessUndeployerServiceLocator();
		ProcessUndeployer port = service.getUndeployBPRService(serviceUrl);
		if(username != null && username.length() > 0){
			Stub stub = (Stub)port;
			stub.setUsername(username);
			stub.setPassword(password);
		}
		return port.undeployBpr(processId);
	}
}
