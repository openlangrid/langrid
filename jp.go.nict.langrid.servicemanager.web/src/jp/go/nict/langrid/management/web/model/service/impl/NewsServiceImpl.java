package jp.go.nict.langrid.management.web.model.service.impl;

import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.entity.News;
import jp.go.nict.langrid.management.logic.NewsLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.web.model.NewsModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.LangridList;
import jp.go.nict.langrid.management.web.model.service.NewsService;
import jp.go.nict.langrid.management.web.utility.resource.MessageManager;

public class NewsServiceImpl implements NewsService {
	public NewsServiceImpl() throws ServiceManagerException {
	}

	public void add(NewsModel obj) throws ServiceManagerException {
		try {
			new NewsLogic().addNews(setProperty(obj, new News()), true);
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	public void addList(List<NewsModel> list) throws ServiceManagerException {
		throw new ServiceManagerException(
			MessageManager.getMessage("message.error.ESI001"));
	}

	public void delete(NewsModel condition) throws ServiceManagerException {
		throw new ServiceManagerException(
			MessageManager.getMessage("message.error.ESI001"));
	}

	public void edit(NewsModel obj) throws ServiceManagerException {
		throw new ServiceManagerException(
			MessageManager.getMessage("message.error.ESI001"));
	}

	public NewsModel get(String id) throws ServiceManagerException {
		try {
			for(News news : new NewsLogic().listNews(userGridId)) {
				if(news.getId() == Integer.valueOf(id)) {
					return makeModel(news);
				}
			}
			return null;
		} catch(NumberFormatException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public LangridList<NewsModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException
	{
		try {
			String gridId = serviceGridId;
			for(MatchingCondition mc : conditions) {
				if(mc.getFieldName().equals("gridId")) {
					gridId = (String)mc.getMatchingValue();
					break;
				}
			}
			List<News> buffList = new NewsLogic().listNews(gridId);
			LangridList<NewsModel> list = new LangridList<NewsModel>();
			if(buffList.size() == 0) {
				return list;
			}
			List<News> result = buffList.subList(index
				, buffList.size() < (index + count) ? buffList.size() : (index + count));
			for(News news : result) {
				list.add(makeModel(news));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		try {
			return new NewsLogic().listNews(userGridId).size();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void setScopeParametar(String serviceGridId, String userGridId, String userId) {
		this.serviceGridId = serviceGridId;
		this.userGridId = userGridId;
		this.userId = userId;
	}

	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	private NewsModel makeModel(News entity) {
		NewsModel model = new NewsModel();
		model.setContents(entity.getContents());
		model.setGridId(entity.getGridId());
		model.setCreatedDateTime(entity.getCreatedDateTime());
		model.setUpdatedDateTime(entity.getUpdatedDateTime());
		model.setNodeId(entity.getNodeId());
		model.setId(entity.getId());
		return model;
	}

	private News setProperty(NewsModel model, News entity) {
		entity.setContents(model.getContents());
		entity.setGridId(model.getGridId());
		entity.setNodeId(model.getNodeId());
		return entity;
	}

	private String serviceGridId;
	private String userGridId;
	private String userId;
}
