/*
 * $Id: ApplicableWord.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
 * 穴埋め情報格納クラス<br>
 * 穴埋め単語のIDと文言を保持．
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class ApplicableWord implements Serializable {

	/**
	 * デフォルトコンストラクタ．
	 */
	public ApplicableWord() {
		;
	}
	/**
	 * コンストラクタ．
	 * @param wordId 穴埋め単語ID
	 * @param word 穴埋め単語情報
	 */
	public ApplicableWord(String wordId, String word) {
		this.wordId = wordId;
		this.word = word;
	}

	/**
	 * serialId
	 */
	private static final long serialVersionUID = 4670230310559542222L;
	/**
	 * 穴埋め単語ID
	 */
	@Field(order=1)
	private String wordId;
	/**
	 * 穴埋め単語情報
	 */
	@Field(order=2)
	private String word;

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
	 * wordIdを返す。
	 * @return wordId
	 */
	public String getWordId() {
		return wordId;
	}

	/**
	 * wordIdを設定する。
	 * @param wordId wordId
	 */
	public void setWordId(String wordId) {
		this.wordId = wordId;
	}

	/**
	 * wordを返す。
	 * @return word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * wordを設定する。
	 * @param word word
	 */
	public void setWord(String word) {
		this.word = word;
	}

}
