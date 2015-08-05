/*
 * $Id: LangridConstants.java 1520 2015-03-10 10:22:12Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.ws;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1520 $
 */
public class LangridConstants {
	/**
	 * 
	 * 
	 */
	public static final String INVOCATION_URL_PATH_BASE
		= "invoker/";

	/**
	 * 
	 * 
	 */
	public static final String ACTIVEBPEL_SERVCIE_BASE
		= "active-bpel/services/";

	/**
	 * 
	 * 
	 */
	public static final String ACTOR_SERVICE_DEFAULTBINDING
		= "http://langrid.nict.go.jp/process/binding/default";

	/**
	 * 
	 * 
	 */
	public static final String ACTOR_SERVICE_OVERRIDEBINDING
		= "http://langrid.nict.go.jp/process/binding/override";

	/**
	 * 
	 * 
	 */
	public static final String ACTOR_SERVICE_TREEBINDING
		= "http://langrid.nict.go.jp/process/binding/tree";

	/**
	 * 
	 * 
	 */
	public static final String ACTOR_SERVICE_CALLTREE
		= "http://langrid.nict.go.jp/process/calltree";

	public static final String ACTOR_SERVICE_INVOCATIONLOG
		= "http://langrid.org/process/invocationlog";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_SERVICENAME
		= "X-LanguageGrid-ServiceName";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_SERVICECOPYRIGHT
		= "X-LanguageGrid-ServiceCopyright";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_SERVICELICENSE
		= "X-LanguageGrid-ServiceLicense";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_FROMADDRESS
		= "X-LanguageGrid-FromAddress";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_CORENODEURL
		= "X-LanguageGrid-CoreNodeUrl";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_CALLNEST
		= "X-LanguageGrid-CallNest";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_TYPEOFUSE
		= "X-Langrid-TypeOfUse";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_TYPEOFAPPPROVISION
		= "X-Langrid-TypeOfAppProvision";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_FEDERATEDCALL_SOURCEGRIDID
		= "X-Langrid-FederatedCall-SourceGridId";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_FEDERATEDCALL_CALLERUSER
		= "X-Langrid-FederatedCall-CallerUser";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_FEDERATEDCALL_FEDERATIONRESPONSE
		= "X-Langrid-FederatedCall-FederationResponse";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_PROTOCOL
		= "X-Langrid-Protocol";

	/**
	 * 
	 * 
	 */
	public static final String HTTPHEADER_STREAMING
		= "X-Langrid-Streaming";

	public static final String HTTPHEADER_TRANSFER_TO_ENDPOINT
		= "X-ServiceGrid-TransferToEndpoint-";

	public static final String HTTPHEADER_SERVICECONTAINER_ENABLE_SERVICE_LOG
		= "X-ServiceGrid-ServiceContainer-GatherInvocationLog";

	// for backward compatibility
	public static final String HTTPHEADER_TRANSFER_TO_ENDPOINT_OBSOLETE
		= "X-Langrid-Service-";
}
