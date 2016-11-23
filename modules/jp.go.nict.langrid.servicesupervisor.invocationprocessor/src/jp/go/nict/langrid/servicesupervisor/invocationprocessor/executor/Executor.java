/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;

/**
 * Define the interface of Service Executor.
 * @author Takao Nakaguchi
 */
public interface Executor {
	/**
	 * Execute the service.
	 * @param servletContext Servlet context
	 * @param request HTTP Request Object
	 * @param response HTTP Response Object
	 * @param serviceContext Service Context
	 * @param daoContext DAO Context
	 * @param serviceGridId Grid ID
	 * @param serviceId Service ID
	 * @param headers HTTP headers to send
	 * @param query Query part of URL
	 * @param protocol Invocation protocol
	 * @param input Input bytes
	 * @throws DaoException
	 * @throws TooManyCallNestException 
	 * @throws NoValidEndpointsException
	 * @throws ProcessFailedException
	 * @throws IOException
	 */
	void execute(
			ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			ServiceContext serviceContext, DaoContext daoContext,
			String sourceGridId, String sourceUserId,
			String targetGridId, String targetServiceId,
			Map<String, String> headers,
			String query, String protocol, byte[] input
			)
	throws DaoException, TooManyCallNestException, NoValidEndpointsException, ProcessFailedException
	, IOException;
}
