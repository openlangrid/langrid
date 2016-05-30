/*
 * $Id: GridDropDownChoice.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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

import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class GridDropDownChoice extends DropDownChoice<GridModel>{
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public GridDropDownChoice(String componentId)
	throws ServiceManagerException
	{
		super(componentId, new Model<GridModel>(), new WildcardListModel<GridModel>());
		setChoiceRenderer(new GridChoiceRenderer());
		list = ServiceFactory.getInstance().getGridService().getAll();
		setChoices(list);
	}
  
	@Override
   public CharSequence getDefaultChoice(Object selected){
      return "";
   }
	
    public GridModel getDefaultGridModel(){
    	if(list.isEmpty()){
    		return null;
    	}
       return list.get(0);
    }
    
    /**
	 * 
	 * 
	 */
	public GridModel getSelectedGrid() {
		if(getModelObject() == null) {
			return list.get(0);
		}
		return getModelObject();
	}
    
    public void setModelById(String gridId){
       for(GridModel model : list){
          if(model.getGridId().equals(gridId)){
             setModelObject(model);
             return;
          }
       }
    }
	
    private LangridList<GridModel> list;
	
    private class GridChoiceRenderer implements IChoiceRenderer<GridModel> {
      public GridChoiceRenderer() {
      }
      @Override
      public Object getDisplayValue(GridModel object) {
         return object.getGridName();
      }
      @Override
      public String getIdValue(GridModel object, int index) {
         return object.getGridId();
      }
   }
}
