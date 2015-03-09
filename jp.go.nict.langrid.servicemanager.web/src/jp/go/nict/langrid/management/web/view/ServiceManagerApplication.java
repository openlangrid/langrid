/*
 * $Id: ServiceManagerApplication.java 1506 2015-03-02 16:03:34Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import jp.go.nict.langrid.commons.parameter.ParameterRequiredException;
import jp.go.nict.langrid.commons.ws.param.ServletContextParameterContext;
import jp.go.nict.langrid.management.web.annotation.RequireLogin;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.federation.OperatersOfConnectedLanguageGridLogOutPage;
import jp.go.nict.langrid.management.web.view.page.error.ErrorInternalPage;
import jp.go.nict.langrid.management.web.view.page.language.LanguageInputFormPopupPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.LanguageResourceProfilePage;
import jp.go.nict.langrid.management.web.view.page.language.resource.LanguageResourcesLogOutPage;
import jp.go.nict.langrid.management.web.view.page.language.service.LanguageServiceLogOutPage;
import jp.go.nict.langrid.management.web.view.page.language.service.MonitoringLanguageServicePublicLogOutPage;
import jp.go.nict.langrid.management.web.view.page.language.service.MonitoringLanguageServiceStatisticPublicLogOutPage;
import jp.go.nict.langrid.management.web.view.page.language.service.ServiceProfilePage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.protocol.ProtocolsOfServiceLogOutPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.ServiceTypeListLogOutPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.ServiceTypeProfilePage;
import jp.go.nict.langrid.management.web.view.page.node.NodeListLogOutPage;
import jp.go.nict.langrid.management.web.view.page.node.NodeProfilePage;
import jp.go.nict.langrid.management.web.view.page.other.ExpiredPasswordChangePage;
import jp.go.nict.langrid.management.web.view.page.other.LoginPage;
import jp.go.nict.langrid.management.web.view.page.other.ManualLogOutPage;
import jp.go.nict.langrid.management.web.view.page.other.NewsLogOutPage;
import jp.go.nict.langrid.management.web.view.page.other.NewsPage;
import jp.go.nict.langrid.management.web.view.page.other.OverviewLogOutPage;
import jp.go.nict.langrid.management.web.view.page.other.SignupPage;
import jp.go.nict.langrid.management.web.view.page.user.LanguageGridUsersLogOutPage;
import jp.go.nict.langrid.management.web.view.page.user.UserProfilePage;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSessionImpl;
import jp.go.nict.langrid.management.web.view.utility.RequestResponseUtil;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.protocol.http.request.CryptedUrlWebRequestCodingStrategy;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.request.target.coding.MixedParamUrlCodingStrategy;
import org.apache.wicket.request.target.coding.QueryStringUrlCodingStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.crypt.KeyInSessionSunJceCryptFactory;

/**
 * サービスマネージャ用アプリケーションクラス.
 * サービスマネージャの全体的な仕様はここで決定する
 *
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public class ServiceManagerApplication extends WebApplication {
	@Override
	public Class getHomePage() {
		if(((ServiceManagerSession)Session.get()).isLogin()) {
			return NewsPage.class;
		}
		return OverviewLogOutPage.class;
	}

	@Override
	public Session newSession(Request request, Response response) {
		Session session = new ServiceManagerSessionImpl(request);
		// default location
		session.setLocale(Locale.ENGLISH);
		return session;
	}

	protected void setSpringSettings(){
		addComponentInstantiationListener(new SpringComponentInjector(this));
	}

	@Override
	protected void init() {
		setSpringSettings();
		try {
			MessageUtil.setContext(getServletContext());
		} catch(ParameterRequiredException e) {
			e.printStackTrace();
			LogWriter.writeError("Service Manager System", e,
							ServiceManagerApplication.class,
							"Service Manager can't initialized.");
		}
		getPageSettings().setAutomaticMultiWindowSupport(true);
		/** When rendered page, Comments are striped in page. **/
		getMarkupSettings().setStripComments(true);
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
		getApplicationSettings().setInternalErrorPage(
				RequestResponseUtil.getPageClassForErrorRequest());
		getApplicationSettings().setPageExpiredErrorPage(
				RequestResponseUtil.getPageClassForSessionTimeOut());
		getSecuritySettings().setCryptFactory(new KeyInSessionSunJceCryptFactory());
		getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy() {
			public boolean isActionAuthorized(Component component, Action action) {
				return true;
			}

			public boolean isInstantiationAuthorized(Class componentClass) {
				if(!ServiceManagerPage.class.isAssignableFrom(componentClass)) {
					return true;
				}
				RequireLogin ra = ((Class<?>)componentClass).getAnnotation(RequireLogin.class);
				if((ra != null && !ra.value()) ||
						NewsLogOutPage.class.isAssignableFrom(componentClass)
						|| NodeListLogOutPage.class
								.isAssignableFrom(componentClass)
						|| LanguageResourcesLogOutPage.class
								.isAssignableFrom(componentClass)
						|| LanguageServiceLogOutPage.class
								.isAssignableFrom(componentClass)
						|| LanguageGridUsersLogOutPage.class
								.isAssignableFrom(componentClass)
						|| OverviewLogOutPage.class.isAssignableFrom(componentClass)
						|| LoginPage.class.isAssignableFrom(componentClass)
						|| LanguageResourceProfilePage.class
								.isAssignableFrom(componentClass)
						|| ServiceProfilePage.class.isAssignableFrom(componentClass)
						|| LanguageInputFormPopupPage.class
								.isAssignableFrom(componentClass)
						|| NodeProfilePage.class
								.isAssignableFrom(componentClass)
						|| UserProfilePage.class.isAssignableFrom(componentClass)
						|| OperatersOfConnectedLanguageGridLogOutPage.class
								.isAssignableFrom(componentClass)
						|| ExpiredPasswordChangePage.class
								.isAssignableFrom(componentClass)
						|| ProtocolsOfServiceLogOutPage.class
								.isAssignableFrom(componentClass)
						|| ServiceTypeListLogOutPage.class
								.isAssignableFrom(componentClass)
						|| ServiceTypeProfilePage.class.isAssignableFrom(componentClass)
						|| MonitoringLanguageServicePublicLogOutPage.class
								.isAssignableFrom(componentClass)
						|| MonitoringLanguageServiceStatisticPublicLogOutPage.class
								.isAssignableFrom(componentClass)
						|| ManualLogOutPage.class.isAssignableFrom(componentClass)
						|| RequestResponseUtil.getPageClassForErrorRequest()
								.isAssignableFrom(componentClass)
						|| RequestResponseUtil.getPageClassForErrorPopupRequest()
								.isAssignableFrom(componentClass)
						)
				{
					return true;
				}
				if(!((ServiceManagerSession)Session.get()).isLoginedAccess()) {
					throw new RestartResponseAtInterceptPageException(LoginPage.class);
				}
				if(!((ServiceManagerSession)Session.get()).isLogin()) {
					throw new RestartResponseAtInterceptPageException(RequestResponseUtil
							.getPageClassForSessionTimeOut());
				}
				return true;
			}
		});
		// bookmarkable pages
		mount(new QueryStringUrlCodingStrategy("/login", LoginPage.class));
		mount(new QueryStringUrlCodingStrategy("/overview", OverviewLogOutPage.class));
		mount(new QueryStringUrlCodingStrategy("/news", NewsLogOutPage.class));
		mount(new QueryStringUrlCodingStrategy("/service-monitoring",
			MonitoringLanguageServicePublicLogOutPage.class));
		mount(new QueryStringUrlCodingStrategy("/service-type",
			ServiceTypeListLogOutPage.class));
		mount(new QueryStringUrlCodingStrategy("/users",
				LanguageGridUsersLogOutPage.class));
		mount(new QueryStringUrlCodingStrategy("/language-resources",
				LanguageResourcesLogOutPage.class));
		mount(new QueryStringUrlCodingStrategy("/language-services",
				LanguageServiceLogOutPage.class));
		mount(new QueryStringUrlCodingStrategy("/computation-resources",
				NodeListLogOutPage.class));
		mount(new MixedParamUrlCodingStrategy("/language-services/profile",
				ServiceProfilePage.class, new String[]{"gridId", "id"}));
		mount(new QueryStringUrlCodingStrategy("/users/profile", UserProfilePage.class));
		mount(new MixedParamUrlCodingStrategy("/language-resources/profile",
				LanguageResourceProfilePage.class, new String[]{"gridId", "id"}));
		mount(new MixedParamUrlCodingStrategy("/computation-resources/profile",
				NodeProfilePage.class, new String[]{"gridId", "id"}));
		mount(new MixedParamUrlCodingStrategy("/service-type/profile",
			ServiceTypeProfilePage.class, new String[]{"domainId", "id"}));

		String selfGridId = new ServletContextParameterContext(getServletContext())
			.getValue("langrid.node.gridId");
		if(selfGridId == null)
			throw new RuntimeException("failed to initialize service manager.");
		ServiceFactory.getInstance().getGridService().setSelfGridId(selfGridId);
	}

	/**
	 * 
	 * 
	 */
	@Override
	protected IRequestCycleProcessor newRequestCycleProcessor() {
		return new WebRequestCycleProcessor() {
			@Override
			protected IRequestCodingStrategy newRequestCodingStrategy() {
				return new CryptedUrlWebRequestCodingStrategy(
					new WebRequestCodingStrategy());
			}

			@Override
			protected Page onRuntimeException(Page page, RuntimeException e) {
				if(e.getClass().equals(PageExpiredException.class)) {
					return super.onRuntimeException(page, e);
				}
				Class errorClass;
				if(page == null) {
					errorClass = this.getClass();
				} else {
					errorClass = page.getClass();
				}
				ServiceManagerException sme = new ServiceManagerException(e, errorClass, "Runtime Error");
				LogWriter.writeError("System", sme, errorClass);
				return new ErrorInternalPage(sme);
			}
		};
	}

	@Override
	protected WebRequest newWebRequest(HttpServletRequest servletRequest) {
		/**
		 * 
		 * 
		 */
		String ae = servletRequest.getHeader("accept-encoding");
		if(ae == null) {
			getResourceSettings().setDisableGZipCompression(true);
		} else {
			getResourceSettings().setDisableGZipCompression(false);
		}
		return super.newWebRequest(servletRequest);
	}
}
