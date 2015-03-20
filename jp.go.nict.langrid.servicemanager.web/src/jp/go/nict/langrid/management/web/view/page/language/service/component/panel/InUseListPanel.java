/*
 * $Id: InUseListPanel.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.component.panel;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public abstract class InUseListPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public static String getRESOURCE_LINK_ID(){
		return RESOURCE_LINK_ID;
	}

	protected static String getDeleteIdId(){
		return DELETE_ID_ID;
	}

	protected static String getDeleteInfomationId(){
		return DELETE_INFOMATION_ID;
	}

	protected static String getInfomationId(){
		return INFOMATION_ID;
	}

	protected static String getLabelCountId(){
		return LABEL_COUNT_ID;
	}

	protected static String getLabelIdId(){
		return LABEL_ID_ID;
	}

	protected static String getLabelNameId(){
		return LABEL_NAME_ID;
	}

	protected static String getLabelTypeId(){
		return LABEL_TYPE_ID;
	}

	protected static String getProfileLinkId(){
		return PROFILE_LINK_ID;
	}

	protected static String getRepeatingId(){
		return REPEATING_ID;
	}
	
	public static String getInvocateInfomationId() {
		return INVOCATE_INFOMATION_ID;
	}

	/**
	 * 
	 * 
	 */
	public InUseListPanel(){
		super(InUseListId);
	}
	
	public InUseListPanel(String panelId) {
		super(panelId);
	}

	/**
	 * 
	 * 
	 */
	public void makeList() throws ServiceManagerException{
		createList();
	}

	protected abstract void createList() throws ServiceManagerException;

	private static final String DELETE_ID_ID = "deleteId";
	private static final String DELETE_INFOMATION_ID = "deleteInformation";
	private static final String INVOCATE_INFOMATION_ID = "invocateInformation";
	private static final String INFOMATION_ID = "infomation";
	private static final String InUseListId = "InUseList";
	private static final String LABEL_COUNT_ID = "labelCount";
	private static final String LABEL_ID_ID = "labelId";
	private static final String LABEL_NAME_ID = "labelName";
	private static final String LABEL_TYPE_ID = "labelType";
	private static final String PROFILE_LINK_ID = "profileLink";
	private static final String REPEATING_ID = "repeating";
	private static final String RESOURCE_LINK_ID = "resourceLink";
	private static final long serialVersionUID = 1L;
}
