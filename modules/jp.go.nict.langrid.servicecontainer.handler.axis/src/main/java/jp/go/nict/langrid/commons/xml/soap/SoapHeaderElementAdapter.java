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
package jp.go.nict.langrid.commons.xml.soap;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class SoapHeaderElementAdapter
implements SOAPHeaderElement{
	@Override
	public String getActor() {
		return null;
	}

	@Override
	public boolean getMustUnderstand() {
		return false;
	}

	//@Override
	public boolean getRelay() {
		return false;
	}

	//@Override
	public String getRole() {
		return null;
	}

	@Override
	public void setActor(String actorURI) {
	}

	@Override
	public void setMustUnderstand(boolean mustUnderstand) {
	}

	//@Override
	public void setRelay(boolean relay) throws SOAPException {
	}

	//@Override
	public void setRole(String uri) throws SOAPException {
	}

	@Override
	public SOAPElement addAttribute(Name name, String value)
			throws SOAPException {
		return null;
	}

	//@Override
	public SOAPElement addAttribute(QName qname, String value)
			throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addChildElement(Name name) throws SOAPException {
		return null;
	}

	//@Override
	public SOAPElement addChildElement(QName qname) throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addChildElement(String localName) throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addChildElement(SOAPElement element)
			throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addChildElement(String localName, String prefix)
			throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addChildElement(String localName, String prefix,
			String uri) throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addNamespaceDeclaration(String prefix, String uri)
			throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addTextNode(String text) throws SOAPException {
		return null;
	}

	//@Override
	public QName createQName(String localName, String prefix)
			throws SOAPException {
		return null;
	}

	@Override
	public Iterator<Name> getAllAttributes() {
		return null;
	}

	//@Override
	public Iterator<QName> getAllAttributesAsQNames() {
		return null;
	}

	@Override
	public String getAttributeValue(Name name) {
		return null;
	}

	//@Override
	public String getAttributeValue(QName qname) {
		return null;
	}

	@Override
	public Iterator<SOAPElement> getChildElements() {
		return null;
	}

	@Override
	public Iterator<SOAPElement> getChildElements(Name name) {
		return null;
	}

	//@Override
	public Iterator<SOAPElement> getChildElements(QName qname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Name getElementName() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public QName getElementQName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEncodingStyle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<String> getNamespacePrefixes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNamespaceURI(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<String> getVisibleNamespacePrefixes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAttribute(Name name) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public boolean removeAttribute(QName qname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeContents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removeNamespaceDeclaration(String prefix) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public SOAPElement setElementQName(QName newName) throws SOAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEncodingStyle(String encodingStyle) throws SOAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void detachNode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SOAPElement getParentElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recycleNode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParentElement(SOAPElement parent) throws SOAPException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node appendChild(Node newChild) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node cloneNode(boolean deep) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short compareDocumentPosition(Node other) throws DOMException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public NamedNodeMap getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBaseURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getChildNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getFeature(String feature, String version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getFirstChild() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getLastChild() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNamespaceURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getNextSibling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getNodeType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getNodeValue() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getOwnerDocument() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getParentNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getPreviousSibling() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTextContent() throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getUserData(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasChildNodes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDefaultNamespace(String namespaceURI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEqualNode(Node arg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSameNode(Node other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSupported(String feature, String version) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String lookupNamespaceURI(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String lookupPrefix(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void normalize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node removeChild(Node oldChild) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPrefix(String prefix) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTextContent(String textContent) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr getAttributeNode(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr getAttributeNodeNS(String namespaceURI, String localName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
			throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttribute(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String name) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String name, String value) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttributeNS(String namespaceURI, String qualifiedName,
			String value) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdAttribute(String name, boolean isId) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIdAttributeNS(String namespaceURI, String localName,
			boolean isId) throws DOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIdAttributeNode(Attr idAttr, boolean isId)
			throws DOMException {
		// TODO Auto-generated method stub
		
	}
	
}
