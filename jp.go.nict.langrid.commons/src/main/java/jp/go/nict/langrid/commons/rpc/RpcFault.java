/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2012 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.rpc;

import jp.go.nict.langrid.commons.rpc.intf.Field;

public class RpcFault {
	public RpcFault() {
	}
	public RpcFault(String faultCode, String faultString, String detail) {
		this.faultCode = faultCode;
		this.faultString = faultString;
		this.detail = detail;
	}
	@Override
	public String toString() {
		return new StringBuilder()
			.append("faultCode: ").append(faultCode)
			.append(", faultString: ").append(faultString)
			.append(", detail: ").append(detail)
			.toString();
	}
	public String getFaultCode() {
		return faultCode;
	}
	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}
	public String getFaultString() {
		return faultString;
	}
	public void setFaultString(String faultString) {
		this.faultString = faultString;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	@Field(order=1)
	private String faultCode;
	@Field(order=2)
	private String faultString;
	@Field(order=3)
	private String detail;
}
