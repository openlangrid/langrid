/*
 * $Id: AccessLimitsForm.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.language.service.component.form;

import java.util.Iterator;
import java.util.List;

import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.LimitType;
import jp.go.nict.langrid.dao.entity.Period;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.AccessLimitControlModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.AccessLimitControlService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.link.AjaxNonSubmitLink;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.form.validator.AccessLimitValidator;
import jp.go.nict.langrid.management.web.view.page.language.resource.component.panel.AccessLimitFieldPanel;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public abstract class AccessLimitsForm extends AbstractForm<String> {
	/**
	 * 
	 * 
	 */
	public AccessLimitsForm(String formId, String userGridId, String userId,
		String serviceGridId, String serviceId)
	throws ServiceManagerException {
		super(formId);
		this.serviceGridId = serviceGridId;
		this.serviceId = serviceId;
		this.userGridId = userGridId;
		this.userId = userId;
		add(new AccessLimitValidator(limits));
		loadLimits();
	}

	@Override
	protected void addComponents(String initialParameter)
	throws ServiceManagerException {
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
		});
		submit.setDefaultFormProcessing(false);
		add(clear = new AjaxNonSubmitLink("clear") {
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				limits.removeAll();
				moreClicked();
				target.addComponent(wmc);
			}
		});
		clear.setDefaultFormProcessing(false);
		limits = new RepeatingView("limits");
		add(wmc = new WebMarkupContainer("limitsContainer"));
		wmc.add(limits);
		wmc.setOutputMarkupId(true);
		add(new Link("cancel") {
			@Override
			public void onClick() {
				setResponsePage(getCancelPage(serviceId));
			}
		});
		add(new Button("set") {
			@Override
			public void onSubmit() {
				try {
					AccessLimitControlService service = ServiceFactory.getInstance()
						.getAccessLimitControlService(serviceGridId);
					// Clear
					int index = 0;
					int count = 30;
					while(true) {
						List<AccessLimitControlModel> list = service.getList(
							index, count, userGridId, serviceId, new Order[]{});
						for(AccessLimitControlModel am : list) {
							if(isInitSettingOperation) {
								if(am.getUserId().equals("*")) {
									service.delete(am);
								}
							} else if(!am.getUserId().equals("*")) {
								service.delete(am);
							}
						}
						if(list.size() < count) {
							break;
						}
						index += count;
					}
					// setting
					StringBuilder limitString = new StringBuilder();
					Iterator it = limits.iterator();
					while(it.hasNext()) {
						AccessLimitFieldPanel c = (AccessLimitFieldPanel)it.next();
						LimitType lt = c.getType().getSelectedType();
						Integer lc = c.getCount().getConvertedInput();
						if(lc == null) {
							continue;
						}
						String periodValue = c.getPeriod().getConvertedInput();
						Period p = c.getPeriod().getSelectedPeriod();
						// add limitString
						String format = null;
						if(lt.equals(LimitType.FREQUENCY)) {
							format = freqFormat;
						} else {
							format = capaFormat;
						}
						limitString.append(String.format(format, lc, periodValue));
						limitString.append(",");
						// setting
						if(lt.equals(LimitType.CAPACITY)) {
							lc *= transferredSizeScale;
						}
						AccessLimitControlModel alcm = new AccessLimitControlModel();
						alcm.setServiceId(serviceId);
						alcm.setServiceGridId(serviceGridId);
						alcm.setUserId(userId);
						alcm.setLimitCount(lc);
						alcm.setLimitType(lt);
						alcm.setPeriod(p);
						alcm.setUserGridId(userGridId);
						service.add(alcm);
						LogWriter.writeInfo(getSessionUserId(),
								"Accesse limit of\"" + serviceId + "\" to user \""
										+ userId + "\" has been changed to \""
										+ limitString.toString() + "\"", getPage()
									.getClass());
						limitResult = limitString.toString();
						addSubmitProcess();
					}
				} catch(ServiceManagerException e) {
					doErrorProcess(e);
				}
			}
		});
	}

	protected void addSubmitProcess() throws ServiceManagerException {
		// noop
	}

	protected abstract Page getCancelPage(String serviceId);

	@Override
	protected String getResultParameter() {
		return limitResult;
	}

	private void loadLimits() throws ServiceManagerException {
		AccessLimitControlService service = ServiceFactory.getInstance().getAccessLimitControlService(serviceGridId);
		List<AccessLimitControlModel> list = service.getList(
			userGridId, userId, serviceGridId, serviceId
		);
		for(AccessLimitControlModel model : list) {
			LimitType lt = model.getLimitType();
			int lc = model.getLimitCount();
			if(lt.equals(LimitType.CAPACITY)) {
				lc /= transferredSizeScale;
			}
			limits.add(new AccessLimitFieldPanel(limits.newChildId(), lt, lc, model.getPeriod()));
		}
		if(limits.size() == 0) {
			moreClicked();
		}
	}

	public void setInitSettingOperation(boolean isOperate) {
		isInitSettingOperation = isOperate;
	}

	private void moreClicked() {
		limits.add(new AccessLimitFieldPanel(limits.newChildId()));
	}

	private AjaxNonSubmitLink clear;
	private String limitResult = "";
	private RepeatingView limits;
	private AjaxNonSubmitLink submit;
	private String userGridId;
	private String userId;
	private String serviceGridId;
	private String serviceId;
	private boolean isInitSettingOperation = false;
	private WebMarkupContainer wmc;
	private static String capaFormat = "Data transfer size limit %dKB/%s";
	private static String freqFormat = "Access limit %dhits/%s";
	private static int transferredSizeScale = 1024;
}
