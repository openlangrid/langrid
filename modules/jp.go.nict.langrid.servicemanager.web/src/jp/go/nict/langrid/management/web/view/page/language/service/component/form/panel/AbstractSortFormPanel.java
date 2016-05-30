/*
 * $Id: AbstractSortFormPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel;

import jp.go.nict.langrid.management.web.view.model.ServiceSortOrder;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public abstract class AbstractSortFormPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public AbstractSortFormPanel(String componentId){
		super(componentId);
		form = new StatelessForm(formId = "sortForm");
		initialize();
	}

	/**
	 * 
	 * 
	 */
	public abstract ServiceSortOrder getSortOrder();

	protected abstract void initialize();

	protected Form form;
	protected String formId;
	private static final long serialVersionUID = 1L;
}
