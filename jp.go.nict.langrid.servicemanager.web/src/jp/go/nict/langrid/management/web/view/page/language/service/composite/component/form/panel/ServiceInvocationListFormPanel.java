/*
 * $Id: ServiceInvocationListFormPanel.java 1519 2015-03-10 10:07:30Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.composite.component.form.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.InvocationModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.ServiceDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.AllServiceTypeDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.service.component.text.RequiredServiceNameField;
import jp.go.nict.langrid.management.web.view.utility.RequestResponseUtil;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1519 $
 */
public class ServiceInvocationListFormPanel extends Panel {

	/**
	 * 
	 * 
	 */
	public ServiceInvocationListFormPanel(String panelId, String gridId)
	throws ServiceManagerException
	{
		super(panelId);
		this.gridId = gridId;
		add(rewriteWrapper = new WebMarkupContainer("rewriteWrapper"));
		rewriteWrapper.setOutputMarkupId(true);
		repeating = new RepeatingView("invocationRepeater");
		rewriteWrapper.add(repeating);
		WebMarkupContainer wmc = new WebMarkupContainer(repeating.newChildId());
		repeating.add(wmc);
		setFormComponents(wmc, true, null);

		add(new AjaxSubmitLink("addInvocationLink"){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> arg1) {
				WebMarkupContainer wmc = new WebMarkupContainer(repeating.newChildId());
				repeating.add(wmc);
				setFormComponents(wmc, false, null);
				WebMarkupContainer first = (WebMarkupContainer)repeating.iterator().next();
				first.get("deleteInvocationLink").setVisibilityAllowed(true);
				target.addComponent(rewriteWrapper);
			}
			public boolean getDefaultFormProcessing() {return false;};
		});
	}


	/**
	 * 
	 * 
	 */
	public ServiceInvocationListFormPanel(String panelId, String gridId, CompositeServiceModel model)
	throws ServiceManagerException
	{
		super(panelId);
		this.gridId = gridId;
		add(rewriteWrapper = new WebMarkupContainer("rewriteWrapper"));
		rewriteWrapper.setOutputMarkupId(true);
		repeating = new RepeatingView("invocationRepeater");
		rewriteWrapper.add(repeating);

		List <InvocationModel> invocations = model.getInvocations();
		Boolean isOne = (invocations.size() <= 1);

		for ( InvocationModel invocation : invocations ) {
			WebMarkupContainer wmc = new WebMarkupContainer(repeating.newChildId());
			repeating.add(wmc);
			setFormComponents(wmc, isOne, invocation);
		}
		add(new AjaxSubmitLink("addInvocationLink"){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> arg1) {
				WebMarkupContainer wmc = new WebMarkupContainer(repeating.newChildId());
				repeating.add(wmc);
				setFormComponents(wmc, false, null);
				WebMarkupContainer first = (WebMarkupContainer)repeating.iterator().next();
				first.get("deleteInvocationLink").setVisibilityAllowed(true);
				target.addComponent(rewriteWrapper);
			}
			public boolean getDefaultFormProcessing() {return false;};
		});
	}


	/**
	 * 
	 * 
	 */
	public List<InvocationModel> getInvocations(String ownerServiceId) throws ServiceManagerException{
		List<InvocationModel> invocations = new ArrayList<InvocationModel>();
		Iterator ite = repeating.iterator();
		while(ite.hasNext()){
			WebMarkupContainer wmc = (WebMarkupContainer)ite.next();
			WebMarkupContainer serviceSelect = (WebMarkupContainer)wmc.get("serviceSelect");

			TextField<String> name = (TextField<String>)wmc.get("invocationName");
			AllServiceTypeDropDownChoice typeChoice = (AllServiceTypeDropDownChoice)wmc.get("serviceTypeChoice");
			ServiceDropDownChoice serviceChoice = (ServiceDropDownChoice)serviceSelect.get("serviceChoice");

			InvocationModel invocation = new InvocationModel();
			invocation.setOwnerServiceGridId(gridId);
			invocation.setOwnerServiceId(ownerServiceId);

			invocation.setInvocationName(name.getModelObject());

			if ( serviceSelect.isVisible() ) { // Other Type
				ServiceModel model = serviceChoice.getSelectedService();
				invocation.setServiceTypeId(null);
				if ( model != null ) {
					invocation.setServiceGridId(model.getGridId());
					if ( !model.getServiceId().isEmpty()) {
						invocation.setServiceId(model.getServiceId());
					}
					if ( !model.getServiceName().isEmpty()) {
						invocation.setServiceName(model.getServiceName());
					}
				} else {
					invocation.setServiceId(null);
					invocation.setServiceName(null);
				}
			} else {
				ServiceTypeModel model = typeChoice.getSelectedType();
				invocation.setServiceGridId(gridId);
				invocation.setDomainId(model == null ? null : model.getDomainId());
				invocation.setServiceTypeId(model == null ? null : model.getTypeId());
			}
			invocations.add(invocation);
		}
		return invocations;
	}

	protected String getOwnerUserId(){
		return "";
	}

	protected String getOwnerGridId(){
		return "";
	}

	private void setFormComponents(final WebMarkupContainer wmc, boolean isFirst, InvocationModel model) {
		String modelGridId = gridId;
		if ( model != null ) {
			modelGridId = model.getServiceGridId();
		}

		try {
			serviceSelect = new WebMarkupContainer("serviceSelect");
			final AllServiceTypeDropDownChoice typeChoice = new AllServiceTypeDropDownChoice(modelGridId, "serviceTypeChoice")
			{
				@Override public boolean isVisible() {return getModelCount() != 0;}
			};
			typeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange"){
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					updateServiceTypeInfomation(typeChoice.getSelectedType(), wmc);
					target.addComponent(serviceSelect);
				}
			});
			wmc.add(typeChoice);

			ServiceDropDownChoice serviceChoice = makeServiceChoice(gridId, serviceSelect);

			serviceSelect.setOutputMarkupId(true);
			serviceSelect.setOutputMarkupPlaceholderTag(true);
			serviceSelect.add(serviceChoice);
			serviceSelect.setVisible(false);
			wmc.add(serviceSelect);
			if ( model != null ) {
				typeChoice.setModelById(model.getServiceTypeId());
				serviceChoice.setModelById(model.getServiceId());
			}
			updateServiceTypeInfomation(typeChoice.getSelectedType(), wmc);
		} catch (ServiceManagerException e) {
			throw new RestartResponseException(RequestResponseUtil.getPageForErrorRequest(e));
		}

		wmc.add(new Label("invocationNumber", new Model<String>(Integer.toString(repeating.size()) + "." )));
		if ( model == null ) {
			wmc.add(new RequiredServiceNameField("invocationName", new Model<String>()));
		} else {
			wmc.add(new RequiredServiceNameField("invocationName", new Model<String>(model.getInvocationName())));
		}
		wmc.add(new AjaxSubmitLink("deleteInvocationLink"){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if(1 < repeating.size()){
					repeating.remove(wmc);
					WebMarkupContainer first = (WebMarkupContainer)repeating.iterator().next();
					first.get("deleteInvocationLink").setVisibilityAllowed(1 != repeating.size());
					target.addComponent(rewriteWrapper);
				}
			}
			public boolean getDefaultFormProcessing() {return false;};
		}.setOutputMarkupPlaceholderTag(true).setVisibilityAllowed( ! isFirst));
	}

	private ServiceDropDownChoice makeServiceChoice(String gridId, WebMarkupContainer choiceContainer)
	throws ServiceManagerException
	{
		ServiceDropDownChoice choice;
		if(selectListMap.containsKey(gridId)){
			choice = new ServiceDropDownChoice(
				gridId, "serviceChoice", getOwnerGridId(), getOwnerUserId(), selectListMap.get(gridId))
			{
				@Override public boolean isVisible() {return getModelCount() != 0;}
			};
		}else{
			choice = new ServiceDropDownChoice(
				gridId, "serviceChoice", getOwnerGridId(), getOwnerUserId())
			{
				@Override public boolean isVisible() {return getModelCount() != 0;}
			};
			selectListMap.put(gridId, choice.getList());
		}
		choiceContainer.add(new Label("noServiceLabel", "-").setVisible( ! choice.isVisible()));
		return choice;
	}

	private void updateServiceTypeInfomation(ServiceTypeModel stm, WebMarkupContainer wmc){
		if ( stm != null && stm.getTypeId() != null && !stm.getTypeId().equals("") && !stm.getTypeId().equals("Other")) {
			serviceSelect.setVisible(false);
		} else {
			serviceSelect.setVisible(true);
		}
	}

	private WebMarkupContainer serviceSelect;
	private Map<String, List<ServiceModel>> selectListMap = new HashMap<String, List<ServiceModel>>();
	private RepeatingView repeating;
	private WebMarkupContainer rewriteWrapper;
	private String gridId;
}
