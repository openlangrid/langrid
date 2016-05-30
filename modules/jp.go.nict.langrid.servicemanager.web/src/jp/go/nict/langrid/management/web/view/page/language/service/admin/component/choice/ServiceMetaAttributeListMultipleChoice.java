/*
 * $Id: ServiceMetaAttributeListMultipleChoice.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.component.choice;

import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ServiceMetaAttributeListMultipleChoice
extends ListMultipleChoice<ServiceMetaAttributeModel> {
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public ServiceMetaAttributeListMultipleChoice(String gridId, String componentId,
		String domainId)
	throws ServiceManagerException {
		super(componentId, new Model<LangridList<ServiceMetaAttributeModel>>(
			new LangridList<ServiceMetaAttributeModel>())
			, new WildcardListModel<ServiceMetaAttributeModel>());
		setChoiceRenderer(new ServiceMetaAttributeChoiceRenderer());
		setModelList(gridId, domainId);
	}

	public void setSelectedList(Collection<ServiceMetaAttributeModel> list) {
		setDefaultModelObject(list);
	}

	@Override
	public CharSequence getDefaultChoice(Object selected) {
		return "";
	}

	public int getListSize() {
		return list.size();
	}

	public void setModelList(String gridId, String domainId)
	throws ServiceManagerException {
		list = ServiceFactory.getInstance().getServiceMetaAttributeService(gridId).getAllListOnDomain(domainId);
		setChoices(list);
		setMaxRows(10);
		if(0 < list.size()) {
			LangridList<ServiceMetaAttributeModel> selected = new LangridList<ServiceMetaAttributeModel>();
			selected.add(list.get(0));
			setSelectedList(selected);
		}
	}

	private List<ServiceMetaAttributeModel> list;

	private class ServiceMetaAttributeChoiceRenderer
	implements IChoiceRenderer<ServiceMetaAttributeModel> {
		public ServiceMetaAttributeChoiceRenderer() {
		}

		@Override
		public Object getDisplayValue(ServiceMetaAttributeModel object) {
			return object.getAttributeId();
			//         return object.getAttributeName();
		}

		@Override
		public String getIdValue(ServiceMetaAttributeModel object, int index) {
			return object.getAttributeId();
		}
	}
}
