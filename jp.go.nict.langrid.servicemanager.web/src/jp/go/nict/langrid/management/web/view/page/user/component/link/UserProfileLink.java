/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services.
 * 
 * Copyright (C) 2005-2010 NICT Language Grid Project.
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

import jp.go.nict.langrid.management.web.view.component.link.GridResourcePopupLink;
import jp.go.nict.langrid.management.web.view.page.user.UserProfilePage;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author Takao Nakaguchi
 */
public class UserProfileLink extends GridResourcePopupLink<UserProfilePage>{
	/**
	 * 
	 * 
	 */
	public UserProfileLink(String label, String gridId, String userId, String uniqueId){
		super(label, gridId, userId, uniqueId, UserProfilePage.class);
		settings();
	}

	private void settings(){
		getSettings().setHeight(530);
	}
}
