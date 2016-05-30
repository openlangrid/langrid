/*
 * $Id: MultihopTranslationLanguagePathPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
import java.util.Iterator;
import java.util.List;

import jp.go.nict.langrid.language.InvalidLanguageTagException;
import jp.go.nict.langrid.language.Language;
import jp.go.nict.langrid.language.LanguagePath;
import jp.go.nict.langrid.management.web.view.component.link.AjaxNonSubmitLink;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class MultihopTranslationLanguagePathPanel extends AbstractLanguagePathPanel{
	/**
	 * 
	 * 
	 */
	public MultihopTranslationLanguagePathPanel(String componentId){
		super(componentId);
		setOutputMarkupId(true);
		repeater = new RepeatingView("repeater");
		OutputLanguagePathPanel outputLanguagePath = new OutputLanguagePathPanel(
		   repeater.newChildId());
		outputLanguagePath.setSimplexOnly();
		repeater.add(outputLanguagePath);
		add(new AjaxNonSubmitLink("addLanguageLink"){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form){
				OutputLanguagePathPanel panel = new OutputLanguagePathPanel(
						repeater.newChildId());
				panel.setSimplexOnly();
				repeater.add(panel);
				if(repeater.size() < 3){
					getParent().add(new AttributeAppender("class"
							, new Model<String>("overflow_box"), " "));
				}
				target.addComponent(getParent());
			}

			private static final long serialVersionUID = 1L;
		}.setDefaultFormProcessing(false));
		add(new AjaxNonSubmitLink("removeLanguageLink"){
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form){
				if(repeater.size() < 2){
					return;
				}
				Iterator it = repeater.iterator();
				OutputLanguagePathPanel output = null;
				while(it.hasNext()){
					output = (OutputLanguagePathPanel)it.next();
				}
				repeater.remove(output);
				if(repeater.size() < 2){
					getParent().add(new AttributeModifier("class", new Model<String>("'")));
				}
				target.addComponent(getParent());
			}

			private static final long serialVersionUID = 1L;
		}.setDefaultFormProcessing(false));
		add(repeater);
	}

	@Override
   public void setPathValue(LanguagePath path, boolean isBidirection) {
	   repeater.removeAll();
      int i = 0;
	   for(Language l : path.getPath()){
         if(i++ == 0){
            inputLanguageChoice.setModelObject(l);
            continue;
         }
         OutputLanguagePathPanel panel = new OutputLanguagePathPanel(repeater.newChildId());
         panel.setOutputValue(l);
         panel.setSimplexOnly();
         repeater.add(panel);
      }
   }
	
   @Override
   public LanguagePath getPathValue() {
      List<Language> list = new ArrayList<Language>();
      list.add(inputLanguageChoice.getModelObject());
      Iterator it = repeater.iterator();
      while(it.hasNext()){
         OutputLanguagePathPanel panel = (OutputLanguagePathPanel)it.next();
         list.add(panel.getLanguageValue());
      }
      return new LanguagePath(list.toArray(new Language[]{}));
   }
   
   @Override
   public String[] getValueFromInput() {
      List<String> list = new ArrayList<String>();
      list.add(inputLanguageChoice.getInput());
      Iterator it = repeater.iterator();
      while(it.hasNext()){
         OutputLanguagePathPanel panel = (OutputLanguagePathPanel)it.next();
         list.add(panel.getLanguageFromInput());
      }
      return list.toArray(new String[]{});
   }

   @Override
   public void setValueFromInput() throws InvalidLanguageTagException{
      inputLanguageChoice.setModelObject(new Language(inputLanguageChoice.getInput()));
      Iterator it = repeater.iterator();
      while(it.hasNext()){
         OutputLanguagePathPanel panel = (OutputLanguagePathPanel)it.next();
         panel.setValueFromInput();
      }
   }
   
   @Override
   public String getKey() {
      return metaAttributeKey;
   }
   
   /**
    * noop
    */
   @Override
   public void setSimplexOnly() {}

   private RepeatingView repeater;
   private String metaAttributeKey = "supportedLanguagePaths_PathList";
}
