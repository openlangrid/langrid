package jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.ServiceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.ServiceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.TypeUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.container.SwitchableContainer;
import jp.go.nict.langrid.management.web.view.page.language.component.form.panel.RepeatingLanguagePathPanel;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.text.HowToGetMembershipField;
import jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.choice.AtomicServiceTypeDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.ServiceTypeDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.service.component.text.RequiredServiceDescriptionField;
import jp.go.nict.langrid.management.web.view.page.language.service.component.text.RequiredServiceNameField;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.component.choice.CompositeServiceTypeDropDownChoice;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1521 $
 */
public class ServiceProfileFieldPanel extends Panel {
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public ServiceProfileFieldPanel(String gridId, String componentId, Form parentForm, InstanceType type)
	throws ServiceManagerException
	{
		super(componentId);
		setRenderBodyOnly(true);
		form = parentForm;
		add(new RequiredServiceNameField("serviceName", serviceName = new Model<String>()));
		add(new RequiredServiceDescriptionField("serviceDescription",
			serviceDescription = new Model<String>()));
		if(type.equals(InstanceType.EXTERNAL)) {
			serviceType = new AtomicServiceTypeDropDownChoice("serviceType");
		} else {
			serviceType = new CompositeServiceTypeDropDownChoice("serviceType");
		}
		serviceType.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				for(IFormValidator fv : getValidatorList()) {
					form.remove(fv);
				}
				pathPanel.setPathType(serviceType.getModelObject().getCurrentPathType());
				Collection<ServiceMetaAttributeModel> list = serviceType.getModelObject().getMetaAttrbuteList();
				if(list == null || list.isEmpty()){
					ServiceTypeModel model = serviceType.getModelObject();
					pathPanel.initialize(model.getDomainId() + "_" + model.getTypeId() + "_SupportedLanguages");
				} else {
					pathPanel.initialize(list.iterator().next().getAttributeId());
				}
				
//				pathPanel.initialize();
				for(IFormValidator fv : getValidatorList()) {
					form.add(fv);
				}
				target.addComponent(pathPanel);
			}

