/*
 * $Id: OverviewPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.other;

import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.LanguageResourcesLogOutPage;
import jp.go.nict.langrid.management.web.view.page.language.service.LanguageServiceLogOutPage;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.ServiceTypeListLogOutPage;
import jp.go.nict.langrid.management.web.view.page.node.NodeListLogOutPage;
import jp.go.nict.langrid.management.web.view.page.user.LanguageGridUsersLogOutPage;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class OverviewPage extends ServiceManagerPage {
	public OverviewPage() {
		add(new BookmarkablePageLink<ServiceManagerPage>("newsLink", NewsLogOutPage.class));
		add(new BookmarkablePageLink<ServiceManagerPage>("usersLink", LanguageGridUsersLogOutPage.class));
		add(new BookmarkablePageLink<ServiceManagerPage>("resourcesLink", LanguageResourcesLogOutPage.class));
		add(new BookmarkablePageLink<ServiceManagerPage>("servicesLink", LanguageServiceLogOutPage.class));
		add(new BookmarkablePageLink<ServiceManagerPage>("computersLink", NodeListLogOutPage.class));
		add(new BookmarkablePageLink<ServiceManagerPage>("typesLink", ServiceTypeListLogOutPage.class));
	}
}
