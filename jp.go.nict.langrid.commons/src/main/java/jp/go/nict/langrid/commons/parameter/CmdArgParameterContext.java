/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class CmdArgParameterContext
extends ParameterContext
{
	/**
	 * 
	 * 
	 */
	public CmdArgParameterContext(String[] args){
		for(String a : args){
			if(a.startsWith("-")){
				String[] v = a.split("^-+|=");
				if(v.length == 2){
					map.put(v[0], v[1]);
				} else if(v.length == 3){
					map.put(v[1], v[2]);
				}
			} else if(a.length() > 0){
				noNameArgs.add(a);
			}
		}
	}

	public String getValue(String name) {
		return map.get(name);
	}

	public Collection<String> getNoNameArgs(){
		return noNameArgs;
	}

	private Map<String, String> map = new HashMap<String, String>();
	private List<String> noNameArgs = new ArrayList<String>();
}
