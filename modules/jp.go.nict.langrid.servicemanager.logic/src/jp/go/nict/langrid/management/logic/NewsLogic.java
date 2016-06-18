/*
 * $Id: NewsLogic.java 302 2010-12-01 02:49:42Z t-nakaguchi $
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
package jp.go.nict.langrid.management.logic;

import java.util.List;
import java.util.logging.Logger;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.News;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 302 $
 */
public class NewsLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public NewsLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getNewsDao().clear();
	}

	@DaoTransaction
	public List<News> listNews(String gridId) throws DaoException
	{
	   return getNewsDao().listNews(gridId);
	}

	@DaoTransaction
	public void addNews(News news, boolean allowAllAccess) throws DaoException {
	   getNewsDao().addNews(news);
	}

	private static Logger logger = Logger.getLogger(
			NewsLogic.class.getName());
}