/*
 * $Id: AbstractLanguagePathPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.view.page.language.component.form.choice.LanguageDropDownChoice;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public abstract class AbstractLanguagePathPanel
extends Panel
{
	/**
	 * 
	 * 
	 */
	public AbstractLanguagePathPanel(String componentId){
		super(componentId);
		add(inputLanguageChoice = new LanguageDropDownChoice(inputLanguageId));
	}
	
	public void addVisibleComponent(Component component){
      switchVisibleList.add(component);
   }
   
   public void switchVisible(){
      for(Component c : switchVisibleList){
         c.setVisible(!c.isVisible());
      }
   }
   
   public void setAllVisibled(){
      for(Component c : switchVisibleList){
         c.setVisible(true);
      }
   }
   
   public abstract String[] getValueFromInput();
   
   public abstract void setValueFromInput() throws InvalidLanguageTagException;

	public abstract void setPathValue(LanguagePath path, boolean isBidirection);
	
	public abstract LanguagePath getPathValue();

	public abstract void setSimplexOnly();
	
	public abstract String getKey();
	
	private List<Component> switchVisibleList = new ArrayList<Component>();
	protected LanguageDropDownChoice inputLanguageChoice;
	protected String inputLanguageId = "inputLanguage";
}
