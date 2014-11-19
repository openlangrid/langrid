/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2011 NICT Language Grid Project.
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
package jp.go.nict.langrid.dao.hibernate;

import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.NewsDao;
import jp.go.nict.langrid.dao.entity.News;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author Takao Nakaguchi
 */
public class HibernateNewsDao
extends HibernateDao
implements NewsDao
{
	/**
	 * 
	 * 
	 */
	public HibernateNewsDao(HibernateDaoContext context){
		super(context);
	}

	public void clear() throws DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try {
			session.createQuery("delete from News").executeUpdate();
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isNewsExist(int newsId) throws DaoException {
		try {
			return ((Integer)getSession().createCriteria(News.class)
				.add(Property.forName("id").eq(newsId))
				.setProjection(Projections.count("id"))
				.uniqueResult()) > 0;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<News> listNews(String newsGridId) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try {
			List<News> list = 	session
				.createCriteria(News.class)
				.add(Property.forName("gridId").eq(newsGridId))
				.addOrder(Order.desc("createdDateTime"))
				.list();
			getContext().commitTransaction();
			return list;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void addNews(News news) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try {
			session.save(news);
			news.setNodeLocalId(news.getId());
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteAllNews(String newsGridId) throws DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try {
			for(News ob : listNews(newsGridId)) {
				session.delete(ob);
			}
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isNewsExistByNodeIds(final String gridId, final String nodeId, final int nodeLocalId)
	throws DaoException {
		return transact(new DaoBlockR<Boolean>() {
			@Override
			public Boolean execute(Session session) throws DaoException {
				return ((Number)session.createCriteria(News.class)
						.add(Property.forName("gridId").eq(gridId))
						.add(nodeId != null ? Property.forName("nodeId").eq(nodeId) : Property.forName("nodeId").isNull())
						.add(Property.forName("nodeLocalId").eq(nodeLocalId))
						.setProjection(Projections.rowCount())
						.uniqueResult()).intValue() > 0;
			}
		});
	}

	@Override
	public void updateNewsByNodeIds(final News news) throws DaoException {
		transact(new DaoBlock() {
			@Override
			public void execute(Session session) throws DaoException {
				News orig = (News)session.createCriteria(News.class)
						.add(Property.forName("gridId").eq(news.getGridId()))
						.add(news.getNodeId() != null ? Property.forName("nodeId").eq(news.getNodeId()) : Property.forName("nodeId").isNull())
						.add(Property.forName("nodeLocalId").eq(news.getNodeLocalId()))
						.uniqueResult();
				orig.setContents(news.getContents());
				orig.setCreatedDateTime(news.getCreatedDateTime());
				orig.setGridId(news.getGridId());
				orig.setUpdatedDateTime(news.getUpdatedDateTime());
			}
		});
	}
}
