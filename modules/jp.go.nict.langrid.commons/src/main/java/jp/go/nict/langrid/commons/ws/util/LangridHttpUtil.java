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
package jp.go.nict.langrid.commons.ws.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class LangridHttpUtil {
	public static void setFederatedCallHeaders(HttpURLConnection con
			, String sourceGridId, String callerUserGridId, String callerUserId){
		con.setRequestProperty(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_SOURCEGRIDID
				, sourceGridId
				);
		con.setRequestProperty(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_CALLERUSER
				, callerUserGridId + ":" + callerUserId
				);
	}

	public static void write403_ServiceNotActive(
			HttpServletResponse response, String gridId, String serviceId)
	throws IOException{
		write403(response, String.format(
				"ServiceNotActive {\"gridId\":\"%s\",\"serviceId\",\"%s\"}"
				, gridId, serviceId
				));
	}

	public static void write403_AccessLimitExceeded(
			HttpServletResponse response, String gridId, String serviceId)
	throws IOException{
		write403(response, String.format(
				"AccessLimitExceeded {\"gridId\":\"%s\",\"serviceId\",\"%s\"}"
				, gridId, serviceId
				));
	}

	public static void write403_AccessLimitExceeded(
			HttpServletResponse response, String gridId, String serviceId
			, String message)
	throws IOException{
		write403(response, String.format(
				"AccessLimitExceeded {\"gridId\":\"%s\",\"serviceId\":\"%s\",\"message\":\"%s\"}"
				, gridId, serviceId, message
				));
	}

	public static void write403_NoAccessPermission(
			HttpServletResponse response, String gridId, String serviceId)
	throws IOException{
		write403(response, String.format(
				"NoAccessPermission {\"gridId\":\"%s\",\"serviceId\",\"%s\"}"
				, gridId, serviceId
				));
	}

	public static void write403_TooManyCallNest(
			HttpServletResponse response, String gridId, String serviceId
			, int threashold, int actualNest)
	throws IOException{
		write403(response, String.format(
				"TooManyCallNest {\"gridId\":\"%s\",\"serviceId\",\"%s\"" +
				",\"threashold\":\"5d\",\"actualNest\":\"%d\"}"
				, gridId, serviceId, threashold, actualNest
				));
	}

	public static void write403_FederationNotFound(
			HttpServletResponse response, String sourceGridId, String targetGridId)
	throws IOException{
		write404(response, String.format(
				"FederationNotFound {\"sourceGridId\":\"%s\",\"targetGridId\",\"%s\"}"
				, sourceGridId, targetGridId
				));
	}

	public static void write404_ServiceNotFound(
			HttpServletResponse response, String gridId, String serviceId)
	throws IOException{
		write404(response, String.format(
				"ServiceNotFound {\"gridId\":\"%s\",\"serviceId\",\"%s\"}"
				, gridId, serviceId
				));
	}

	public static void write500_InternalServerError(
			HttpServletResponse response, String gridId, String serviceId)
	throws IOException{
		write500(response, String.format(
				"ServerError {\"gridId\":\"%s\",\"serviceId\",\"%s\"}"
				, gridId, serviceId
				));
	}

	public static void write500_Exception(
			HttpServletResponse response, String gridId, String serviceId
			, Throwable exception)
	throws IOException{
		write500(response, String.format(
				exception.getClass().getSimpleName() + " {\"gridId\":\"%s\",\"serviceId\",\"%s\"}"
				, gridId, serviceId), exception
				);
	}

	public static void write403(HttpServletResponse response, String exceptionMessage)
	throws IOException{
		response.setStatus(403);
		response.setHeader("X-ServiceGrid-Exception", exceptionMessage);
		PrintWriter w = response.getWriter();
		w.println("<html><body>");
		w.println("<h2>HTTP 403 Forbidden.</h2>");
		w.println("Service Grid Exception: " + exceptionMessage);
		w.println("</body></html>");
		w.flush();
	}

	public static void write404(HttpServletResponse response, String exceptionMessage)
	throws IOException{
		response.setStatus(404);
		response.setHeader("X-ServiceGrid-Exception", exceptionMessage);
		PrintWriter w = response.getWriter();
		w.println("<html><body>");
		w.println("<h2>HTTP 404 Not Found.</h2>");
		w.println("Service Grid Exception: " + exceptionMessage);
		w.println("</body></html>");
		w.flush();
	}

	public static void write500(HttpServletResponse response, String exceptionMessage)
	throws IOException{
		response.setStatus(500);
		response.setHeader("X-ServiceGrid-Exception", exceptionMessage);
		PrintWriter w = response.getWriter();
		w.println("<html><body>");
		w.println("<h2>HTTP 500 Internal Server error.</h2>");
		w.println("Service Grid Exception: " + exceptionMessage);
		w.println("</body></html>");
		w.flush();
	}

	public static void write500(HttpServletResponse response, String exceptionMessage
			, Throwable exception)
	throws IOException{
		response.setStatus(500);
		response.setHeader("X-ServiceGrid-Exception", exceptionMessage);
		PrintWriter w = response.getWriter();
		w.println("<html><body>");
		w.println("<h2>HTTP 500 Internal Server error.</h2>");
		w.println("Service Grid Exception: " + exceptionMessage);
		w.println("<br/><pre>");
		w.print(ExceptionUtil.getMessageWithStackTrace(exception, 10, 20));
		w.println("</pre>");
		w.println("</body></html>");
		w.flush();
	}
}
