/*
 * $Id: ServiceInUseListPanel.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.composite.component.panel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.management.web.model.InvocationModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.view.page.language.service.component.panel.InUseListPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ServiceInUseListPanel extends InUseListPanel{
	/**
	 * 
	 * 
	 */
	public ServiceInUseListPanel(String gridId, List<InvocationModel> invocations){
		this.invocations = invocations;
	}

	@Override
	protected void createList() throws ServiceManagerException{
		Map<InvocationModel, Pair<Boolean, String>> existList = new LinkedHashMap<InvocationModel, Pair<Boolean, String>>();
		Map<InvocationModel, Pair<Boolean, String>> otherList = new LinkedHashMap<InvocationModel, Pair<Boolean, String>>();
		
		ServiceFactory f = ServiceFactory.getInstance();
		
		for(InvocationModel invocation : invocations){
			LangridServiceService<ServiceModel> service = f.getLangridServiceService(invocation.getServiceGridId());
			ServiceModel model = service.get(invocation.getServiceId());
			
			if(model != null) {
				if(model.getServiceType() != null && ! model.getServiceType().getTypeId().equals("Other")) {
					existList.put(invocation, new Pair<Boolean, String>(true, model.getServiceType().getTypeName()));
				} else {
					otherList.put(invocation, new Pair<Boolean, String>(true, "Other"));
				}
			} else {
				if(StringUtil.isValidString(invocation.getServiceTypeId())
						&& ! invocation.getServiceTypeId().equals("Other"))
				{
					ServiceTypeModel stm = ServiceFactory.getInstance().getServiceTypeService("").get(
							invocation.getDomainId(), invocation.getServiceTypeId());
					if( ! stm.getTypeId().equals("Other")){
						existList.put(invocation, new Pair<Boolean, String>(false, invocation.getServiceTypeId()));
					}else{
						otherList.put(invocation, new Pair<Boolean, String>(false, "Other"));
					}
				}else {
					otherList.put(invocation, new Pair<Boolean, String>(false, "Other"));
				}
			}
		}
		add(new ServiceInUsePanel("existList", existList));
		WebMarkupContainer otherContainer = new WebMarkupContainer("otherContainer");
		add(otherContainer);
		ServiceInUsePanel siup = new ServiceInUsePanel("otherList", otherList);
		otherContainer.add(siup);
		siup.setOutputMarkupId(true);
		siup.setOutputMarkupPlaceholderTag(true);
		siup.setVisible(false);
		otherContainer.add(new AjaxLink<ServiceInUsePanel>("isVisibleOther", new Model<ServiceInUsePanel>(siup)) {
			@Override
			public void onClick(AjaxRequestTarget target){
				getModelObject().setVisible( ! getModelObject().isVisible());
				target.addComponent(getModelObject());
			}
		});
		if(otherList.size() == 0){
			otherContainer.setVisible(false);
		}
	}
	
	private boolean isAlive(InvocationModel invocation){
		return StringUtil.isValidString(invocation.getServiceId())
			&& ! invocation.getServiceId().equals("-")
			&& ! invocation.getServiceName().startsWith("[IN TEST]")
			|| StringUtil.isValidString(invocation.getInvocationName())
			;
	}
	
	private Map<String, String> typeMap = new HashMap<String, String>();
	
	private List<InvocationModel> invocations;
}
