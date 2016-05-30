/*
 * $Id: OtherLanguagePathValidator.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
public class OtherLanguagePathValidator extends AbstractFormValidator{
	/**
	 * 
	 * 
	 */
	public OtherLanguagePathValidator(RepeatingLanguagePathPanel repeater){
		this.parent = repeater;
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
	   String value = parent.getNoValidateOtherValue();
	   if(value == null || value.equals("")){
			form.error("Field 'Languages' is required.");
			return;
		}
	   String[] paths = value.split("[)],\\s*[(]");
		String[] messageCode = new String[paths.length];
		for(int i = 0; i < paths.length; i++){
			messageCode[i] = paths[i];
		}
		int j = 0;
		for(String path : paths){
			path = path.replaceAll("[)]$", "");
			path = path.replaceAll("^[(]", "");
			if(path.contains("<->")){
				String[] codes = path.split("<->");
				if(codes.length > 2){
					form.error(MessageManager.getMessage(
							"OtherLanguagePathValidator.invalidPath", Locale.ENGLISH,
							messageCode[j]));
				}
				for(String code : codes){
					if(code.contains("->")){
						form.error(MessageManager.getMessage(
								"OtherLanguagePathValidator.invalidPath", Locale.ENGLISH,
								messageCode[j]));
						break;
					}
					if(!InternalLanguageModel.containName(code, form.getLocale()) && !InternalLanguageModel.containCode(code)){
						form.error(MessageManager.getMessage(
								"OtherLanguagePathValidator.invalidPath", Locale.ENGLISH,
								code));
					}
				}
			}else if(path.contains("->")){
				if(hasSimplexValueError(path, form.getLocale())){
					form.error(MessageManager.getMessage(
							"OtherLanguagePathValidator.invalidPath", Locale.ENGLISH,
							messageCode[j]));
				}
			}else if(!InternalLanguageModel.containName(path, form.getLocale()) && !InternalLanguageModel.containCode(path)){
				form.error(MessageManager.getMessage(
						"OtherLanguagePathValidator.invalidCode", Locale.ENGLISH,
						messageCode[j]));
			}
			j++;
		}
	}

	private boolean hasSimplexValueError(String pairString, Locale locale){
		String[] tmp = pairString.split("-");
		String value = pairString;
		int i = 0;
		for(; i < tmp.length; i++){
			tmp[i] = tmp[i].replace(">", "");
			String buf = "";
			String buf2 = "";
			int j = i;
			for(; j < tmp.length; j++){
				buf2 += tmp[j];
				if(InternalLanguageModel.containName(buf2, locale) || InternalLanguageModel.containCode(buf2)){
					buf = buf2 + "-";
				}
				buf2 += "-";
			}
			buf = buf.replaceAll("-$", "");
			if(buf.equals("")){
				continue;
			}
			value = value.replace(buf, "");
		}
		value = value.replaceAll("[->]", "");
		return !value.equals("");
	}

	private RepeatingLanguagePathPanel parent;
	private static final long serialVersionUID = 1L;
}
