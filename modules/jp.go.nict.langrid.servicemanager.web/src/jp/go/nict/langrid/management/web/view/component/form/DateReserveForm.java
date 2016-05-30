package jp.go.nict.langrid.management.web.view.component.form;

import java.util.Date;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.view.component.text.RequiredDateTextField;
import jp.go.nict.langrid.management.web.view.component.validator.FutureDateValidator;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public abstract class DateReserveForm extends AbstractForm<Date>{
	/**
	 * 
	 * 
	 */
	public DateReserveForm(String componentId, Date date){
		super(componentId, date);
	}

	@Override
	protected void addComponents(Date initialParameter) throws ServiceManagerException{
		dateField = new RequiredDateTextField("dateTextField", new Model<Date>());
		dateField.add(new FutureDateValidator());
		add(dateField);
		add(new Link("cancel"){
			@Override
			public void onClick(){
				setCancelResponsePage();
			}

			private static final long serialVersionUID = 1L;
		});
		add(new Button("submit"){
			@Override
			public void onSubmit(){
				doSubmitProcess();
			}
			
			@Override
			protected String getOnClickScript(){
				return "return confirm('" + getConfirmMessage() + "')";
			}
			
			private static final long serialVersionUID = 1L;
		});
		dateField.setModelObject(initialParameter);
	}
	
	protected abstract String getConfirmMessage();
	
	protected abstract void doSubmitProcess();

	protected Date getDate(){
		return dateField.getModelObject();
	}
	
	protected String getFormatedDateString(){
		return DateUtil.formatYMDWithSlashLocale(dateField.getModelObject());
	}

	protected abstract void setCancelResponsePage();
	protected Date date;
	private RequiredDateTextField dateField;
	private static final long serialVersionUID = 1L;
}
