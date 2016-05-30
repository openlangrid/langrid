/*
 * $Id: AbstractListPanel.java 406 2011-08-25 02:12:29Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.view.component.list;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.component.link.BookmarkablePagingNavigator;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public abstract class AbstractListPanel<T> extends Panel{
	/**
	 * 
	 * 
	 */
	public AbstractListPanel(String gridId, String componentId, IDataProvider<T> provider){
		super(componentId);
		this.gridId = gridId;
		paramName = componentId + "ListIndex";
		rewriteWrapper = new WebMarkupContainer("rewriteWrapper");
		rewriteWrapper.setOutputMarkupId(true);
		makeList(provider);
		add(rewriteWrapper);
	}

	public String getParamName(){
		return paramName;
	}

	/**
	 * 
	 * 
	 */
	public WebMarkupContainer getRewritableComponent(){
		return rewriteWrapper;
	}

	/**
	 * 
	 * 
	 */
	public void makeList(IDataProvider<T> provider){
		DataView<T> dataView = new DataView<T>(getListId(), provider){
			@Override
			protected void populateItem(Item<T> item){
				addListItem(gridId, newChildId(), item);
			}
			
			@Override
			protected int getViewSize() {
				int size = super.getViewSize();
				emptyPanel.setVisible(size == 0);
				return size;
			}
			

			private static final long serialVersionUID = 1L;
		};
		dataView.setItemsPerPage(getPAGING_COUNT());
		boolean isVisible = (dataView.getRowCount() > getPAGING_COUNT());
		topPagingNavigator = getPagingNavigator(gridId, getTopNavigatorId(), dataView);
		topPagingNavigator.setVisible(isVisible);
		rewriteWrapper.addOrReplace(topPagingNavigator);
		underPagingNavigator = getPagingNavigator(gridId, getUnderNavigatorId(), dataView);
		underPagingNavigator.setVisible(isVisible);
		rewriteWrapper.addOrReplace(underPagingNavigator);
		rewriteWrapper.addOrReplace(getHeaderPanel());
		emptyPanel = getEmptyRowPanel();
		rewriteWrapper.addOrReplace(emptyPanel);
		pageable = dataView;
		setCurrentIndex(0);
		rewriteWrapper.addOrReplace(dataView);
	}

	public void setCurrentIndex(int index){
		pageable.setCurrentPage(index);
		if(topPagingNavigator instanceof BookmarkablePagingNavigator){
			((BookmarkablePagingNavigator)topPagingNavigator).setCurrentIndex(index);
		}
		if(topPagingNavigator instanceof BookmarkablePagingNavigator){
			((BookmarkablePagingNavigator)underPagingNavigator).setCurrentIndex(index);
		}
	}
	
	public String getPanelGridId(){
	   return gridId;
	}
	
	/**
	 * 
	 * 
	 */
	protected abstract void addListItem(String gridId, String rowId, Item<T> item);

	/**
	 * 
	 * 
	 */
	protected abstract String getListId();

	/**
	 * 
	 * 
	 */
	protected int getPAGING_COUNT(){
		return PAGING_COUNT;
	}

	/**
	 * 
	 * 
	 */
	protected PagingNavigator getPagingNavigator(String gridId, String componentId, IPageable pageable){
		return new PagingNavigator(componentId, pageable);
	}
	
	/**
	 * 
	 * 
	 */
	protected abstract Panel getRowPanel(String gridId, Item<T> item, String uniqueId)
	throws ServiceManagerException;
	
	/**
	 * 
	 * 
	 */
	protected abstract Panel getHeaderPanel();

	/**
	 * 
	 * 
	 */
	protected abstract Panel getEmptyRowPanel();
	
	/**
	 * 
	 * 
	 */
	protected abstract String getTopNavigatorId();

	/**
	 * 
	 * 
	 */
	protected abstract String getUnderNavigatorId();
	
	protected WebMarkupContainer rewriteWrapper;
	
	private IPageable pageable;
	private String paramName;
	private PagingNavigator topPagingNavigator;
	private PagingNavigator underPagingNavigator;
	private String gridId;
	private Panel emptyPanel;
	
	private static final int PAGING_COUNT = 50;
}
