/*
 * $Id: DatePickerTextField.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import java.util.Calendar;
import java.util.Date;

import jp.go.nict.langrid.management.web.utility.DateUtil;
import jp.go.nict.langrid.management.web.view.component.yui.StandardDatePicker;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.joda.time.format.DateTimeFormat;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class DatePickerTextField extends TextField<String>{
	/**
	 * 
	 * 
	 */
	public DatePickerTextField(String componentId, IModel<String> model){
		super(componentId, model);
		add(new StandardDatePicker(datePattern));
	}

	/**
	 * 
	 * 
	 */
	public DatePickerTextField(String componentId, IModel<String> model, String datePattern){
		super(componentId, model);
		add(new StandardDatePicker(datePattern));
		this.datePattern = datePattern;
	}
	
	/**
	 * 
	 * 
	 */
	public Date getFormattedValue(){
		return DateTimeFormat.forPattern(datePattern).withLocale(
				getLocale()).parseDateTime(getModelObject()).toDate();
	}

	/**
	 * 
	 * 
	 */
	public Date getFormattedInput(){
		return DateTimeFormat.forPattern(datePattern).withLocale(
				getLocale()).parseDateTime(getInput()).toDate();
	}
	
	public void setDefaultValue(Calendar value){
		setModelObject(DateUtil.formatYMDWithSlash(value.getTime()));
	}
	
	public Boolean isConvert(){
		try{
			String input = getModelObject();
			if(input == null || input.equals("")){
				input = getInput();
			}
			getFormattedInput();
			return true;
		}catch(IllegalArgumentException e){
			return false;
		}catch(UnsupportedOperationException e){
			return false;
		}
	}

	private String datePattern = DateUtil.STR_YMD_SLASH;
	private static final long serialVersionUID = 1L;
}
