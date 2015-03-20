/*
 * $Id: RegistrationOfBpelCompositeServiceAdminPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.composite.admin;

import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.RegistrationOfBpelCompositeServicePage;

import org.apache.wicket.Page;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RegistrationOfBpelCompositeServiceAdminPage
extends RegistrationOfBpelCompositeServicePage {
	/**
	 * 
	 * 
	 */
	public RegistrationOfBpelCompositeServiceAdminPage(
		String gridId, String ownerUserId, CompositeServiceModel model)
	{
		super(gridId, ownerUserId, model);
	}
	
	@Override
	protected Page getCancelPage(CompositeServiceModel model, boolean isEdit) {
		return new RegistrationOfCompositeServiceProfileAdminPage(model, isEdit);
	}

	@Override
	protected void doResultProcess(CompositeServiceModel model)
	throws ServiceManagerException {
		NewsModel nm = new NewsModel();
		nm.setGridId(model.getGridId());
		Map<String, String> param = new HashMap<String, String>();
		param.put("name", model.getServiceName());
		nm.setContents(MessageManager.getMessage("news.service.composite.Registered", param));
		ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
		ServiceFactory.getInstance().getCompositeServiceService(model.getGridId())
			.approveService(model.getServiceId());
		setResponsePage(new RegistrationOfBpelCompositeServiceResultAdminPage(model.getServiceId()));
	}
}
