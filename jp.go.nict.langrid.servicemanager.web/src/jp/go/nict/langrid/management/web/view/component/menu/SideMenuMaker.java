/*
 * $Id: SideMenuMaker.java 303 2010-12-01 04:21:52Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.component.menu;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jp.go.nict.langrid.management.web.model.enumeration.UserRole;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.menu.panel.SideMenuPanel;
import jp.go.nict.langrid.management.web.view.page.admin.OperationRequestPage;
import jp.go.nict.langrid.management.web.view.page.other.LoginPage;
import jp.go.nict.langrid.management.web.view.page.other.OverviewPage;
import jp.go.nict.langrid.management.web.view.page.other.Unoperatable;

import org.apache.wicket.IClusterable;
import org.apache.wicket.markup.html.panel.Panel;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class SideMenuMaker implements IClusterable{
	public Panel makeMenu(
			String componentId, String requestPageName, Set<UserRole> userRoles)
	throws ServiceManagerException
	{
		BufferedInputStream bis = null;
		try {
			String classPath = getClass().getPackage().getName().replace(".", "/") + "/" + MessageManager.getMessage("menu.file");
			ClassLoader loader = getClass().getClassLoader();
			bis = new BufferedInputStream(loader.getResourceAsStream(classPath));
			Document menuDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(bis);
	
			// link target node
			Node target = getOpenTargetNode(requestPageName, menuDoc, userRoles);
			Node first = menuDoc.getFirstChild().getFirstChild();
			List<Node> topLinkList = createClassNameMap(first, userRoles);
			List<Node> subLinkList = createClassNameMap(target, userRoles);
			SideMenuPanel smp = new SideMenuPanel(componentId, userRoles);
			smp.setRenderBodyOnly(true);

			if(target.getParentNode().isSameNode(first.getParentNode())
				&& ! target.getAttributes().getNamedItem("class").getNodeValue().equals(Unoperatable.class.getName()))
			{
				smp.makeMenuComponents(topLinkList);
			} else {
				smp.makeNestMenuComponents(target, topLinkList, subLinkList);
			}
			return smp;
		} catch(IOException e) {
			if(bis != null) {
				try {
					bis.close();
				} catch (IOException e1) {
					e1.setStackTrace(e.getStackTrace());
					e = e1;
				}
			}
			throw new ServiceManagerException(e, getClass());
		} catch (SAXException e) {
			throw new ServiceManagerException(e, getClass());
		} catch (ParserConfigurationException e) {
			throw new ServiceManagerException(e, getClass());
		} catch (XPathExpressionException e) {
			throw new ServiceManagerException(e, getClass());
		} catch (DOMException e) {
			throw new ServiceManagerException(e, getClass());
		} catch (ClassNotFoundException e) {
			throw new ServiceManagerException(e, getClass());
		}
	}

	private List<Node> createClassNameMap(Node target, Set<UserRole> userRoles) {
		List<Node> cn = new ArrayList<Node>();
		Node current = target.getParentNode().getFirstChild();
		do {
			Node node = current;
			String requiredRole = "NONE";
			while(node != null && node.getAttributes() != null) {
				Node sessionNode = node.getAttributes().getNamedItem("requiredRoles");
				if(sessionNode != null && sessionNode.getNodeType() == Node.ATTRIBUTE_NODE) {
					requiredRole = sessionNode.getTextContent();
					break;
				}
				node = node.getParentNode();
			}
			if((current.getNodeType() == Node.ELEMENT_NODE) && isAuthorize(requiredRole, userRoles)) {
				cn.add(current);
			}
		}while((current = current.getNextSibling()) != null);
		return cn;
	}
	
	private boolean isAuthorize(String requiredRole, Set<UserRole> userRoles) {
		requiredRole = requiredRole.toUpperCase(Locale.ENGLISH);
		if(requiredRole.equals("NONE")) return true;
		if(requiredRole.equals("ANY")) return !userRoles.isEmpty();
		if(requiredRole.equals("EMPTY")) return userRoles.isEmpty();
		if(userRoles.contains(UserRole.valueOf(requiredRole))) return true;
		return false;
	}
	
	private Node getOpenTargetNode(String requestPageName, Document doc, Set<UserRole> userRoles)
	throws XPathExpressionException
	{
		// replace for logout page.
		if(requestPageName.endsWith("LogOutPage")){
			requestPageName = requestPageName.replaceAll("LogOutPage$", "Page");
		}
		if (requestPageName.equals(LoginPage.class.getName())) {
			requestPageName = OverviewPage.class.getName();
		}
		if (requestPageName.equals(Unoperatable.class.getName())) {
			requestPageName = OperationRequestPage.class.getName();
		}

		XPath xpath = XPathFactory.newInstance().newXPath();

		NodeList targetList = (NodeList) xpath.evaluate(
				"//link[@class='"+ requestPageName + "']"
			+ " | //relatedpage[@class='"+ requestPageName + "']"
			, doc, XPathConstants.NODESET);
		if(targetList.getLength() == 0) {
			return (Node) xpath.evaluate(
					"//link[@class='"+ OverviewPage.class.getName() + "']"
					, doc, XPathConstants.NODE);
		}
		Node target = targetList.item(targetList.getLength() - 1);
		if(target.getNodeName().equals("relatedpage")) {
			target = target.getParentNode();
		}
		
		return target;
	}

	private static final long serialVersionUID = 1L;
}
