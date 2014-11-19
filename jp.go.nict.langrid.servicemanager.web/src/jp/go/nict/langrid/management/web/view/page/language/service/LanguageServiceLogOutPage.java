/*
 * $Id: LanguageServiceLogOutPage.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service;


/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class LanguageServiceLogOutPage extends LanguageServicePage {
//	@Override
//	protected ServiceListFullOptionTabPanel getTabPanel(String panelId, String gridId,
//	   LangridSearchCondition condition)
//	throws ServiceManagerException {
//	   condition.putOrReplaceCondition("approved", true);
//	   return new ServiceListFullOptionTabPanel(panelId, gridId, gridId, "", condition)
//	   {
//	      @Override
//	      protected <T extends ServiceModel>ServiceListPanel<T> getListPanel(
//	         String gridId, String panelId, SortableDataProvider<T> provider) 
//	         {
//	         ServiceListPanel<T> panel = new ServiceListPanel<T>(gridId, panelId, provider) {
//	            @Override
//	            protected PagingNavigator getPagingNavigator(String gridId, String componentId, IPageable pageable) {
//	               return new BookmarkablePagingNavigator(gridId, componentId, pageable, getParamName());
//	            }
//	         };
//	         String param = RequestResponseUtil.getParameter(panel.getParamName(), getRequest());
//	         if(param != null){
//	            panel.setCurrentIndex(Integer.valueOf(param));
//	         }
//	         return panel;
//	         }
//	   };
//	}
}
