/*
 * $Id: PerMinutesJob.java 497 2012-05-24 04:13:03Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.backend.job;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.dao.entity.InstanceType;
import jp.go.nict.langrid.dao.entity.ScheduleActionType;
import jp.go.nict.langrid.dao.entity.ScheduleTargetType;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.ResourceModel;
import jp.go.nict.langrid.management.web.model.ScheduleModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridServiceService;
import jp.go.nict.langrid.management.web.model.service.ResourceService;
import jp.go.nict.langrid.management.web.model.service.ScheduleService;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.TemporaryUserService;
import jp.go.nict.langrid.management.web.model.service.impl.ScheduleServiceImpl;
import jp.go.nict.langrid.management.web.model.service.impl.TemporaryUserServiceImpl;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.utility.resource.MessageUtil;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 497 $
 */
public class PerMinutesJob extends AbstractJob {
	@Override
	protected boolean doExecute() {
		return MessageUtil.IS_INVOKE_JOB_ON_TIME;
	}

	@Override
	protected void doJobExecution() throws ServiceManagerException {
		ScheduleService service = new ScheduleServiceImpl();
		String gridId = ServiceFactory.getInstance().getGridService().getSelfGridId();
		service.setScopeParameter(gridId, gridId, "");
		try {
			languageResourceJob(service);
		} catch(ServiceManagerException e) {
			LogWriter.writeError("System", e, PerMinutesJob.class);
		}
		try {
			languageServiceJob(service);
		} catch(ServiceManagerException e) {
			LogWriter.writeError("System", e, PerMinutesJob.class);
		}
		try {
			clearExpiredTemporaryUser(gridId);
		} catch(ServiceManagerException e) {
			LogWriter.writeError("System", e, PerMinutesJob.class);
		}
	}

	/**
	* 
	* 
	*/
	private void clearExpiredTemporaryUser(String gridId) throws ServiceManagerException {
		TemporaryUserService service = new TemporaryUserServiceImpl();
		service.setScopeParameter(gridId, gridId, "");
		service.clearExpiredUsers();
	}

	/**
	 * 
	 * 
	 */
	private void languageResourceJob(ScheduleService service)
	throws ServiceManagerException {
		Calendar cal = Calendar.getInstance();
		List<ScheduleModel> list = service.getListPassedBookingTime(
			ScheduleTargetType.RESOURCE, cal);
		for(ScheduleModel model : list) {
			ResourceService rService = ServiceFactory.getInstance().getResourceService(
				model.getGridId());
			ScheduleService sService = ServiceFactory.getInstance().getScheduleService(
				model.getGridId());
			ResourceModel rModel = rService.get(model.getTargetId());
			rService.delete(rModel);
			sService.delete(model);
			NewsModel nm = new NewsModel();
			nm.setGridId(model.getGridId());
			Map<String, String> param = new HashMap<String, String>();
			param.put("id", model.getTargetId());
			param.put("name", rModel.getResourceName());
			nm.setContents(MessageManager.getMessage(
				"news.resource.language.Unregistered", param));
			ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
		}
	}

	/**
	 * 
	 * 
	 */
	private void languageServiceJob(ScheduleService service)
	throws ServiceManagerException {
		Calendar cal = Calendar.getInstance();
		List<ScheduleModel> list = service.getListPassedBookingTime(
			ScheduleTargetType.SERVICE, cal);
		for(ScheduleModel model : list) {
			LangridServiceService<ServiceModel> lService = ServiceFactory.getInstance()
				.getLangridServiceService(model.getGridId(), model.getGridId(), "");
			ServiceModel sModel = lService.get(model.getTargetId());

			if(model.getActionType().equals(ScheduleActionType.SUSPENSION)) {
				lService.deactivate(model.getTargetId());
			} else if(model.getActionType().equals(ScheduleActionType.UNREGISTRATION)) {
				lService.delete(sModel);
			}
			ScheduleService sService = ServiceFactory.getInstance().getScheduleService(
				model.getGridId());
			sService.delete(model);
			NewsModel nm = new NewsModel();
			nm.setGridId(model.getGridId());
			Map<String, String> param = new HashMap<String, String>();
			param.put("id", model.getTargetId());
			param.put("name", sModel.getServiceName());
			String message = sModel.getInstanceType()
				.equals(InstanceType.EXTERNAL) ?
				"news.service.atomic" : "news.service.composite";
			message += model.getActionType().equals(ScheduleActionType.UNREGISTRATION) ?
				".Unregistered" : ".Suspended";
			nm.setContents(MessageManager.getMessage(message, param));
			ServiceFactory.getInstance().getNewsService(model.getGridId()).add(nm);
		}
	}
}
