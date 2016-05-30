/*
 * $Id: LanguageDirectionDropDownChoice.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.component.form.choice;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathDirection;

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
public class LanguageDirectionDropDownChoice extends DropDownChoice<LanguagePathDirection>{
	public LanguageDirectionDropDownChoice(String componentId){
		super(componentId, new Model<LanguagePathDirection>()
			, new WildcardListModel<LanguagePathDirection>()
			, new EnumChoiceRenderer<LanguagePathDirection>());
		List<LanguagePathDirection> enums = LanguagePathDirection.getListWithoutCombination();
		setChoices(new ArrayList<LanguagePathDirection>(enums));
		setEscapeModelStrings(false);
	}

	@Override
	public CharSequence getDefaultChoice(Object selected){
		return null;
	}

	/**
	 * 
	 * 
	 */
	public void setSimplexOnly(){
		ArrayList<LanguagePathDirection> list = new ArrayList<LanguagePathDirection>();
		list.add(LanguagePathDirection.SIMPLEX);
		setChoices(list);
	}

	/**
	 * 
	 * 
	 */
	public void setUse(boolean flag){
		this.setEnabled(flag);
		this.setVisible(flag);
	}

	private static final long serialVersionUID = 1L;
}
