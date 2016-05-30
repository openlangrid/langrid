/*
 * $Id: NodeUtil.java 1183 2014-04-10 13:59:44Z t-nakaguchi $
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
package jp.go.nict.langrid.commons.ws;

import java.util.UUID;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1183 $
 */
public class NodeUtil {
	/**
	 * 
	 * 
	 */
	public static String generateNodeId(){
		return "node-" + UUID.randomUUID().toString();
	}
}
