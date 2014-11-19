package jp.go.nict.langrid.management.web.view.component.panel;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.commons.util.CalendarUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.text.DatePickerTextField;

import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class DefaultedDatePeriodFieldPanel extends Panel{
	public DefaultedDatePeriodFieldPanel(String id, boolean isDefault){
		super(id);
		radioGroup = new RadioGroup<Boolean>("radioGroup", new Model<Boolean>());
		Model<Boolean> defaultModel = new Model<Boolean>(true);
		Model<Boolean> manualModel = new Model<Boolean>(false);
		Radio<Boolean> defaultDate = new Radio<Boolean>("default", defaultModel);
		Radio<Boolean> manualDate = new Radio<Boolean>("manual", manualModel);
		if(isDefault){
			radioGroup.setModel(defaultModel);
		}else{
			radioGroup.setModel(manualModel);
		}
		radioGroup.add(defaultDate);
		radioGroup.add(manualDate);
		radioGroup.add(start = new DatePickerTextField("startDate", new Model<String>()));
		radioGroup.add(end = new DatePickerTextField("endDate", new Model<String>()));
		add(radioGroup);
		radioGroup.add(new DefaultDatePeriodFieldValidator(start, end));
		Calendar now = Calendar.getInstance();
		start.setDefaultValue(now);
		end.setDefaultValue(now);
	}
	
	/**
	 * 
	 * 
	 */
	public Date getBeginDate(){
		if(radioGroup.getModelObject()){
			return Calendar.getInstance().getTime();
		}
		return start.getFormattedValue();
	}
	
	/**
	 * 
	 * 
	 */
	public Date getEndDate(){
		if(radioGroup.getModelObject()){
			Calendar end = Calendar.getInstance();
			end.add(Calendar.DATE, 1);
			return end.getTime();
		}
		return end.getFormattedValue();
	}
	
	/**
	 * 
	 * 
	 */
	public boolean isDefault(){
		return radioGroup.getModelObject();
	}
	
	private class DefaultDatePeriodFieldValidator extends AbstractValidator<Boolean>{
		public DefaultDatePeriodFieldValidator(DatePickerTextField start, DatePickerTextField end){
			this.start = start;
			this.end = end;
		}
		@Override
		protected void onValidate(IValidatable<Boolean> arg0){
			if(!arg0.getValue()){
				long now = CalendarUtil.createBeginningOfDay(Calendar.getInstance()).getTimeInMillis();
				Map<String, String> param = new HashMap<String, String >();
				param.put("label", MessageManager.getMessage(start.getId(), getLocale()));
				if(start.getInput() == null || start.getInput().equals("")){
					start.error(MessageManager.getMessage("message.error.Required", param));
				}
				if(!start.hasErrorMessage() && !start.isConvert()){
					start.error(MessageManager.getMessage("message.error.date.InValid", param));
				}
				Date startDate = null;
				if(!start.hasErrorMessage()){
					startDate = start.getFormattedInput();
					if(startDate.getTime() < now){
						start.error(MessageManager.getMessage("FutureDateValidator", param));
					}
				}
				
				param.put("label", MessageManager.getMessage(end.getId(), getLocale()));
				if(end.getInput() == null || end.getInput().equals("")){
					end.error(MessageManager.getMessage("message.error.Required", param));
				}
				if(!end.hasErrorMessage() && !end.isConvert()){
					end.error(MessageManager.getMessage("message.error.date.InValid", param));
				}
				Date endDate = null;
				if(!end.hasErrorMessage()){
					endDate = end.getFormattedInput();
					if(endDate.getTime() < now){
						end.error(MessageManager.getMessage("FutureDateValidator", param));
					}
				}

				if((startDate != null && endDate != null) && (startDate.getTime() > endDate.getTime())){
					start.error(MessageManager.getMessage("DateScopeValidator", getLocale()));
				}
			}
		}

		private DatePickerTextField start;
		private DatePickerTextField end;

		private static final long serialVersionUID = 1L;
	}
	
	private RadioGroup<Boolean> radioGroup; 
	private DatePickerTextField start;
	private DatePickerTextField end;
	private static final long serialVersionUID = 1L;
}
