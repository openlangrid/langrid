/*
 * $Id: SearchResult.java 587 2012-10-19 06:41:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.bilingualdictionary;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;
import jp.go.nict.langrid.commons.rpc.intf.Schema;

/**
 * 
 * 
 * totalCountFixedがfalseの場合、検索条件が複雑等の理由で、totalCount値が確定できないことを表す。
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 587 $
 */
@Schema(namespace="http://langrid.nict.go.jp/ws_1_2/bilingualdictionary/")
public class SearchResult
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public SearchResult(){
	}

	/**
	 * 
	 * 
	 */
	public SearchResult(int totalCount, boolean totalCountFixed){
		this.totalCount = totalCount;
		this.totalCountFixed = totalCountFixed;
	}

	/**
	 * 
	 * 
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 
	 * 
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 
	 * 
	 */
	public boolean isTotalCountFixed(){
		return totalCountFixed;
	}

	/**
	 * 
	 * 
	 */
	public void setTotalCountFixed(boolean totalCountFixed){
		this.totalCountFixed = totalCountFixed;
	}

	@Field(order=1)
	private int totalCount;
	@Field(order=2)
	private boolean totalCountFixed;

	private static final long serialVersionUID = -8947607514011085965L;
}
