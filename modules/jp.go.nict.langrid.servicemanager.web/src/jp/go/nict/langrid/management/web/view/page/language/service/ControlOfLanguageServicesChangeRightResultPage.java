/*
 * $Id: ControlOfLanguageServicesChangeRightResultPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
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
public class ControlOfLanguageServicesChangeRightResultPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public ControlOfLanguageServicesChangeRightResultPage(
			ServiceModel entry, boolean permit, UserModel user)
	{
	   final String serviceId = entry.getServiceId();
		add(new Label("resourceName", entry.getServiceName()));
		Link userPopup = new UserProfileLink("provider", user.getGridId(), user.getUserId(), "result");
		userPopup.add(new Label("organization", user.getOrganization()));
		add(userPopup);
		add(new Label("homepage", StringUtil.shortenString(user.getHomepageUrl() == null ? "" : user.getHomepageUrl() == null ? "" : user.getHomepageUrl().getValue().toString(), 24)));
		add(new Label("doPermit", permit ? "permitted" : "prohibited"));
		add(new Label("isPermit", permit ? "permitted" : "prohibited"));
		add(new Link("back"){
         @Override
         public void onClick() {
            setResponsePage(getBackPage(serviceId));
         }
      });
	}
	
   protected Page getBackPage(String serviceId){
      return new ControlOfLanguageServicesPage(serviceId); 
   }

	private static final long serialVersionUID = 1L;
}
