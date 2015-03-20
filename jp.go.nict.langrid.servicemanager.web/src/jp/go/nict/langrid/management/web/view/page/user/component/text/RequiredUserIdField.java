/*
 * $Id: RequiredUserIdField.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.user.component.text;

import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumValidator;
import jp.go.nict.langrid.management.web.view.component.validator.LengthValidator;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RequiredUserIdField extends RequiredTextField<String>{
	/**
	 * 
	 * 
	 */
	public RequiredUserIdField(String componentId){
		super(componentId, new Model<String>());
		add(new LengthValidator(4, 254, "LanguageGridOperator.error.validate.format.Id"));
		add(new HalfSizeAlphaNumeralSiglumValidator("LanguageGridOperator.error.validate.format.Id"));
	}

	/**
	 * 
	 * 
	 */
	public RequiredUserIdField(String componentId, Model<String> model){
		super(componentId, model);
		add(new LengthValidator(4, 254, "LanguageGridOperator.error.validate.format.Id"));
		add(new HalfSizeAlphaNumeralSiglumValidator("LanguageGridOperator.error.validate.format.Id"));
	}

	private static final long serialVersionUID = 1L;
}
