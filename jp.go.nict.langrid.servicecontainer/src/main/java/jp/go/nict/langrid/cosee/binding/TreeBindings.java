/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
package jp.go.nict.langrid.cosee.binding;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.binding.DynamicBindingUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class TreeBindings{
	/**
	 * 
	 * 
	 */
	public TreeBindings(){
	}

	/**
	 * 
	 * 
	 */
	public TreeBindings(List<RpcHeader> headers)
	throws ParseException{
		for(RpcHeader he : headers){
			String value = he.getValue();
			if(value == null) continue;
			for(BindingNode node : DynamicBindingUtil.decodeTree(value)){
				bindings.put(node.getInvocationName(), node);
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public void merge(TreeBindings other){
		bindings.putAll(other.bindings);
	}

	/**
	 * 
	 * 
	 */
	public String getBindingFor(String invocationName){
		BindingNode node = bindings.get(invocationName);
		if(node == null) return null;
		else return node.getServiceId();
	}

	/**
	 * 
	 * 
	 */
	public Collection<BindingNode> getChildrenNodes(String invocationName){
		BindingNode node = bindings.get(invocationName);
		if(node == null) return null;
		else return node.getChildren();
	}

	private Map<String, BindingNode> bindings =
		new HashMap<String, BindingNode>(); 
}
