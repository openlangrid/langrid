/*
 * $Id: ClientFactory.java 370 2011-08-19 10:10:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2.management;

import java.net.URL;
import java.util.logging.Logger;

import jp.go.nict.langrid.client.axis.ProxySelectingAxisSocketFactory;
import jp.go.nict.langrid.client.axis.ProxySelectingSecureAxisSocketFactory;
import jp.go.nict.langrid.client.ws_1_2.EditableBilingualDictionaryClient;
import jp.go.nict.langrid.client.ws_1_2.ServiceClient;
import jp.go.nict.langrid.client.ws_1_2.error.ExceptionConverter;
import jp.go.nict.langrid.client.ws_1_2.error.LangridError;
import jp.go.nict.langrid.client.ws_1_2.impl.EditableBilingualDictionaryClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.AuthenticationClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.BPELServiceManagementClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.ExternalServiceManagementClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.NodeManagementClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.OverUseMonitorClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.ResourceManagementClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.ServiceAccessLimitManagementClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.ServiceAccessRightManagementClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.ServiceDeploymentManagementClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.ServiceManagementClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.ServiceMonitorClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.TemporaryUserManagementClientImpl;
import jp.go.nict.langrid.client.ws_1_2.management.impl.UserManagementClientImpl;
import jp.go.nict.langrid.commons.net.proxy.pac.PacUtil;
import jp.go.nict.langrid.ws_1_2.foundation.serviceaccesslimitmanagement.AccessLimitNotFoundException;
import jp.go.nict.langrid.ws_1_2.foundation.serviceaccessrightmanagement.AccessRightNotFoundException;
import jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.EndpointNotFoundException;
import jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceAlreadyExistsException;
import jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotActivatableException;
import jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotDeactivatableException;
import jp.go.nict.langrid.ws_1_2.foundation.servicemanagement.ServiceNotInactiveException;
import jp.go.nict.langrid.ws_1_2.foundation.usermanagement.InvalidUserIdException;
import jp.go.nict.langrid.ws_1_2.foundation.usermanagement.UserAlreadyExistsException;
import jp.go.nict.langrid.ws_1_2.foundation.usermanagement.UserNotFoundException;

import org.apache.axis.AxisProperties;
import org.apache.axis.components.net.SecureSocketFactory;
import org.apache.axis.components.net.SocketFactory;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 370 $
 */
public class ClientFactory{
	/**
	 * 
	 * 
	 */
	public static ServiceManagementClient createServiceManagementClient(URL serviceUrl){
		return setup(new ServiceManagementClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static NodeManagementClient createNodeManagementClient(URL serviceUrl){
		return setup(new NodeManagementClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ServiceDeploymentManagementClient createServiceDeploymentanagementClient(
			URL serviceUrl){
		return setup(new ServiceDeploymentManagementClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ExternalServiceManagementClient createExternalServiceManagementClient(
			URL serviceUrl){
		return setup(new ExternalServiceManagementClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static BPELServiceManagementClient createBPELServiceManagementClient(
			URL serviceUrl){
		return setup(new BPELServiceManagementClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static AuthenticationClient createAuthenticationClient(URL serviceUrl){
		return setup(new AuthenticationClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ServiceAccessRightManagementClient createServiceAccessRightManagementClient(
			URL serviceUrl){
		return setup(new ServiceAccessRightManagementClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ServiceAccessLimitManagementClient createServiceAccessLimitManagementClient(
			URL serviceUrl){
		return setup(new ServiceAccessLimitManagementClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ServiceMonitorClient createServiceMonitorClient(URL serviceUrl){
		return setup(new ServiceMonitorClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static UserManagementClient createUserManagementClient(URL serviceUrl){
		return setup(new UserManagementClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static TemporaryUserManagementClient createTemporaryUserManagementClient(
			URL serviceUrl){
		return setup(new TemporaryUserManagementClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static OverUseMonitorClient createOverUseMonitorClient(URL serviceUrl){
		return setup(new OverUseMonitorClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static ResourceManagementClient createResourceManagementClient(
			URL serviceUrl){
		return setup(new ResourceManagementClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static EditableBilingualDictionaryClient createEditableBilingualDictionaryClient(
			URL serviceUrl){
		return setup(new EditableBilingualDictionaryClientImpl(serviceUrl));
	}

	/**
	 * 
	 * 
	 */
	public static void setDefaultUserId(String userId){
		defaultUserId = userId;
	}

	/**
	 * 
	 * 
	 */
	public static void setDefaultPassword(String password){
		defaultPassword = password;
	}

	private static <T extends ServiceClient>T setup(T client){
		if(defaultUserId != null){
			client.setUserId(defaultUserId);
		}
		if(defaultPassword != null){
			client.setPassword(defaultPassword);
		}
		return client;
	}

	private static String defaultUserId;
	private static String defaultPassword;
	private static Logger logger = Logger.getLogger(ClientFactory.class.getName());
	static{
		try{
			System.setProperty("java.net.useSystemProxies", "true");
		}catch(SecurityException e){
			logger.warning("failed to set system property \"java.net.useSystemProxies\"");
		}
		AxisProperties.setProperty(SocketFactory.class.getName(),
				ProxySelectingAxisSocketFactory.class.getName());
		AxisProperties.setProperty(SecureSocketFactory.class.getName(),
				ProxySelectingSecureAxisSocketFactory.class.getName());
		PacUtil.setupDefaultProxySelector();

		ExceptionConverter.addErrorMapping(ServiceAlreadyExistsException.class, LangridError.E153);
		ExceptionConverter.addErrorMapping(ServiceNotActivatableException.class, LangridError.E154);
		ExceptionConverter.addErrorMapping(ServiceNotDeactivatableException.class, LangridError.E155);
		ExceptionConverter.addErrorMapping(ServiceNotInactiveException.class, LangridError.E156);
		ExceptionConverter.addErrorMapping(EndpointNotFoundException.class, LangridError.E157);
		ExceptionConverter.addErrorMapping(UserAlreadyExistsException.class, LangridError.E1250);
		ExceptionConverter.addErrorMapping(UserNotFoundException.class, LangridError.E1252);
		ExceptionConverter.addErrorMapping(InvalidUserIdException.class, LangridError.E1252);
		ExceptionConverter.addErrorMapping(AccessRightNotFoundException.class, LangridError.E1350);
		ExceptionConverter.addErrorMapping(AccessLimitNotFoundException.class, LangridError.E1351);
	}
}
