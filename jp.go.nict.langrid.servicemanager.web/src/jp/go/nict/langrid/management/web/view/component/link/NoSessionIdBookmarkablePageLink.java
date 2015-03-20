package jp.go.nict.langrid.management.web.view.component.link;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.util.string.Strings;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 328 $
 */
public class NoSessionIdBookmarkablePageLink extends BookmarkablePageLink{
	/**
	 * 
	 * 
	 */
	public NoSessionIdBookmarkablePageLink(String componentId, Class page){
		super(componentId, page);
	}
	
	@Override
	protected CharSequence getURL(){
		return Strings.stripJSessionId(super.getURL());
//		String url = new StringBuffer(super.getURL()).toString();
//		String temp = url.replaceAll(";jsessionid=(.+\\?|.+)", "?");
//		return temp.endsWith("?") ? temp.substring(0, temp.length() - 1) : temp;
	}

	private static final long serialVersionUID = 1L;
}
