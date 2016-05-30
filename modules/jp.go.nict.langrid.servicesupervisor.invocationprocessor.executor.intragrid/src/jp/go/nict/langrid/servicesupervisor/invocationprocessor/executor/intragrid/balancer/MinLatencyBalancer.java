/*
 * $Id: MinLatencyBalancer.java 407 2011-08-25 02:21:46Z t-nakaguchi $
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

import java.util.List;

import jp.go.nict.langrid.dao.entity.ServiceEndpoint;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 407 $
 */
public class MinLatencyBalancer implements Balancer{
/*
	public ServiceEndpoint balance(String serviceId, List<ServiceEndpoint> endpoints) {
		int selected = -1;
		List<Pair<Long, Double>> epStats = new ArrayList<Pair<Long,Double>>();
		int n = endpoints.size();
		if(n == 0) return null;
		if(n == 1) return endpoints.get(0);
		for(int i = 0; i < n; i++){
			ServiceEndpoint ep = endpoints.get(i);
			ServiceEndpointPK pk = new ServiceEndpointPK(
					ep.getGridId(), ep.getServiceId(), ep.getProtocolId(), ep.getUrl().toString());
			Pair<Long, Double> stat = stats.get(pk);
			if(stat != null){
				epStats.add(stat);
			} else{
				epStats.add(Pair.create(0L, 0.0));
			}
			if(selected == -1 || stat == null
					|| stat.getSecond() < epStats.get(selected).getSecond()){
				selected = i;
			}
		}
		long count = epStats.get(selected).getFirst();
		if(count > 0){
			double countSq = Math.sqrt(count);
			for(int i = 0; i < n; i++){
				if(i == selected) continue;
				if(epStats.get(i).getFirst() < countSq){
					selected = i;
				}
			}
		}
		return endpoints.get(selected);
	}
/*/
	public ServiceEndpoint balance(String serviceId, List<ServiceEndpoint> endpoints) {
		long minMillis = Long.MAX_VALUE;
		ServiceEndpoint minMillisEP = null;
		int minMillisIndex = -1;
		int index = -1;
		for(ServiceEndpoint e : endpoints){
			index++;
			
			if(e.getAveResponseMillis() < minMillis){
				minMillis = e.getAveResponseMillis();
				minMillisEP = e;
				minMillisIndex = index;
			}
		}
		if(minMillisEP == null){
			return minMillisEP;
		}
		if(minMillisEP.getExperience() == 0){
			return minMillisEP;
		}
		int n = endpoints.size();
		double exp = Math.sqrt(minMillisEP.getExperience());
		for(int i = 0; i < n; i++){
			if(i == minMillisIndex) continue;
			ServiceEndpoint e = endpoints.get(i);
			if(e.getExperience() < exp){
				return e;
			}
		}
		return minMillisEP;
	}
//*/
	@Override
	public void succeeded(ServiceEndpoint endpoint, long delta, int responceCode) {
		//ServiceEndpointPK key = new ServiceEndpointPK()
		// TODO Auto-generated method stub
		
	}

	@Override
	public void failed(ServiceEndpoint endpoint, long delta, int responceCode,
			Exception exception) {
		// TODO Auto-generated method stub
		
	}

//	private static Map<ServiceEndpointPK, Pair<Long, Double>> stats
//		= new ConcurrentHashMap<ServiceEndpointPK, Pair<Long,Double>>();
}
