package jp.go.nict.langrid.management.web.view.page.language.service.component.list;

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.MatchingMethod;
import jp.go.nict.langrid.dao.OrderDirection;
import jp.go.nict.langrid.management.web.model.ServiceModel;
import jp.go.nict.langrid.management.web.model.enumeration.ListType;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.model.LangridSearchCondition;
import jp.go.nict.langrid.management.web.view.model.provider.AtomicServiceSortableDataProvider;
import jp.go.nict.langrid.management.web.view.model.provider.CompositeServiceSortableDataProvider;
import jp.go.nict.langrid.management.web.view.page.ServiceManagerPage;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceAccessRightRadioPanel;
import jp.go.nict.langrid.management.web.view.page.language.service.component.form.panel.ServiceListSortFormPanel;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class ServiceListFullOptionTabPanel extends Panel {
	/**
	 * 
	 * 
	 */
   public ServiceListFullOptionTabPanel(
      String panelId, String targetGridId, String userGridId, String userId) 
   throws ServiceManagerException
   {
      this(panelId, targetGridId, userGridId, userId, new LangridSearchCondition());
   }
   
	public ServiceListFullOptionTabPanel(
			String panelId, String targetGridId, String userGridId, String userId, LangridSearchCondition conditions) 
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
		conditions.putOrReplaceOrder("serviceName", OrderDirection.ASCENDANT);

		setOutputMarkupId(true);
		listContainer.add(
				makeList("atomicList", ListType.PUBLICATOMICSERVICE, conditions));
		listContainer.add(getSortPanel( 
				"atomicListSortPanel", "atomicList", ListType.PUBLICATOMICSERVICE));
		listContainer.add(
				new ShowAllLink("showAllAtomic", "atomicList", ListType.PUBLICATOMICSERVICE));
		listContainer.add(getAccessRightPanel("atomicAccessRightPanel", "atomicList", ListType.PUBLICATOMICSERVICE));
		listContainer.add(makeList("compositeList", ListType.PUBLICCOMPOSITESERVICE, conditions));
		listContainer.add(getSortPanel( 
				"compositeListSortPanel", "compositeList", ListType.PUBLICCOMPOSITESERVICE));
		listContainer.add(new ShowAllLink("showAllComposite", "compositeList", ListType.PUBLICCOMPOSITESERVICE));
		listContainer.add(getAccessRightPanel("compositeAccessRightPanel", "compositeList", ListType.PUBLICCOMPOSITESERVICE));
		add(listContainer);
	}
	
	public List<Component> getRewritableComponentList() {
		return new ArrayList() {{
			add(listContainer);
		}};
	}
	
	public void rewriteAllList(LangridSearchCondition conditions) throws ServiceManagerException {
	   this.conditions.clearCondition();
	   for(MatchingCondition m : conditions.getConditions()){
	      this.conditions.putOrReplaceCondition(
	         m.getFieldName(), (String)m.getMatchingValue(), m.getMatchingMethod());
	   }
//	   this.conditions = conditions;
	   
	   this.conditions.putOrReplaceCondition("gridId", targetGridId, MatchingMethod.COMPLETE);
	   this.conditions.putOrReplaceCondition("membersOnly", atomicMembersOnly);
//	   this.conditions.putOrReplaceOrder(conditions.getOrders());
	   this.conditions.setScope(conditions.getScope());
		listContainer.addOrReplace(makeList("atomicList", ListType.PUBLICATOMICSERVICE, this.conditions));
		this.conditions.putOrReplaceCondition("membersOnly", compositeMembersOnly);
		listContainer.addOrReplace(makeList("compositeList", ListType.PUBLICCOMPOSITESERVICE, this.conditions));
	}
	
	protected <T extends ServiceModel> ServiceListPanel<T> getListPanel(
			String gridId, String panelId, SortableDataProvider<T> provider)
	{
		return new ServiceListPanel<T>(gridId, panelId, provider);
	}
	
	private void doErrorProcess(ServiceManagerException e) {
		((ServiceManagerPage)getPage()).doErrorProcess(e);
	}
	
	private ServiceListSortFormPanel getSortPanel( 
			String panelId, final String listId, final ListType listType)
	{
		return new ServiceListSortFormPanel(panelId){
			@Override
			protected void onSortRequested(AjaxRequestTarget target){
				try{
					conditions.clearOrder();
					conditions.putOrReplaceOrder(getSortOrder().getOrder());
					if(listType.equals(ListType.PUBLICATOMICSERVICE)){
					   conditions.putOrReplaceCondition("membersOnly", atomicMembersOnly);
					}else{
					   conditions.putOrReplaceCondition("membersOnly", compositeMembersOnly);
					}
					target.addComponent(makeList(listId, listType, conditions));
				}catch(ServiceManagerException e){
					doErrorProcess(e);
				}
			}
		};
	}
	
	public LangridSearchCondition getConditions(){
	   return conditions;
	}

	private ServiceAccessRightRadioPanel getAccessRightPanel(
      String panelId, final String listId, final ListType listType) 
   {
      return new ServiceAccessRightRadioPanel(panelId){
         @Override
         protected void doUpdate(boolean isMembersOnly, AjaxRequestTarget target) {
            try{
               if(listType.equals(ListType.PUBLICATOMICSERVICE)){
                  atomicMembersOnly = isMembersOnly;
               }else{
                  compositeMembersOnly = isMembersOnly;
               }
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
	private boolean atomicMembersOnly = false;
	private boolean compositeMembersOnly = false;
	private String targetGridId;
	private String userGridId;
	private String userId;
	
	private class ShowAllLink extends IndicatingAjaxLink{
		public ShowAllLink(String componentId, String targetListName, ListType listType) {
			super(componentId);
			listName = targetListName;
			this.listType = listType;
		}
		@Override
		public void onClick(AjaxRequestTarget target){
			try{
				conditions = new LangridSearchCondition();
				String prefix = listType.equals(ListType.PUBLICATOMICSERVICE) ? "atomic" : "composite";
				ServiceListSortFormPanel sort;
				listContainer.addOrReplace(sort = getSortPanel(prefix + "ListSortPanel", listName, listType));
				ServiceAccessRightRadioPanel accessRight;
				listContainer.addOrReplace(accessRight = getAccessRightPanel(prefix + "AccessRightPanel", listName, listType));
				conditions.putOrReplaceCondition("membersOnly", false);
				target.addComponent(makeList(listName, listType, conditions));
				target.addComponent(sort);
				target.addComponent(accessRight);
			}catch(ServiceManagerException e){
				doErrorProcess(e);
			}
		}
		
		private String listName;
		private ListType listType;
	}
}
