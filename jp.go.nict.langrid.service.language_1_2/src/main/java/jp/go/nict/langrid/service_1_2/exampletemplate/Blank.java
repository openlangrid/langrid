/*
 * $Id: Blank.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
 * 単語カテゴリ保持クラス<br>
 * 穴あき部分毎に選択可能穴埋め単語カテゴリを保持．
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class Blank implements Serializable {

	/**
	 * serialId
	 */
	private static final long serialVersionUID = -8452479132419930319L;
	/**
	 * デフォルトコンストラクタ．
	 */
	public Blank() {
		;
	}
	/**
	 * コンストラクタ．
	 * @param pId パラメータID
	 * @param wordCategories 穴埋め単語カテゴリ情報
	 */
	public Blank(String pId, WordCategory[] wordCategories) {
		this.pId = pId;
		this.wordCategories = wordCategories;
	}
	/**
	 * パラメータID
	 */
	@Field(order=1)
	private String pId;
	/**
	 * 穴埋め単語カテゴリ情報
	 */
	@Field(order=2)
	private WordCategory[] wordCategories;
	
	/**
	 * セパレート後の用例
	 */
	@Field(order=3)
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
	 * pIdを返す。
	 * @return pId
	 */
	public String getPId() {
		return pId;
	}

	/**
	 * pIdを設定する。
	 * @param id pId
	 */
	public void setPId(String id) {
		pId = id;
	}

	/**
	 * wordCategoriesを返す。
	 * @return wordCategories
	 */
	public WordCategory[] getWordCategories() {
		return wordCategories;
	}

	/**
	 * wordCategoriesを設定する。
	 * @param wordCategories wordCategories
	 */
	public void setWordCategories(WordCategory[] wordCategories) {
		this.wordCategories = wordCategories;
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
