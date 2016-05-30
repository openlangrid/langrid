/*
 * $Id: AjaxNonSubmitLink.java 303 2010-12-01 04:21:52Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.component.link;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.AbstractSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public abstract class AjaxNonSubmitLink extends AbstractSubmitLink {
	/**
	 * 
	 * 
	 */
	public AjaxNonSubmitLink(String componentId){
		this(componentId, null);
	}

	/**
	 * 
	 * 
	 */
	public AjaxNonSubmitLink(String componentId, Form form){
		super(componentId);
		add(new AjaxFormSubmitBehavior(form, "onclick"){
			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator(){
				return AjaxNonSubmitLink.this.getAjaxCallDecorator();
			}

			@Override
			protected CharSequence getEventHandler(){
				return new AppendingStringBuffer(super.getEventHandler())
						.append("; return false;");
			}

			@Override
			protected void onComponentTag(ComponentTag tag){
				// write the onclick handler only if link is enabled
				if(isLinkEnabled()){
					super.onComponentTag(tag);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target){
				AjaxNonSubmitLink.this.onError(target, getForm());
			}

			@Override
			protected void onEvent(AjaxRequestTarget target){
				if(getDefaultFormProcessing()){
					super.onEvent(target);
				}else{
					onSubmit(target);
				}
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target){
				AjaxNonSubmitLink.this.onSubmit(target, getForm());
			}

			private static final long serialVersionUID = 1L;
		});
	}
	
	/**
	 * Final implementation of the Button's onSubmit. AjaxSubmitLinks have there own onSubmit which
	 * is called.
	 * 
	 * @see org.apache.wicket.markup.html.Common.label#onSubmit()
	 */
	public final void onSubmit(){
	}

	/**
	 * Returns the {@link IAjaxCallDecorator} that will be used to modify the generated javascript.
	 * This is the preferred way of changing the javascript in the onclick handler
	 * 
	 * @return call decorator used to modify the generated javascript or null for none
	 */
	protected IAjaxCallDecorator getAjaxCallDecorator(){
		return null;
	}

	@Override
	protected void onComponentTag(ComponentTag tag){
		super.onComponentTag(tag);
		if(isLinkEnabled()){
			if(tag.getName().toLowerCase().equals("a")){
				tag.put("href", "#");
			}
		}else{
			disableLink(tag);
		}
	}

	/**
	 * Listener method invoked on form submit with errors
	 * 
	 * @param target
	 * @param form
	 * 
	 * TODO 1.3: Make abstract to be consistent with onsubmit()
	 */
	protected void onError(AjaxRequestTarget target, Form form){
	}

	/**
	 * Listener method invoked on form submit
	 * 
	 * @param target
	 * @param form
	 */
	protected abstract void onSubmit(AjaxRequestTarget target, Form form);

	private static final long serialVersionUID = 1L;
}
