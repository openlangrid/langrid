/*
 * $Id: PairLanguagePathPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.model.enumeration.LanguagePathDirection;



/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class PairLanguagePathPanel extends AbstractLanguagePathPanel{
	/**
	 * 
	 * 
	 */
	public PairLanguagePathPanel(String componentId){
		super(componentId);
		add(outputLanguagePathContainer = new OutputLanguagePathPanel(
				"outputLanguage"));
	}

	@Override
   public void setPathValue(LanguagePath path, boolean isBidirection) {
	   inputLanguageChoice.setModelObject(path.getPath()[0]);
	   outputLanguagePathContainer.setOutputValue(path.getPath()[1]);
	   if( ! isBidirection){
	      outputLanguagePathContainer.setDirectionValue(LanguagePathDirection.SIMPLEX);
	   }
   }

   @Override
   public LanguagePath getPathValue() {
      return new LanguagePath(
         inputLanguageChoice.getModelObject()
         , outputLanguagePathContainer.getLanguageValue());
   }
   
   @Override
   public void setSimplexOnly() {
      outputLanguagePathContainer.setSimplexOnly();
   }
   
   @Override
   public String getKey() {
      return metaAttributePairKey;
   }
   
   @Override
   public String[] getValueFromInput() {
      return new String[]{inputLanguageChoice.getInput()
         , outputLanguagePathContainer.getLanguageFromInput()};
   }

   @Override
   public void setValueFromInput() throws InvalidLanguageTagException {
      inputLanguageChoice.setModelObject(new Language(inputLanguageChoice.getInput()));
      outputLanguagePathContainer.setValueFromInput();
   }
   
   public String getCombinationKey() {
      return metaAttributeCombinationKey;
   }
   
   public boolean isBidirection(){
      if(outputLanguagePathContainer.getDirectionValue() == null){
         return outputLanguagePathContainer.getDirectionFromInput().equals(LanguagePathDirection.BOTH.getCode());
      }
      return outputLanguagePathContainer.getDirectionValue().equals(LanguagePathDirection.BOTH);
   }
   
   protected OutputLanguagePathPanel outputLanguagePathContainer;
   private String metaAttributePairKey = "supportedLanguagePairs_PairList";
   private String metaAttributeCombinationKey = "supportedLanguagePairs_AnyCombination";
}
