/*
 * $Id: ResourceTypeDropDownChoice.java 1521 2015-03-10 10:29:09Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource.component.choice;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.management.web.model.DomainModel;
import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ResourceModelUtil;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1521 $
 */
public class ResourceTypeDropDownChoice extends
		DropDownChoice<ResourceTypeModel> {
	public ResourceTypeDropDownChoice(String componentId)
			throws ServiceManagerException {
		super(componentId, new Model<ResourceTypeModel>(),
				new WildcardListModel<ResourceTypeModel>());
		setChoiceRenderer(new ResourceTypeModelChoiceRenderer());
		list = new ArrayList<ResourceTypeModel>();
		for(DomainModel dm : ServiceFactory.getInstance().getDomainService(null).getAllList()){
			list.addAll(ServiceFactory.getInstance().getResourceTypeService(null)
					.getAllList(dm.getDomainId()));
		}
		list.add(ResourceModelUtil.makeOtherResourceTypeModel());
		setChoices(list);
	}

	public ResourceTypeDropDownChoice(String gridId, String componentId)
			throws ServiceManagerException {
		super(componentId, new Model<ResourceTypeModel>(),
				new WildcardListModel<ResourceTypeModel>());
		setChoiceRenderer(new ResourceTypeModelChoiceRenderer());
		list = new ArrayList<ResourceTypeModel>();
		for(DomainModel dm : ServiceFactory.getInstance().getDomainService(gridId).getListOnGrid(gridId)){
			list.addAll(ServiceFactory.getInstance().getResourceTypeService(gridId)
					.getAllList(dm.getDomainId()));
		}
		list.add(ResourceModelUtil.makeOtherResourceTypeModel());
		setChoices(list);
	}

	/**
	 * 
	 * 
	 */
	public ResourceTypeDropDownChoice(String gridId, String domainId,
			String componentId) throws ServiceManagerException {
		super(componentId, new Model<ResourceTypeModel>(),
				new WildcardListModel<ResourceTypeModel>());
		setChoiceRenderer(new ResourceTypeModelChoiceRenderer());
		list = ServiceFactory.getInstance().getResourceTypeService(gridId)
				.getAllList(domainId);
		list.add(ResourceModelUtil.makeOtherResourceTypeModel());
		setChoices(list);
	}

	/**
	 * 
	 * 
	 */
	public String getAjaxMethod() {
		return this.ajaxMethod;
	}

	/**
	 * 
	 * 
	 */
	public AbstractAjaxBehavior getBehavior() {
		return (AbstractAjaxBehavior) getBehaviors().get(0);
	}

	@Override
	public CharSequence getDefaultChoice(Object selected) {
		return "";
	}

	// /**
	// * 
	// * 
	// */
	// public LanguagePathType getLanguagePathType(){
	// return getModelObject().getPathType();
	// }

	/**
	 *  
	 */
	public ResourceTypeModel getResourceType() {
		return getModelObject();
	}

	/**
	 * 
	 * 
	 */
	public ResourceTypeModel getDefaultResourceType() {
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 
	 * 
	 */
	public int getModelCount() {
		return list.size();
	}

	private List<ResourceTypeModel> list;
	private final String ajaxMethod = "onchange";
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 
	 * @author Masaaki Kamiya
	 * @author $Author: t-nakaguchi $
	 * @version $Revision: 1521 $
	 */
	private class ResourceTypeModelChoiceRenderer
	implements IChoiceRenderer<ResourceTypeModel>
	{
		@Override
		public Object getDisplayValue(ResourceTypeModel object) {
			return object.getResourceTypeId();
		}

		@Override
		public String getIdValue(ResourceTypeModel object, int index) {
			return object.getResourceTypeId();
		}

		private static final long serialVersionUID = 2984945528514767487L;
	}
}
