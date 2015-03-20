/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.p2pgridbasis.data;

import java.util.Calendar;
import java.util.HashMap;

import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessLimitData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessLogData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessRightData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.AccessStateData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DomainData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.FederationData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.GridData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.NewsData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.NodeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.OverUseLimitData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ProtocolData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceMetaAttributeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceTypeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceMetaAttributeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ServiceTypeData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.TemporaryUserData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.UserData;

/**
 * 
 * 
 * @author Naoki Miyata
 * @author Masato Mori
 * @author Takao Nakaguchi
 */
public class DataFactory {
	static private HashMap<String, Instantiator> instantiators;
	static {
		instantiators = new HashMap<String, Instantiator>();
		instantiators.put(AccessLimitData.getDataType()          , new AccessLimitData.Instantiator()          );
		instantiators.put(AccessLogData.getDataType()            , new AccessLogData.Instantiator()            );
		instantiators.put(AccessRightData.getDataType()          , new AccessRightData.Instantiator()          );
		instantiators.put(AccessStateData.getDataType()          , new AccessStateData.Instantiator()          );
		instantiators.put(DomainData.getDataType()               , new DomainData.Instantiator()               );
		instantiators.put(FederationData.getDataType()           , new FederationData.Instantiator()           );
		instantiators.put(GridData.getDataType()                 , new GridData.Instantiator()                 );
		instantiators.put(NewsData.getDataType()                 , new NewsData.Instantiator()                 );
		instantiators.put(NodeData.getDataType()                 , new NodeData.Instantiator()                 );
		instantiators.put(OverUseLimitData.getDataType()         , new OverUseLimitData.Instantiator()         );
		instantiators.put(ProtocolData.getDataType()             , new ProtocolData.Instantiator()             );
		instantiators.put(ResourceData.getDataType()             , new ResourceData.Instantiator()             );
		instantiators.put(ResourceTypeData.getDataType()         , new ResourceTypeData.Instantiator()         );
		instantiators.put(ResourceMetaAttributeData.getDataType(), new ResourceMetaAttributeData.Instantiator());
		instantiators.put(ServiceData.getDataType()              , new ServiceData.Instantiator()              );
		instantiators.put(ServiceTypeData.getDataType()          , new ServiceTypeData.Instantiator()          );
		instantiators.put(ServiceMetaAttributeData.getDataType() , new ServiceMetaAttributeData.Instantiator() );
		instantiators.put(TemporaryUserData.getDataType()        , new TemporaryUserData.Instantiator()        );
		instantiators.put(UserData.getDataType()                 , new UserData.Instantiator()                 );
	}

	/**
	 * 
	 * 
	 */
	public interface Instantiator {
		String getDataType( );
		Data newInstance(String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) throws RequiredAttributeNotFoundException;
	}

	/**
	 * 
	 * 
	 */
	static public Data createData(String dataType, String gid, DataID dataID, Calendar lastUpdateDate, DataAttributes attributes) throws RequiredAttributeNotFoundException {
		Instantiator instantiator = instantiators.get(dataType);
		return instantiator.newInstance(gid, dataID, lastUpdateDate, attributes);
	}

	/**
	 * 
	 * 
	 */
	static public void registerDataInstance(String dataName, Instantiator instantiator) {
		instantiators.put(dataName, instantiator);
	}
}
