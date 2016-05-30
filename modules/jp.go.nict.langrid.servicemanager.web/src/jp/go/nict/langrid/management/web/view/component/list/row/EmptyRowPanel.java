package jp.go.nict.langrid.management.web.view.component.list.row;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class EmptyRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public EmptyRowPanel(String componentId, int columnCount) {
		super(componentId);
		RepeatingView rv = new RepeatingView("repeating");
		for(int i = 0; i < columnCount; i++){
			rv.add(new Label(rv.newChildId(), "-"));
		}
		add(rv);
	}

	private static final long serialVersionUID = 1L;
}
