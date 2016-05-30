/*
 * $Id: ExampleTemplate.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
 * 穴あき用例クラス<br>
 * 穴あき用例と関連するカテゴリ情報を保持する．
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class ExampleTemplate implements Serializable {

	/**
	 * serialId
	 */
	private static final long serialVersionUID = 2744066667864599415L;
	/**
	 * デフォルトコンストラクタ．
	 */
	public ExampleTemplate() {
		;
	}
	/**
	 * コンストラクタ．
	 * @param exampleId 穴あき用例ID
	 * @param example 穴あき用例
	 */
	public ExampleTemplate(String exampleId, String example) {
		this.exampleId = exampleId;
		this.example = example;
	}
	/**
	 * 穴あき用例ID
	 */
	@Field(order=1)
	private String exampleId;
	/**
	 * 穴あき用例
	 */
	@Field(order=2)
	private String example;
	/**
	 * 取得した穴あき用例IDの関連している穴あき用例カテゴリID
	 */
	@Field(order=3)
	private String[] categoryIds;
	/**
	 * 穴き部分の選択情報
	 */
	@Field(order=4)
	private Blank[] blanks;
	
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
	 * exampleを返す。
	 * @return example
	 */
	public String getExample() {
		return example;
	}

	/**
	 * exampleを設定する。
	 * @param example example
	 */
	public void setExample(String example) {
		this.example = example;
	}

	/**
	 * categoryIdsを返す。
	 * @return categoryIds
	 */
	public String[] getCategoryIds() {
		return categoryIds;
	}

	/**
	 * categoryIdsを設定する。
	 * @param categoryIds categoryIds
	 */
	public void setCategoryIds(String[] categoryIds) {
		this.categoryIds = categoryIds;
	}

	/**
	 * blanksを返す。
	 * @return blanks
	 */
	public Blank[] getBlanks() {
		return blanks;
	}

	/**
	 * blanksを設定する。
	 * @param blanks blanks
	 */
	public void setBlanks(Blank[] blanks) {
		this.blanks = blanks;
	}

}
