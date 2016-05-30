/*
 * $Id: ProfileKeyUtil.java 225 2010-10-03 00:23:14Z t-nakaguchi $
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
package jp.go.nict.langrid.service_1_2.foundation.util;

import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.AttributeName;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.typed.ServiceEntryAttributes;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.typed.ServiceEntryFields;
import jp.go.nict.langrid.service_1_2.foundation.servicemanagement.typed.WorkflowAttributes;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 225 $
 */
public class ProfileKeyUtil {
	/**
	 * 
	 * 
	 */
	public static boolean isReadonlyKey(AttributeName key){
		for(AttributeName k : readonlyKeys){
			if(k.getAttributeName().equals(key.getAttributeName())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * 
	 */
	public static AttributeName[] generatedKeys = new AttributeName[]{
		ServiceEntryFields.endpointUrl
		, ServiceEntryFields.registeredDate
		, ServiceEntryFields.updatedDate
		, WorkflowAttributes.isBpel
		, WorkflowAttributes.isDeployed
		};

	/**
	 * 
	 * 
	 */
	public static AttributeName[] readonlyKeys = new AttributeName[]{
		ServiceEntryFields.serviceId
		, ServiceEntryFields.endpointUrl
		, ServiceEntryFields.serviceType
		, ServiceEntryFields.registeredDate
		, ServiceEntryFields.updatedDate
		, ServiceEntryAttributes.workflowId
		, WorkflowAttributes.isBpel
		, WorkflowAttributes.isDeployed
		};
}
