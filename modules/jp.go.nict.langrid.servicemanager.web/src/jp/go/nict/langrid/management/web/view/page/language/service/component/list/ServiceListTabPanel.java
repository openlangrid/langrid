package jp.go.nict.langrid.management.web.view.page.language.service.component.list;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.UserModel;
import jp.go.nict.langrid.management.web.model.enumeration.GridRelation;
import jp.go.nict.langrid.management.web.model.enumeration.ListType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.ServiceFactory;
import jp.go.nict.langrid.management.web.view.model.LangridSearchCondition;
import jp.go.nict.langrid.management.web.view.model.provider.AtomicServiceSortableDataProvider;
import jp.go.nict.langrid.management.web.view.model.provider.CompositeServiceSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceAccessRightRadioPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.list.row.LanguageServicesListRowPanel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ServiceListTabPanel extends Panel {
	/**
	 * 
	 * 
	 */
	public ServiceListTabPanel(
			String panelId, String targetGridId, String userGridId, String userId, LangridSearchCondition conditions, GridRelation relation) 
	throws ServiceManagerException
	{
		super(panelId);
		this.targetGridId = targetGridId;
		this.userGridId = userGridId;
		this.userId = userId;
		listContainer = new WebMarkupContainer("listContainer");
		listContainer.setOutputMarkupId(true);
		this.conditions = conditions;
		conditions.putOrReplaceCondition("membersOnly", false);
		setOutputMarkupId(true);
		listContainer.add(makeList("atomicList", ListType.PUBLICATOMICSERVICE, conditions));
		listContainer.add(getAccessRightPanel("atomicAccessRightPanel", "atomicList", ListType.PUBLICATOMICSERVICE));
		listContainer.add(makeList("compositeList", ListType.PUBLICCOMPOSITESERVICE, conditions));
		listContainer.add(getAccessRightPanel("compositeAccessRightPanel", "compositeList", ListType.PUBLICCOMPOSITESERVICE));
		add(listContainer);
	}
	
	public List<Component> getRewritableComponentList() {
		return new ArrayList() {{
			add(listContainer);
		}};
	}
	
	public void rewriteAllList(LangridSearchCondition conditions) throws ServiceManagerException {
	   this.conditions = conditions;
	   for(MatchingCondition m : conditions.getConditions()){
	      this.conditions.putOrReplaceCondition(
	         m.getFieldName(), (String)m.getMatchingValue(), m.getMatchingMethod());
	   }
	   this.conditions.putOrReplaceOrder(conditions.getOrders());
	   this.conditions.setScope(conditions.getScope());
	   
		listContainer.addOrReplace(makeList("atomicList", ListType.PUBLICATOMICSERVICE, conditions));
		listContainer.addOrReplace(makeList("compositeList", ListType.PUBLICCOMPOSITESERVICE, conditions));
	}
	
	protected <T extends ServiceModel> ServiceListPanel<T> getListPanel(
			String gridId, String panelId, SortableDataProvider<T> provider)
	{
		return new ServiceListPanel<T>(gridId, panelId, provider){
		   @Override
		   protected Panel getRowPanel(String nowGridId, Item<T> item, String uniqueId)
		   throws ServiceManagerException {
		      return getListRowPanel(nowGridId, item, uniqueId);
		   }
		};
	}
	
	protected <T extends ServiceModel> Panel getListRowPanel(String nowGridId, Item<T> item, String uniqueId)
	throws ServiceManagerException
	{
     return new LanguageServicesListRowPanel("row", item.getModelObject(), uniqueId){
         @Override
         protected String getOrganizationName(ServiceModel obj) throws ServiceManagerException{
            UserModel ue = ServiceFactory.getInstance().getUserService(obj.getGridId()).get(obj.getOwnerUserId());
            return ue == null ? "" : ue.getOrganization();
         }

         private static final long serialVersionUID = 1L;
      };
	}
	
	private void doErrorProcess(ServiceManagerException e) {
		((ServiceManagerPage)getPage()).doErrorProcess(e);
	}
	
	public LangridSearchCondition getConditions(){
	   return conditions;
	}

	protected ServiceAccessRightRadioPanel getAccessRightPanel(
      String panelId, final String listId, final ListType listType) 
   {
      return new ServiceAccessRightRadioPanel(panelId){
         @Override
         protected void doUpdate(boolean isMembersOnly, AjaxRequestTarget target) {
            try{
               conditions.putOrReplaceCondition("membersOnly", isMembersOnly);
               target.addComponent(makeList(listId, listType, conditions));
            }catch(ServiceManagerException e){
               doErrorProcess(e);
            }
         }
      };
   }

   private Component makeList(
			String listId, ListType type, LangridSearchCondition conditions)
	throws ServiceManagerException
	{
		if(type.equals(ListType.PUBLICATOMICSERVICE)){
			AtomicServiceSortableDataProvider provider = new AtomicServiceSortableDataProvider(targetGridId, userGridId, userId);
			provider.setConditions(conditions.getConditions(), conditions.getOrders(), conditions.getScope());
			Component component = getListPanel(targetGridId, listId, provider);
			component.setOutputMarkupId(true);
			listContainer.addOrReplace(component);
			return component;
		}else if(type.equals(ListType.PUBLICCOMPOSITESERVICE)) {
			CompositeServiceSortableDataProvider provider = new CompositeServiceSortableDataProvider(targetGridId, userGridId, userId);
			provider.setConditions(conditions.getConditions(), conditions.getOrders(), conditions.getScope());
			Component component = getListPanel(targetGridId, listId, provider);
			component.setOutputMarkupId(true);
			listContainer.addOrReplace(component);
			return component;
		}
		return null;
	}
   
	private LangridSearchCondition conditions;
	private WebMarkupContainer listContainer;
	private String targetGridId;
	private String userGridId;
	private String userId;
}
