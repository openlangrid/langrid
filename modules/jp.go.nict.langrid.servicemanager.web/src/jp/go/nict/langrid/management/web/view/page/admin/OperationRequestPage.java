/*
 * $Id: OperationRequestPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.admin;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.provider.OperationRequestSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.admin.component.list.OperationRequestListPanel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class OperationRequestPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public OperationRequestPage(){
      try {
         String gridId = getSelfGridId();
         OperationRequestSortableDataProvider provider = new OperationRequestSortableDataProvider(
               gridId, getSessionUserId());
         provider.setConditions(new MatchingCondition[]{
            new MatchingCondition("gridId", getSelfGridId())
         }, new Order[]{new Order("createdDateTime", OrderDirection.DESCENDANT)}, Scope.ALL);
         OperationRequestListPanel listPanel = new OperationRequestListPanel(
            gridId, "operationListPanel", provider);
         add(listPanel);
      } catch(ServiceManagerException e) {
         doErrorProcess(e);
      }
	}
}
