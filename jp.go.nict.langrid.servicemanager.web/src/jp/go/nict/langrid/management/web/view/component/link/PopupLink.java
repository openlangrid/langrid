/*
 * $Id: PopupLink.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.component.link;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.PopupSettings;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public abstract class PopupLink<T extends Page> extends BookmarkablePageLink<T>{
	/**
	 * 
	 * 
	 */
	public PopupLink(final String label, String id, final String uniqueId, Class<T> pageClass){
		super(label, pageClass);
		setParameter("id", id);
		settings = new PopupSettings(PopupSettings.SCROLLBARS | PopupSettings.RESIZABLE);
		settings.setHeight(HEIGHT);
		settings.setWidth(WIDTH);
		settings.setTop(TOP);
		settings.setLeft(LEFT);
		setPopupSettings(settings);
	}

	/**
	 * 
	 * 
	 */
	public PopupSettings getSettings(){
		return settings;
	}

	@Override
	protected CharSequence getURL(){
		return super.getURL().toString().replaceAll(";jsessionid=[^\\?]+", "");
	}

	protected String id;
	private PopupSettings settings;
	private final static int HEIGHT = 730;
	private final static int LEFT = 200;
	private final static long serialVersionUID = 1L;
	private final static int TOP = 0;
	private final static int WIDTH = 760;
}
