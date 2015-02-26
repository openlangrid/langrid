package jp.go.nict.langrid.management.web.view.page.user.component.form;

import java.net.MalformedURLException;
import java.net.URL;

import jp.go.nict.langrid.dao.entity.AppProvisionType;
import jp.go.nict.langrid.dao.entity.EmbeddableStringValueClass;
import jp.go.nict.langrid.dao.entity.UseType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.RequiredRepresentativeTextField;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.ProvisionControlDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.UsingServiceDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.user.EditUserProfilePage;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredAddressTextArea;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredEMailAddressField;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredHomepageField;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredOrganizationNameTextArea;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public abstract class EditUserProfileForm extends AbstractForm<UserModel> {
	/**
	 * 
	 * 
	 */
	public EditUserProfileForm(String formId, String userId, String gridId, UserModel profile) {
		super(formId, profile);
		this.userId = userId;
		this.gridId = gridId;
	}

	/**
	 * 
	 * 
	 */
	public UserModel getProfile() {
		return profile;
	}

	protected void updateUserModel(UserModel profile)
	throws MalformedURLException{
		if(provision.getModelObject() != null){
			profile.setDefaultAppProvisionType(provision.getModelObject().name());
		}else{
			profile.setDefaultAppProvisionType(AppProvisionType.CLIENT_CONTROL.name());
		}

		if(useService.getModelObject() != null){
			profile.setDefaultUseType(useService.getModelObject().name());
		}else{
			profile.setDefaultUseType(UseType.NONPROFIT_USE.name());
		}
		profile.setHomepageUrl(new EmbeddableStringValueClass<URL>(new URL(homepage.getValue())));
	}

	@Override
	protected void addComponents(UserModel initialParameter) throws ServiceManagerException {
		if(initialParameter == null) {
			profile = new UserModel();
		} else {
			profile = initialParameter;
		}
		add(new RequiredOrganizationNameTextArea("organization",
				new PropertyModel<String>(profile, "organization")));
		add(new RequiredRepresentativeTextField("representative",
				new PropertyModel<String>(profile, "representative")));
		add(new RequiredAddressTextArea("address", new PropertyModel<String>(profile,
				"address")));
		add(new RequiredEMailAddressField("emailAddress"
				, new PropertyModel<String>(profile, "emailAddress")));
		add(homepage = new RequiredHomepageField("homepageUrl"
				, new Model<String>(profile.getHomepageUrl() == null ? "" : profile.getHomepageUrl() == null ? "" : profile.getHomepageUrl().getValue().toString())));
		add(provision = new ProvisionControlDropDownChoice("provision", profile.getDefaultAppProvisionType()));
		add(useService = new UsingServiceDropDownChoice("useService", profile.getDefaultUseType()));

		add(new Button("submit") {
			@Override
			public void onSubmit() {
				try {
					updateUserModel(profile);
					ServiceFactory.getInstance().getUserService(profile.getGridId()).edit(profile);
					LogWriter.writeInfo(getSessionUserId(), "\"" + userId
							+ "\" of langrid user has been edited.", getPage().getClass());
				} catch(ServiceManagerException e) {
					raisedException = e;
				} catch(MalformedURLException e) {
					raisedException = new ServiceManagerException(e);
				}
			}

			private static final long serialVersionUID = 1L;
		});
		add(new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(getCancelPageClass());
			}

			private static final long serialVersionUID = 1L;
		}.setVisible(hasCancel()));
	}

	private RequiredHomepageField homepage;
	private ProvisionControlDropDownChoice provision;
	private UsingServiceDropDownChoice useService;


	protected boolean hasCancel() {
		return false;
	}

	protected Page getCancelPageClass() {
		return new EditUserProfilePage();
	}

	@Override
	protected UserModel getResultParameter() {
		return profile;
	}

	private String gridId;
	private String userId;
	private UserModel profile;
}
