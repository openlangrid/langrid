package jp.go.nict.langrid.management.web.view.page.user.component.link.panel;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class NoCountPagingLinkPanel extends Panel {
	public NoCountPagingLinkPanel(
			String panelId, IModel<Class<? extends Page>> model, int pageIndex
			, PageParameters parameters, boolean isFixed)
	{
		super(panelId, model);
		this.parameters = parameters;
		Link previewLink = createLink("previewLink", pageIndex - 1);
		previewLink.add(new Label("preview", "< Newer").setRenderBodyOnly(true));
		previewLink.setEnabled(0 <= pageIndex - 1);
		add(previewLink);
		Link nextLink = createLink("nextLink", pageIndex + 1);
		nextLink.add(new Label("next", "Older >").setRenderBodyOnly(true));
		nextLink.setEnabled( ! isFixed);
		add(nextLink);
	}
	
	protected Link createLink(String linkId, int index) {
		return new Link<Integer>(linkId, new Model<Integer>(index)) {
         @Override
         @SuppressWarnings("unchecked")
			public void onClick() {
				parameters.put("index", String.valueOf(getModelObject()));
				setResponsePage((Class<Page>) getDefaultModelObject(), parameters);
			}
		};
	}
	
	private PageParameters parameters;
}
