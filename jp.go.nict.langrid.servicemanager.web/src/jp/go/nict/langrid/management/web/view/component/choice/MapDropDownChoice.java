/*
 * $Id: MapDropDownChoice.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.component.choice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class MapDropDownChoice extends DropDownChoice<String> {
   public MapDropDownChoice(String componentId, Model<String> model, LinkedHashMap<String, String> valueMap) {
      super(componentId, model, new ArrayList<String>(valueMap.keySet()));
      values = valueMap;
      setChoiceRenderer(new ChoiceRenderer());
      setNullValid(false);
   }
   
	public MapDropDownChoice(String componentId, LinkedHashMap<String, String> valueMap) {
	   this(componentId, new Model<String>(valueMap.keySet().iterator().next()), valueMap);
	}
	
	private Map<String, String> values;
	
	private class ChoiceRenderer implements IChoiceRenderer<String> {
	   public ChoiceRenderer() {
      }
	   @Override
	   public Object getDisplayValue(String object) {
	      return values.get(object);
	   }
	   @Override
	   public String getIdValue(String object, int index) {
	      return object;
	   }
	}
}
