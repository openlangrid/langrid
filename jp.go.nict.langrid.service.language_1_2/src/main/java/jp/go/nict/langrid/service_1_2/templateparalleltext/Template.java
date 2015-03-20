/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.service_1_2.templateparalleltext;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.service_1_2.Category;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Takao Nakaguchi
 */
public class Template
implements Serializable{
	public Template(){
	}
	
	public Template(String templateId, String template,
			ChoiceParameter[] choiceParameters,
			ValueParameter[] valueParameters, Category[] categories) {
		this.templateId = templateId;
		this.template = template;
		this.choiceParameters = choiceParameters;
		this.valueParameters = valueParameters;
		this.categories = categories;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public ChoiceParameter[] getChoiceParameters() {
		return choiceParameters;
	}

	public void setChoiceParameters(ChoiceParameter[] choiceParameters) {
		this.choiceParameters = choiceParameters;
	}

	public ValueParameter[] getValueParameters() {
		return valueParameters;
	}

	public void setValueParameters(ValueParameter[] valueParameters) {
		this.valueParameters = valueParameters;
	}

	public Category[] getCategories() {
		return categories;
	}

	public void setCategories(Category[] categories) {
		this.categories = categories;
	}

	@Field(order=1)
	private String templateId;
	@Field(order=2)
	private String template;
	@Field(order=3)
	private ChoiceParameter[] choiceParameters;
	@Field(order=4)
	private ValueParameter[] valueParameters;
	@Field(order=5)
	private Category[] categories;

	private static final long serialVersionUID = 1565314031571647490L;
}
