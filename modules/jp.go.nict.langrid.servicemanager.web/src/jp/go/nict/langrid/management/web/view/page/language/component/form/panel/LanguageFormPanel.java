/*
 * $Id: LanguageFormPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.component.form.panel;

import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.view.page.language.component.form.choice.LanguageDropDownChoice;

import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.Model;



/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class LanguageFormPanel extends FormComponentPanel<LanguagePath[]>{
	/**
	 * 
	 * 
	 */
	public LanguageFormPanel(String componentId){
		super(componentId, new Model<LanguagePath[]>());
		add(inputChoice = new LanguageDropDownChoice("source"));
	}

	@Override
	protected void convertInput() {
	   LanguagePath[] paths = new LanguagePath[1];
	   paths[0] = new LanguagePath(inputChoice.getConvertedInput());
	   setConvertedInput(paths);
	}
	
   private LanguageDropDownChoice inputChoice;
}
