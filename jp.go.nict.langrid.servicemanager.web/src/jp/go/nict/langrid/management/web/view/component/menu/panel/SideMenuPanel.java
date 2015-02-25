package jp.go.nict.langrid.management.web.view.component.menu.panel;

import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.management.web.model.enumeration.UserRole;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;
import jp.go.nict.langrid.management.web.view.page.other.Unoperatable;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.w3c.dom.Node;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class SideMenuPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public SideMenuPanel(String componentId){
		super(componentId);
		menu = new RepeatingView("repeating");
		add(menu);
	}
	
	/**
	 * 
	 * 
	 */
	public SideMenuPanel(String componentId, Set<UserRole> userRoles){
		super(componentId);
		menu = new RepeatingView("repeating");
		this.userRoles = userRoles;
		add(menu);
	}
	
	public void makeMenuComponents(List<Node> nodeList) throws ClassNotFoundException {
		for(Node node : nodeList) {
			String label = node.getAttributes().getNamedItem("label").getNodeValue();
			String className = node.getAttributes().getNamedItem("class").getNodeValue();
			Link link = getLink("link-on", label, className);
			
			WebMarkupContainer labelContainer = new WebMarkupContainer("link-off");
			labelContainer.add(new Label("linkLabel", label));
			
			WebMarkupContainer wmc = new WebMarkupContainer(menu.newChildId());
			wmc.add(link);
			wmc.add(labelContainer.setVisible(false));
			wmc.add(new WebMarkupContainer("nestedMenu").setVisible(false));
			menu.add(wmc);
		}
	}

	public void makeDisabledNestMenuComponents(List<Node> nodeList) throws ClassNotFoundException {
		for(Node node : nodeList) {
			String label = node.getAttributes().getNamedItem("label").getNodeValue();
			String className = node.getAttributes().getNamedItem("class").getNodeValue();
			Link link = getLink("link-on", label, className);
			
			WebMarkupContainer labelContainer = new WebMarkupContainer("link-off");
			labelContainer.add(new Label("linkLabel", MessageManager.getMessage(label)));
			
			WebMarkupContainer wmc = new WebMarkupContainer(menu.newChildId());
			wmc.add(link.setVisible(false));
			wmc.add(labelContainer);
			wmc.add(new WebMarkupContainer("nestedMenu").setVisible(false));
			menu.add(wmc);
		}
	}
	
	public void makeNestMenuComponents(
			Node openTarget, List<Node> topNodeList, List<Node> subNodeList)
	throws ClassNotFoundException
	{
		for (Node node: topNodeList) {
			String label = node.getAttributes().getNamedItem("label").getNodeValue();

			Node requiredRolesAttr = node.getAttributes().getNamedItem("requiredRoles");
			boolean unoperatable = false;
			do{
				if(requiredRolesAttr == null) break;
				String requiredRoles = requiredRolesAttr.getTextContent().toUpperCase();
				if(requiredRoles.equals("NONE")) break;
				if(requiredRoles.equals("ANY")){
					if(!userRoles.isEmpty()) break;
				} else if(requiredRoles.equals("EMPTY")){
					if(userRoles.isEmpty()) break;
				} else{
					UserRole requiredRole = UserRole.valueOf(requiredRoles);
					if(userRoles.contains(requiredRole)) break;
				}
				unoperatable = true;
			} while(false);
			Link link = getLink("link-on", label,
					unoperatable ?
							Unoperatable.class.getName() :
							node.getAttributes().getNamedItem("class").getNodeValue());
			
			WebMarkupContainer labelContainer = new WebMarkupContainer("link-off");
			labelContainer.add(new Label("linkLabel", MessageManager.getMessage(label, getLocale())));
			
			WebMarkupContainer wmc = new WebMarkupContainer(menu.newChildId());

			wmc.add(link);
			wmc.add(labelContainer);
			menu.add(wmc);

			if(node.equals(openTarget) ||
					(subNodeList.contains(openTarget) && node.equals(subNodeList.get(0).getParentNode())))
			{
				link.setVisible(false);
				SideMenuPanel nestMenu = new SideMenuPanel("nestedMenu", userRoles);
				if(unoperatable){
					nestMenu.makeDisabledNestMenuComponents(subNodeList);
				}else {
					nestMenu.makeMenuComponents(subNodeList);
				}
				
				wmc.add(nestMenu);
			}else{
				labelContainer.setVisible(false);
				wmc.add(new WebMarkupContainer("nestedMenu").setVisible(false));
			}
		}
	}

	private Link getLink(String componentId, String label, String className)
	throws ClassNotFoundException
	{
		if(userRoles.isEmpty()){
			final Class page = Class.forName(className.replaceAll("Page$", "LogOutPage"));
//			Link link = new NoSessionIdBookmarkablePageLink(componentId, page);
			Link link = new BookmarkablePageLink(componentId, page);
			link.add(new Label("linkLabel", MessageManager.getMessage(label, getLocale())));
			return link;
		}else{
			final Class page = Class.forName(className);
			Link link = new Link(componentId){
				@Override
				public void onClick(){
					try{
						setResponsePage((Page)page.newInstance());
					}catch(InstantiationException e){
						e.printStackTrace();
					}catch(IllegalAccessException e){
						e.printStackTrace();
					}
				}

				@Override
				protected CharSequence getURL(){
					String url = new StringBuffer(super.getURL()).toString();
					String temp = url.replaceAll(";jsessionid=(.+\\?|.+)", "?");					
					return temp.endsWith("?") ? temp.substring(0, temp.length() - 1) : temp;
				}

				private static final long serialVersionUID = 1L;
			};
			link.add(new Label("linkLabel", MessageManager.getMessage(label, getLocale())));
			return link;
		}
	}

	private RepeatingView menu;
	private Set<UserRole> userRoles;
	private static final long serialVersionUID = 1L;
}
