/*
 * $Id: RequiredServiceNameField.java 10124 2010-01-13 10:10:17Z Masaaki Kamiya $
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
package jp.go.nict.langrid.management.web.view.page.node.component.text;

import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumSpaceValidator;
import jp.go.nict.langrid.management.web.view.component.validator.LengthValidator;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: Masaaki Kamiya $
 * @version $Revision$
 */
public class RequiredNodeNameField extends RequiredTextField<String>{
	public RequiredNodeNameField (String componentId) {
		super(componentId);
		addValidator();
	}
	
	/**
	 * 
	 * 
	 */
	public RequiredNodeNameField(String componentId, IModel<String> model){
		super(componentId, model);
		addValidator();
	}
	
	private void addValidator(){
		add(new LengthValidator(1, 254));
		add(new HalfSizeAlphaNumeralSiglumSpaceValidator());
	}

}
