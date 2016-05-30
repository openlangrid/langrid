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
import jp.go.nict.langrid.dao.SystemPropertyDao;
import jp.go.nict.langrid.dao.entity.SystemProperty;

import org.hibernate.Session;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class HibernateSystemPropertyDao
extends HibernateCRUDDao<SystemProperty>
implements SystemPropertyDao{
	/**
	 * 
	 * 
	 */
	public HibernateSystemPropertyDao(HibernateDaoContext context){
		super(context, SystemProperty.class);
	}

	public void clear() throws DaoException {
		transact(new DaoBlock(){
			public void execute(Session session) {
				for(Object o : createCriteria(session).list()){
					session.delete(o);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SystemProperty> listAllProperties(final String gridId)
			throws DaoException {
		return transact(new DaoBlockR<List<SystemProperty>>(){
			@Override
			public List<SystemProperty> execute(Session session)
					throws DaoException {
				return (List<SystemProperty>)createCriteria(session)
						.add(Property.forName("gridId").eq(gridId))
						.list();
			}
		});
	}

	public String getProperty(final String gridId, final String name)
	throws DaoException{
		return transact(new DaoBlockR<String>(){
			public String execute(Session session) {
				SystemProperty sp = (SystemProperty)createCriteria(session)
					.add(Property.forName("gridId").eq(gridId))
					.add(Property.forName("name").eq(name))
					.uniqueResult();

				if(sp == null) return null;
				else return sp.getValue();
			}
		});
	}

	public void setProperty(final String gridId, final String name, final String value)
	throws DaoException{
		transact(new DaoBlock(){
			public void execute(Session session) {
				session.saveOrUpdate(
						new SystemProperty(gridId, name, value)
						);
			}
		});
	}

	@Override
	public void deletePropertiesOfGrid(final String gridId) throws DaoException {
		transact(new DaoBlock(){
			public void execute(Session session) {
				for(Object o : createCriteria(session)
						.add(Property.forName("gridId").eq(gridId))
						.list()){
					session.delete(o);
				}
			}
		});
	}
}
