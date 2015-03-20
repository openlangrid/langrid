/*
 * $Id: ErrorInternalPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.error;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ErrorInternalPage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public ErrorInternalPage(){
		this(new ServiceManagerException("System Error"));
	}
	
	/**
	 * 
	 * 
	 */
	public ErrorInternalPage(ServiceManagerException e){
		e.printStackTrace();
		Label title = new Label("errorTitle", e.getErrorCode()
				+ " : "
				+ MessageManager.getMessage("message.error." + e.getErrorCode(),
						getLocale()));
		add(title);
		Label message = new Label("errorMessage", MessageManager.getMessage(
				"message.error." + e.getErrorCode() + ".Navigation", getLocale()
				));
		message.setEscapeModelStrings(false);
		add(message);
		AjaxLink link = new AjaxLink("stackTraceTitle"){
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onClick(AjaxRequestTarget target){
				container.setVisible(!container.isVisible());
				target.addComponent(container);
			}

			@Override
			protected boolean getStatelessHint(){
				return true;
			}
		};
		link.add(new Label("linkLabel", "Error description - " + e.getRaisedDate()));
		add(link);
		Throwable th = e;
		RepeatingView repeater = new RepeatingView("repeat");
		do{
			StringBuffer errorSb = new StringBuffer();
//			if(th instanceof LangridException){
//				LangridException le = (LangridException)th;
//				errorSb.append("-");
//				errorSb.append(le.getClass().getName());
//				errorSb.append(": ");
//				errorSb.append(le.getErrorString() == null ? "" : le.getErrorString());
//				errorSb.append(" [");
//				errorSb.append(le.getServiceUrl());
//				errorSb.append("#");
//				errorSb.append(le.getOperationName());
//				errorSb.append("]");
//			}else{
				errorSb.append("-");
				errorSb.append(th.getClass().getName());
				errorSb.append(": ");
				errorSb.append(th.getMessage() == null ? "" : th.getMessage());
//			}
			errorSb.append("<br/>");
			for(StackTraceElement trace : th.getStackTrace()){
				errorSb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				errorSb.append(trace.getClassName());
				errorSb.append("(");
				errorSb.append(trace.getMethodName());
				errorSb.append(":");
				errorSb.append(trace.getLineNumber());
				errorSb.append(")");
				errorSb.append("<br/>");
			}
			Label stacktrace = new Label(repeater.newChildId(), new Model<String>(errorSb
					.toString()));
			repeater.add(stacktrace.setEscapeModelStrings(false));
		}while((th = th.getCause()) != null);
		container = new WebMarkupContainer("stacktrace");
		container.setOutputMarkupId(true);
		container.setOutputMarkupPlaceholderTag(true);
		container.setVisible(false);
		container.add(repeater);
		add(container);
	}

	private WebMarkupContainer container;
}
