/*
 * $Id: PasswordLengthValidator.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class PasswordLengthValidator extends StringValidator{
	/**
	 * 
	 * 
	 */
	public PasswordLengthValidator(int aMinimize, int aMaximize){
		super();
		minimize = aMinimize;
		maximize = aMaximize;
	}

	@Override
	protected void onValidate(IValidatable<String> arg0){
		int arg0Length = arg0.getValue().length();
		if(arg0Length < minimize || maximize < arg0Length){
			error(arg0);
		}
	}

	int maximize;
	int minimize;
	private static final long serialVersionUID = 1L;
}
