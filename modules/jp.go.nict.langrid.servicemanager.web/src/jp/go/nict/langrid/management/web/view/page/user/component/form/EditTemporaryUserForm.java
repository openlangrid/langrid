package jp.go.nict.langrid.management.web.view.page.user.component.form;

import java.util.Calendar;
import java.util.Date;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.dao.UserAlreadyExistsException;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.TemporaryUserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.TemporaryUserService;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.text.RequiredDateTextField;
import jp.go.nict.langrid.management.web.view.component.text.RequiredPasswordTextField;
import jp.go.nict.langrid.management.web.view.component.validator.DateScopeValidator;
import jp.go.nict.langrid.management.web.view.component.validator.FutureDateValidator;
import jp.go.nict.langrid.management.web.view.page.user.RegistrationOfTemporaryUserResultPage;
import jp.go.nict.langrid.management.web.view.page.user.YourTemporaryUsersPage;
import jp.go.nict.langrid.management.web.view.page.user.component.form.validator.FormRegistUserValidator;
import jp.go.nict.langrid.management.web.view.page.user.component.text.RequiredUserIdField;
import jp.go.nict.langrid.management.web.view.page.user.component.validator.TemporaryUserIdAlreadyExistsValidator;
import jp.go.nict.langrid.service_1_2.foundation.usermanagement.TemporaryUserEntry;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class EditTemporaryUserForm extends AbstractForm<TemporaryUserModel>{
	/**
	 * 
	 * 
	 */
	public EditTemporaryUserForm(String formId, String gridId, TemporaryUserModel entry){
		super(formId, entry);
	}
	
	@Override
	protected void addComponents(TemporaryUserModel initialParameter){
		entry = initialParameter;
		add(new Label("userId", entry.getUserId()));
		add(start = new RequiredDateTextField("startDate"
				, new Model<Date>(entry.getBeginAvailableDateTime().getTime())));
		start.add(new FutureDateValidator());
		add(end = new RequiredDateTextField("endDate"
				, new Model<Date>(entry.getEndAvailableDateTime().getTime())));
		end.add(new FutureDateValidator());
		add(new Label("timezone", DateUtil.defaultTimeZone()));
		end.add(new DateScopeValidator(start));
		add(new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(new YourTemporaryUsersPage());
			}

			private static final long serialVersionUID = 1L;
		});
	}

	@Override
	protected void doErrorProcess(ServiceManagerException e){
		if(e.getCause().getClass().equals(UserAlreadyExistsException.class)){
			setIsValidateError(true);
			setValidateErrorMessage(MessageManager.getMessage(
					"LanguageGrid.error.UserAlreadyExists", getLocale()));
		}
		super.doErrorProcess(e);
	}
	
	@Override
	protected void onSubmit(){
		try{
			TemporaryUserService service = ServiceFactory.getInstance().getTemporaryUserService(entry.getGridId());
			Calendar startTime = Calendar.getInstance();
			startTime.setTime(start.getModelObject());
			Calendar endTime = Calendar.getInstance();
			endTime.setTime(end.getModelObject());
			endTime = CalendarUtil.createEndingOfDay(endTime);
			entry.setBeginAvailableDateTime(startTime);
			entry.setEndAvailableDateTime(endTime);
			service.edit(entry);
			LogWriter.writeInfo(getSessionUserId()
					, "\"" + entry.getUserId() + "\" of temporary user has been edited."
					, getPage().getClass()
			);
		}catch(ServiceManagerException e){
			raisedException = e;
		}
	}
	
	@Override
	protected void setResultPage(TemporaryUserModel resultParameter){
		setResponsePage(new RegistrationOfTemporaryUserResultPage(entry.getUserId()));
	}

	private TemporaryUserModel entry;
	private RequiredDateTextField start;
	private RequiredDateTextField end;
}
