/*
 * $Id: LanguagePairFormPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathDirection;
import jp.go.nict.langrid.management.web.view.page.language.component.form.choice.LanguageDirectionDropDownChoice;
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
public class LanguagePairFormPanel extends FormComponentPanel<LanguagePath[]>{
	/**
	 * 
	 * 
	 */
	public LanguagePairFormPanel(String componentId){
		super(componentId, new Model<LanguagePath[]>());
		add(direction = new LanguageDirectionDropDownChoice("direction"));
		add(inputChoice = new LanguageDropDownChoice("source"));
		add(outputChoice = new LanguageDropDownChoice("target"));
	}
	
	@Override
	protected void convertInput() {
	   LanguagePath path = new LanguagePath(
	      inputChoice.getConvertedInput(), outputChoice.getConvertedInput());
	   
	   LanguagePath[] paths = null;
	   if(direction.getConvertedInput().equals(LanguagePathDirection.SIMPLEX)){
	      paths = new LanguagePath[1];
	      paths[0] = path;
	   }else{
	      paths = new LanguagePath[2];
	      paths[0] = path;
	      paths[1] = path.reverse();
	   }
	   setConvertedInput(paths);
	}
	
	private LanguageDirectionDropDownChoice direction;
   private LanguageDropDownChoice inputChoice;
   private LanguageDropDownChoice outputChoice;
}
