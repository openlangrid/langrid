/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.composite.commons.test;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.servicecontainer.service.ComponentServiceFactory;

public class ComponentServiceFactoryImpl
implements ComponentServiceFactory{
	public ComponentServiceFactoryImpl(){
	}

	@SafeVarargs
	public ComponentServiceFactoryImpl(Pair<String, Object>... services){
		for(Pair<String, Object> s : services){
			this.services.put(s.getFirst(), s.getSecond());
		}
	}
	
	@Override
	public <T> T getService(String invocationName, Class<T> interfaceClass) {
		return interfaceClass.cast(services.get(invocationName));
	}

	public ComponentServiceFactoryImpl add(String invocationName, Object service){
		services.put(invocationName, service);
		return this;
	}

	protected Map<String, Object> services = new HashMap<String, Object>();
}
