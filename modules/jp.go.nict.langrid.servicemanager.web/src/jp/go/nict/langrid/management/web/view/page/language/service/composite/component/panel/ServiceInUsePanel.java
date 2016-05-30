/*
 * $Id: ServiceInUsePanel.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.composite.component.panel;

import java.util.Map;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.management.web.model.InvocationModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.StringUtil;
import jp.go.nict.langrid.management.web.view.component.label.HyphenedLabel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.link.ServiceProfileLink;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ServiceInUsePanel extends Panel{
	public ServiceInUsePanel(String panelId, Map<InvocationModel, Pair<Boolean, String>> invocations)
	throws ServiceManagerException 
	{
		super(panelId);
		int count = 1;
		RepeatingView repeating = new RepeatingView("invocationRepeater");
		add(repeating);
		for(InvocationModel invocation : invocations.keySet()){
			setInvocation(repeating, invocation, invocations.get(invocation), count++);
		}
	}

	private void setInvocation(
		RepeatingView repeating, InvocationModel invocation, Pair<Boolean, String> attr, int count)
	throws ServiceManagerException
	{
		WebMarkupContainer repeat = new WebMarkupContainer(repeating.newChildId());
		repeating.add(repeat);
		repeat.add(makeServiceInfomation(invocation, attr));
		repeat.add(new Label("labelCount", String.valueOf(count) + "."));
	}

	private boolean hasInformation(InvocationModel invocation) {
		return StringUtil.isValidString(invocation.getServiceId())
			&& ! invocation.getServiceId().equals("-")
			&& ! (StringUtil.isValidString(invocation.getServiceName())
				&& invocation.getServiceName().startsWith("[IN TEST]"))
			|| StringUtil.isValidString(invocation.getInvocationName())
			;
	}

	private WebMarkupContainer makeServiceInfomation(InvocationModel invocation, Pair<Boolean, String> attr)
	throws ServiceManagerException
	{
		WebMarkupContainer invocationInformation = new WebMarkupContainer("information");
		invocationInformation.add(new HyphenedLabel("labelInvocationName", invocation.getInvocationName()));

		WebMarkupContainer serviceInformation = new WebMarkupContainer("serviceInformation");
		WebMarkupContainer serviceIdSelect = new WebMarkupContainer("serviceIdSelect");
		WebMarkupContainer serviceNameSelect = new WebMarkupContainer("serviceNameSelect");
		
		invocationInformation.add(serviceInformation);
		ServiceProfileLink usedServiceProfileLink = new ServiceProfileLink("invocationServiceProfileLink"
				, invocation.getServiceGridId(), invocation.getServiceId(), invocation.getServiceId());
		serviceIdSelect.add(usedServiceProfileLink);
		usedServiceProfileLink.add(new HyphenedLabel("labelInvocationServiceId", invocation.getServiceId()));
		usedServiceProfileLink.setEnabled(attr.getFirst());
		
		serviceNameSelect.add( new HyphenedLabel("labelInvocationServiceName", invocation.getServiceName()));
		serviceInformation.add( new Label("labelInvocationServiceType", attr.getSecond()));
		serviceInformation.add(serviceIdSelect);
		serviceInformation.add(serviceNameSelect);
		serviceInformation.setVisible(hasInformation(invocation));
		if ( invocation.getServiceName() == null ) {
			serviceIdSelect.setVisible(false);
			serviceNameSelect.setVisible(false);
		}
		return invocationInformation;
	}

	private static final long serialVersionUID = 1L;
}
