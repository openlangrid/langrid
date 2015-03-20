package jp.go.nict.langrid.management.web.view.page.node.component.form.panel;

import jp.go.nict.langrid.management.web.view.component.text.CPUField;
import jp.go.nict.langrid.management.web.view.component.text.MemoryField;
import jp.go.nict.langrid.management.web.view.component.text.RequiredURLField;
import jp.go.nict.langrid.management.web.view.component.text.SpecialNoteField;
import jp.go.nict.langrid.management.web.view.page.node.component.text.RequiredNodeNameField;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author$
 * @version $Revision$
 */
public class NodeProfileFieldPanel extends Panel {
	public NodeProfileFieldPanel(String gridId, String panelId) {
		super(panelId);
		setRenderBodyOnly(true);		
		add(new RequiredNodeNameField("nodeName"));
		add(new RequiredURLField("url"));
		add(new CPUField("cpu"));
		add(new MemoryField("memory"));
		add(new TextField<String>("os"));
		add(new SpecialNoteField("specialNotes"));
	}
}
