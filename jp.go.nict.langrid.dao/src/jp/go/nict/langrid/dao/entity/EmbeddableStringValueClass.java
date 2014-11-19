/*
 * $Id: EmbeddableStringValueClass.java 205 2010-10-02 13:53:40Z t-nakaguchi $
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
package jp.go.nict.langrid.dao.entity;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 205 $
 */
@Embeddable
public class EmbeddableStringValueClass<T>
implements Serializable
{
	/**
	 * 
	 * 
	 */
	public EmbeddableStringValueClass(){
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public EmbeddableStringValueClass(T value){
		this.original = value;
		this.stringValue = value.toString();
		this.clazz = (Class<T>)value.getClass();
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
	public T getValue(){
		try{
			if(original != null){
				return original;
			}
			if(ctor == null){
				ctor = clazz.getConstructor(String.class);
			}
			original = ctor.newInstance(stringValue);
			return original;
		} catch(IllegalAccessException e){
			throw new RuntimeException(e);
		} catch(InstantiationException e){
			throw new RuntimeException(e);
		} catch(InvocationTargetException e){
			throw new RuntimeException(e);
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setValue(T value){
		this.original = value;
		this.stringValue = value.toString();
		this.clazz = (Class<T>)value.getClass();
	}

	private String stringValue;
	private Class<T> clazz;
	private transient T original;
	private transient Constructor<T> ctor;

	private static final long serialVersionUID = 7291713579891169212L;
}
