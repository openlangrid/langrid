package jp.go.nict.langrid.management.web.view.page.user.admin.component.form;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.UserRole;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.UserService;
import jp.go.nict.langrid.management.web.view.page.user.component.form.EditUserProfileForm;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.PropertyModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public abstract class EditUserProfileAdminForm extends EditUserProfileForm {
	/**
	 * 
	 * 
	 */
	public EditUserProfileAdminForm(String formId, String userId, String gridId, UserModel profile) {
		super(formId, userId, gridId, profile);
	}

	private boolean providerRole;
	private boolean userRole;

	@Override
	protected void addComponents(UserModel model) throws ServiceManagerException {
		if(model == null) {
			model = new UserModel();
		}
		super.addComponents(model);

		
		@SuppressWarnings("unchecked")
		Set<UserRole> roles = Collections.EMPTY_SET;
		if(model.getUserId() != null){
			roles = ServiceFactory.getInstance().getUserService(model.getGridId())
					.getUserRoles(model.getUserId());
		}
		providerRole = roles.contains(UserRole.SERVICEPROVIDER);
		userRole = roles.contains(UserRole.SERVICEUSER);
		add(new CheckBox("providerRole", new PropertyModel<Boolean>(this, "providerRole")));
		add(new CheckBox("userRole", new PropertyModel<Boolean>(this, "userRole")));
		this.remove("submit");
		add(new Button("submit") {
			@Override
			public void onSubmit() {
				try {
					UserModel model = getProfile();
					UserService us = ServiceFactory.getInstance().getUserService(model.getGridId());
					updateUserModel(model);
					us.edit(model);
					Set<UserRole> roles = new HashSet<UserRole>();
					if(providerRole) roles.add(UserRole.SERVICEPROVIDER);
					if(userRole) roles.add(UserRole.SERVICEUSER);
					us.updateUserRoles(model.getUserId(), roles);
					LogWriter.writeInfo(getSessionUserId(), "\"" + model.getUserId()
							+ "\" of langrid user has been edited.", getPage().getClass());
				} catch(ServiceManagerException e) {
					raisedException = e;
				} catch(MalformedURLException e) {
					raisedException = new ServiceManagerException(e);
				}
			}

			private static final long serialVersionUID = 1L;
		});
	}
}
