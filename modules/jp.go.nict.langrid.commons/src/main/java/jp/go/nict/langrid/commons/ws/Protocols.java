/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1183 $
 */
public class Protocols {
	/**
	 * 
	 * 
	 */
	public static final String SOAP_RPCENCODED = "SOAP_RPC_ENCODED";

	/**
	 * 
	 * 
	 */
	public static final String SOAP_DOCLIT_WRAPPED = "SOAP_DOCUMENT_LITERAL_WRAPPED";
	
	/**
	 * 
	 * 
	 */
	public static final String PROTOBUF_RPC = "PROTOCOLBUFFERS_RPC";

	/**
	 * 
	 * 
	 */
	public static final String JSON_RPC = "JSON_RPC";

	/**
	 * 
	 * 
	 */
	public static final String JAVA_CALL = "JAVA_CALL";

	/**
	 * 
	 * 
	 */
	public static final String DEFAULT = SOAP_RPCENCODED;
}
