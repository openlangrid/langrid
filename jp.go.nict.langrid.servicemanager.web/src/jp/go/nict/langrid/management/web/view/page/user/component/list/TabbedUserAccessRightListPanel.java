package jp.go.nict.langrid.management.web.view.page.user.component.list;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.AccessRightControlModel;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.provider.AccessRightSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.language.service.ControlOfLanguageServicesChangeLimitPage;
import jp.go.nict.langrid.management.web.view.page.language.service.ControlOfLanguageServicesChangeRightConfirmPage;
import jp.go.nict.langrid.management.web.view.page.user.component.list.row.ControlOfUserAccessRightListRowPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

public class TabbedUserAccessRightListPanel extends Panel {
	public TabbedUserAccessRightListPanel(
	   String panelId, String userId, String serviceGridId, String serviceOwnerUserId
	   , final String serviceId, String targetUserGridId)
	throws ServiceManagerException
	{
		super(panelId);
		AccessRightSortableDataProvider provider = new AccessRightSortableDataProvider(
         userId, serviceGridId, serviceOwnerUserId, serviceId, targetUserGridId, "*");
		provider.setConditions(
			new MatchingCondition[]{}
			, new Order[]{new Order("organization", OrderDirection.ASCENDANT)}
			, Scope.ALL);
		add(new UserAccessRightListPanel("userListPanel", serviceGridId, provider) {
         @Override
         protected Panel getRowPanel(
               String gridId, Item<AccessRightControlModel> item, String uniqueId)
         throws ServiceManagerException
         {
            return new ControlOfUserAccessRightListRowPanel(
                  "row", gridId, serviceId, item.getModelObject(), uniqueId)
            {
               @Override
               protected Page getDoChangeLimitPage(
                     ServiceModel entry, boolean permit, String userGridId, String userId, UserModel user)
               {
                  return getChangeLimitPage(entry, permit, userGridId, userId, user);
               }
   
               @Override
               protected Page getDoChangeRightConfirmPage(
                     ServiceModel entry, boolean permit, UserModel user)
               {
                  return getChangeRightConfirmPage(entry, permit, user);
               }
            };
         }
      });
	}
   
	protected Page getChangeLimitPage(
      ServiceModel entry, boolean permit, String userGridId, String userId, UserModel user)
   {
      return new ControlOfLanguageServicesChangeLimitPage(entry, permit, userGridId, userId, user);
   }
   
   protected Page getChangeRightConfirmPage(
      ServiceModel entry, boolean permit, UserModel user)
   {
      return new ControlOfLanguageServicesChangeRightConfirmPage(
            entry.getServiceId(), permit, user);
   }
}
