/*
 * $Id: BasePage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.ServiceManagerApplication;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;
import jp.go.nict.langrid.management.web.view.utility.RequestResponseUtil;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.protocol.http.request.WebClientInfo;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class BasePage extends WebPage {
	/**
	 * 
	 * 
	 */
	public BasePage() {
		try {
			add(new Label("pageTitleLabel", MessageUtil.getServiceGridName())
				.setRenderBodyOnly(true));
			initialize();
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		} catch(WicketRuntimeException e) {
			doErrorProcess(new ServiceManagerException(e));
		}
	}

	/**
	 * 
	 * 
	 */
	public void doErrorProcess(ServiceManagerException e) {
		doErrorProcess(e, e.getMessage());
	}

	/**
	 * 
	 * 
	 */
	public void doErrorProcess(ServiceManagerException e, String message) {
		e.setMaskMessage(false);
		LogWriter.writeError(getSessionUserId(), e, getPageClass(), message);
		throw new RestartResponseException(RequestResponseUtil.getPageForErrorRequest(e));
	}

	/**
	 * 
	 * 
	 */
	public void doErrorProcessForPopup(ServiceManagerException e) {
		e.setMaskMessage(false);
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		LogWriter.writeError(session.getUserId(), e, getPageClass(), e.getMessage());
		throw new RestartResponseException(
			RequestResponseUtil.getPageForErrorPopupRequest(e));
	}

	protected final String getSelfGridId() {
		if(selfGridId == null) {
			try {
				selfGridId = ServiceFactory.getInstance().getGridService().getSelfGridId();
			} catch(ServiceManagerException e) {
				throw new RestartResponseException(RequestResponseUtil.getPageForErrorPopupRequest(e));
			}
		}
		return selfGridId;
	}

	/**
	 * 
	 * 
	 */
	protected final HttpServletRequest getHttpRequest() {
		return ((WebRequest)RequestCycle.get().getRequest()).getHttpServletRequest();
	}

	/**
	 * 
	 * 
	 */
	protected final HttpServletResponse getHttpResponse() {
		return ((WebResponse)RequestCycle.get().getResponse()).getHttpServletResponse();
	}

	/**
	 * 
	 * 
	 */
	protected final ServletContext getServletContext() {
		return ServiceManagerApplication.get().getServletContext();
	}

	/**
	 * 
	 * 
	 */
	protected final String getSessionUserId() {
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		return session.getUserId();
	}

	protected final String getSessionUserGridId() {
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		return session.getUserGridId();
	}

	/**
	 * 
	 * 
	 */
	protected final String getSessionUserPassword() {
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		return session.getPassword();
	}

	protected final boolean isLogin() {
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		return session.isLogin();
	}

	/**
	 * 
	 * 
	 */
	protected void initialize() throws ServiceManagerException {
		// noop
	}

	@Override
	protected void setHeaders(WebResponse response) {
		WebClientInfo ci = (WebClientInfo)getWebRequestCycle().getClientInfo();
		if(ci.getProperties().isBrowserMozillaFirefox()
			|| ci.getProperties().isBrowserMozilla()
			|| ci.getProperties().isBrowserSafari()) {
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control",
				"no-cache, max-age=0, must-revalidate, no-store");
			return;
		}
		//        String userAgent = getWebRequestCycle().getWebRequest().getHttpServletRequest().getHeader("User-Agent");
		//        if (userAgent != null) {
		//            String lowerUserAgent = userAgent.toLowerCase();
		//            if (lowerUserAgent.contains("firefox") || lowerUserAgent.contains("safari")) {
		//                response.setHeader("Pragma", "no-cache");
		//                response.setHeader("Cache-Control", "no-cache, max-age=0, must-revalidate, no-store");
		//                return;
		//            }
		//        }
		super.setHeaders(response);
	}

	private String selfGridId;
}
