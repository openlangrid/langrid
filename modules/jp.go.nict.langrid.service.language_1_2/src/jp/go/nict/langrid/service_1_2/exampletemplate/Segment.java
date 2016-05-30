/*
 * $Id: Segment.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
 * 木構造穴あき用例保持クラス<br>
 * 入力支援用にセパレート、マージ、木構造化された穴あき用例を保持．
 * @author koyama
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class Segment implements Serializable {

	/**
	 * serialId
	 */
	private static final long serialVersionUID = -6480888385341506769L;
	/**
	 * デフォルトコンストラクタ．
	 */
	public Segment() {
		;
	}
	/**
	 * コンストラクタ．
	 * @param exampleId 穴あき用例ID
	 * @param text マージ後の用例
	 * @param categoryIds 穴埋め単語カテゴリID
	 * @param childSegments チャイルドセグメント情報
	 */
	public Segment(String exampleId, String text, String[] categoryIds, Segment[] childSegments) {
		this.exampleId = exampleId;
		this.text = text;
		this.categoryIds = categoryIds;
		this.childSegments = childSegments;
	}
	/**
	 * 穴あき用例ID
	 */
	@Field(order=1)
	private String exampleId;
	/**
	 * マージ後の用例
	 */
	@Field(order=2)
	private String text;
	/**
	 * 穴埋め単語カテゴリID
	 */
	@Field(order=3)
	private String[] categoryIds;
	/**
	 * チャイルドセグメント情報
	 */
	@Field(order=4)
	private Segment[] childSegments;
	
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
	 * textを返す。
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * textを設定する。
	 * @param text text
	 */
	public void setText(String text) {
		this.text = text;
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
	 * childSegmentsを返す。
	 * @return childSegments
	 */
	public Segment[] getChildSegments() {
		return childSegments;
	}

	/**
	 * childSegmentsを設定する。
	 * @param childSegments childSegments
	 */
	public void setChildSegments(Segment[] childSegments) {
		this.childSegments = childSegments;
	}

}
