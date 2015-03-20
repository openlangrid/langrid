/*
 * $Id: AccessLimitFieldPanel.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.resource.component.panel;

import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.management.web.view.page.language.component.form.choice.AccessLimitTypeDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.component.form.choice.AccessPeriodDropDownChoice;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.text.LimitCountField;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class AccessLimitFieldPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public AccessLimitFieldPanel(String componentId){
		super(componentId);
		init();
		add(count = new LimitCountField("count", new Model<Integer>()));
	}

	/**
	 * 
	 * 
	 */
	public AccessLimitFieldPanel(String componentId, LimitType limitType, int limitCount, Period period){
		super(componentId);
		init();
		typeField.setSelectedType(limitType);
		periodField.setLimitType(limitType);
		periodField.setSelectedPeriod(period);
		add(count = new LimitCountField("count", new Model<Integer>(limitCount)));
	}

	/**
	 * 
	 * 
	 */
	public LimitCountField getCount(){
		return count;
	}

	/**
	 * 
	 * 
	 */
	public AccessPeriodDropDownChoice getPeriod(){
		return periodField;
	}

	/**
	 * 
	 * 
	 */
	public AccessLimitTypeDropDownChoice getType(){
		return typeField;
	}

	/**
	 * 
	 * 
	 */
	public void setCountByInput(){
		if(count.getInput().equals("")){
			return;
		}
		count.setModelObject(Integer.valueOf(count.getInput()));
	}

	/**
	 * 
	 * 
	 */
	public void setPeriodValueByInput(){
		periodField.setSelectedPeriod(periodField.getSelectedPeriod());
	}

	protected void init(){
		typeField.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			@Override
			protected void onUpdate(AjaxRequestTarget target){
				LimitType limitType = getType().getSelectedType();
				if(limitType != null){
					periodField.setLimitType(limitType);
					periodField.setModelObject("");
					target.addComponent(periodField);
				}
			}

			private static final long serialVersionUID = 1L;
		});
		add(typeField);
		periodField.setOutputMarkupId(true);
		add(periodField);
	}

	private LimitCountField count;
	private AccessPeriodDropDownChoice periodField = new AccessPeriodDropDownChoice("period");
	private AccessLimitTypeDropDownChoice typeField = new AccessLimitTypeDropDownChoice("limitType");
	private static final long serialVersionUID = -1L;
}
