/*
 * $Id: DataAttributes.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
package jp.go.nict.langrid.p2pgridbasis.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class DataAttributes {
	private Map<String, String> attributes;
	
	/**
	 * The constructor.
	 */
	public DataAttributes() {
		this.attributes = new HashMap<String, String>();
	}

	/**
	 * 
	 * 
	 */
	public void setAttribute(String name, String value) {
		this.attributes.put(name, value);
	}
	
	/**
	 * 
	 * 
	 */
	public String getValue(String name) {
		return this.attributes.get(name);
	}

	public String removeValue(String name){
		return this.attributes.remove(name);
	}

	/**
	 * 
	 * 
	 */
	public Set<String> getKeys() {
		return this.attributes.keySet();
	}
	
	public Collection<String> getValues() {
		return this.attributes.values();
	}
	
	/**
	 * 
	 * 
	 */
	public void checkRequiredAttributes(String[] attr_names) throws RequiredAttributeNotFoundException{
		ArrayList<String> notFoundNames = new ArrayList<String>();
		for (String attr_name : attr_names) {
			if(attributes.containsKey(attr_name) == false) {
				notFoundNames.add(attr_name);
			}
		}
		
		if(notFoundNames.size() > 0) {
			throw new RequiredAttributeNotFoundException(notFoundNames.toArray(new String[0]));
		}
	}
}
