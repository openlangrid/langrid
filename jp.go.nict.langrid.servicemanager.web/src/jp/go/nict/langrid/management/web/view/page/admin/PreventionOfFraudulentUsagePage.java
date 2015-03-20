/*
 * $Id: PreventionOfFraudulentUsagePage.java 328 2010-12-08 05:43:18Z t-nakaguchi $
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
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.AccessLimitControlModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.OveruseLimitControlService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.link.AjaxNonSubmitLink;
import jp.go.nict.langrid.management.web.view.component.text.IntegerField;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.form.validator.AccessLimitValidator;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.panel.AccessLimitFieldPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class PreventionOfFraudulentUsagePage extends ServiceManagerPage{
	/**
	 * 
	 * 
	 */
	public PreventionOfFraudulentUsagePage(){
		AbstractForm<String> form = new AbstractForm<String>("form", getSelfGridId()){
			@Override
			protected void addComponents(String initialParameter)
					throws ServiceManagerException{
				limits = new RepeatingView("limits");
				add(wmc = new WebMarkupContainer("limitsContainer"));
				wmc.add(limits);
				wmc.setOutputMarkupId(true);
				add(day);
				// TODO
				int expired = ServiceFactory.getInstance().getGridService().getPasswordExpiredDay();
				day.setModelObject(expired);
				add(submit = new AjaxNonSubmitLink("more") {
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form form) {
						Iterator it = limits.iterator();
						while(it.hasNext()) {
							AccessLimitFieldPanel alc = (AccessLimitFieldPanel)it.next();
							alc.setPeriodValueByInput();
							alc.setCountByInput();
						}
						moreClicked();
						target.addComponent(wmc);
					}

					private static final long serialVersionUID = 1L;
				});
				submit.setDefaultFormProcessing(false);
				add(clear = new AjaxNonSubmitLink("clear"){
					@Override
					protected void onSubmit(AjaxRequestTarget target, Form form){
						limits.removeAll();
						moreClicked();
						target.addComponent(wmc);
					}

					private static final long serialVersionUID = 1L;
				});
				clear.setDefaultFormProcessing(false);
				add(new Button("set"){
					@Override
					public void onSubmit(){
						try{
							OveruseLimitControlService service = ServiceFactory.getInstance().getOveruseLimitControlService(
							   getSelfGridId(), getSessionUserId());
							service.clearAll();
							Iterator it = limits.iterator();
							while(it.hasNext()){
								AccessLimitFieldPanel alfp = (AccessLimitFieldPanel)it.next();
								Integer lc = alfp.getCount().getConvertedInput();
								if(lc == null) {
									continue;
								}
								if(alfp.getType().getSelectedType().equals(
										LimitType.CAPACITY)) {
									lc *= transferredSizeScale;
								}
								AccessLimitControlModel model =  new AccessLimitControlModel();
								model.setLimitCount(lc);
								model.setLimitType(alfp.getType().getSelectedType());
								model.setPeriod(alfp.getPeriod().getSelectedPeriod());
								service.add(model);
								LogWriter.writeInfo(getSessionUserId()
										, "Accesse limit of service to all has been changed to \""
												+ alfp.getCount().getConvertedInput()
												+ (alfp.getType().getSelectedType().equals(
														LimitType.FREQUENCY) ? "hits" : "KB")
												+ "/" + alfp.getPeriod().getSelectedString()
												+ "\"", getPage().getClass());
							}
							ServiceFactory.getInstance().getGridService().setPasswordExpiredDay(day.getConvertedInput());
							LogWriter.writeInfo(getSessionUserId(),
									"Period for changing password has been changed to \""
											+ day.getModelObject() == null ? "-" : day
											.getModelObject()
											+ "\" days", getPage().getClass());
						} catch(ServiceManagerException e) {
						   doErrorProcess(e);
                  }
					}

					private static final long serialVersionUID = 1L;
				});
			}

			@Override
			protected void setResultPage(String resultParameter){
				setResponsePage(new PreventionOfFraudulentUsageResultPage(day
						.getModelObject(), limits));
			}

			private static final long serialVersionUID = 1L;
		};
		form.add(new AccessLimitValidator(limits));
		add(form);
		loadLimits();
	}

	private void loadLimits() {
		try {
		   OveruseLimitControlService service = ServiceFactory.getInstance().getOveruseLimitControlService(
		      getSelfGridId(), getSessionUserId());
		   LangridList<AccessLimitControlModel> list = service.getAll();
		   
		   for(AccessLimitControlModel model : list){
		      int lc = model.getLimitCount();
		      if(model.getLimitType().equals(LimitType.CAPACITY)) {
		         lc /= transferredSizeScale;
		      }
				limits.add(new AccessLimitFieldPanel(
				   limits.newChildId(), model.getLimitType(), lc, model.getPeriod()));
		   }
			if(limits.size() == 0) {
				limits.add(new AccessLimitFieldPanel(limits.newChildId()));
			}
		} catch(ServiceManagerException e) {
		   doErrorProcess(e);
      }
	}

	private void moreClicked() {
		limits.add(new AccessLimitFieldPanel(limits.newChildId()));
	}

	private AjaxNonSubmitLink clear;

	private IntegerField day = new IntegerField("limitDay", new Model<Integer>());
	private RepeatingView limits;
	private AjaxNonSubmitLink submit;
	private WebMarkupContainer wmc;
	private static final int transferredSizeScale = 1024;
}
