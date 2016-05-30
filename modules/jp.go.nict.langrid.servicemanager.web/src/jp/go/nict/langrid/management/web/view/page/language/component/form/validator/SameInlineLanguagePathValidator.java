/*
 * $Id: SameInlineLanguagePathValidator.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.component.form.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.go.nict.langrid.management.web.model.InternalLanguageModel;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.language.component.form.panel.RepeatingLanguagePathPanel;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class SameInlineLanguagePathValidator extends AbstractFormValidator{
	/**
	 * 
	 * 
	 */
	public SameInlineLanguagePathValidator(RepeatingLanguagePathPanel target){
		this.target = target;
	}
	
	@Override
	public boolean equals(Object obj){
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public FormComponent[] getDependentFormComponents(){
		return null;
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public void validate(Form form){
		List<String[]> list = target.getNoValidateValueList();
	   for(String[] inputs : list){
			List<String> tmp = new ArrayList<String>();
			for(int i = 0; i < inputs.length; i++){
				if(isWildcard(inputs[i]) || !InternalLanguageModel.getCodeList().contains(inputs[i])){
					continue;
				}
				if(tmp.contains(inputs[i])){
					form.error(MessageManager.getMessage(
							"SameInlineLanguagePathValidator", Locale.ENGLISH));
					return;
				}
				tmp.add(inputs[i]);
			}
		}
	}

	private boolean isWildcard(String value){
		return value.equals(InternalLanguageModel.getWildcard().getCode());
	}

	private RepeatingLanguagePathPanel target;
	private static final long serialVersionUID = 1L;
}
