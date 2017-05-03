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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class SoapHeaderAdapter implements SOAPHeader{
	@Override
	public SOAPHeaderElement addHeaderElement(Name arg0) throws SOAPException {
		return null;
	}

	@Override
	public Iterator<SOAPHeaderElement> examineAllHeaderElements() {
		return empty.iterator();
	}

	@Override
	public Iterator<SOAPHeaderElement> examineHeaderElements(String arg0) {
		return empty.iterator();
	}

	@Override
	public Iterator<SOAPHeaderElement> examineMustUnderstandHeaderElements(String arg0) {
		return empty.iterator();
	}

	@Override
	public Iterator<SOAPHeaderElement> extractAllHeaderElements() {
		return empty.iterator();
	}

	@Override
	public Iterator<SOAPHeaderElement> extractHeaderElements(String arg0) {
		return empty.iterator();
	}

	@Override
	public SOAPElement addAttribute(Name arg0, String arg1)
			throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addChildElement(Name arg0) throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addChildElement(String arg0) throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addChildElement(SOAPElement arg0) throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addChildElement(String arg0, String arg1)
			throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addChildElement(String arg0, String arg1, String arg2)
			throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addNamespaceDeclaration(String arg0, String arg1)
			throws SOAPException {
		return null;
	}

	@Override
	public SOAPElement addTextNode(String arg0) throws SOAPException {
		return null;
	}

	@Override
	public Iterator<?> getAllAttributes() {
		return null;
	}

	@Override
	public String getAttributeValue(Name arg0) {
		return null;
	}

	@Override
	public Iterator<?> getChildElements() {
		return null;
	}

	@Override
	public Iterator<?> getChildElements(Name arg0) {
		return null;
	}

	@Override
	public Name getElementName() {
		return null;
	}

	@Override
	public String getEncodingStyle() {
		return null;
	}

	@Override
	public Iterator<?> getNamespacePrefixes() {
		return null;
	}

	@Override
	public String getNamespaceURI(String arg0) {
		return null;
	}

	@Override
	public Iterator<?> getVisibleNamespacePrefixes() {
		return null;
	}

	@Override
	public boolean removeAttribute(Name arg0) {
		return false;
	}

	@Override
	public void removeContents() {
	}

	@Override
	public boolean removeNamespaceDeclaration(String arg0) {
		return false;
	}

	@Override
	public void setEncodingStyle(String arg0) throws SOAPException {
	}

	@Override
	public void detachNode() {
	}

	@Override
	public SOAPElement getParentElement() {
		return null;
	}

	@Override
	public String getValue() {
		return null;
	}

	@Override
	public void recycleNode() {
	}

	@Override
	public void setParentElement(SOAPElement arg0) throws SOAPException {
	}

	@Override
	public void setValue(String arg0) {
	}

	@Override
	public Node appendChild(Node newChild) throws DOMException {
		return null;
	}

	@Override
	public Node cloneNode(boolean deep) {
		return null;
	}

	@Override
	public short compareDocumentPosition(Node other) throws DOMException {
		return 0;
	}

	@Override
	public NamedNodeMap getAttributes() {
		return null;
	}

	@Override
	public String getBaseURI() {
		return null;
	}

	@Override
	public NodeList getChildNodes() {
		return null;
	}

	@Override
	public Object getFeature(String feature, String version) {
		return null;
	}

	@Override
	public Node getFirstChild() {
		return null;
	}

	@Override
	public Node getLastChild() {
		return null;
	}

	@Override
	public String getLocalName() {
		return null;
	}

	@Override
	public String getNamespaceURI() {
		return null;
	}

	@Override
	public Node getNextSibling() {
		return null;
	}

	@Override
	public String getNodeName() {
		return null;
	}

	@Override
	public short getNodeType() {
		return 0;
	}

	@Override
	public String getNodeValue() throws DOMException {
		return null;
	}

	@Override
	public Document getOwnerDocument() {
		return null;
	}

	@Override
	public Node getParentNode() {
		return null;
	}

	@Override
	public String getPrefix() {
		return null;
	}

	@Override
	public Node getPreviousSibling() {
		return null;
	}

	@Override
	public String getTextContent() throws DOMException {
		return null;
	}

	@Override
	public Object getUserData(String key) {
		return null;
	}

	@Override
	public boolean hasAttributes() {
		return false;
	}

	@Override
	public boolean hasChildNodes() {
		return false;
	}

	@Override
	public Node insertBefore(Node newChild, Node refChild) throws DOMException {
		return null;
	}

	@Override
	public boolean isDefaultNamespace(String namespaceURI) {
		return false;
	}

	@Override
	public boolean isEqualNode(Node arg) {
		return false;
	}

	@Override
	public boolean isSameNode(Node other) {
		return false;
	}

	@Override
	public boolean isSupported(String feature, String version) {
		return false;
	}

	@Override
	public String lookupNamespaceURI(String prefix) {
		return null;
	}

	@Override
	public String lookupPrefix(String namespaceURI) {
		return null;
	}

	@Override
	public void normalize() {
	}

	@Override
	public Node removeChild(Node oldChild) throws DOMException {
		return null;
	}

	@Override
	public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
		return null;
	}

	@Override
	public void setNodeValue(String nodeValue) throws DOMException {
	}

	@Override
	public void setPrefix(String prefix) throws DOMException {
	}

	@Override
	public void setTextContent(String textContent) throws DOMException {
	}

	@Override
	public Object setUserData(String key, Object data, UserDataHandler handler) {
		return null;
	}

	@Override
	public String getAttribute(String name) {
		return null;
	}

	@Override
	public String getAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		return null;
	}

	@Override
	public Attr getAttributeNode(String name) {
		return null;
	}

	@Override
	public Attr getAttributeNodeNS(String namespaceURI, String localName)
			throws DOMException {
		return null;
	}

	@Override
	public NodeList getElementsByTagName(String name) {
		return null;
	}

	@Override
	public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
			throws DOMException {
		return null;
	}

	@Override
	public TypeInfo getSchemaTypeInfo() {
		return null;
	}

	@Override
	public String getTagName() {
		return null;
	}

	@Override
	public boolean hasAttribute(String name) {
		return false;
	}

	@Override
	public boolean hasAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		return false;
	}

	@Override
	public void removeAttribute(String name) throws DOMException {

	}

	@Override
	public void removeAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		
	}

	@Override
	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		return null;
	}

	@Override
	public void setAttribute(String name, String value) throws DOMException {
		
	}

	@Override
	public void setAttributeNS(String namespaceURI, String qualifiedName,
			String value) throws DOMException {
		
	}

	@Override
	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		return null;
	}

	@Override
	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		return null;
	}

	@Override
	public void setIdAttribute(String name, boolean isId) throws DOMException {
		
	}

	@Override
	public void setIdAttributeNS(String namespaceURI, String localName,
			boolean isId) throws DOMException {
		
	}

	@Override
	public void setIdAttributeNode(Attr idAttr, boolean isId)
			throws DOMException {
		
	}
	
	public SOAPHeaderElement addUpgradeHeaderElement(String arg){
		return null;
	}
	
	public SOAPHeaderElement addUpgradeHeaderElement(String[] arg){
		return null;
	}

	@SuppressWarnings("rawtypes")
	public SOAPHeaderElement addUpgradeHeaderElement(Iterator arg){
		return null;
	}

	public SOAPHeaderElement addNotUnderstoodHeaderElement(QName arg){
		return null;
	}
	
	public SOAPHeaderElement addHeaderElement(QName arg){
		return null;
	}

	public Iterator<?> getChildElements(QName arg){
		return null;
	}

	public boolean removeAttribute(QName arg){
		return false;
	}

	public SOAPElement setElementQName(QName arg){
		return null;
	}
	
	public QName getElementQName(){
		return null;
	}

	public QName createQName(String arg0, String arg1){
		return null;
	}

	public Iterator<?> getAllAttributesAsQNames(){
		return null;
	}

	public String getAttributeValue(QName arg){
		return null;
	}

	public SOAPElement addAttribute(QName arg0, String arg1){
		return null;
	}

	public SOAPHeaderElement addChildElement(QName arg){
		return null;
	}

	private static List<SOAPHeaderElement> empty = Arrays.asList(new SOAPHeaderElement[]{}); 
}
