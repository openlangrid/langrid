/*
 * $Id: PreventionOfFraudulentUsageResultPage.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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

import java.util.Iterator;

import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.panel.AccessLimitFieldPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class PreventionOfFraudulentUsageResultPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public PreventionOfFraudulentUsageResultPage(int days, RepeatingView repeatingView){
		Label label = new Label("cleared"
				, MessageManager.getMessage(
						"LanguageGridOperator.message.clear.Complete", getLocale()));
		int i = 0;
		Iterator it = repeatingView.iterator();
		while(it.hasNext()){
			AccessLimitFieldPanel alfp = (AccessLimitFieldPanel)it.next();
			if(alfp.getCount().getInput() == null || alfp.getCount().getInput().length() == 0){
				continue;
			}
			
			WebMarkupContainer repeat = new WebMarkupContainer(view.newChildId());
			repeat.add(new Label("value", format(alfp)));
			view.add(repeat);
			i++;
		}
		
		if(i > 0){
			label.setVisible(false);
		}
		add(new Label("days", String.valueOf(days)));
		add(label);
		add(view);
	}
	private String format(AccessLimitFieldPanel container){
		String format = capaFormat;
		if(container.getType().getSelectedType().equals(LimitType.FREQUENCY)){
			format = freqFormat;
		}
		return String.format(format, Integer.parseInt(container.getCount().getInput()),
				container.getPeriod().getConvertedInput());
	}

	private RepeatingView view = new RepeatingView("repeatingView");

	private static String capaFormat = "Data transfer size %dKB/%s";
	private static String freqFormat = "Access %dhits/%s";
}
