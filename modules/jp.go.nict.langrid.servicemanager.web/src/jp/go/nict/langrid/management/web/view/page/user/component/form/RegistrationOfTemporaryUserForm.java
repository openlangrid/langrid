package jp.go.nict.langrid.management.web.view.page.user.component.form;

import java.util.Calendar;
import java.util.HashMap;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.TemporaryUserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.TemporaryUserService;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.panel.DefaultedDatePeriodFieldPanel;
import jp.go.nict.langrid.management.web.view.component.text.RequiredPasswordTextField;
import jp.go.nict.langrid.management.web.view.page.user.RegistrationOfTemporaryUserResultPage;
import jp.go.nict.langrid.management.web.view.page.user.component.form.validator.FormRegistUserValidator;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredUserIdField;
import jp.go.nict.langrid.management.web.view.page.user.component.validator.TemporaryUserIdAlreadyExistsValidator;

import org.apache.wicket.markup.html.basic.Label;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RegistrationOfTemporaryUserForm extends AbstractForm<HashMap<String, String>>{
	/**
	 * 
	 * 
	 */
	public RegistrationOfTemporaryUserForm(String gridId, String formId){
		super(formId);
		this.gridId = gridId;
		userId.add(new TemporaryUserIdAlreadyExistsValidator(gridId, userId));
	}
	
	@Override
	protected void addComponents(HashMap<String, String> initialParameter){
		RequiredPasswordTextField confirm;
		add(userId = new RequiredUserIdField("userId"));
		add(password = new RequiredPasswordTextField("password"));
		add(period = new DefaultedDatePeriodFieldPanel("period", true));
		period.setRenderBodyOnly(true);
		add(confirm = new RequiredPasswordTextField("confirm"));
		add(new FormRegistUserValidator(password, confirm));
		add(new Label("timezone", DateUtil.defaultTimeZone()));
	}

	@Override
	protected void onSubmit(){
		try{
			Calendar startTime = Calendar.getInstance();
			Calendar endTime = Calendar.getInstance();
			startTime.setTime(period.getBeginDate());
			endTime.setTime(period.getEndDate());
			if(!period.isDefault()){
				endTime = CalendarUtil.createEndingOfDay(endTime);
			}
			TemporaryUserService service = ServiceFactory.getInstance().getTemporaryUserService(gridId);
			TemporaryUserModel tum = new TemporaryUserModel();
			tum.setUserId(userId.getModelObject());
			tum.setPassword(password.getModelObject());
			tum.setBeginAvailableDateTime(startTime);
			tum.setEndAvailableDateTime(endTime);
			tum.setGridId(gridId);
			tum.setParentUserId(getSessionUserId());
			service.add(tum);
			LogWriter.writeInfo(getSessionUserId()
					, "\"" + userId.getModelObject() + "\" of temporary user has been registered."
					, getPage().getClass()
			);
		}catch(ServiceManagerException e){
			raisedException = e;
		}
	}
	
	@Override
	protected void setResultPage(HashMap<String, String> resultParameter){
		setResponsePage(new RegistrationOfTemporaryUserResultPage(userId.getModelObject()));
	}
	
	private String gridId;
	private RequiredPasswordTextField password;
	private RequiredUserIdField userId;
	private DefaultedDatePeriodFieldPanel period;
}
