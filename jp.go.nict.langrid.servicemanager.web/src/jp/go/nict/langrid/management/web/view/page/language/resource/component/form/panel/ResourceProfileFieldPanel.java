package jp.go.nict.langrid.management.web.view.page.language.resource.component.form.panel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.ResourceTypeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.component.container.SwitchableContainer;
import jp.go.nict.langrid.management.web.view.component.text.LicenseField;
import jp.go.nict.langrid.management.web.view.component.text.RequiredCopyrightField;
import jp.go.nict.langrid.management.web.view.page.language.component.form.panel.RepeatingLanguagePathPanel;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.choice.ResourceTypeDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.text.RequiredResourceNameField;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.text.ResourceDescriptionField;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ResourceProfileFieldPanel extends Panel {
	public ResourceProfileFieldPanel(String gridId, String componentId, Form parentForm)
	throws ServiceManagerException {
		super(componentId);
		setRenderBodyOnly(true);
		form = parentForm;
		add(new RequiredResourceNameField("resourceName",
			resourceName = new Model<String>()));
		add(new LicenseField("licenseInfo", licenseInfo = new Model<String>()));
		add(new ResourceDescriptionField("resourceDescription",
			resourceDescription = new Model<String>()));
		add(new RequiredCopyrightField("copyrightInfo",
			copyrightInfo = new Model<String>()));
		type = new ResourceTypeDropDownChoice(gridId, "resourceType");
		type.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				for(IFormValidator fv : getValidatorList()) {
					form.remove(fv);
				}

				pathPanel.setPathType(type.getModelObject().getCurrentPathType());
				
				Collection<ResourceMetaAttributeModel> list = type.getModelObject().getMetaAttrbuteList();
				if(list == null || list.isEmpty()){
					ResourceTypeModel model = type.getModelObject();
					pathPanel.initialize(model.getDomainId() + "_" + model.getResourceTypeId() + "_SupportedLanguages");
				} else {
					pathPanel.initialize(list.iterator().next().getAttributeId());
				}
				
				for(IFormValidator fv : getValidatorList()) {
					form.add(fv);
				}
				
				target.addComponent(pathPanel);
			}
		});
		add(typeContainer = new SwitchableContainer<ResourceTypeDropDownChoice, Label>(
				"switchable", type, typeLabel = new Label("resourceTypeLabel", "Other")));
		if(type.getModelCount() != 0) {
			Collection<ResourceMetaAttributeModel> list = type.getDefaultResourceType().getMetaAttrbuteList();
			if(list == null || list.isEmpty()){
				ResourceTypeModel model = type.getDefaultResourceType();
				add(pathPanel = new RepeatingLanguagePathPanel("languages", type.getDefaultResourceType()
					, model.getDomainId() + "_" + model.getResourceTypeId() + "_SupportedLanguages"));
			} else {
				add(pathPanel = new RepeatingLanguagePathPanel("languages", type.getDefaultResourceType()
					, list.iterator().next().getAttributeId()));
			}
		} else {
			add(new Label("languages", "-"));
			typeContainer.switchComponent();
		}
	}

	public void doSubmitProcess(ResourceModel obj) throws ServiceManagerException {
		obj.setResourceName(resourceName.getObject());
		if(type.isVisible() || isEdit) {
			if(type.isVisible()) {
				obj.setResourceType(type.getResourceType());
			}
			if(pathPanel != null){
				obj.setSupportedLanguagePathModel(pathPanel.getLanguagePathModel());
			}
		}
		obj.setResourceDescription(resourceDescription.getObject());
		obj.setCopyrightInfo(copyrightInfo.getObject());
		obj.setLicenseInfo(licenseInfo.getObject());
	}

	public List<IFormValidator> getValidatorList() {
		List<IFormValidator> list = new ArrayList<IFormValidator>();
		if(!typeLabel.getDefaultModelObjectAsString().equals("Other") && pathPanel != null) {
			list.add(pathPanel.getValidatar());
		}
		return list;
	}

	public void setProfileInfo(ResourceModel obj) throws ServiceManagerException {
		if(type.getModelCount() != 0) {
			typeLabel.setDefaultModelObject(obj.getResourceType().getResourceTypeId());
			typeContainer.switchComponent();
		}
		if(pathPanel != null) {
			pathPanel.setPathType(obj.getResourceType().getCurrentPathType());
			if(!obj.getSupportedLanguagePathModel().isEmpty()) {
				Collection<ResourceMetaAttributeModel> list = obj.getResourceType().getMetaAttrbuteList();
				if(list == null || list.isEmpty()){
					ResourceTypeModel model = type.getModelObject();
					pathPanel.setValueModel(obj.getSupportedLanguagePathModel()
						, model.getDomainId() + "_" + model.getResourceTypeId() + "_SupportedLanguages");
				} else {
					pathPanel.setValueModel(obj.getSupportedLanguagePathModel(), list.iterator().next().getAttributeId());
				}
			}
		}
		resourceName.setObject(obj.getResourceName());
		licenseInfo.setObject(obj.getLicenseInfo());
		resourceDescription.setObject(obj.getResourceDescription());
		copyrightInfo.setObject(obj.getCopyrightInfo());
		this.isEdit = true;
	}

	private Form form;
	// basic profile
	private Model<String> resourceName;
	private Model<String> resourceDescription;
	private Model<String> copyrightInfo;
	private Model<String> licenseInfo;
	// language path
	private RepeatingLanguagePathPanel pathPanel;
	// resource type
	private SwitchableContainer<ResourceTypeDropDownChoice, Label> typeContainer;
	private ResourceTypeDropDownChoice type;
	private Label typeLabel;
	private boolean isEdit = false;
}
