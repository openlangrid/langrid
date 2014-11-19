/*
 * $Id: ImplementationLanguagePanel.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.composite.component.form.panel;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class ImplementationLanguagePanel extends Panel {
	public ImplementationLanguagePanel(String panelId)
	throws ServiceManagerException
	{
		this(panelId, InstanceType.BPEL);
	}

	public ImplementationLanguagePanel(String panelId, InstanceType type)
	throws ServiceManagerException
	{
		super(panelId);
		radioGroup = new RadioGroup<InstanceType>("radioGroup", new Model<InstanceType>(type));
		add(radioGroup);
		Radio<InstanceType> bpel = new Radio<InstanceType>("bpelImpl", new Model<InstanceType>(InstanceType.BPEL));
		radioGroup.add(new Label("bpelName", bpel.getModel()));
		radioGroup.add(bpel);
		Radio<InstanceType> java = new Radio<InstanceType>("javaImpl", new Model<InstanceType>(InstanceType.Java));
		radioGroup.add(new Label("javaName", java.getModel()));
		radioGroup.add(java);
	}

	public InstanceType getInstanceType(){
		return radioGroup.getModelObject();
	}

	public Component getAjaxEventComponent(){
		return radioGroup;
	}

	public void setImplementationLanguage(ServiceModel model){
		radioGroup.setModelObject(model.getInstanceType());
	}

	private RadioGroup<InstanceType> radioGroup;
}
