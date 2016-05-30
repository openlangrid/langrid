/*
 * $Id: ServiceInstance.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.servicemanagement;

import java.io.Serializable;
import java.util.Arrays;

import jp.go.nict.langrid.service_1_2.LanguagePath;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * 
 * Stores data relating to the instance of the service.
 * The serviceType is, {@link jp.go.nict.langrid.service_1_2.typed.ServiceType ServiceType}'s
 * enumerated value, cast as a string.
 * The instanceType is, {@link jp.go.nict.langrid.service_1_2.typed.InstanceType InstanceType}'s
 * enumerated value, cast as a string.
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ServiceInstance
implements Serializable
{
	/**
	 * 
	 * Constructor.
	 * 
	 */
	public ServiceInstance(){
	}
	
	/**
	 * 
	 * Constructor.
	 * @param serviceType Type of the service
	 * @param supportedLanguages Supported languages
	 * @param instanceType Type of the instance
	 * @param instanceSize Size of the instance
	 * @param instance Instance
	 * 
	 */
	public ServiceInstance(
			String serviceType
			, LanguagePath[] supportedLanguages
			, String instanceType
			, int instanceSize, byte[] instance
			){
		this.serviceType = serviceType;
		this.supportedLanguages = supportedLanguages;
		this.instanceType = instanceType;
		this.instanceSize = instanceSize;
		this.instance = instance;
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
		ReflectionToStringBuilder b = new ReflectionToStringBuilder(this);
		b.setExcludeFieldNames(new String[]{"instance"});
		if(instance != null){
			byte[] inst = instance;
			if(inst.length > 256){
				inst = Arrays.copyOf(instance, 64);
			}
			b.append("instance", inst);
		} else{
			b.append("instance", "null");
		}
		return b.toString();
	}

	/**
	 * 
	 * Returns service type.
	 * @return Service type
	 * 
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * 
	 * Sets service type.
	 * @param serviceType Service type
	 * 
	 */
	public void setServiceType(String serviceType){
		this.serviceType = serviceType;
	}

	/**
	 * 
	 * Returns the supported language.
	 * @return Supported language
	 * 
	 */
	public LanguagePath[] getSupportedLanguages() {
		return supportedLanguages;
	}

	/**
	 * 
	 * Sets supported language.
	 * @param supportedLangauges Supported languages
	 * 
	 */
	public void setSupportedLanguages(LanguagePath[] supportedLangauges){
		this.supportedLanguages = supportedLangauges;
	}

	/**
	 * 
	 * Returns the type of instance.
	 * @return Type of instance
	 * 
	 */
	public String getInstanceType() {
		return instanceType;
	}

	/**
	 * 
	 * Sets the type of instance.
	 * @param instanceType Type of the instance
	 * 
	 */
	public void setInstanceType(String instanceType){
		this.instanceType = instanceType;
	}

	/**
	 * 
	 * Returns the size of the instance.
	 * @return Size of instance
	 * 
	 */
	public int getInstanceSize() {
		return instanceSize;
	}

	/**
	 * 
	 * Sets the size of the instance.
	 * @param instanceSize Size of the instance
	 * 
	 */
	public void setInstanceSize(int instanceSize){
		this.instanceSize = instanceSize;
	}

	/**
	 * 
	 * Returns the instance.
	 * @return Instance
	 * 
	 */
	public byte[] getInstance() {
		return instance;
	}

	/**
	 * 
	 * Sets the instance.
	 * @param instance Instance
	 * 
	 */
	public void setInstance(byte[] instance){
		this.instance = instance;
	}

	private String serviceType;
	private LanguagePath[] supportedLanguages;
	private String instanceType;
	private int instanceSize;
	private byte[] instance;

	private static final long serialVersionUID = 7036708888727804324L;
}
