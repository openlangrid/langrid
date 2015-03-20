package jp.go.nict.langrid.management.web.view.component.link;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public class BookmarkablePagingNavigator extends PagingNavigator {
	/**
	 * 
	 * 
	 */
	public BookmarkablePagingNavigator(
	   String gridId, String componentId, IPageable pageable, String paramName)
	{
		super(componentId, pageable);
		this.paramName = paramName;
		targetGridId = gridId;
	}

	@Override	
	protected PagingNavigation newNavigation(
			IPageable pageable, IPagingLabelProvider labelProvider)
	{
		return new PagingNavigation("navigation", pageable, labelProvider){
			@Override
			protected AbstractLink newPagingNavigationLink(
					String id, IPageable pageable, int pageIndex)
			{
				PageParameters pp = new PageParameters();
				int idx = pageIndex;
				if (idx < 0){
					idx = pageable.getPageCount() + idx;
				}
				if (idx > (pageable.getPageCount() - 1)){
					idx = pageable.getPageCount() - 1;
				}
				if (idx < 0){
					idx = 0;
				}
				pp.put(paramName, String.valueOf(idx));
				boolean isEnable = true;
				if(currentIndex == idx){
					isEnable = false;
				}
//			     pp.put("gridId", targetGridId);
				AbstractLink link = new BookmarkablePageLink<PageParameters>(id, getPage().getPageClass(), pp);
				link.setEnabled(isEnable);
				return link;
			}
			
			private static final long serialVersionUID = 1L;
		};
	}
	
	@Override
	protected AbstractLink newPagingNavigationIncrementLink(
			String id, IPageable pageable, int increment)
	{
		int idx = pageable.getCurrentPage() + increment;
		int pageIndex = Math.max(0, Math.min(pageable.getPageCount() - 1, idx));
		PageParameters pp = new PageParameters();
		pp.put(paramName, String.valueOf(pageIndex));
		boolean isEnable = true;
		if(currentIndex == pageIndex){
			isEnable = false;
		}
//	    pp.put("gridId", targetGridId);
		AbstractLink link = new BookmarkablePageLink<PageParameters>(id, getPage().getPageClass(), pp);
		link.setEnabled(isEnable);
		return link;
	}
	
	@Override
	protected AbstractLink newPagingNavigationLink(
			String id, final IPageable pageable, final int pageNumber)
	{
		PageParameters pp = new PageParameters();
		int idx = pageNumber;
		if (idx < 0){
			idx = pageable.getPageCount() + idx;
		}
		if (idx > (pageable.getPageCount() - 1)){
			idx = pageable.getPageCount() - 1;
		}
		if (idx < 0){
			idx = 0;
		}
		pp.put(paramName, String.valueOf(idx));
		boolean isEnable = true;
		if(currentIndex == idx){
			isEnable = false;
		}
//		pp.put("gridId", targetGridId);
		AbstractLink link = new BookmarkablePageLink<PageParameters>(id, getPage().getPageClass(), pp);
		link.setEnabled(isEnable);
		return link;
	}
	
	public void setCurrentIndex(int index){
		currentIndex = index;
	}
	
	private String paramName;
	private String targetGridId;
	private int currentIndex = 0;
	private static final long serialVersionUID = 1L;
}