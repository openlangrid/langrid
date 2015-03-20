/*
 * $Id: ServiceLoader_1_3.java 1162 2014-03-19 15:23:57Z t-nakaguchi $
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
package jp.go.nict.langrid.testresource.loader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import jp.go.nict.langrid.service_1_2.foundation.Attribute;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceInstance;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.ServiceProfile;
import jp.go.nict.langrid.testresource.Services_1_2;

public class ServiceLoader_1_3
extends AbstractServiceLoader<ServiceProfile, ServiceInstance, Attribute>
{
	public ServiceProfile loadProfile(Services_1_2 resource) throws IOException{
		return loadProfile(resource, new ServiceProfile());
	}

	public ServiceInstance loadInstance(Services_1_2 resource) throws IOException{
		return loadInstance(resource, new ServiceInstance());
	}

	public Attribute[] loadAttributes(Services_1_2 resource) throws IOException{
		return loadAttributes(resource, Attribute.class);
	}

	protected Set<String> getProfileTupleProperties(){
		return profileTuples;
	}

	protected Set<String> getInstanceTupleProperties(){
		return instanceTuples;
	}

	protected Iterable<String> getProfileAndInstanceProperties(){
		return profileAndInstanceProps;
	}

	private static Set<String> profileTuples = new HashSet<String>();
	private static Set<String> instanceTuples = new HashSet<String>();
	private static Set<String> profileAndInstanceProps = new HashSet<String>();

	static{
		// リフレクションでServiceProfileとServiceInstanceのプロパティを
		// profileAndInstancePropsに設定する。
		addProperties(ServiceProfile.class, profileAndInstanceProps, profileTuples); 
		addProperties(ServiceInstance.class, profileAndInstanceProps, instanceTuples); 
	}
}
