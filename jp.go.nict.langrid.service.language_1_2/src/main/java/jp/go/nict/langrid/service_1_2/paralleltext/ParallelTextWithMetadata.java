/*
 * $Id: ParallelTextWithMetadata.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.paralleltext;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class ParallelTextWithMetadata implements Serializable{
	/**
	 * 
	 * 
	 */
	public ParallelTextWithMetadata(){
	}

	/**
	 * 
	 * 
	 */
	public ParallelTextWithMetadata(String source, String target, String[] metadata){
		this.source = source;
		this.target = target;
		this.metadata = metadata;
	}

	@Override
	public boolean equals(Object value){
		return EqualsBuilder.reflectionEquals(this, value);
	}

	/**
	 * 
	 * 
	 */
	public String[] getMetadata(){
		return metadata;
	}

	/**
	 * 
	 * 
	 */
	public String getSource(){
		return source;
	}

	/**
	 * 
	 * 
	 */
	public String getTarget(){
		return target;
	}

	@Override
	public int hashCode(){
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * 
	 * 
	 */
	public void setMetadata(String[] metadata){
		this.metadata = metadata;
	}

	/**
	 * 
	 * 
	 */
	public void setSource(String source){
		this.source = source;
	}

	/**
	 * 
	 * 
	 */
	public void setTarget(String target){
		this.target = target;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * 
	 * 
	 */
	@Field(order=1)
	private String[] metadata;
	/**
	 * 
	 * 
	 */
	@Field(order=2)
	private String source;
	/**
	 * 
	 * 
	 */
	@Field(order=3)
	private String target;
	private static final long serialVersionUID = -6193211168959610171L;
}
