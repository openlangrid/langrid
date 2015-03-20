/*
 * $Id: ResourceMetaAttributeListMultipleChoice.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource.admin.component;

import java.util.Collection;
import java.util.List;

import jp.go.nict.langrid.management.web.model.ResourceMetaAttributeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.WildcardListModel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ResourceMetaAttributeListMultipleChoice
extends ListMultipleChoice<ResourceMetaAttributeModel> {
   /**
    * 
    * 
    * @throws ServiceManagerException 
    */
   public ResourceMetaAttributeListMultipleChoice(String gridId, String componentId, String domainId)
   throws ServiceManagerException 
   {
      super(componentId, new Model<LangridList<ResourceMetaAttributeModel>>(
         new LangridList<ResourceMetaAttributeModel>())
         , new WildcardListModel<ResourceMetaAttributeModel>());
      setChoiceRenderer(new ResourceMetaAttributeChoiceRenderer());
      setModelList(gridId, domainId);
   }
   
   public void setSelectedList(Collection<ResourceMetaAttributeModel> collection){
      setDefaultModelObject(collection);
   }

   @Override
   public CharSequence getDefaultChoice(Object selected) {
      return "";
   }
   
   public int getListSize(){
      return list.size();
   }
   
   public void setModelList(String gridId, String domainId) throws ServiceManagerException{
	   list = ServiceFactory.getInstance().getResourceMetaAttributeService(gridId).getAllListOnDomain(domainId);
      setChoices(list);
      setMaxRows(10);
      if(0 < list.size()){
         LangridList<ResourceMetaAttributeModel> selected = new LangridList<ResourceMetaAttributeModel>();
         selected.add(list.get(0));
         setSelectedList(selected);
      }
   }

   private List<ResourceMetaAttributeModel> list;

   private class ResourceMetaAttributeChoiceRenderer
    implements IChoiceRenderer<ResourceMetaAttributeModel> {
      public ResourceMetaAttributeChoiceRenderer() {
      }

      @Override
      public Object getDisplayValue(ResourceMetaAttributeModel object) {
         return object.getAttributeName();
      }

      @Override
      public String getIdValue(ResourceMetaAttributeModel object, int index) {
         return object.getAttributeId();
      }
   }
}
