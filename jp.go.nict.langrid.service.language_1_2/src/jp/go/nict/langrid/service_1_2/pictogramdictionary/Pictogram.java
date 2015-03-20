/*
 * $Id: Pictogram.java 567 2012-08-06 11:37:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.pictogramdictionary;

import java.io.Serializable;

import jp.go.nict.langrid.commons.rpc.intf.Field;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * Stores pictogram data.
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 567 $
 */
public class Pictogram
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public Pictogram(){
	}

	/**
	 * 
	 * Constructor.
	 * @param word A word being related to a pictogram
	 * @param imageType Icon image file type
	 * @param image Icon image data
	 * 
	 */
	public Pictogram(String word, String imageType, byte[] image) {
		this.word = word;
		this.imageType = imageType;
		this.image = image;
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
	 * Returns word.
	 * 
	 */
	public String getWord() {
		return word;
	}
	/**
	 * 
	 * Sets word.
	 * 
	 */
	public void setWord(String word) {
		this.word = word;
	}
	/**
	 * 
	 * Returns imageType.
	 * 
	 */
	public String getImageType() {
		return imageType;
	}
	/**
	 * 
	 * Sets imageType.
	 * 
	 */
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	/**
	 * 
	 * Returns image.
	 * 
	 */
	public byte[] getImage() {
		return image;
	}
	/**
	 * 
	 * Sets image.
	 * 
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}

	@Field(order=1)
	private String word;
	@Field(order=2)
	private String imageType;
	@Field(order=3)
	private byte[] image;

	private static final long serialVersionUID = -7446159664928173018L;
}
