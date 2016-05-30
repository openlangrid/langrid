/*
 * $Id: CPUField.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeNumeralForCpuValidator;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class CPUField extends TextField<String> {
	public CPUField(String componentId) {
		super(componentId);
		addValidator();
	}

	/**
	 * 
	 * 
	 */
	public CPUField(String componentId, IModel<String> model) {
		super(componentId, model);
		addValidator();
	}

	private void addValidator() {
//		add(new HalfSizeNumeralForCpuValidator());
	}
	
}
