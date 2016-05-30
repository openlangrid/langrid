/*
 * $Id: ExternalHomePageLink.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.user.component.link;

import jp.go.nict.langrid.management.web.utility.StringUtil;

import org.apache.wicket.PageMap;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ExternalHomePageLink extends ExternalLink{
	/**
	 * 
	 * 
	 */
	public ExternalHomePageLink(String componentId, String url, String uniqueId){
		super(componentId, url);
		PopupSettings settings = new PopupSettings(PageMap.forName(componentId.concat(uniqueId)),
				PopupSettings.SCROLLBARS | PopupSettings.RESIZABLE
						| PopupSettings.MENU_BAR | PopupSettings.LOCATION_BAR
						| PopupSettings.STATUS_BAR | PopupSettings.TOOL_BAR);
		settings.setHeight(HEIGHT);
		settings.setWidth(WIDTH);
		setPopupSettings(settings);
		add(createLabel(componentId, url).add(new AttributeAppender("title", new Model<String>(url), " ")));
	}

	/**
	 * 
	 * 
	 */
	public ExternalHomePageLink(String componentId, String url, String uniqueId, int limit){
		super(componentId, url);
		PopupSettings settings = new PopupSettings(PageMap.forName(componentId.concat(uniqueId)),
				PopupSettings.SCROLLBARS | PopupSettings.RESIZABLE
						| PopupSettings.MENU_BAR | PopupSettings.LOCATION_BAR
						| PopupSettings.STATUS_BAR | PopupSettings.TOOL_BAR);
		settings.setHeight(HEIGHT);
		settings.setWidth(WIDTH);
		setPopupSettings(settings);
		if(url == null || url.equals("")){
			url = "-";
		}
		add(createLabel(componentId, StringUtil.shortenString(url, limit)).add(
				new AttributeAppender("title", new Model<String>(url), " ")));
		setEnabled( ! (url == null || url.equals("") || url.equals("-")));
	}

	private Label createLabel(String id, String url){
		return new Label(id.concat("Label"), url);
	}

	private final static int HEIGHT = 700;
	private final static int WIDTH = 760;
}
