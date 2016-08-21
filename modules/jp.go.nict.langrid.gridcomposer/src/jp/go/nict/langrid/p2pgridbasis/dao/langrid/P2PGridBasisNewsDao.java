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
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.NewsDao;
import jp.go.nict.langrid.dao.entity.News;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
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
extends AbstractP2PGridBasisDao<News>
implements DataDao, NewsDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisNewsDao(NewsDao dao, DaoContext context) {
		super(context);
		this.dao = dao;
		setHandler(handler);
	}

	@Override
	synchronized public boolean updateData(Data data) throws UnmatchedDataTypeException, DataDaoException {
		logger.finest("[News] : " + data.getId());
		if(data.getClass().equals(NewsData.class) == false) {
			throw new UnmatchedDataTypeException(NewsData.class.toString(), data.getClass().toString());
		}

		News entity = null;
		try {
			entity = ((NewsData)data).getNews();
			if(entity.getGridId().equals(getSelfGridId())) return false;
			if(!isReachableToOrFrom(entity.getGridId())) return false;
		} catch(Exception e){
			throw new DataDaoException(e);
		}
		return handleData(data, entity);
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
	private GenericHandler<News> handler = new GenericHandler<News>(){
		protected boolean onNotificationStart() {
			try{
				getDaoContext().beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.log(Level.SEVERE, "failed to access dao.", e);
				return false;
			}
		}

		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			try{
				getController().publish(new NewsData(
						getDaoContext().loadEntity(News.class, id)
						));
				logger.info("published[News(id=" + id + ")]");
			} catch(ControllerException e){
				logger.log(Level.SEVERE, "failed to publish instance.", e);
			} catch(DaoException e){
				logger.log(Level.SEVERE, "failed to access dao.", e);
			} catch(DataConvertException e){
				logger.log(Level.SEVERE, "failed to convert data.", e);
			}
		}

		protected void doRemove(Serializable id){
			// 
			// 
			logger.info("revoked[News(id=" + id + ")]");
		}

		protected void onNotificationEnd(){
			try{
				getDaoContext().commitTransaction();
			} catch (DaoException e) {
				logger.log(Level.SEVERE, "failed to access dao.", e);
			}
		}
	};

	static private Logger logger = Logger.getLogger(P2PGridBasisNewsDao.class.getName());
}
