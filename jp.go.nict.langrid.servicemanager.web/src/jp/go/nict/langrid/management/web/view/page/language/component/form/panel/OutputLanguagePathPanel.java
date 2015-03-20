/*
 * $Id: OutputLanguagePathPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathDirection;
import jp.go.nict.langrid.management.web.view.page.language.component.form.choice.LanguageDirectionDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.component.form.choice.LanguageDropDownChoice;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class OutputLanguagePathPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public OutputLanguagePathPanel(String componentId){
		super(componentId);
		initialize();
	}
	
	/**
    * 
    * 
    */
   public void initialize(){
   	add(direction = new LanguageDirectionDropDownChoice("direction"));
   	add(languageChoice = new LanguageDropDownChoice(languageChoiceId));
   }

   public LanguagePathDirection getDirectionValue() {
	   return direction.getModelObject();
	}
	
	/**
    * 
    * 
    */
   public void setDirectionValue(LanguagePathDirection value){
   	direction.setModelObject(value);
   }
   
   public String getDirectionFromInput(){
      return direction.getInput();
   }

   /**
	 * 
	 * 
	 */
	public Language getLanguageValue(){
		return languageChoice.getModelObject();
	}
	
	public String getLanguageFromInput(){
	   return languageChoice.getInput();
	}

	/**
    * 
    * 
    */
   public void setOutputValue(Language value){
   	languageChoice.setModelObject(value);
   }
   
   public void setValueFromInput() throws InvalidLanguageTagException{
      languageChoice.setModelObject(new Language(languageChoice.getInput()));
      direction.setModelObject(LanguagePathDirection.valueOf(direction.getInput()));
   }

   /**
	 * 
	 * 
	 */
	public void setSimplexOnly(){
		direction.setSimplexOnly();
	}

	private LanguageDirectionDropDownChoice direction;
	private LanguageDropDownChoice languageChoice;
	private static final String languageChoiceId = "languageChoice";
}
