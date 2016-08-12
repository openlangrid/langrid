/*
 * $Id: P2PGridBasisNewsDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2009 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.p2pgridbasis.dao.langrid;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.NewsDao;
import jp.go.nict.langrid.dao.entity.News;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.NewsData;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisNewsDao
extends AbstractP2PGridBasisDao
implements DataDao, NewsDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisNewsDao(NewsDao dao, DaoContext context) {
		this.dao = dao;
		this.daoContext = context;
	}

	private P2PGridController getController() throws ControllerException{
		if (controller == null) {
			controller = JXTAController.getInstance();
		}

		return controller;
	}

	public void setEntityListener() {
		logger.debug("### News : setEntityListener ###");
		daoContext.addEntityListener(News.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### News : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(News.class, handler);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.dao#updateDataSource(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public boolean updateDataSource(Data data) throws DataDaoException, UnmatchedDataTypeException {
		return updateDataTarget(data);
	}
	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.dao#updateData(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public boolean updateDataTarget(Data data) throws UnmatchedDataTypeException, DataDaoException {
		logger.debug("[News] : " + data.getId());
		if(data.getClass().equals(NewsData.class) == false) {
			throw new UnmatchedDataTypeException(NewsData.class.toString(), data.getClass().toString());
		}

		NewsData newsData = (NewsData) data;
		try{
			if(!isReachableForwardOrBackward(
					this.getController().getSelfGridId(), newsData.getGridId())){
				return false;
			}
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		} catch (DaoException e) {
			throw new DataDaoException(e);
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
			// 
			// 
			logger.info("Delete");
			return false;
		}

		News news = null;
		try {
			news = newsData.getNews();

			logger.debug("New or UpDate");
			removeEntityListener();
			daoContext.beginTransaction();
			try{
				if(dao.isNewsExistByNodeIds(news.getGridId(), news.getNodeId(), news.getNodeLocalId())){
					dao.updateNewsByNodeIds(news);
				} else{
					int nlid = news.getNodeLocalId();
					dao.addNews(news);
					news.setNodeLocalId(nlid);
				}
			} finally{
				daoContext.commitTransaction();
			}
			setEntityListener();
			getController().baseSummaryAdd(data);
			return true;
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public boolean isNewsExist(int newsId) throws DaoException {
		return dao.isNewsExist(newsId);
	}

	@Override
	public boolean isNewsExistByNodeIds(String gridId, String nodeId, int nodeLocalId)
	throws DaoException {
		return dao.isNewsExistByNodeIds(gridId, nodeId, nodeLocalId);
	}

	@Override
	public void updateNewsByNodeIds(News news) throws DaoException {
		dao.updateNewsByNodeIds(news);
	}

	@Override
	public void addNews(News news) throws DaoException {
		dao.addNews(news);
	}

	@Override
	public List<News> listNews(String newsGridId) throws DaoException {
		return dao.listNews(newsGridId);
	}

	@Override
	public void deleteAllNews(String newsGridId) throws DaoException {
		dao.deleteAllNews(newsGridId);
	}

	private NewsDao dao;
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<News> handler = new GenericHandler<News>(){
		protected boolean onNotificationStart() {
			try{
				daoContext.beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
				return false;
			}
		}

		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			try{
				getController().publish(new NewsData(
						daoContext.loadEntity(News.class, id)
						));
				logger.info("published[News(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to publish instance.", e);
			} catch(DaoException e){
				logger.error("failed to access dao.", e);
			} catch(DataConvertException e){
				logger.error("failed to convert data.", e);
			}
		}

		protected void doRemove(Serializable id){
			// 
			// 
			logger.info("revoked[News(id=" + id + ")]");
		}

		protected void onNotificationEnd(){
			try{
				daoContext.commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};

	static private Logger logger = Logger.getLogger(P2PGridBasisNewsDao.class);
}
