package jp.go.nict.langrid.management.web.view.component.choice;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 *  
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public abstract class IndicatingAjaxDropDownChoice<T> extends DropDownChoice<T>
implements IAjaxIndicatorAware
{
	/**
	 * 
	 * 
	 */
	public IndicatingAjaxDropDownChoice(String componentId, IModel<T> model, IModel<List<? extends T>> choices){
		super(componentId, model, choices);
		add(indicator);
		add(new AjaxFormComponentUpdatingBehavior("onchange"){
			@Override
			protected void onUpdate(AjaxRequestTarget target){
				doUpdate(target);
			}

			private static final long serialVersionUID = 1L;});
	}
		
	public String getAjaxIndicatorMarkupId(){
		return indicator.getMarkupId();
	}
	
	@Override
	public CharSequence getDefaultChoice(Object selected){
		return null;
	}
	
	protected abstract void doUpdate(AjaxRequestTarget target);
	
	private AjaxIndicatorAppender indicator = new AjaxIndicatorAppender();
	
	private static final long serialVersionUID = 1L;
}