/*
 * $Id: ServiceTypeManagement.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.foundation.experimental.servicetype;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class ServiceTypeManagement {
	public ServiceType[] listServiceTypes(){
		return null;
	}

	public void addServiceType(String serviceTypeId
			, String description, byte[] wsdl){
		/*
		 * サービスタイプを登録する。
		 * wsdlを解析し、テンプレートを作成する。
		 * テンプレートでは名前空間、サービス名、エンドポイントURLを変数化。
		 */
	}

	public void deleteServiceType(String serviceTypeId){
		
	}

	public void setServiceTypeDescription(String serviceTypeId
			, String description){
		
	}

	public void setServiceTypeWsdl(String serviceTypeId, byte[] wsdl){
		
	}
}
