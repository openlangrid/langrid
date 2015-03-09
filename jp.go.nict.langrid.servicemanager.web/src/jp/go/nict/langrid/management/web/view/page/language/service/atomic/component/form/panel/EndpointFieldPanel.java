package jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.panel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.management.web.model.ProtocolModel;
import jp.go.nict.langrid.management.web.model.ServiceEndpointModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.component.choice.ProtocolDropDownChoice;
import jp.go.nict.langrid.management.web.view.component.container.SwitchableContainer;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedLabel;
import jp.go.nict.langrid.management.web.view.component.link.AjaxNonSubmitLink;
import jp.go.nict.langrid.management.web.view.component.text.RequiredURLField;
import jp.go.nict.langrid.management.web.view.component.validator.PasswordConfirmValidator;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.validator.DuplicatedEndpointValidator;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.form.validator.RelatedAtoBRequiredFormValidator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;


/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1398 $
 */
public class EndpointFieldPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public EndpointFieldPanel(
			String componentId, final String gridId, String serviceId, List<ServiceEndpointModel> endpointList)
	throws ServiceManagerException
	{
		super(componentId);
		this.serviceId = serviceId;
		this.serviceGridId = gridId;
		add(rewriteWrapper = new WebMarkupContainer("rewrite"));
		rewriteWrapper.setOutputMarkupId(true);
		setRenderBodyOnly(true);
		setOutputMarkupId(true);
		if(endpointList.size() == 0) {
			ServiceEndpointModel ep = new ServiceEndpointModel();
			ep.setEnabled(true);
			ep.setGridId(gridId);
			endpointList.add(ep);
		}
		
		repeater= new RepeatingView("endpointList");
		for(ServiceEndpointModel ep : endpointList) {
			LastOneSizeContainer container = new LastOneSizeContainer(gridId, repeater.newChildId(), ep, false, repeater);
			if(endpointList.size() == 1) {
				container.setVisibleLastOne();
			}
			repeater.add(container);
		}
		rewriteWrapper.add(repeater);

		add(new AjaxNonSubmitLink("add") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				ServiceEndpointModel ep = new ServiceEndpointModel();
				ep.setEnabled(true);
				ep.setAuthPassword("");
				ep.setUrl("");
				ep.setProtocolId("SOAP_RPC_ENCODED");
				ep.setAuthUserName("");
				ep.setGridId(serviceGridId);
				List<LastOneSizeContainer> list = new ArrayList<LastOneSizeContainer>();
				Iterator ite = repeater.iterator();
				while(ite.hasNext()) {
					LastOneSizeContainer losc = (LastOneSizeContainer) ite.next();
					losc.setModelFromInput();
					losc.clearVisible();
					list.add(losc);
				}
				try{
					LastOneSizeContainer container = new LastOneSizeContainer(gridId, repeater.newChildId(), ep, true, repeater);
					repeater.add(container);
					target.addComponent(rewriteWrapper);
				} catch(ServiceManagerException e){
					throw new RuntimeException(e);
				}
			}
			private static final long serialVersionUID = 1L;
		}.setDefaultFormProcessing(false));
	}

	public List<ServiceEndpointModel> getAddEndpointList() {
		List<ServiceEndpointModel> list = new ArrayList<ServiceEndpointModel>();
		Iterator ite = repeater.iterator();
		while(ite.hasNext()) {
			LastOneSizeContainer container = (LastOneSizeContainer)ite.next();
			ServiceEndpointModel obj = container.getEndpoint();
			if(obj != null) {
				obj.setProtocolId(((ProtocolModel)container.protocolChoice.getDefaultModelObject()).getProtocolId());
				list.add(obj);
			}
		}
		return list;
	}
	
	public Set<ServiceEndpointModel> getRemoveEndopintList() {
		return removed;
	}
	
	public void setValidator(IFormValidator validator) {
		((Form)getParent()).add(validator);
	}
	
	private String serviceId;
	private String serviceGridId;
	private Set<ServiceEndpointModel> removed = new HashSet<ServiceEndpointModel>();
	private RepeatingView repeater;	
	private WebMarkupContainer rewriteWrapper;
	
	private class LastOneSizeContainer extends WebMarkupContainer {
		public LastOneSizeContainer(
				String gridId, String componentId, final ServiceEndpointModel ep, boolean isAdd, WebMarkupContainer container)
		throws ServiceManagerException
		{
			super(componentId, new Model<ServiceEndpointModel>(ep));
			parentContainer = container;
			this.isAdded = isAdd;

			Label urlLabel = new HyphenedLabel("urlLabel", ep.getUrl());
			urlLabel.setEscapeModelStrings(false);
			RequiredURLField ruf = new RequiredURLField("url", new PropertyModel<String>(ep, "url")
					, "LanguaegServiceSettings.error.endpoint.url.Required");
			ruf.add(new DuplicatedEndpointValidator(ruf, ep.getGridId(), serviceId, removed));
			SwitchableContainer<RequiredURLField, Label> urlContainer = new SwitchableContainer<RequiredURLField, Label>(
					"urlContainer"
					, ruf
					,  urlLabel
			);

			Label protocolLabel = new HyphenedLabel("protocolLabel", ep.getProtocolId());
			protocolLabel.setEscapeModelStrings(false);
			protocolChoice = new ProtocolDropDownChoice(gridId, "protocol");
			protocolChoice.setModelById(ep.getProtocolId());
			SwitchableContainer<ProtocolDropDownChoice, Label> protocolContainer = new SwitchableContainer<ProtocolDropDownChoice, Label>(
					"protocolContainer"
					, protocolChoice
					, protocolLabel
			);
			protocolContainer.getFirstComponent().setRequired(false);

			Label nameLabel = new HyphenedLabel("userNameLabel", ep.getAuthUserName());
			nameLabel.setEscapeModelStrings(false);
			SwitchableContainer<TextField<String>, Label> nameContainer = new SwitchableContainer<TextField<String>, Label>(
					"userNameContainer"
					, new TextField<String>("userName", new PropertyModel<String>(ep, "authUserName"))
					, nameLabel
			);
			nameContainer.getFirstComponent().setRequired(false);
			
			Label passwordLabel = new Label("passwordLabel", "*************");
			passwordLabel.setEscapeModelStrings(false);
			PasswordTextField password = new PasswordTextField("password"
			   , new PropertyModel<String>(ep, "authPassword"));
			password.setRequired(false);
			SwitchableContainer<PasswordTextField, Label> passwordContainer = new SwitchableContainer<PasswordTextField, Label>(
					"passwordContainer", password, passwordLabel
			);

			PasswordTextField confirm = new PasswordTextField("confirm", new Model<String>());
			confirm.setRequired(false);
			SwitchableContainer<PasswordTextField, Label> confirmContainer = new SwitchableContainer<PasswordTextField, Label>(
					"confirmContainer", confirm
			);
			
			if(isAdd){
				passwordContainer.getFirstComponent().add(new PasswordConfirmValidator(
						confirmContainer.getFirstComponent()));
				RelatedAtoBRequiredFormValidator<String> validater = new RelatedAtoBRequiredFormValidator<String>(
						passwordContainer.getFirstComponent(), confirmContainer.getFirstComponent());
				validater.setMessageValue("", "");
				setValidator(validater);
				setValidator(new RelatedAtoBRequiredFormValidator<String>(passwordContainer.getFirstComponent()
						, nameContainer.getFirstComponent()));
			} else{
				urlContainer.switchComponent();
				protocolContainer.switchComponent();
				if(ep.getAuthUserName() == null || ep.getAuthUserName().equals("")){
					nameContainer.setVisible(false);
					passwordContainer.setVisible(false);
				}else{
					nameContainer.switchComponent();
					passwordContainer.switchComponent();
				}
				confirmContainer.switchComponent();
				confirmContainer.setVisible(isAdd);
				confirmContainer.setEnabled(isAdd);
				
			}
			
			removeLink = new AjaxNonSubmitLink("remove") {
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form form){
					if(parentContainer.size() > 1){
						if(!isAdded){
							removed.add((ServiceEndpointModel)getParent().getDefaultModelObject());
						}
						parentContainer.remove(getParent());
						
						Iterator ite = parentContainer.iterator();
						while(ite.hasNext()){
							LastOneSizeContainer losc = (LastOneSizeContainer)ite.next();
							losc.setModelFromInput();
							if(parentContainer.size() == 1){
								losc.setVisibleLastOne();
							}
						}
					}
					target.addComponent(rewriteWrapper);
				}
				
				private static final long serialVersionUID = 1L;
			};
			removeLink.setOutputMarkupPlaceholderTag(true);
			removeLink.setDefaultFormProcessing(false);
			add(removeLink);
			add(urlContainer);
			add(protocolContainer);
			add(nameContainer);
			add(passwordContainer);
			add(confirmContainer);
		}
		
		public void clearVisible(){
			removeLink.setVisible(true);
			removeLink.setEnabled(true);
		}
		
		public void setModelFromInput(){
			Iterator<? extends Component> ite= iterator();
			while(ite.hasNext()){
				Component child = ite.next();
				if(child instanceof SwitchableContainer){
					SwitchableContainer<FormComponent<?>, Label> sc = (SwitchableContainer<FormComponent<?>, Label>)child;
					FormComponent<?> c = sc.getFirstComponent();
					if(c instanceof TextField){
						String modelObject;
						if(sc.getFirstComponent().getModelObject() == null || sc.getFirstComponent().getModelObject().equals("")){
							modelObject = c.getInput();
						}else{
							modelObject = c.getModelObject().toString();
						}
						((FormComponent<String>)c).setModelObject(modelObject);
					} else if(c instanceof ProtocolDropDownChoice){
						ProtocolDropDownChoice p = (ProtocolDropDownChoice)c;
						p.setModelById(c.getInput());
					}
				}
			}
		}

		public ServiceEndpointModel getEndpoint(){
			if(isAdded){
			   ServiceEndpointModel ep = (ServiceEndpointModel)getDefaultModelObject();
				if(ep.getAuthPassword() == null){
					ep.setAuthPassword("");
				}
				if(ep.getAuthUserName() == null){
					ep.setAuthUserName("");
				}
				return ep;
			}
			return null;
		}
		
		public void setVisibleLastOne(){
			removeLink.setVisible(!removeLink.isVisible());
			removeLink.setEnabled(!removeLink.isEnabled());
		}
		
		private boolean isAdded;
		private AjaxNonSubmitLink removeLink;
		private WebMarkupContainer parentContainer;
		private ProtocolDropDownChoice protocolChoice;
		private static final long serialVersionUID = 1L;
	}
	
	private static final long serialVersionUID = 1L;
}