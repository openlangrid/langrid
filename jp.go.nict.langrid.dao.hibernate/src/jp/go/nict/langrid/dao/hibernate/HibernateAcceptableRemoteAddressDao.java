/*
 * $Id:HibernateAccessRightDao.java 4384 2007-04-03 08:56:48Z nakaguchi $
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

import jp.go.nict.langrid.dao.AcceptableRemoteAddressDao;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.AcceptableRemoteAddress;

import org.hibernate.Session;
import org.hibernate.criterion.Property;

/**
 * 
 * 
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class HibernateAcceptableRemoteAddressDao
extends HibernateDao
implements AcceptableRemoteAddressDao
{
	/**
	 * 
	 * 
	 */
	public HibernateAcceptableRemoteAddressDao(HibernateDaoContext context){
		super(context);
	}

	public void clear() throws DaoException{
		transact(new DaoBlock(){public void execute(Session p1) {
			createDeleteQuery(AcceptableRemoteAddress.class).executeUpdate();
		}});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AcceptableRemoteAddress> listAcceptableRemoteAddress()
	throws DaoException {
		return transact(new DaoBlockR<List<AcceptableRemoteAddress>>(){
			public List<AcceptableRemoteAddress> execute(Session session) {
				return session
						.createCriteria(AcceptableRemoteAddress.class)
						.list();
			}
		});
	}

	@Override
	public void addRemoteAddress(final String address)
	throws DaoException {
		transact(new DaoBlock(){public void execute(Session session) {
			session.save(new AcceptableRemoteAddress(address));
		}});
	}

	@Override
	public void deleteRemoteAddress(final String address) throws DaoException {
		transact(new DaoBlock(){public void execute(Session session) {
			AcceptableRemoteAddress addr = (AcceptableRemoteAddress)session.get(
					AcceptableRemoteAddress.class, address);
			if(addr != null){
				session.delete(addr);
			}
		}});
	}

	@Override
	public boolean contains(final String remoteAddress) throws DaoException {
		return transact(new DaoBlockR<Boolean>(){public Boolean execute(Session session) {
			return session
					.createCriteria(AcceptableRemoteAddress.class)
					.add(Property.forName("address").eq(remoteAddress))
					.list()
					.size() > 0;
		}});
	}
}
