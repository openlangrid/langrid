/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor;

import java.net.URL;

import jp.go.nict.langrid.commons.parameter.annotation.Parameter;
import jp.go.nict.langrid.commons.parameter.annotation.ParameterConfig;
import jp.go.nict.langrid.commons.ws.Protocols;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
@ParameterConfig(prefix="langrid.")
public class ExecutorParams {
	/**
	 * 
	 * 
	 */
	@Parameter
	public String activeBpelServicesUrl;

	/**
	 * 
	 * 
	 */
	@Parameter
	public String activeBpelAppAuthKey;

	@Parameter
	public String javaEngineAppAuthKey;

	/**
	 * 
	 * 
	 */
	@Parameter(defaultValue="16")
	public int maxCallNest;

	@Parameter(defaultValue="false")
	public boolean webappTimeoutEnabled;

	/**
	 * 
	 * 
	 */
	@Parameter(defaultValue="10000")
	public int atomicServiceConnectionTimeout;

	/**
	 * 
	 * 
	 */
	@Parameter(defaultValue="10000")
	public int atomicServiceReadTimeout;

	/**
	 * 
	 * 
	 */
	@Parameter(defaultValue="10000")
	public int compositeServiceConnectionTimeout;

	/**
	 * 
	 * 
	 */
	@Parameter(defaultValue="10000")
	public int compositeServiceReadTimeout;

	/**
	 * 
	 * 
	 */
	@Parameter
	public URL compositeServiceTransferURL;

	/**
	 * 
	 * 
	 */
	@Parameter(defaultValue="10000")
	public int interGridCallConnectionTimeout;

	/**
	 * 
	 * 
	 */
	@Parameter(defaultValue="30000")
	public int interGridCallReadTimeout;

	/**
	 * 
	 * 
	 */
	@Parameter(defaultValue=Protocols.DEFAULT)
	public String defaultProtocol;
}
