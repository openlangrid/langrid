/*
 * $Id: ExceptionServlet.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.service_1_2.AccessLimitExceededException;
import jp.go.nict.langrid.service_1_2.NoAccessPermissionException;
import jp.go.nict.langrid.service_1_2.UnknownException;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class ExceptionServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		doProcess(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		doProcess(request, response);
	}

	private void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		if(!request.getLocalAddr().equals("127.0.0.1")){
			throw new ServletException();
		}
		String hostName = request.getLocalName();
		String path = request.getPathInfo();
		String exception = null;
		if(path != null){
			exception = path.substring(1);
		}
		boolean processed = false;
		do{
			if(exception.equals("NoAccessPermissionException")){
				String userId = request.getParameter("userId");
				if(userId == null) break;
				ResponseProcessor.processFaultTemplate(
						response, new NoAccessPermissionException(userId)
						, hostName
						, new HashMap<String, List<String>>()
						, response.getOutputStream()
						);
				processed = true;
			} else if(exception.equals("UnknownException")){
					String message = request.getParameter("message");
					if(message == null) break;
					ResponseProcessor.processFaultTemplate(
							response, new UnknownException(message)
							, hostName
							, new HashMap<String, List<String>>()
							, response.getOutputStream()
							);
					processed = true;
			} else if(exception.equals("AccessConstraintViolationException")){
				ResponseProcessor.processFaultTemplate(
						response, new AccessLimitExceededException(
								"You violate access constraints.")
						, hostName
						, new HashMap<String, List<String>>()
						, response.getOutputStream()
						);
			}
		} while(false);
		if(!processed){
			ResponseProcessor.processFaultTemplate(
					response, new UnknownException("failed to process")
					, hostName
					, new HashMap<String, List<String>>()
					, response.getOutputStream()
					);
		}
	}

	private static final long serialVersionUID = -1633156651627668105L;
}
