/*
 * $Id: ServiceTypeProfileLink.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin.type.component.link;

import jp.go.nict.langrid.management.web.view.component.link.DomainResourcePopupLink;
import jp.go.nict.langrid.management.web.view.page.language.service.admin.type.ServiceTypeProfilePage;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ServiceTypeProfileLink extends DomainResourcePopupLink<ServiceTypeProfilePage>{
	/**
	 * 
	 * 
	 */
	public ServiceTypeProfileLink(String label, String domainId, String typeId, String uniqueId){
		super(label, domainId, typeId, uniqueId, ServiceTypeProfilePage.class);
		getSettings().setHeight(400);
		this.typeId = typeId;
	}
	
	@Override
	public boolean isEnabled() {
		return ! typeId.equals("Other");
	}
	
	private String typeId;
}
