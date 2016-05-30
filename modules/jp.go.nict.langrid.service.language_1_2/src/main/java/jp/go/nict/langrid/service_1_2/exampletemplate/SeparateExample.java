/*
 * $Id: SeparateExample.java 567 2012-08-06 11:37:14Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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

package jp.go.nict.langrid.service_1_2.exampletemplate;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 文章区切り穴あき用例保持クラス<br>
 * 穴あき用例セパレートによって，セパレートされた穴あき用例ID、文字配列の保持．
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class SeparateExample implements Serializable {

	/**
	 * serialId
	 */
	private static final long serialVersionUID = -7123652883640534528L;
	/**
	 * デフォルトコンストラクタ。
	 */
	public SeparateExample() {
		;
	}
	/**
	 * コンストラクタ。
	 * @param examleId 穴あき用例ID
	 * @param texts セパレート後の用例
	 */
	public SeparateExample(String examleId, String[] texts) {
		this.exampleId = examleId;
		this.texts = texts;
	}
	/**
	 * 穴あき用例ID
	 */
	@Field(order=1)
	private String exampleId;
	/**
	 * セパレート後の用例
	 */
	@Field(order=2)
	private String[] texts;
	
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

	/**
	 * exampleIdを返す。
	 * @return exampleId
	 */
	public String getExampleId() {
		return exampleId;
	}

	/**
	 * exampleIdを設定する。
	 * @param exampleId exampleId
	 */
	public void setExampleId(String exampleId) {
		this.exampleId = exampleId;
	}

	/**
	 * textsを返す。
	 * @return texts
	 */
	public String[] getTexts() {
		return texts;
	}

	/**
	 * textsを設定する。
	 * @param texts texts
	 */
	public void setTexts(String[] texts) {
		this.texts = texts;
	}

}
