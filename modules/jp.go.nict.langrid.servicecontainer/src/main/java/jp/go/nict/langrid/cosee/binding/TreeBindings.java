/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
 * Copyright (C) 2015 Language Grid Project.
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import jp.go.nict.langrid.commons.cs.binding.BindingNode;
import jp.go.nict.langrid.commons.cs.binding.Condition;
import jp.go.nict.langrid.commons.cs.binding.DynamicBindingUtil;
import jp.go.nict.langrid.commons.rpc.RpcHeader;
import jp.go.nict.langrid.commons.util.MapUtil;

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
				MapUtil.addValueToCollection(bindings, node.getInvocationName(), node, new Supplier<List<BindingNode>>(){
					@Override
					public List<BindingNode> get() {
						return new ArrayList<BindingNode>();
					}
				});
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
	public BindingNode getBindingNodeFor(String invocationName, String methodName, String[] paramNames, Object[] args){
		List<BindingNode> nodes = bindings.get(invocationName);
		if(nodes != null) for(BindingNode node : nodes){
			Condition[] conds = node.getConditions();
			if(conds != null && conds.length > 0){
				boolean matched = false;
				for(Condition c : conds){
					String p = c.getParam();
					String op = c.getOp();
					String val = c.getValue();
					matched = false;
					for(int i = 0; i < paramNames.length; i++){
						if(paramNames[i].equals(p)){
							if(op.equals("eq")){
								matched = val.equals(args[i].toString());
								break;
							} else if(op.equals("ne")){
								matched = !val.equals(args[i].toString());
								break;
							}
						}
					}
					if(!matched) break;
				}
				if(matched) return node;
			} else{
				return node;
			}
		}
		throw new NoBindingFoundException(invocationName);
	}

	private Map<String, List<BindingNode>> bindings =
		new HashMap<String, List<BindingNode>>(); 
}
