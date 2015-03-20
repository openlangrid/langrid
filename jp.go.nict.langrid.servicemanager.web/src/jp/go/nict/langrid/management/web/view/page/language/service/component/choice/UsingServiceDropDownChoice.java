/*
 * $Id: UsingServiceDropDownChoice.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.component.choice;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.entity.UseType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class UsingServiceDropDownChoice extends DropDownChoice<UseType>
{
	public UsingServiceDropDownChoice(String id) throws ServiceManagerException {
		super(id, new Model<UseType>(), new WildcardListModel<UseType>()
			, new EnumChoiceRenderer<UseType>());
		List<UseType> list = new ArrayList<UseType>();
		boolean isCommercial = ServiceFactory.getInstance().getGridService().isCommercialUse();
      for(UseType ut : UseType.values()){
         if(ut.equals(UseType.COMMERCIAL_USE) && ! isCommercial){
            continue;
         }
         list.add(ut);
      }
		setChoices(list);
		setNullValid(true);
	}

	public UsingServiceDropDownChoice(String id, String type) throws ServiceManagerException {
		super(id, new Model<UseType>(), new WildcardListModel<UseType>()
			, new EnumChoiceRenderer<UseType>());
		List<UseType> list = new ArrayList<UseType>();
      boolean isCommercial = ServiceFactory.getInstance().getGridService().isCommercialUse();
      for(UseType ut : UseType.values()){
         if(ut.equals(UseType.COMMERCIAL_USE) && ! isCommercial){
            continue;
         }
         list.add(ut);
      }
      setChoices(list);
		if(type != null){
			setModel(new Model<UseType>(UseType.valueOf(type)));
		}
	}
}
