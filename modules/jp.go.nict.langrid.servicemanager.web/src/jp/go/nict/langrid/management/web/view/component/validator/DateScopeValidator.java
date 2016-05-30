/*
 * $Id: DateScopeValidator.java 406 2011-08-25 02:12:29Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.component.validator;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.text.RequiredDateTextField;
import jp.go.nict.langrid.management.web.view.component.text.RequiredFromDateTextField;

import org.apache.commons.lang.time.DateUtils;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.DateValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class DateScopeValidator extends DateValidator{
	/**
	 * 
	 * 
	 */
	public DateScopeValidator(RequiredDateTextField dateTextField){
		this.dateTextField = dateTextField;
	}
	
	/**
	 * 
	 * 
	 */
	public DateScopeValidator(RequiredFromDateTextField dateTextField){
		this.dateTextField = dateTextField;
	}

	@Override
	protected void onValidate(IValidatable<Date> arg0){
		Date fromDate = null;
		if(dateTextField.getInput().length() == 0){
			return;
		}
		try{
			fromDate = DateUtil.parseDateTextWithSlash(dateTextField.getInput());
		}catch(ParseException e){
			LogWriter.writeError("Validator", e, DateScopeValidator.class);
			Map<String, String> param = new HashMap<String, String>();
			param.put("$label", MessageManager.getMessage("startDate", dateTextField.getLocale()));
			dateTextField.error(MessageManager.getMessage("message.error.date.InValid", param));
			return;
		}
		if(DateUtils.isSameDay(fromDate, arg0.getValue())){
			return;
		}
		if(fromDate.after(arg0.getValue())){
			error(arg0);
		}
	}

	private RequiredDateTextField dateTextField;
	private static final long serialVersionUID = 1L;
}
