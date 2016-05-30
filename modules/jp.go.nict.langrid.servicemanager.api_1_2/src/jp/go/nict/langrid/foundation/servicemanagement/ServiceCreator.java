/*
 * $Id: ServiceCreator.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation.servicemanagement;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.go.nict.langrid.commons.beanutils.Converter;
import jp.go.nict.langrid.commons.jxpath.WSDLUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.parameter.StringMapParameterContext;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.Protocols;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.util.ExternalServiceInstanceReader;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.AttributeName;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceInstance;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceProfile;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.typed.DefaultPermission;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.typed.ExternalServiceAddTimeOnlyAttribute;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.typed.ServiceAddTimeOnlyAttribute;
import jp.go.nict.langrid.service_1_2.typed.InstanceType;

import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class ServiceCreator {
	/**
	 * 
	 * 
	 */
	public static Pair<Service, Boolean> createService(
			ServiceDao dao, NodeDao ndao
			, String gridId, String serviceId, InstanceType type
			, ServiceProfile profile, ServiceInstance instance
			, Map<String, String> attributes, String userId
			, Converter converter, ServiceContext context
			)
		throws InvalidServiceInstanceException, IOException
		, MalformedURLException, SAXException, URISyntaxException
	{
		// サービスの作成
		Service s = null;
		switch(type){
			case EXTERNAL:
				s = setUpInstanceAndAttributes(
						ndao, new ExternalService(gridId, serviceId)
						, instance.getInstance(), attributes);
				break;
			case BPEL:
				s = setUpInstanceAndAttributes(
						dao, new BPELService(gridId, serviceId)
						, instance, attributes);
				break;
			default:
				s = new ExternalService();
				break;
		}

		// プロパティのコピー
		converter.copyProperties(s, profile);
		converter.copyProperties(s, instance);
		Set<String> ignoreNames = new HashSet<String>();
		for(AttributeName e : ServiceAddTimeOnlyAttribute.values()){
			ignoreNames.add(e.getAttributeName());
		}
		for(AttributeName e : ExternalServiceAddTimeOnlyAttribute.values()){
			ignoreNames.add(e.getAttributeName());
		}
		for(Map.Entry<String, String> e : attributes.entrySet()){
			if(ignoreNames.contains(e.getKey())) continue;
			s.setAttributeValue(e.getKey(), e.getValue());
		}

		ParameterContext attr = new StringMapParameterContext(attributes);

		// 
		// Gets default access privileges from profile
		// 
		DefaultPermission dp = attr.getEnum(
					ServiceAddTimeOnlyAttribute.defaultPermission.name()
					, DefaultPermission.ALLOW_ALL, DefaultPermission.class
					);
		boolean defaultPermitted = dp.equals(DefaultPermission.ALLOW_ALL);

		// 
		// Gets the default visibility state from attributes
		// 
		s.setVisible(attr.getBoolean(
				ServiceAddTimeOnlyAttribute.defaultVisible.name()
				, true
				));

		// 
		// Gets the default active state from attributes
		// 
		s.setActive(attr.getBoolean(ServiceAddTimeOnlyAttribute.defaultActive.name(), false));

		return Pair.create(s, defaultPermitted);
	}

	private static ExternalService setUpInstanceAndAttributes(
			NodeDao ndao, ExternalService service, byte[] instance
			, Map<String, String> attributes)
		throws IOException, MalformedURLException
		, SAXException, URISyntaxException
	{
		ServiceEndpoint ep = getEndpoint(
				service.getGridId(), service.getServiceId()
				, instance, attributes);
		if(ep != null){
			service.getServiceEndpoints().add(ep);
		}
		return service;
	}

	private static BPELService setUpInstanceAndAttributes(
			ServiceDao dao, BPELService service, ServiceInstance instance
			, Map<String, String> attributes)
		throws IOException, InvalidServiceInstanceException
		, SAXException, URISyntaxException
	{
		return service;
	}

	private static ServiceEndpoint getEndpoint(
			String gridId, String serviceId, byte[] instance, Map<String, String> attributes)
	throws IOException, MalformedURLException{
		String endpointUrl = attributes.get(
				ExternalServiceAddTimeOnlyAttribute.actualEndpointUrl.getAttributeName());
		String authName = attributes.get(
				ExternalServiceAddTimeOnlyAttribute.actualEndpointAuthUserName.getAttributeName()
				);
		String authPass = attributes.get(
				ExternalServiceAddTimeOnlyAttribute.actualEndpointAuthPassword.getAttributeName()
				);
		URL serviceAddress = null;
		if(endpointUrl != null){
			serviceAddress = new URL(endpointUrl);
		} else if(instance != null && authName != null && authName.trim().length() > 0){
			try{
				serviceAddress = WSDLUtil.getServiceAddress(
						new ExternalServiceInstanceReader(new ByteArrayInputStream(instance))
						.getWsdl());
			} catch(MalformedURLException e){
				return null;
			} catch(IOException e){
				return null;
			} catch(SAXException e){
				return null;
			}
		} else{
			return null;
		}

		if(authPass != null){
			return new ServiceEndpoint(gridId, serviceId, Protocols.DEFAULT
					, serviceAddress, authName, authPass, true);
		} else{
			return new ServiceEndpoint(gridId, serviceId, Protocols.DEFAULT
					, serviceAddress, authName, "", true);
		}
	}
}
