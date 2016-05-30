/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicecontainer.executor.umbrella;

import java.net.URI;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.cosee.AbstractEndpointRewriter;
import jp.go.nict.langrid.cosee.Endpoint;
import jp.go.nict.langrid.cosee.EndpointRewriter;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.DaoException;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.EndpointAddressProtocolDao;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.ServiceProtocolDao;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.entity.EndpointAddressProtocol;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.entity.Protocols;
import jp.go.nict.langrid.servicecontainer.executor.umbrella.dao.entity.ServiceProtocol;

public class ProtocolResolveEndpointRewriter
extends AbstractEndpointRewriter
implements EndpointRewriter {
	public ProtocolResolveEndpointRewriter(ServiceProtocolDao spDao, EndpointAddressProtocolDao eapDao){
		this.spDao = spDao;
		this.eapDao = eapDao;
	}

	public Endpoint rewrite(
			Endpoint original, Map<String, Object> properties
			, URI processNamespace, String partnerLinkName, URI serviceNamespace
			)
	{
		if(original.getServiceId() != null){
			Endpoint ret = new Endpoint(original);
			ret.setProtocol(getProtocolForServiceId(original.getServiceId()));
			return ret;
		} else if(original.getAddress() != null){
			Endpoint ret = new Endpoint(original);
			ret.setProtocol(getProtocolForEndpointAddress(original.getAddress()));
			return ret;
		} else{
			return original;
		}
	}

	private String getProtocolForServiceId(String serviceId){
		try{
			String[] ids = serviceId.split(":");
			String gid = null, sid = null;
			if(ids.length == 2){
				gid = ids[0];
				sid = ids[1];
			} else{
				gid = "*";
				sid = serviceId;
			}
			int qi = serviceId.indexOf('?');
			if(qi != -1){
				serviceId = serviceId.substring(0, qi);
			}

			ServiceProtocol p = spDao.getServiceProtocol(gid, sid);
			if(p == null){
				return Protocols.DEFULAT;
			} else{
				return p.getProtocol();
			}
		} catch(DaoException e){
			logger.log(Level.WARNING, "failed to access DAO.", e);
			return Protocols.DEFULAT;
		}
	}

	private String getProtocolForEndpointAddress(URI address){
		try{
			EndpointAddressProtocol p = eapDao.getEndpointAddressProtocol(address.toString());
			if(p == null){
				return Protocols.DEFULAT;
			} else{
				return p.getProtocol();
			}
		} catch(DaoException e){
			logger.log(Level.WARNING, "failed to access DAO.", e);
			return Protocols.DEFULAT;
		}
	}

	private ServiceProtocolDao spDao;
	private EndpointAddressProtocolDao eapDao;
	private static Logger logger = Logger.getLogger(ProtocolResolveEndpointRewriter.class.getName());
}
