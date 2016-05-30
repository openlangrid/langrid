/*
 * $Id: YourComputationResourceEditResultPage.java 8823 2009-01-21 05:25:09Z Masaaki Kamiya $
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
package jp.go.nict.langrid.management.web.view.page.node;

import jp.go.nict.langrid.management.web.model.NodeModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.NodeService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.markup.html.basic.Label;

/**
 * 計算資源編集結果ページクラス
 * @author $Author: Masaaki Kamiya $
 * @version $Revision$
 */
public class YourNodeEditResultPage
extends ServiceManagerPage
{
	public YourNodeEditResultPage(String nodeId){
		try{
			NodeService service = ServiceFactory.getInstance().getNodeService(getSelfGridId());
			NodeModel nm = service.get(nodeId);
			add(new Label("nodeId", nodeId));
			add(new Label("nodeName", nm.getNodeName()));
			add(new Label("url", nm.getUrl().toString()));
			add(new Label("cpu", nm.getCpu()));
			add(new Label("memory", nm.getMemory()));
			add(new Label("os", nm.getOs()));
			add(new Label("specialNotes", nm.getSpecialNotes()));
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}
	}
}
