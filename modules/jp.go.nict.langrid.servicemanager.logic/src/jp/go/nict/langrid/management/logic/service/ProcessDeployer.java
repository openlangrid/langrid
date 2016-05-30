/*
 * $Id:ProcessDeployer.java 5259 2007-09-06 10:10:27Z nakaguchi $
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
package jp.go.nict.langrid.management.logic.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import jp.go.nict.langrid.bpel.ProcessAnalysisException;
import jp.go.nict.langrid.bpel.ProcessAnalyzer;
import jp.go.nict.langrid.bpel.deploy.BPRDeployer;
import jp.go.nict.langrid.bpel.deploy.BPRUndeployer;
import jp.go.nict.langrid.bpel.deploy.DeploymentException;
import jp.go.nict.langrid.bpel.entity.MyRoleBinding;
import jp.go.nict.langrid.bpel.entity.PartnerLink;
import jp.go.nict.langrid.bpel.entity.ProcessInfo;
import jp.go.nict.langrid.bpel.entity.WSDL;
import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.util.BPELServiceInstanceReader;
import jp.go.nict.langrid.dao.util.LobUtil;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author  $Author:nakaguchi $
 * @version  $Revision:5259 $
 */
public class ProcessDeployer {
	/**
	 * 
	 * 
	 */
	public ProcessDeployer(String activeBpelServicesUrl
			, String username, String password){
		this.activeBpelServicesUrl = activeBpelServicesUrl;
		this.username = username;
		this.password = password;
	}

	/**
	 * 
	 * 
	 */
	public void deploy(
		BPELService service, BPELServiceInstanceReader instance
		, String binding, Map<URI, String> handlers)
		throws ClassNotFoundException, DeploymentException
		, IOException, MalformedURLException
		, ProcessAnalysisException
		, SAXException, ServiceException, URISyntaxException
		, SQLException
	{
		// 
		// 
		URL serviceUrl = new URL(activeBpelServicesUrl + "/DeployBPRService");
		ProcessInfo pi = ProcessAnalyzer.analyze(instance);
		pi.setBinding(MyRoleBinding.RPC);
		try{
			if(binding != null){
				pi.setBinding(MyRoleBinding.valueOf(binding));
			}
		} catch(IllegalArgumentException e){
		}
		for(Map.Entry<URI, String> e : handlers.entrySet()){
			PartnerLink pl = pi.getPartnerLinks().get(e.getKey().toString());
			if(pl == null) continue;
			pl.setPartnerInvokeHandlerClass(e.getValue());
		}
		BPRDeployer.deploy(
				service.getServiceId(), pi, serviceUrl
				, username, password
				);
		// 
		// 
		String deployedId = service.getServiceId();
		do{
			URI ns = null;
			try{
				for(PartnerLink pl : pi.getBpel().getPartnerLinks()){
					if((pl.getMyRole() != null) && (pl.getMyRole().length() > 0)){
						ns = new URI(pl.getPartnerLinkType().getNamespaceURI());
						break;
					}
				}
			} catch(URISyntaxException e){
				logger.log(Level.WARNING, "invalid uri", e);
				break;
			}
			if(ns == null) break;
			WSDL wsdl = pi.getWsdls().get(ns);
			if(wsdl == null) break;
			Set<String> names = wsdl.getServices().keySet();
			if(names.size() == 0) break;
			deployedId = names.iterator().next();
		} while(false);
		// 
		// 
		service.setWsdl(LobUtil.createBlob(openStream(
				new URL(activeBpelServicesUrl + "/" + deployedId + "?wsdl")
				, username, password
				)));
		service.setDeployedId(deployedId);
		service.setDeployed(true);
	}

	/**
	 * 
	 * 
	 */
	public void undeploy(BPELService service)
		throws IOException, ServiceException
	{
		// 
		// 
		String serviceId = service.getDeployedId();

		URL serviceUrl = new URL(activeBpelServicesUrl + "/UndeployBPRService");
		BPRUndeployer.undeploy(
				serviceId, serviceUrl, username, password
				);

		// 
		// 
		service.setDeployed(false);
	}

	static InputStream openStream(URL url)
	throws IOException{
		return openStream(url, null, null);
	}

	static InputStream openStream(URL url, String userName, String password)
		throws IOException
	{
		HttpClient client = HttpClientUtil.createHttpClientWithHostConfig(url);
		SimpleHttpConnectionManager manager = new SimpleHttpConnectionManager(true);
		client.setHttpConnectionManager(manager);
		GetMethod m = new GetMethod(url.getFile());
		if((userName != null) && (userName.length() > 0)){
			client.getState().setCredentials(
					AuthScope.ANY
					, new UsernamePasswordCredentials(userName, password)
					);
			client.getParams().setAuthenticationPreemptive(true);
			m.setDoAuthentication(true);
		}
		try{
			client.executeMethod(m);
			if(m.getStatusCode() != 200){
				throw new IOException("failed to get vaild http response: " + m.getStatusCode());
			}
			return new ByteArrayInputStream(StreamUtil.readAsBytes(
					m.getResponseBodyAsStream()));
		} finally{
			manager.shutdown();
		}
	}

	private String activeBpelServicesUrl;
	private String username;
	private String password;

	private static Logger logger = Logger.getLogger(
			ProcessDeployer.class.getName());
}
