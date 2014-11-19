/*
 * $Id: RequestResponseUtil.java 303 2010-12-01 04:21:52Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.utility;

import java.io.File;
import java.io.IOException;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.error.ErrorInternalPage;
import jp.go.nict.langrid.management.web.view.page.error.ExpiredErrorPage;
import jp.go.nict.langrid.management.web.view.page.error.PopupErrorInternalPage;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.resource.FileResourceStream;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RequestResponseUtil{
	/**
	 * 
	 * 
	 */
	public static void setRequestForFile(RequestCycle cycle, File file, String prefix)
	throws IOException
	{
		FileResourceStream frs = new FileResourceStream(file);
		ResourceStreamRequestTarget rsrt = new ResourceStreamRequestTarget(
				frs, prefix + file.getName());
		cycle.setRequestTarget(rsrt);
	}
	
	/**
	 * 
	 * 
	 */
	public static Class<? extends Page> getPageClassForErrorRequest(){
		return ErrorInternalPage.class;
	}

	/**
	 * 
	 * 
	 */
	public static Class<? extends Page> getPageClassForErrorPopupRequest(){
		return PopupErrorInternalPage.class;
	}
	
	/**
	 * 
	 * 
	 */
	public static Page getPageForErrorRequest(ServiceManagerException e){
		return new ErrorInternalPage(e);
	}
	
	/**
	 * 
	 * 
	 */
	public static Page getPageForErrorPopupRequest(ServiceManagerException e){
		return new PopupErrorInternalPage(e);
	}

	/**
	 * 
	 * 
	 */
	public static Class<? extends Page> getPageClassForSessionTimeOut(){
		return ExpiredErrorPage.class;
	}
	
	public static String getParameter(String key, Request request){
		Object someParams = request.getRequestParameters().getParameters().get(key);
		if(someParams != null){
			String[] params = (String[])someParams;
			for(String requestedParams : params){
				return requestedParams;
			}
		}
		return null;
	}
}
