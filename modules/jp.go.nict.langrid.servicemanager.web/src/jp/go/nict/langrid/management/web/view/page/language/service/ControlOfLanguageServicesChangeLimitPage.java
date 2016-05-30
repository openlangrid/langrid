/*
 * $Id: ControlOfLanguageServicesChangeLimitPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service;

import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.AccessLimitsForm;
import jp.go.nict.langrid.management.web.view.page.user.component.link.UserProfileLink;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ControlOfLanguageServicesChangeLimitPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public ControlOfLanguageServicesChangeLimitPage(
			ServiceModel model, boolean permit, String userGridId, String userId, UserModel user)
	{
		targetUserGridId = userGridId;
		targetUserId = userId;
		targetUser = user;
		isPermitted = permit;
//		targetModel = model;
		final String serviceGridId = model.getGridId();
		final String serviceId = model.getServiceId();
		add(new Label("resourceName", model.getServiceName()));
		Link userPopup = new UserProfileLink("provider", userGridId, userId, "limit");
		userPopup.add(new Label("organization", targetUser.getOrganization()));
		add(userPopup);
		add(new Label("homepage"
		   , StringUtil.shortenString(targetUser.getHomepageUrl() == null ? "" : user.getHomepageUrl().getValue().toString(), 24)));
		add(new Label("accessRight", MessageManager.getMessage(
				"Common.label."
						+ (isPermitted ? "Permitted" : "Prohibited"), getLocale())));
		try{
		   AccessLimitsForm form = new AccessLimitsForm("form", userGridId, userId, getSelfGridId(), model.getServiceId()){
				@Override
				protected Page getCancelPage(String serviceId){
					return getBackPage(serviceId);
				}

				@Override
				protected void setResultPage(String resultParameter){
					setResponsePage(getResponsePage(serviceGridId, serviceId, targetUserGridId, targetUserId, targetUser, isPermitted, resultParameter));
				}

				private static final long serialVersionUID = 1L;
			};
			add(form);
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}
	}
	
	protected Page getBackPage(String serviceId){
		return new ControlOfLanguageServicesPage(serviceId);
	}
	
	protected Page getResponsePage(
			String serviceGridId, String serviceId
			, String userGridId, String userId, UserModel profile, boolean isPermitted, String limitString)
	{
		return new ControlOfLanguageServicesChangeLimitResultPage(
		   serviceGridId, serviceId
		   , userGridId, userId, profile, isPermitted, limitString);
	}

	private boolean isPermitted;
	private String targetUserGridId;
	private String targetUserId;
	private UserModel targetUser;
//	private ServiceModel targetModel;

	private static final long serialVersionUID = 1L;
}
