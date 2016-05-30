/*
 * $Id: FilledBlank.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
 * 穴あき部分対する穴埋め単語の情報を保持する．
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class FilledBlank implements Serializable {

	/**
	 * デフォルトコンストラクタ．
	 */
	public FilledBlank() {
		;
	}
	/**
	 * コンストラクタ．
	 * @param pId パラメータID
	 * @param wordId 穴埋め単語ID
	 */
	public FilledBlank(String pId, String wordId) {
		this.pId = pId;
		this.wordId = wordId;
	}
	/**
	 * serialID
	 */
	private static final long serialVersionUID = 5733641361024691095L;
	/**
	 * パラメータID
	 */
	@Field(order=1)
	private String pId;
	/**
	 * 穴埋め単語ID
	 */
	@Field(order=2)
	private String wordId;

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

	
}
