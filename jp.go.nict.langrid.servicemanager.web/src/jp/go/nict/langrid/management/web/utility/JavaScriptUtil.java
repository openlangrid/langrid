/*
 * $Id: JavaScriptUtil.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.utility;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class JavaScriptUtil{
	/**
	 * 
	 * 
	 */
	public static String getCheckBoxValidatorAndAlertScript(
			String defaultScript, LinkedHashMap<String, String> nodes, String confirmMessage)
	{
		return "var flag = false;"
		+ "var smForms = document.getElementsByTagName('form');"
		+ "if(smForms == null){"
			+ "return;"
		+ "}"
		+ "for(var i = 0; i < smForms.length; i++){" 
			+ "formElements = smForms[i].elements;"
			+ "if(formElements == null){"
				+ "return;"
			+ "}"
			+ "for(var j = 0; j < formElements.length; j++){"
				+ "if(formElements[j].getAttribute('type') == 'checkbox'){"
					+ "if(formElements[j].checked){"
						+ "flag = true"
					+ "}"
				+ "}"
			+ "}"
		+ "}"
		+ "if(flag){"
			+ "if(confirm('" + confirmMessage + "')){"
				+ defaultScript
			+ "}"
			+ "return false;"
		+ "}else{"
			+ getScriptToRemoveChildren(nodes) + getScriptToMoveWindowTop()
			+ "return false;"
		+ "}";
	}

	private static String getScriptToMoveWindowTop(){
		return "scrollTo(0,0);";
	}

	/**
	 * 
	 * 
	 */
	private static String getScriptToRemoveChildren(LinkedHashMap<String, String> nodes){
		String removeString = "";
		String message = "";
		Set<String> ids = nodes.keySet();
		for(String id : ids){
			message = nodes.get(id);
			if(message == null || message.equals("")){
				removeString += getScriptToRemoveChildrenById(id);
			}else{
				removeString += getScriptToRemoveChildrenAndAddMessageById(id, message);
			}
		}
		return removeString;
	}

	/**
	 * 
	 * 
	 */
	private static String getScriptToRemoveChildrenAndAddMessageById(
			String id, String message)
	{
		return getScriptToRemoveChildrenById(id)
				+ "node.appendChild(document.createTextNode(\"" + message + "\"));";
	}

	/**
	 * 
	 * 
	 */
	private static String getScriptToRemoveChildrenById(String id){
		return "var node = document.getElementById(\"" + id + "\");"
				+ "var children = node.childNodes;" + "while(0<children.length){"
				+ "node.removeChild(children[0]);" + "}";
	}
}
