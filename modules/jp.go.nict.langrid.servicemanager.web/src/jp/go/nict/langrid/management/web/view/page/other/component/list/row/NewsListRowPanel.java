package jp.go.nict.langrid.management.web.view.page.other.component.list.row;

import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.utility.DateUtil;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class NewsListRowPanel extends Panel{
	/**
	 * 
	 * 
	 */
	public NewsListRowPanel(
			String componentId, NewsModel news, String uniqueId)
	{
		super(componentId);
		add(new Label("date", DateUtil.formatYMDWithSlash(
				news.getCreatedDateTime().getTime())));
		Label label = new Label("news", news.getContents());
		label.setEscapeModelStrings(false);
		add(label);
	}

	private static final long serialVersionUID = 1L;
}
