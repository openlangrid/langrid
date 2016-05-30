/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
import java.lang.reflect.InvocationTargetException;

import javax.persistence.Entity;

import org.apache.commons.beanutils.BeanUtilsBean;

/**
 * 
 * 
 * @author Takao nakaguchi
 */
@Entity
public class WebappService
extends Service
implements Serializable{
	/**
	 * 
	 * 
	 */
	public WebappService(){
		setInstanceType(InstanceType.WEBAPP); 
		setContainerType(ServiceContainerType.COMPOSITE);
	}

	/**
	 * 
	 * 
	 */
	public WebappService(String gridId, String serviceId){
		super(gridId, serviceId);
		setInstanceType(InstanceType.WEBAPP); 
		setContainerType(ServiceContainerType.COMPOSITE);
	}

	@Override
	public WebappService clone(){
		try{
			return (WebappService)BeanUtilsBean.getInstance().cloneBean(this);
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

	private static final long serialVersionUID = 2258665502088110068L;
}
