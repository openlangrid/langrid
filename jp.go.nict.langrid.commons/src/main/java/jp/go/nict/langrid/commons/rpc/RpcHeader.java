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

public class RpcHeader implements Cloneable{
	public RpcHeader() {
	}
	public RpcHeader(String namespace, String name, String value) {
		this.namespace = namespace;
		this.name = name;
		this.value = value;
	}
	@Override
	public String toString() {
		return new StringBuilder()
				.append("namespace: ").append(namespace)
				.append(", name: ").append(name)
				.append(", value: ").append(value)
				.toString();
	}
	public RpcHeader clone(){
		return new RpcHeader(namespace, name, value);
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private String namespace;
	private String name;
	private String value;
}
