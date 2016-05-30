/*
 * $Id:UserDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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
package jp.go.nict.langrid.dao;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class SearchResult{
	/**
	 * 
	 * 
	 */
	public SearchResult(
			int totalCount, boolean totalCountFixed
			){
		this.totalCount = totalCount;
		this.totalCountFixed = totalCountFixed;
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

	/**
	 * 
	 * 
	 */
	/* 下位で具象化(i.e. UserEntry[])してもプロパティはObject[](T[])又は
	 * Object(Object)型になるので、各クラスで実装する必要がある。
	public T[] getElements() {
		return elements;
	}
	public Object getElements() {
		return elements;
	}
	*/

	/**
	 * 
	 * 
	 */
	/* 下位で具象化(i.e. UserEntry[])してもプロパティはObject[](T[])又は
	 * Object(Object)型になるので、各クラスで実装する必要がある。
	public void setElements(T[] elements) {
		this.elements = elements;
	}
	 */

	/**
	 * 
	 * 
	 */
	public void setTotalCount(int maxCount) {
		this.totalCount = maxCount;
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
	public void setTotalCountFixed(boolean totalCountFixed){
		this.totalCountFixed = totalCountFixed;
	}

	/**
	 * 
	 * 
	 */
	public boolean isTotalCountFixed(){
		return totalCountFixed;
	}

	private int totalCount;
	private boolean totalCountFixed;
}
