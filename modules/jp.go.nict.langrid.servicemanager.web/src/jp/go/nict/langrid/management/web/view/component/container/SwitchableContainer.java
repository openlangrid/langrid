package jp.go.nict.langrid.management.web.view.component.container;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class SwitchableContainer<T extends Component, T2 extends Component>
extends WebMarkupContainer
{
	/**
	 * 
	 * 
	 */
	public SwitchableContainer(String componentId, T firstComponent){
		this(componentId, firstComponent, null);
	}

	/**
	 * 
	 * 
	 */
	public SwitchableContainer(String componentId, T firstComponent, T2 secondComponent){
		super(componentId);
		add(fc = firstComponent);
		if(secondComponent != null){
			add(sc = secondComponent);
			sc.setOutputMarkupPlaceholderTag(true);
			sc.setVisible(false);
			sc.setEnabled(false);
		}
		fc.setOutputMarkupPlaceholderTag(true);
	}

	public T getFirstComponent(){
		return fc;
	}
	
	public T2 getSecondComponent(){
		return sc;
	}
	
	/**
	 * 
	 * 
	 */
	public void switchComponent(){
		fc.setVisible(!fc.isVisible());
		fc.setEnabled(!fc.isEnabled());
		if(sc != null){
			sc.setVisible(!sc.isVisible());
			sc.setEnabled(!sc.isEnabled());
		}
	}
	
	public boolean isFirstVisible(){
		return fc.isVisible();
	}
	
	private T fc;
	private T2 sc;

	private static final long serialVersionUID = 1L;
}