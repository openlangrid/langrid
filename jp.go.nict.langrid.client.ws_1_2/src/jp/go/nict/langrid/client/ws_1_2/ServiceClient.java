/*
 * $Id: ServiceClient.java 562 2012-08-06 10:56:18Z t-nakaguchi $
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
package jp.go.nict.langrid.client.ws_1_2;

import java.util.Collection;

import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.calltree.CallNode;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 562 $
 */
public interface ServiceClient {
	/**
	 * 
	 * 
	 */
	void setUserId(String userId);

	/**
	 * 
	 * 
	 */
	void setPassword(String password);

	/**
	 * 
	 * 
	 */
	Collection<BindingNode> getTreeBindings();

	/**
	 * 
	 * 
	 */
	void addRequestHeader(String name, String value);

	/**
	 * 
	 * 
	 */
	String getLastName();

	/**
	 * 
	 * 
	 */
	String getLastCopyrightInfo();

	/**
	 * 
	 * 
	 */
	String getLastLicenseInfo();

	/**
	 * 
	 * 
	 */
	Collection<CallNode> getLastCallTree();
}
