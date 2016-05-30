/*
 * $Id: UnregistrationOfLanguageResourcesCancelResultPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.list.row.LanguageResourcesListRowPanel;

import org.apache.wicket.markup.html.link.ExternalLink;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class UnregistrationOfLanguageResourcesCancelResultPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public UnregistrationOfLanguageResourcesCancelResultPage(ResourceModel resource){
		try{
			add(new LanguageResourcesListRowPanel(resource.getGridId(), "row", resource, ""){
				@Override
				protected String getOrganizationName(String gridId, ResourceModel resource)
				throws ServiceManagerException
				{
					return ServiceFactory.getInstance().getUserService(gridId).get(resource.getOwnerUserId()).getOrganization();
				}

				private static final long serialVersionUID = 1L;
			});
			ExternalLink isoLink = new ExternalLink("linkLanguagePage",
					MessageUtil.ISO_LANGUAGE_CODE_URL);
			add(isoLink);
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}
	}

	private static final long serialVersionUID = 1L;
}
