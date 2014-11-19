/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2014 Language Grid Project.
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
package jp.go.nict.langrid.dao.jsonic;

import java.util.Collections;
import java.util.List;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.NewsDao;
import jp.go.nict.langrid.dao.entity.News;

public class JsonicNewsDao implements NewsDao {

	@Override
	public void clear() throws DaoException {
	}

	@Override
	public boolean isNewsExist(int newsId) throws DaoException {
		return false;
	}

	@Override
	public void addNews(News news) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<News> listNews(String newsGridId) throws DaoException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public void deleteAllNews(String newsGridId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isNewsExistByNodeIds(String gridId, String nodeId,
			int nodeLocalId) throws DaoException {
		return false;
	}

	@Override
	public void updateNewsByNodeIds(News news) throws DaoException {
		throw new UnsupportedOperationException();
	}

}
