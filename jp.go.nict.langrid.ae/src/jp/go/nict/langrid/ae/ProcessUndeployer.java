/*
 * $Id: ProcessUndeployer.java 186 2010-10-02 10:57:04Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
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
package jp.go.nict.langrid.ae;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 186 $
 */
public class ProcessUndeployer {
	/**
	 * 
	 * 
	 */
	public boolean undeployBpr(String processName){
		if(!getRequest().getRemoteAddr().equals("127.0.0.1")) return false;
		File tomcat = getTomcatPath();
		File bpr = new File(tomcat, "bpr");
		File bprFile = new File(bpr, processName + ".bpr");
		if(!bprFile.exists()) return false;
		return bprFile.delete();
	}

	private static File getTomcatPath(){
		HttpServlet servlet = (HttpServlet) getMessageProperty(
				HTTPConstants.MC_HTTP_SERVLET
				);
		ServletContext context = servlet.getServletContext();
		return new File(
			context.getRealPath(".")
			).getParentFile().getParentFile().getParentFile();
	}

	private static HttpServletRequest getRequest(){
		return (HttpServletRequest) getMessageProperty(
			HTTPConstants.MC_HTTP_SERVLETREQUEST
			);
	}

	private static Object getMessageProperty(String aKey){
		return MessageContext.getCurrentContext().getProperty(aKey);
	}
}
