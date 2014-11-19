/*
 * $Id: EditResultPage.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource.admin.type;

import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedLabel;
import jp.go.nict.langrid.management.web.view.component.label.LimitedHyphenedLabel;
import jp.go.nict.langrid.management.web.view.component.label.LimitedLabel;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class EditResultPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public EditResultPage(String domainId , String typeId){
      try {
         ResourceTypeModel model = ServiceFactory.getInstance().getResourceTypeService(getSelfGridId()).get(domainId, typeId);
         String domainName = ServiceFactory.getInstance().getDomainService(getSelfGridId()).get(
            model.getDomainId()).getDomainName();
         add(new Label("domainId", domainName));
         add(new LimitedLabel("id", model.getResourceTypeId(), 48));
         add(new LimitedHyphenedLabel("name", model.getResourceTypeName(), 48));
         add(new HyphenedLabel("description", model.getDescription()));
         StringBuilder sb = new StringBuilder();
         int i = 0;
         for(ResourceMetaAttributeModel smam : model.getMetaAttrbuteList()){
            sb.append(smam.getAttributeId());
            i++;
            if(i < model.getMetaAttrbuteList().size()){
               sb.append("<br/>");
            }
         }
         add(new Label("attribute", sb.toString()).setEscapeModelStrings(false));
         add(new Link("back"){
            @Override
            public void onClick() {
               setResponsePage(new ListPage());
            }
         });
      } catch(ServiceManagerException e) {
         doErrorProcess(e);
      }
	}
}
