/*
 * $Id: MonitoringLanguageServiceAdminPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.admin;

import java.util.Calendar;

import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.enumeration.GridRelation;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.LangridSearchCondition;
import jp.go.nict.langrid.management.web.view.page.language.service.MonitoringLanguageServiceTabbedPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.ServiceListTabPanel;

import org.apache.wicket.Page;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class MonitoringLanguageServiceAdminPage extends MonitoringLanguageServiceTabbedPage {
	protected ServiceListTabPanel getTabPanel(
      String panelId, String gridId, GridRelation relation, LangridSearchCondition condition)
   throws ServiceManagerException
   {
	   condition.setScope(Scope.ALL);
      return super.getTabPanel(panelId, gridId, relation, condition);
   }
	
	@Override
	protected Page getMonitarPage(String serviceGridId, String serviceId, String serviceName, Calendar start, Calendar end){
		return new MonitoringLanguageServiceStatisticAdminPage(serviceGridId, serviceId, serviceName, start, end);
	}
}
