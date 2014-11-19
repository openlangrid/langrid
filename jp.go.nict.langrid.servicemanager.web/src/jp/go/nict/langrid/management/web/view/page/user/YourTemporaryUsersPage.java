/*
 * $Id: YourTemporaryUsersPage.java 406 2011-08-25 02:12:29Z t-nakaguchi $
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
package jp.go.nict.langrid.management.web.view.page.user;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.log.LogWriter;
import jp.go.nict.langrid.management.web.model.TemporaryUserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.model.service.TemporaryUserService;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.component.form.AbstractForm;
import jp.go.nict.langrid.management.web.view.component.link.ConfirmSubmitLink;
import jp.go.nict.langrid.management.web.view.model.provider.TemporaryUserModelSortableDataProvider;
import jp.go.nict.langrid.management.web.view.model.provider.UserSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.user.component.list.LanguageUserListPanel;
import jp.go.nict.langrid.management.web.view.page.user.component.list.TemporaryUserListPanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class YourTemporaryUsersPage extends ServiceManagerPage {
	public YourTemporaryUsersPage(){
		this("");
	}
	/**
	 * 
	 * 
	 */
	public YourTemporaryUsersPage(String message){
		add(getForm());
		Label label;
		add(label = new Label("completeMessage", message));
		label.setMarkupId("infoPanel");
		label.setOutputMarkupId(true);
		add(label);
	}
	private Form<String> getForm(){
		return new AbstractForm<String>("form", "", getSelfGridId()){
			@Override
			protected void addComponents(String initialParameter)
			throws ServiceManagerException
			{
			   String gridId = getSelfGridId();
				TemporaryUserModelSortableDataProvider provider = new TemporaryUserModelSortableDataProvider(
						gridId, getSessionUserId());
				provider.setConditions(
						new MatchingCondition[]{
							new MatchingCondition("parentUserId", getSessionUserId(), MatchingMethod.COMPLETE)}
						, new Order[]{new Order("userId", OrderDirection.ASCENDANT)}
						, Scope.ALL);
				TemporaryUserListPanel panel = new TemporaryUserListPanel(gridId, "userListPanel", provider);
				LinkedHashMap<String, String> nodes = new LinkedHashMap<String, String>();
				nodes.put(getFeedbackPanel().getMarkupId(),
						MessageManager.getMessage(
								"UserSettings.error.validate.require.check.TemporaryUser", getLocale()));
				nodes.put("infoPanel", "");
				add(new ConfirmSubmitLink("delete", this, MessageManager.getMessage(
						"UserSettings.message.delete.alert.Comfirm", getLocale()), nodes)
				{
					@Override
					public void onSubmit() {
						List<TemporaryUserModel> list = (ArrayList<TemporaryUserModel>)checkGroup.getModelObject();
						if(list.size() == 0) {
							error(MessageManager.getMessage(
									"UserSettings.error.validate.require.check.TemporaryUser", getLocale()));
							return;
						}
						try {
							TemporaryUserService service = ServiceFactory.getInstance().getTemporaryUserService(getSelfGridId());
							for(TemporaryUserModel entry : list) {
								service.delete(entry);
								LogWriter.writeInfo(getSessionUserId()
										, "TemporaryUser \"" + entry.getUserId() + "\" has been deleted.", getClass());
							}
						}catch(ServiceManagerException e) {
							raisedException = new ServiceManagerException(e, getPageClass());
						}
						setResponsePage(new YourTemporaryUsersPage(
								MessageManager.getMessage(
										"Common.message.delete.Complete", getLocale())));
					}
					
					private static final long serialVersionUID = 1L;
				});
				
				checkGroup = new CheckGroup<TemporaryUserModel>("group", new ArrayList<TemporaryUserModel>());
				checkGroup.add(panel);
				add(checkGroup);
			}

			@Override
			protected void setResultPage(String resultParameter){
				setResponsePage(new YourTemporaryUsersPage(resultParameter));
			}
		};
	}
	
	private CheckGroup<TemporaryUserModel> checkGroup;
}
