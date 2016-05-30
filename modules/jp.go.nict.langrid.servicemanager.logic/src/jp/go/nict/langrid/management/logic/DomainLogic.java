/*
 * $Id: DomainLogic.java 1521 2015-03-10 10:29:09Z t-nakaguchi $
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

import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.entity.Domain;

/**
 * 
 * 
 * @author Masato Mori
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1521 $
 */
public class DomainLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public DomainLogic() throws DaoException {
	}

	@DaoTransaction
	public void clear() throws DaoException {
		getDomainDao().clear();
	}
	
	@DaoTransaction
	public List<Domain> listAllDomains() throws DaoException {
		List<Domain> list = new ArrayList<Domain>();
		for(Domain d : getDomainDao().listAllDomains()){
			list.add(d);
		}
		return list;
	}

	@DaoTransaction
	public List<Domain> listAllDomains(String gridId) throws DaoException {
		List<Domain> list = new ArrayList<Domain>();
		for(Domain d : getDomainDao().listAllDomains(gridId)){
			list.add(d);
		}
		return list;
	}

	@DaoTransaction
	public List<Domain> listDomain(String gridId) throws DaoException {
		List<Domain> list = new ArrayList<Domain>();
		for(Domain d : getDomainDao().listAllDomains(gridId)){
			list.add(d);
		}
		return list;
	}

	@DaoTransaction
	public void addDomain(Domain domain) throws DaoException {
		getDomainDao().addDomain(domain);
	}

	@DaoTransaction
	public void deleteDomain(String domainId) throws DaoException {
		getDomainDao().deleteDomain(domainId);
	}

	@DaoTransaction
	public Domain getDomain(String domainId) throws DaoException {
		return getDomainDao().getDomain(domainId);
	}

	@DaoTransaction
	public void transactUpdate(String domainId, BlockP<Domain> block) throws DaoException{
		Domain d = getDomainDao().getDomain(domainId);
		block.execute(d);
		d.touchUpdatedDateTime();
	}
}
