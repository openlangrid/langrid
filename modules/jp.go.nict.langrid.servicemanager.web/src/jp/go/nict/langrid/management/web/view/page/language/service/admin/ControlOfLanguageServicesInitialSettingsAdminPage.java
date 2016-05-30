/*
 * $Id: ControlOfLanguageServicesInitialSettingsAdminPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin;

import jp.go.nict.langrid.management.web.view.page.language.service.ControlOfLanguageServicesInitialSettingsPage;

import org.apache.wicket.Page;

/**
 * 言語資源のデフォルトのアクセス制限編集用ページ
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ControlOfLanguageServicesInitialSettingsAdminPage
extends ControlOfLanguageServicesInitialSettingsPage
{
	/**
	 * 
	 * 
	 */
	public ControlOfLanguageServicesInitialSettingsAdminPage(String targetUserGridId, String serviceGridId, String serviceId){
		super(targetUserGridId, serviceGridId, serviceId);
	}
	
	@Override
	protected Page getBackPage(String serviceId){
		return new ControlOfLanguageServicesAdminPage(serviceId);
	}
	
	@Override
	protected Page getResponsePage(String serviceId, String limitString){
		return new ControlOfLanguageServicesInitialSettingsResultAdminPage(serviceId, limitString);
	}
}
