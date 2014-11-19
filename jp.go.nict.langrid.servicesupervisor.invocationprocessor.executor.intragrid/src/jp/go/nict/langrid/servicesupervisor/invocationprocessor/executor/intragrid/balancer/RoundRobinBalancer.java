/*
 * $Id: RoundRobinBalancer.java 407 2011-08-25 02:21:46Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.intragrid.balancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.ServiceEndpoint;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 407 $
 */
public class RoundRobinBalancer implements Balancer{
	public ServiceEndpoint balance(String serviceId, List<ServiceEndpoint> endpoints) {
		int sz = endpoints.size();
		if(sz == 0) return null;
		Integer indexValue = indices.get(serviceId);
		int index = 0;
		if(indexValue != null){
			index = indexValue;
		}
		index %= sz;
		indices.put(serviceId, index + 1);
		return endpoints.get(index);
	}

	@Override
	public void succeeded(ServiceEndpoint endpoint, long delta, int responceCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(ServiceEndpoint endpoint, long delta, int responceCode,
			Exception exception) {
		// TODO Auto-generated method stub
		
	}

	private Map<String, Integer> indices = new HashMap<String, Integer>();
}
