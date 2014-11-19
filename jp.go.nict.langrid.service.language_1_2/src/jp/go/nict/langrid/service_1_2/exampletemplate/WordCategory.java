/*
 * $Id: WordCategory.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
 * 穴埋め単語情報格納クラス<br>
 * 穴埋め単語カテゴリのID、文言を保持
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class WordCategory implements Serializable {

	/**
	 * seiralId
	 */
	private static final long serialVersionUID = 3453384085515724616L;
	/**
	 * デフォルトコンストラクタ。
	 */
	public WordCategory() {
		;
	}
	/**
	 * コンストラクタ。
	 * @param categoryId 穴埋め単語カテゴリID
	 * @param wordCategory 穴埋め単語カテゴリ
	 */
	public WordCategory(String categoryId, String wordCategory) {
		this.categoryId = categoryId;
		this.wordCategory = wordCategory;
	}
	/**
	 * 穴埋め単語カテゴリID
	 */
	@Field(order=1)
	private String categoryId;
	/**
	 * 穴埋め単語カテゴリ
	 */
	@Field(order=2)
	private String wordCategory;
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
	 * categoryIdを返す。
	 * @return categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * categoryIdを設定する。
	 * @param categoryId categoryId
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * wordCategoryを返す。
	 * @return wordCategory
	 */
	public String getWordCategory() {
		return wordCategory;
	}

	/**
	 * wordCategoryを設定する。
	 * @param wordCategory wordCategory
	 */
	public void setWordCategory(String wordCategory) {
		this.wordCategory = wordCategory;
	}

	
}
