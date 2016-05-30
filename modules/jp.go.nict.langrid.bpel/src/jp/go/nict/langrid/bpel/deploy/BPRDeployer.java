/*
 * $Id: BPRDeployer.java 367 2011-08-19 09:07:07Z t-nakaguchi $
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.bpel.entity.ProcessInfo;
import jp.go.nict.langrid.commons.jxpath.JXPathUtil;
import localhost.active_bpel.services.DeployBPRService.AeDeployBPRSkeleton;
import localhost.active_bpel.services.DeployBPRService.AeDeployBPRSkeletonServiceLocator;

import org.activebpel.rt.bpel.AeBusinessProcessException;
import org.apache.axis.AxisProperties;
import org.apache.axis.client.Stub;
import org.apache.axis.components.net.SecureSocketFactory;
import org.apache.axis.components.net.SocketFactoryFactory;
import org.apache.axis.encoding.Base64;
import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 367 $
 */
public class BPRDeployer {
	/**
	 * 
	 * 
	 */
	public static void deploy(
			String serviceId, ProcessInfo processInfo
			, URL serviceUrl
			, String userName, String password)
		throws SAXException
		, ClassNotFoundException, DeploymentException, IOException
		, RemoteException, ServiceException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BPRMaker.make(processInfo, out);

		String filename = serviceId + ".bpr";
		byte[] data = out.toByteArray();

		DeploymentResult result = parseResult(
			doDeploy(filename, data, serviceUrl, userName, password)
			);
		if(!result.isDeployed()){
			throw new DeploymentException(result);
		}
	}

	/**
	 * 
	 * 
	 */
	static String doDeploy(
			String fileName, byte[] data, URL serviceUrl
			)
		throws AeBusinessProcessException,
		RemoteException
		, ServiceException
	{
		return doDeploy(fileName, data, serviceUrl, null, null);
	}

	/**
	 * 
	 * 
	 */
	static String doDeploy(
			String fileName, byte[] data
			, URL serviceUrl, String userName, String password
			)
		throws AeBusinessProcessException,
		RemoteException
		, ServiceException
	{
		AeDeployBPRSkeleton port = new AeDeployBPRSkeletonServiceLocator()
				.getDeployBPRService(serviceUrl);
		if(userName != null){
			Stub stub = (Stub)port;
			stub.setUsername(userName);
			stub.setPassword(password);
		}
		try{
			return port.deployBpr(
					fileName, Base64.encode(data)
					);
		} catch(RemoteException e){
			logger.warning("failed to deploy service when deploying \""
					+ fileName + "\" to " + serviceUrl
					+ " [userId: " + userName + "]");
			throw e;
		}
	}

	/**
	 * 
	 * 
	 */
	static DeploymentResult parseResult(String aResult)
		throws SAXException, IOException
	{
		DeploymentResult result = new DeploymentResult();
		result.setSourceString(aResult);

		JXPathContext context = JXPathUtil.newXMLContext(aResult);
		context.setLenient(true);

		JXPathContext summary = JXPathContext.newContext(
			context, context.getPointer("/deploymentSummary").getNode()
			);
		result.setSummaryErrorCount(
			Integer.parseInt(summary.getValue("/@numErrors").toString())
			);
		result.setSummaryWarningCount(
			Integer.parseInt(summary.getValue("/@numWarnings").toString())
			);
		result.setSummaryMessages(
			summary.getValue("/globalMessages").toString()
			);

		summary.setLenient(true);
		Node deploymentInfoNode = (Node)summary.getPointer("/deploymentInfo").getNode();
		if(deploymentInfoNode != null){
			JXPathContext info = JXPathContext.newContext(
					summary, deploymentInfoNode 
					);
			result.setDeploymentErrorCount(Integer.parseInt(
				info.getValue("/@numErrors").toString()
				));
			result.setDeploymentWarningCount(Integer.parseInt(
				info.getValue("/@numWarnings").toString()
				));
			result.setDeployed(Boolean.parseBoolean(
				info.getValue("/@deployed").toString()
				));
			result.setPddFilename(
				info.getValue("/@pddName").toString()
				);
			result.setDeploymentLog(info.getValue("/log").toString());
		}
		return result;
	}

	private static Logger logger = Logger.getLogger(BPRDeployer.class.getName());
	static{
		SocketFactoryFactory.class.getName();
		AxisProperties.setProperty(
				SecureSocketFactory.class.getName()
				, TrustfulJSSESocketFactory.class.getName()
				);
	}
}
