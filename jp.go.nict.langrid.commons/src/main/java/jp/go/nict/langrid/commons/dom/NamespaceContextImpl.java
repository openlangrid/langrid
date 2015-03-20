/*
 * $Id: NamespaceContextImpl.java 182 2010-10-02 03:16:36Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2009 NICT Language Grid Project.
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class NamespaceContextImpl implements NamespaceContext{
	/**
	 * 
	 * 
	 */
	public NamespaceContextImpl(){
	}

	/**
	 * 
	 * 
	 */
	public NamespaceContextImpl(NamespaceContext parent){
		this.parent = parent;
	}

	public String getNamespaceURI(String prefix) {
		String ret = prefixToNs.get(prefix);
		if(ret != null){
			return ret;
		} else{
			return parent.getNamespaceURI(prefix);
		}
	}

	public String getPrefix(String namespaceURI) {
		List<String> ret = nsToPrefixes.get(namespaceURI);
		if(ret != null){
			return ret.get(ret.size() - 1);
		} else{
			return parent.getPrefix(namespaceURI);
		}
	}

	public Iterator<?> getPrefixes(String namespaceURI) {
		List<String> ret = nsToPrefixes.get(namespaceURI);
		if(ret != null){
			return Collections.unmodifiableCollection(ret).iterator();
		} else{
			return parent.getPrefixes(namespaceURI);
		}
	}

	/**
	 * 
	 * 
	 */
	public void addMapping(String prefix, String namespace){
		String ns = prefixToNs.get(prefix);
		if(ns != null){
			if(ns.equals(namespace)) return;
			throw new IllegalArgumentException(
					"The prefix \"" + prefix + "\" is already mapped to ns \""
					+ namespace + "\"");
		}
		prefixToNs.put(prefix, namespace);
		List<String> prefixes = nsToPrefixes.get(prefix);
		if(prefixes == null){
			prefixes = new ArrayList<String>();
			nsToPrefixes.put(namespace, prefixes);
		}
		prefixes.add(prefix);
	}

	private NamespaceContext parent = new NamespaceContext(){
		public String getNamespaceURI(String prefix) {
			return null;
		}
		public String getPrefix(String namespaceURI) {
			return null;
		}
		public Iterator<?> getPrefixes(String namespaceURI) {
			return null;
		}
	};
	private Map<String, String> prefixToNs = new HashMap<String, String>();
	private Map<String, List<String>> nsToPrefixes = new HashMap<String, List<String>>();
}
