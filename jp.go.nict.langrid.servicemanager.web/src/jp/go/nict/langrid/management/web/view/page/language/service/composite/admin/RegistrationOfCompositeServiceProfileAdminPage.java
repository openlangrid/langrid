/*
 * $Id: RegistrationOfCompositeServiceProfileAdminPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.composite.admin;

import jp.go.nict.langrid.management.web.model.CompositeServiceModel;
import jp.go.nict.langrid.management.web.model.JavaCompositeServiceModel;
import jp.go.nict.langrid.management.web.view.page.language.service.composite.RegistrationOfCompositeServiceProfilePage;

import org.apache.wicket.Page;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class RegistrationOfCompositeServiceProfileAdminPage
extends RegistrationOfCompositeServiceProfilePage
{
	/**
	 * 
	 * 
	 */
	public RegistrationOfCompositeServiceProfileAdminPage(String ownerId){
	   super(ownerId);
	}

	public RegistrationOfCompositeServiceProfileAdminPage(CompositeServiceModel model, boolean isEdit){
		super(model, isEdit);
	}
	
	@Override
	protected Page getJavaRegistPage(String gridId, String ownerId, JavaCompositeServiceModel model) {
		return new RegistrationOfJavaCompositeServiceAdminPage(gridId, ownerId, model);
	}
	
	@Override
	protected Page getBpelRegistPage(String gridId, String ownerId, CompositeServiceModel model) {
		return new RegistrationOfBpelCompositeServiceAdminPage(gridId, ownerId, model);
	}
	
	@Override
	protected void doCancelProcess() {
		setResponsePage(new RegistrationOfCompositeServiceUserListPage());
	}
}
