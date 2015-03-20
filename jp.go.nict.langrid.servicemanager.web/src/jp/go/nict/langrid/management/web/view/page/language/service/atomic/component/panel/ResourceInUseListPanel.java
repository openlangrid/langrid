/*
 * $Id: ResourceInUseListPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.atomic.component.panel;

import java.util.List;

import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.link.LanguageResourceProfileLink;
import jp.go.nict.langrid.management.web.view.page.language.service.component.panel.InUseListPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ResourceInUseListPanel extends InUseListPanel{
	/**
	 * 
	 * 
	 */
	public ResourceInUseListPanel(List<ResourceModel> resourceList){
		this.resourceList = resourceList;
	}

	@Override
	protected void createList(){
		int count = 0;
		RepeatingView repeating = new RepeatingView(getRepeatingId());
		if(resourceList.size() == 0){
			WebMarkupContainer repeat = new WebMarkupContainer(repeating.newChildId());
			WebMarkupContainer resourceInfomation = makeNoResourceInfomation();
			repeat.add(new Label(getLabelCountId(), "-"));
			repeat.add(resourceInfomation);
			repeating.add(repeat);
			add(repeating);
			return;
		}
		for(ResourceModel resource : resourceList){
			WebMarkupContainer repeat = new WebMarkupContainer(repeating.newChildId());
			WebMarkupContainer resourceInfomation = makeResourceInfomation(resource);
			repeat.add(new Label(getLabelCountId(), String.valueOf(++count) + "."));
			repeat.add(resourceInfomation);
			repeating.add(repeat);
		}
		add(repeating);
	}

	private WebMarkupContainer makeNoResourceInfomation(){
		WebMarkupContainer serviceInfomation = new WebMarkupContainer(getInfomationId());
		LanguageResourceProfileLink usedServiceProfileLink = new LanguageResourceProfileLink(
				getProfileLinkId(), "-", "-", "-");
		usedServiceProfileLink.add(new Label(getLabelIdId(), "-"));
		serviceInfomation.add(usedServiceProfileLink);
		usedServiceProfileLink.setEnabled(false);
		serviceInfomation.add(new Label(getLabelNameId(), ("-")));
		serviceInfomation.add(new Label(getLabelTypeId(), ("-")));
		return serviceInfomation;
	}

	private WebMarkupContainer makeResourceInfomation(ResourceModel resource){
		WebMarkupContainer serviceInfomation = new WebMarkupContainer(getInfomationId());
		LanguageResourceProfileLink usedServiceProfileLink = new LanguageResourceProfileLink(
				getProfileLinkId(), resource.getGridId(), resource.getResourceId(), resource.getResourceId());
		usedServiceProfileLink.add(new Label(getLabelIdId(), resource.getResourceId()));
		serviceInfomation.add(usedServiceProfileLink);
		serviceInfomation.add(new Label(getLabelNameId(), (resource.getResourceName())));
		serviceInfomation.add(new Label(getLabelTypeId(), resource.getResourceType().getResourceTypeId()));
		return serviceInfomation;
	}

	private List<ResourceModel> resourceList;

	private static final long serialVersionUID = 1L;
}
