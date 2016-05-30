/*
 * $Id: LanguageResourceProfilePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource;

import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ResourceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.language.LanguageDomainPopUpPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.panel.ResourceProfilePanel;

import org.apache.wicket.PageParameters;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class LanguageResourceProfilePage extends LanguageDomainPopUpPage{
	/**
	 * 
	 * 
	 */
	public LanguageResourceProfilePage(PageParameters pp) {
		try{
			String gridId = pp.getString("gridId");
			String resourceId = pp.getString("id");
			if(gridId == null || resourceId == null){
				redirectTop();
				return;
			}
			ResourceService service = ServiceFactory.getInstance().getResourceService(gridId);
			ResourceModel resource = service.get(resourceId);
			if(resource == null){
				redirectTop();
				return;
			}
			add(new ResourceProfilePanel("profilePanel", gridId, resource));
		}catch(ServiceManagerException e) {
			doErrorProcessForPopup(e);
		}
	}

	private static final long serialVersionUID = 1L;
}