			private static final long serialVersionUID = 1L;
		});
		add(typeContainer = new SwitchableContainer<ServiceTypeDropDownChoice, Label>(
				"switchable", serviceType, typeLabel = new Label("serviceTypeLabel",
					"Other")));
		membership = new WebMarkupContainer("membershipContainer");
		membership.add(new HowToGetMembershipField("howToGetMembershipInfo"
			, howToGetMembershipInfo = new Model<String>()));
		membership.setOutputMarkupId(true);
		membership.setOutputMarkupPlaceholderTag(true);
		membership.setVisible(false);
		add(membership);
		Radio<Boolean> all = new Radio<Boolean>("all", new Model<Boolean>(false));
		all.add(new AjaxEventBehavior("onclick") {
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				membership.setVisible(false);
				target.addComponent(membership);
			}

			private static final long serialVersionUID = 1L;
		});
		all.setLabel(new Model<String>(
			MessageManager.getMessage(
					"ProvidingServices.language.service.label.accesible.AllUsers",
				getLocale())));
		Radio<Boolean> members = new Radio<Boolean>("members", new Model<Boolean>(true));
		members.add(new AjaxEventBehavior("onclick") {
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				membership.setVisible(true);
				target.addComponent(membership);
			}

			private static final long serialVersionUID = 1L;
		});
		members.setLabel(new Model<String>(MessageManager.getMessage(
					"ProvidingServices.language.service.label.accesible.MembersOnly",
			getLocale())));
		membersOnlyRadio = new RadioGroup<Boolean>("isMembers",
			isMembers = new Model<Boolean>(false));
		membersOnlyRadio.add(all);
		membersOnlyRadio.add(members);
		add(membersOnlyRadio);
		if(serviceType.getModelCount() != 0) {
			Collection<ServiceMetaAttributeModel> list = serviceType.getDefaultServiceType().getMetaAttrbuteList();
			if(list == null || list.isEmpty()){
				ServiceTypeModel model = serviceType.getDefaultServiceType();
				add(pathPanel = new RepeatingLanguagePathPanel("languagePath", serviceType.getDefaultServiceType()
					, model.getDomainId() + "_" + model.getTypeId() + "_SupportedLanguages"));
			} else {
				add(pathPanel = new RepeatingLanguagePathPanel("languagePath", serviceType.getDefaultServiceType()
					, list.iterator().next().getAttributeId()));
			}
		} else {
			add(new Label("languagePath", "-"));
			typeContainer.switchComponent();
		}
	}

	/**
	 * 
	 * 
	 */
	public void doSubmitProcess(ServiceModel obj) throws ServiceManagerException {
		obj.setServiceName(serviceName.getObject());
		obj.setServiceDescription(serviceDescription.getObject());
		obj.setHowToGetMembershipInfo(howToGetMembershipInfo.getObject());
		obj.setMembersOnly(isMembers.getObject());
		if(serviceType.isVisible() || isEdit) {
			if(serviceType.isVisible()) {
				obj.setServiceType(serviceType.getSelectedType());
			}
			if(pathPanel != null) {
				obj.setSupportedLanguagePathModel(pathPanel.getLanguagePathModel());
			}
		}
	}

	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public void setProfileInfo(ServiceModel obj, boolean isEdit) throws ServiceManagerException {
		serviceName.setObject(obj.getServiceName());
		serviceDescription.setObject(obj.getServiceDescription());
		if(serviceType.getModelCount() != 0) {
			serviceType.setModelObject(obj.getServiceType());
			if(isEdit) {
				typeLabel.setDefaultModelObject(obj.getServiceType().getTypeId());
				typeContainer.switchComponent();
			}
		}
		howToGetMembershipInfo.setObject(obj.getHowToGetMembershipInfo());
		isMembers.setObject(obj.isMembersOnly());
		membership.setVisible(obj.isMembersOnly());
		if(pathPanel != null && obj.getServiceType() != null) {
			pathPanel.setPathType(obj.getServiceType().getCurrentPathType());
			if( ! obj.getSupportedLanguagePathModel().isEmpty()) {
				Collection<ServiceMetaAttributeModel> list = obj.getServiceType().getMetaAttrbuteList();
				if(list == null || list.isEmpty()){
					ServiceTypeModel model = obj.getServiceType();
					pathPanel.setValueModel(obj.getSupportedLanguagePathModel()
						, model.getDomainId() + "_" + model.getTypeId() + "_SupportedLanguages");
				} else {
					pathPanel.setValueModel(obj.getSupportedLanguagePathModel()
						, list.iterator().next().getAttributeId());
				}
			}
		}
		this.isEdit = true;
	}

	public void setResourceInfo(ResourceModel model) throws ServiceManagerException {
		ServiceTypeModel serviceTypeModel = TypeUtil.getServiceTypeByResourceType(
			model.getGridId(), model.getResourceType().getDomainId(),
			model.getResourceType());
		if( ! serviceTypeModel.getTypeId().equals("Other")) {
			serviceType.setModelObject(serviceTypeModel);
			pathPanel.setPathType(serviceTypeModel.getCurrentPathType());
			if(!model.getSupportedLanguagePathModel().isEmpty()) {
				Collection<ResourceMetaAttributeModel> list = model.getResourceType().getMetaAttrbuteList();
				if(list == null || list.isEmpty()){
					ResourceTypeModel typeModel = model.getResourceType();
					pathPanel.setValueModel(model.getSupportedLanguagePathModel()
						, typeModel.getDomainId() + "_" + typeModel.getResourceTypeId() + "_SupportedLanguages");
				} else {
					pathPanel.setValueModel(model.getSupportedLanguagePathModel()
						, list.iterator().next().getAttributeId());
				}
//				pathPanel.setValueModel(model.getSupportedLanguagePathModel(), metaKey);
			}
		}
	}

	public List<IFormValidator> getValidatorList() {
		List<IFormValidator> list = new ArrayList<IFormValidator>();
		if(!typeLabel.getDefaultModelObjectAsString().equals("Other")
			&& pathPanel != null) {
			list.add(pathPanel.getValidatar());
		}
		return list;
	}

	private Model<String> serviceName;
	private Model<String> serviceDescription;
	private SwitchableContainer<ServiceTypeDropDownChoice, Label> typeContainer;
	private ServiceTypeDropDownChoice serviceType;
	private Label typeLabel;
	private WebMarkupContainer membership;
	private RadioGroup<Boolean> membersOnlyRadio;
	private Model<Boolean> isMembers;
	private Model<String> howToGetMembershipInfo;
	private RepeatingLanguagePathPanel pathPanel;
	private boolean isEdit = false;
	private Form form;
}
