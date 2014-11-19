/*
 * $Id: RequiredDateTextField.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.component.text;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.converter.StandardStyleDateConverter;
import jp.go.nict.langrid.management.web.view.component.yui.StandardDatePicker;

import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RequiredDateTextField extends DateTextField{
	/**
	 * 
	 * 
	 */
	public RequiredDateTextField(String componentId, IModel<Date> model){
		super(componentId, model, new StandardStyleDateConverter(datePattern));
		add(new StandardDatePicker(datePattern));
		setRequired(true);
	}

	/**
	 * 
	 * 
	 */
	public String getFormatedString(){
		return DateUtil.formatYMDWithSlashLocale(getModelObject());
	}

	/**
	 * 
	 * 
	 */
	public String getInvalidErrorMessage(){
		return invalidErrorMessage;
	}

	/**
	 * 
	 * 
	 */
	public String getRequireErrorMessage(){
		return requireErrorMessage;
	}

	/**
	 * 
	 * 
	 */
	public void setInvalidErrorMessage(String invalidErrorMessage){
		this.invalidErrorMessage = invalidErrorMessage;
	}

	/**
	 * 
	 * 
	 */
	public void setRequireErrorMessage(String requireErrorMessage){
		this.requireErrorMessage = requireErrorMessage;
	}

	@Override
	protected void convertInput(){
		IConverter converter = getConverter(getType());
		try{
			converter.convertToObject(getInput(), getLocale());
		}catch(Exception e){
			Map<String, String> map = new HashMap<String, String>();
			map.put("label", getInput());
			raisedErrorMessage = MessageManager.getMessage(invalidErrorMessage, map);
			error((IValidationError)new ValidationError().setMessage(raisedErrorMessage));
			return;
		}
		super.convertInput();
	}
	
	public String getErrorMessage(){
		return raisedErrorMessage;
	}
	
	private String raisedErrorMessage = "";
	private String invalidErrorMessage = "message.error.date.InValid";
	private String requireErrorMessage = "message.error.Required";
	private static final String datePattern = DateUtil.STR_YMD_SLASH;
	private static final long serialVersionUID = 1L;
}
