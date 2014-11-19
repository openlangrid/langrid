/*
 * $Id: EditOfServiceTypeResultPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.type;

import jp.go.nict.langrid.management.web.model.InterfaceDefinitionModel;
import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.ServiceTypeService;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedLabel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class EditOfServiceTypeResultPage extends ServiceManagerPage {
	/**
	 * 
	 * 
	 */
	public EditOfServiceTypeResultPage(String domainId, String id) {
		try {
			ServiceTypeModel model = ServiceFactory.getInstance().getServiceTypeService(
				getSelfGridId()).get(domainId, id);
			String domainName = ServiceFactory.getInstance()
				.getDomainService(getSelfGridId()).get(
					model.getDomainId()).getDomainName();
			add(new Label("domain", domainName));
			add(new Label("id", model.getTypeId()));
			add(new HyphenedLabel("name", model.getTypeName()));
			add(new HyphenedLabel("description", model.getDescription()));
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for(ServiceMetaAttributeModel smam : model.getMetaAttrbuteList()) {
				//            sb.append(smam.getAttributeName());
				sb.append(smam.getAttributeId());
				i++;
				if(i < model.getMetaAttrbuteList().size()) {
					sb.append("<br/>");
				}
			}
			add(new Label("attribute", model.getMetaAttrbuteList().size() == 0 ? "-"
				: sb.toString()).setEscapeModelStrings(false));
			ServiceTypeService stService = ServiceFactory.getInstance()
				.getServiceTypeService(getSelfGridId());
			RepeatingView rv = new RepeatingView("repeating");
			add(rv);
			for(InterfaceDefinitionModel idm : model.getInterfaceList()) {
				WebMarkupContainer wmc = new WebMarkupContainer(rv.newChildId());
				wmc.add(new Label("protocol", idm.getProtocolId()));
				RepeatingView frv = new RepeatingView("fileRepeating");
				wmc.add(frv);
				for(String fileName : stService.getDefinitionFileNames(
					model.getDomainId(), model.getTypeId(), idm.getProtocolId())) {
					WebMarkupContainer fileContainer = new WebMarkupContainer(
						frv.newChildId());
					frv.add(fileContainer);
					fileContainer.add(new Label("fileName", fileName));
				}
				rv.add(wmc);
			}
			add(new Link("back") {
				@Override
				public void onClick() {
					setResponsePage(new ServiceTypeListAdminPage());
				}
			});
		} catch(ServiceManagerException e) {
			doErrorProcess(e);
		}
	}
}
