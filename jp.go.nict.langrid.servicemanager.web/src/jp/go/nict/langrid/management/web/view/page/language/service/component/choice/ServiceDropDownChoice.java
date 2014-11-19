/*
 * $Id: ServiceDropDownChoice.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

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
public class ServiceDropDownChoice extends DropDownChoice<ServiceModel> {
	public ServiceDropDownChoice(
		String gridId, String componentId, String userGridId, String userId, List<ServiceModel> list)
	throws ServiceManagerException
	{
		super(componentId
			, new Model<ServiceModel>()
			, new WildcardListModel<ServiceModel>());
		setChoiceRenderer(new ServiceModelChoiceRenderer());
		serviceList = list == null ? getServiceList(gridId, userGridId, userId) : list;
		setChoices(serviceList);
	}
	
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public ServiceDropDownChoice(String gridId, String componentId, String userGridId, String userId)
	throws ServiceManagerException
	{
		this(gridId, componentId, userGridId, userId, null);
	}

	@Override
	public CharSequence getDefaultChoice(Object selected) {
		return "";
	}

	/**
	 * 
	 * 
	 */
	public ServiceModel getSelectedService() {
		if(getModelObject() == null) {
			if(serviceList.size() == 0){
				return null;
			}
			return serviceList.get(0);
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
	
	protected List<ServiceModel> getServiceList(String gridId, String userGridId, String userId)
	throws ServiceManagerException
	{
		LangridServiceService<ServiceModel> service = ServiceFactory.getInstance().getLangridServiceService(gridId, userGridId, userId);
		return service.getOtherList(gridId, new Order("serviceName", OrderDirection.ASCENDANT));
	}
	

	public ServiceModel getDefaultServiceType() {
		if(serviceList.isEmpty()) {
			return null;
		}
		return serviceList.get(0);
	}

	public int getModelCount() {
		return serviceList.size();
	}

//	protected void setServiceTypeList(List<ServiceModel> list) {
//		serviceList = list;
//	}
	
	public List<ServiceModel> getList(){
		return serviceList;
	}
		
	public void setModelById(String serviceId){
		for(ServiceModel model : serviceList){
			if(model.getServiceId().equals(serviceId)){			
				setModelObject(model);
				return;
			}
		}
	}
	//---------------------------------------
	
	private List<ServiceModel> serviceList;

	private class ServiceModelChoiceRenderer
	implements IChoiceRenderer<ServiceModel>
	{
		@Override
		public Object getDisplayValue(ServiceModel object) {
			return object.getServiceName();
		}

		@Override
		public String getIdValue(ServiceModel object, int index) {
			return object.getServiceId();
		}
	}

	private static final long serialVersionUID = 1L;
}
