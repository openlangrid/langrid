/*
 * $Id: RegistMaintenanceNews.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.utility.administration;

import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class RegistMaintenanceNews{
	public static void main(String[] args) throws ServiceManagerException {
		NewsModel news = new NewsModel();
		StringBuffer message = new StringBuffer();
		message.append("<b>[NOTICE]</b> The Language Grid will be suspended from 7:00am(JST) to 10:00am ");
		message.append("(JST) on March 3th due to the maintenance of the language grid core ");
		message.append("node and the language grid service manager. If you have a request to use ");
		message.append("the language grid during the maintenance, please contact us (operation ");
		message.append("[at] langrid.org).");
		news.setContents(message.toString());
		ServiceFactory.getInstance().getNewsService(ServiceFactory.getInstance().getGridService().getSelfGridId()).add(news);
	}
}
