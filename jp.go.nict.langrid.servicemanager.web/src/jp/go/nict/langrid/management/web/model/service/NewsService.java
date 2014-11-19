package jp.go.nict.langrid.management.web.model.service;

import java.util.List;

import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;

public interface NewsService extends DataService<NewsModel> {
	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	public void addList(List<NewsModel> list) throws ServiceManagerException;
}
