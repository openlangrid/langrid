/*
 * $Id: RequiredServiceIdField.java 11592 2011-01-13 09:10:20Z Masaaki Kamiya $
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

import jp.go.nict.langrid.management.web.view.component.text.IdTextField;
import jp.go.nict.langrid.management.web.view.component.validator.HalfSizeAlphaNumeralSiglumValidator;
import jp.go.nict.langrid.management.web.view.component.validator.LengthValidator;
import jp.go.nict.langrid.management.web.view.page.node.component.form.validator.AlreadyExistsNodeIdValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: Masaaki Kamiya $
 * @version $Revision: 11592 $
 */
public class RequiredNodeIdField extends IdTextField{
	/**
	 * 
	 * 
	 */
   public RequiredNodeIdField(String componentId, String gridId) {
	   super(componentId, "", gridId, true);
	}

	@Override
	protected void addValidater(String gridId){
		add(new HalfSizeAlphaNumeralSiglumValidator("ProvidingServices.language.service.error.ServiceID"));
		add(new LengthValidator(4, 254, "ProvidingServices.language.service.error.ServiceID"));
		add(new AlreadyExistsNodeIdValidator(gridId, this));
	}
}
