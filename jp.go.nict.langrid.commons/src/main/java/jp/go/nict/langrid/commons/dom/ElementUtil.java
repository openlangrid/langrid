/*
 * $Id: ElementUtil.java 182 2010-10-02 03:16:36Z t-nakaguchi $
 *
 * Copyright (c) 2002, 2004 Takao Nakaguchi.
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
package jp.go.nict.langrid.commons.dom;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author: t-nakaguchi $
 * @version $Revision: 182 $
 */
public class ElementUtil extends NodeUtil{
	/**
	 * 
	 * 
	 */
	public static void removeChildren(Element parent, String tagName) {
		for(Element e : ElementUtil.getChildNodes(parent, tagName)){
			parent.removeChild(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void removeChildren(Element parent) {
		for(Element e : ElementUtil.getChildNodes(parent)){
			parent.removeChild(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public static void replaceElements(Element parent, Element element) {
		removeChildren(parent, element.getNodeName());
		parent.appendChild(element);
	}

	/**
	 * 
	 * 
	 */
	public static Document prepareParentDocument(Element element){
		Document document = null;
		Node parent = element.getParentNode();
		if(parent != null){
			if(parent instanceof Document){
				document = (Document)parent;
			} else{
				element = (Element)element.cloneNode(true);
			}
		}
		if(document == null){
			document = DocumentUtil.newDocument();
			document.adoptNode(element);
			document.appendChild(element);
		}
		return document;
	}

	/**
	 * 
	 * 
	 */
	public static Iterable<Element> getChildNodes(Element parent){
		final NodeList nodes = parent.getChildNodes();
		return new Iterable<Element>(){
			public Iterator<Element> iterator() {
				return new Iterator<Element>(){
					public void remove() {
						throw new UnsupportedOperationException();
					}
					public boolean hasNext() {
						return nodes.getLength() > c;
					}
					public Element next() {
						if(c >= nodes.getLength()){
							throw new NoSuchElementException();
						}
						return (Element)nodes.item(c++);
					}
					private int c = 0;
				};
			}
		};
	}

	/**
	 * 
	 * 
	 */
	public static Collection<Element> getChildNodes(Element parent, String tagName){
		return new ElementUtil.ElementListCollection(parent.getElementsByTagName(tagName));
	}

	/**
	 * 
	 * 
	 */
	public static String getChildNodeText(Element parent, String childTagName){
		Element node = ElementUtil.getFirstChild(parent, childTagName);
		if(node == null) return null;
		return node.getTextContent();
	}

	/**
	 * 
	 * 
	 */
	public static Element getFirstChild(Element parent, String childTagName){
		NodeList nodes = parent.getElementsByTagName(childTagName);
		if(nodes.getLength() == 0) return null;
		return (Element)nodes.item(0);
	}

	/**
	 * 
	 * 
	 */
	public static final String toString(Element element) {
		return DocumentUtil.toString(prepareParentDocument(element));
	}

	private static class ElementListCollection implements Collection<Element>{
		public ElementListCollection(NodeList nodes){
			this.nodes = nodes;
		}
	
		public boolean add(Element o){
			throw new UnsupportedOperationException("ElementListCollection.add");
		}
	
		public boolean addAll(Collection<? extends Element> c){
			throw new UnsupportedOperationException("ElementListCollection.addAll");
		}
	
		public void clear(){
			throw new UnsupportedOperationException("ElementListCollection.clear");
		}
	
		public boolean contains(Object o){
			for(Element e : this){
				if(e.equals(o)) return true;
			}
			return false;
		}
	
		public boolean containsAll(Collection<?> c){
			for(Object o : c){
				if(!contains(o)) return false;
			}
			return true;
		}
	
		public boolean equals(Object o){
			if(!(o instanceof ElementListCollection)) return false;
			ElementListCollection v = (ElementListCollection)o;
			int n = size();
			if(n != v.size()) return false;
			Iterator<Element> s = iterator();
			Iterator<Element> d = v.iterator();
			while(s.hasNext()){
				if(!s.next().equals(d.next())) return false;
			}
			return true;
		}
	
		public int hashCode(){
			int hashCode = 0;
			for(Element e : this){
				hashCode = hashCode * 31 + e.hashCode();
			}
			return hashCode;
		}
	
		public boolean isEmpty(){
			return size() == 0;
		}
	
		public Iterator<Element> iterator() {
			return new Iterator<Element>(){
				public void remove() {
					throw new UnsupportedOperationException();
				}
				public boolean hasNext() {
					return nodes.getLength() > c;
				}
				public Element next() {
					if(c >= nodes.getLength()){
						throw new NoSuchElementException();
					}
					return (Element)nodes.item(c++);
				}
				private int c = 0;
			};
		}
	
		public boolean remove(Object o){
			throw new UnsupportedOperationException("ElementListCollection.remove");
		}
	
		public boolean removeAll(Collection<?> c){
			throw new UnsupportedOperationException("ElementListCollection.removeAll");
		}
	
		public boolean retainAll(Collection<?> c){
			throw new UnsupportedOperationException("ElementListCollection.retainAll");
		}
	
		public int size(){
			return nodes.getLength();
		}
	
	    public Object[] toArray(){
			Object[] objects = new Object[nodes.getLength()];
			for(int i = 0; i < objects.length; i++){
				objects[i] = nodes.item(i);
			}
			return objects;
		}
	
		@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] a){
			if(!Element.class.isAssignableFrom(a.getClass().getComponentType())){
				throw new ArrayStoreException();
			}
			T[] elements = a;
			if(elements.length < nodes.getLength()){
				elements = (T[])Array.newInstance(
						a.getClass().getComponentType()
						, nodes.getLength()
						);
			}
			for(int i = 0; i < nodes.getLength(); i++){
				elements[i] = (T)nodes.item(i);
			}
			return elements;
		}
	
		private NodeList nodes;
	}
}
