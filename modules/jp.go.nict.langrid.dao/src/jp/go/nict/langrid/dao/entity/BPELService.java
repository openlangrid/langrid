/*
 * $Id: BPELService.java 388 2011-08-23 10:24:50Z t-nakaguchi $
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;

import jp.go.nict.langrid.commons.util.CollectionUtil;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.annotations.CollectionOfElements;

/**
 * 
 * 
 * @author  $Author: t-nakaguchi $
 * @version  $Revision: 388 $
 */
@Entity
public class BPELService
extends Service
implements Serializable{
	/**
	 * 
	 * 
	 */
	public BPELService(){
		setInstanceType(InstanceType.BPEL);
		setContainerType(ServiceContainerType.COMPOSITE);
	}

	/**
	 * 
	 * 
	 */
	public BPELService(String gridId, String serviceId){
		super(gridId, serviceId);
		setInstanceType(InstanceType.BPEL); 
		setContainerType(ServiceContainerType.COMPOSITE);
	}

	/**
	 * 
	 * 
	 */
	public boolean isDeployed(){
		return deployed;
	}

	/**
	 * 
	 * 
	 */
	public void setDeployed(boolean deployed){
		this.deployed = deployed;
	}

	/**
	 * 
	 * 
	 */
	public String getDeployedId(){
		return deployedId;
	}

	/**
	 * 
	 * 
	 */
	public void setDeployedId(String deployedId){
		this.deployedId = deployedId;
	}

	/**
	 * 
	 * 
	 */
	public List<String> getPartnerServiceNamespaceURIs(){
		return partnerServiceNamespaceURIs;
	}

	/**
	 * 
	 * 
	 */
	public void setPartnerServiceNamespaceURIs(List<String> partnerServiceNamespaceURIs){
		this.partnerServiceNamespaceURIs.clear();
		this.partnerServiceNamespaceURIs.addAll(partnerServiceNamespaceURIs);
	}

	@Override
	public BPELService clone(){
		try{
			return (BPELService)BeanUtilsBean.getInstance().cloneBean(this);
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

	@Override
	protected EqualsBuilder appendSpecialEquals(EqualsBuilder builder,
			Object value, Collection<String> appendedFields, boolean ignoreDates) {
		BPELService s = (BPELService)value;
		EqualsBuilder b = super.appendSpecialEquals(
				builder, value, appendedFields, ignoreDates);
		appendedFields.add("invocations");
		if(!ignoreDates){
			return b.appendSuper(CollectionUtil.equalsAsSet(
					getInvocations(), s.getInvocations()
					));
		}
		try{
			return b.appendSuper(CollectionUtil.equalsAsSet(
					getInvocations(), s.getInvocations()
					, Invocation.class, "equalsIgnoreDates"
					));
		} catch(IllegalAccessException e){
			throw new RuntimeException(e);
		} catch(InvocationTargetException e){
			throw new RuntimeException(e);
		} catch(NoSuchMethodException e){
			throw new RuntimeException(e);
		}
	}

	private boolean deployed;
	private String deployedId;
	@CollectionOfElements
	private List<String> partnerServiceNamespaceURIs = new ArrayList<String>();

	private static final long serialVersionUID = -1770343889009259124L;
}
