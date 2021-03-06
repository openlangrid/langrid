package jp.go.nict.langrid.management.web.view.page.other.component.form;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import jp.go.nict.langrid.dao.entity.AppProvisionType;
import jp.go.nict.langrid.dao.entity.EmbeddableStringValueClass;
import jp.go.nict.langrid.dao.entity.UseType;
import jp.go.nict.langrid.dao.entity.UserAttribute;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.UserRole;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.UserService;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.RequiredPasswordTextField;
import jp.go.nict.langrid.management.web.view.component.text.RequiredRepresentativeTextField;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.ProvisionControlDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.service.component.choice.UsingServiceDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.other.SignupPage;
import jp.go.nict.langrid.management.web.view.page.other.UserSignupResultPage;
import jp.go.nict.langrid.management.web.view.page.user.component.form.validator.FormRegistUserValidator;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredAddressTextArea;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredEMailAddressField;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredHomepageField;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredOrganizationNameTextArea;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredUserIdField;
import jp.go.nict.langrid.management.web.view.page.user.component.text.TermofUseField;
import jp.go.nict.langrid.management.web.view.page.user.component.validator.TermofUserCheckedValidator;
import jp.go.nict.langrid.management.web.view.page.user.component.validator.UserIdAlreadyExistsValidator;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @author Trang
 * @version $Revision: 406 $
 */
public abstract class RegisterUserForm extends AbstractForm<String> {
	/**
	 * 
	 * 
	 */
	public RegisterUserForm(String formId, String gridId) {
		super(formId, gridId);
		this.gridId = gridId;
	}

	@Override
	protected void addComponents(String initialParameter)
			throws ServiceManagerException {

		add(userId = new RequiredUserIdField("userId", new Model<String>()));
		userId.add(new UserIdAlreadyExistsValidator(initialParameter, userId));
		add(password = new RequiredPasswordTextField("password",
				new Model<String>()));
		add(confirm = new RequiredPasswordTextField("confirm",
				new Model<String>()));
		add(organization = new RequiredOrganizationNameTextArea("organization",
				new Model<String>()));
		add(representative = new RequiredRepresentativeTextField(
				"representative", new Model<String>()));
		add(address = new RequiredAddressTextArea("address",
				new Model<String>()));
		add(email = new RequiredEMailAddressField("emailAddress",
				new Model<String>()));
		add(homepage = new RequiredHomepageField("homepageUrl",
				new Model<String>()));
		add(provision = new ProvisionControlDropDownChoice("provision",
				AppProvisionType.CLIENT_CONTROL.name()));
		add(useService = new UsingServiceDropDownChoice("useService",
				UseType.NONPROFIT_USE.name()));
		
		
		try {
			String termofuse = loadTermofUser();
			Label divTermofUse = new Label("divTermofUse", termofuse);
			divTermofUse.setEscapeModelStrings(false);
			add(divTermofUse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		add(checkterm = new CheckBox("checkterm", new Model<Boolean>()));
		checkterm.add(new TermofUserCheckedValidator(checkterm));
		add(new FormRegistUserValidator(password, confirm));
		
		add(new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(getCancelPage());
			}

			private static final long serialVersionUID = 1L;
		}.setVisible(hasCancel()));
	}

	@Override
	protected void onSubmit() {
		if(!MessageUtil.isOpenLangrid()){
			setResponsePage(getCancelPage());
		}
		try {
			UserModel um = new UserModel();
			um.setHomepageUrl(new EmbeddableStringValueClass<URL>(
					new URL(homepage.getModelObject())));
			um.setUserId(userId.getModelObject());
			um.setPassword(password.getModelObject());
			um.setAddress(address.getModelObject());
			um.setEmailAddress(email.getModelObject());
			um.setOrganization(organization.getModelObject());
			um.setRepresentative(representative.getModelObject());
			um.setAbleToCallServices(true);
			um.setGridId(gridId);
			Calendar now = Calendar.getInstance();
			um.setCreatedDateTime(now);
			um.setUpdatedDateTime(now);
			um.setAttributes(new HashMap<String, UserAttribute>());
			um.setDefaultAppProvisionType(AppProvisionType.CLIENT_CONTROL
					.name());
			um.setDefaultUseType(UseType.NONPROFIT_USE.name());

			UserService us = ServiceFactory.getInstance().getUserService(gridId);
			us.add(um);
			us.updateUserRoles(um.getUserId(), Arrays.asList(UserRole.SERVICEUSER));
			LogWriter
					.writeInfo(
							getSessionUserId(),
							"\""
									+ userId.getModelObject()
									+ "\" of langrid user has been registered.",
							getClass());
		} catch (ServiceManagerException e) {
			raisedException = e;
		} catch (MalformedURLException e) {
			raisedException = new ServiceManagerException(e,
					this.getClass());
		}
	}

	@Override
	protected void setResultPage(String resultParameter) {
		setResponsePage(new UserSignupResultPage(resultParameter));
	}
	
	@Override
	protected String getResultParameter() {
	   return userId.getModelObject();
	}
	
	protected Page getCancelPage() {
		return new SignupPage();
	}
	
	
	
	private String loadTermofUser() throws IOException {
		// todo: Load term of use from a property file or text file
		/*String termofusefile = "termofuse.html";
		new ClassPathResource("/termofuse.html");
		BufferedReader reader = new BufferedReader(new FileReader(termofusefile));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		
		while((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		
		return stringBuilder.toString();*/
		
		String termofuse = IOUtils.toString(new ClassPathResource("/termofuse.html").getInputStream());
		return termofuse;
	}

	private ProvisionControlDropDownChoice provision;
	private UsingServiceDropDownChoice useService;
	private CheckBox checkterm;

	protected boolean hasCancel() {
		return false;
	}

	private RequiredPasswordTextField confirm;
	private RequiredPasswordTextField password;

	private String gridId;
	private RequiredUserIdField userId;
	private RequiredOrganizationNameTextArea organization;
	private RequiredRepresentativeTextField representative;
	private RequiredAddressTextArea address;
	private RequiredEMailAddressField email;
	private RequiredHomepageField homepage;
	private TermofUseField term;
}
