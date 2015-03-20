/*
 * $Id: InvocationServiceTypeDropDownChoice.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.component.choice;

import java.util.List;

import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.ServiceModelUtil;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class InvocationServiceTypeDropDownChoice extends DropDownChoice<ServiceTypeModel> {
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public InvocationServiceTypeDropDownChoice(String gridId, String componentId)
	throws ServiceManagerException {
		super(componentId
				, new Model<ServiceTypeModel>()
				, new WildcardListModel<ServiceTypeModel>());
		setChoiceRenderer(new ServiceTypeModelChoiceRenderer());
		setChoices(getServiceTypeList(gridId));
	}

	@Override
	public CharSequence getDefaultChoice(Object selected) {
		return "";
	}

	/**
	 * 
	 * 
	 */
	public ServiceTypeModel getSelectedType() {
		if(getModelObject() == null) {
			return typeList.get(0);
		}
		return getModelObject();
	}

	/**
	 * 
	 * 
	 */
	public boolean isSelected() {
		return getValue().equals(NO_SELECTION_VALUE);
	}
	
	protected List<ServiceTypeModel> getServiceTypeList(String gridId)
	throws ServiceManagerException {
		try {
			typeList = ServiceFactory.getInstance().getServiceTypeService(gridId).getAllList();
			typeList.add(ServiceModelUtil.makeOtherServiceTypeModel());
		} catch(ServiceManagerException e) {
			return new LangridList<ServiceTypeModel>();
		}
		return typeList;
	}
	
	protected void setServiceTypeList(List<ServiceTypeModel> list) {
		typeList = list;
	}

	public ServiceTypeModel getDefaultServiceType() {
		if(typeList.isEmpty()) {
			return null;
		}
		return typeList.get(0);
	}

	public int getModelCount() {
		return typeList.size();
	}
	
	private List<ServiceTypeModel> typeList;

	private class ServiceTypeModelChoiceRenderer
	implements IChoiceRenderer<ServiceTypeModel>
	{
		@Override
		public Object getDisplayValue(ServiceTypeModel object) {
			return object.getTypeName();
		}

		@Override
		public String getIdValue(ServiceTypeModel object, int index) {
			return object.getTypeId();
		}
	}

	private static final long serialVersionUID = 1L;
}
