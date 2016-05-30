/*
 * $Id: ServiceProfilePage.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.management.web.model.AtomicServiceModel;
import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AtomicServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.language.LanguageDomainPopUpPage;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.panel.AtomicServiceProfilePanel;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.component.panel.CompositeServiceProfilePanel;

import org.apache.wicket.PageParameters;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ServiceProfilePage extends LanguageDomainPopUpPage {
	/**
	 * 
	 * 
	 */
	public ServiceProfilePage(PageParameters pp) {
		try {
			String gridId = pp.getString("gridId");
			String serviceId = pp.getString("id");
			if(gridId == null || serviceId == null) {
				redirectTop();
				return;
			}
			AtomicServiceService service = ServiceFactory.getInstance().getAtomicServiceService(gridId);
			if( ! service.isExist(serviceId)) {
				redirectTop();
				return;
			}
			if(service.isExternalService(serviceId)) {
				AtomicServiceModel model = service.get(serviceId);
				add(new AtomicServiceProfilePanel("profilePanel", gridId, model));
			} else {
				CompositeServiceModel cModel = ServiceFactory.getInstance()
					.getCompositeServiceService(gridId).get(serviceId);
				add(new CompositeServiceProfilePanel("profilePanel", gridId, cModel));
			}
		} catch(ServiceManagerException e) {
			doErrorProcessForPopup(e);
		} catch(InvalidLanguageTagException e) {
			doErrorProcessForPopup(new ServiceManagerException(e, getPageClass()));
		}
	}

	private static final long serialVersionUID = 1L;
}
