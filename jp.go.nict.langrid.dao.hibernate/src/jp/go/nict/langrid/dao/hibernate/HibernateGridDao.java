/*
 * $Id:HibernateGridDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
 *
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2005-2008 NICT Language Grid Project.
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

import org.hibernate.HibernateException;
import org.hibernate.Session;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GridAlreadyExistsException;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.GridNotFoundException;
import jp.go.nict.langrid.dao.entity.Grid;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 216 $
 */
public class HibernateGridDao
extends HibernateCRUDDao<Grid>
implements GridDao{
	/**
	 * 
	 * 
	 */
	public HibernateGridDao(HibernateDaoContext context){
		super(context, Grid.class);
	}

	@Override
	public void clear() throws DaoException {
		transact(new DaoBlock(){
			@Override
			public void execute(Session session) throws DaoException {
				for(Object g : session.createCriteria(Grid.class).list()){
					session.delete(g);
				}
			}
		});
	}

	public void addGrid(Grid grid)
	throws GridAlreadyExistsException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			String gid = grid.getGridId();
			Grid current = (Grid)session.get(Grid.class, gid);
			if(current != null){
				getContext().commitTransaction();
				throw new GridAlreadyExistsException(gid);
			} else{
				session.save(grid);
				getContext().commitTransaction();
			}
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(RuntimeException e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		} catch(Error e){
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public void deleteGrid(String gridId)
	throws GridNotFoundException, DaoException{
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Grid current = (Grid)session.get(Grid.class, gridId);
			if(current == null){
				getContext().commitTransaction();
				throw new GridNotFoundException(gridId);
			}
			session.delete(current);
			getContext().commitTransaction();
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	public Grid getGrid(String gridId)
	throws GridNotFoundException, DaoException {
		Session session = getSession();
		getContext().beginTransaction();
		try{
			Grid current = (Grid)session.get(Grid.class, gridId);
			getContext().commitTransaction();
			if(current == null){
				throw new GridNotFoundException(gridId);
			}
			return current;
		} catch(HibernateException e){
			logAdditionalInfo(e);
			getContext().rollbackTransaction();
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isGridExist(String gridId) throws DaoException {
		return exists(gridId);
	}

	@Override
	public List<Grid> listAllGrids() throws DaoException {
		return list();
	}
}
