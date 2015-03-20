/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) Language Grid Project.
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
package jp.go.nict.langrid.commons.dom;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Takao Nakaguchi
 */
public class NodeListUtil {
	public static Collection<Node> toCollection(NodeList nl){
		Collection<Node> ret = new ArrayList<Node>();
		int n = nl.getLength();
		for(int i = 0; i < n; i++){
			ret.add(nl.item(i));
		}
		return ret;
	}
}
