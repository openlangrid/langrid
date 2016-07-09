/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) Language Grid Project.
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

import jp.go.nict.langrid.commons.util.function.Functions;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DomainAlreadyExistsException;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.DomainNotFoundException;
import jp.go.nict.langrid.dao.entity.Domain;

/**
 * 
 * @author Takao Nakaguchi
 */
public class JsonicDomainDao
extends AbstractJsonicDao
implements DomainDao {
	public JsonicDomainDao(JsonicDaoContext context){
		super(context);
	}

	@Override
	public void clear() throws DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Domain> listAllDomains() throws DaoException {
		return listAll(getContext().getDomainsBaseDir(), Domain.class);
	}

	@Override
	public List<Domain> listAllDomains(final String gridId) throws DaoException {
		return listAll(getContext().getDomainsBaseDir(), Domain.class, new Predicate<Domain>() {
			public boolean test(Domain value) {
				return value.getOwnerUserGridId().equals(gridId);
			};
		}, Functions.<Domain>nullComsumer());
	}

	@Override
	public void addDomain(Domain domain)
	throws DomainAlreadyExistsException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteDomain(String domainId)
	throws DomainNotFoundException, DaoException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Domain getDomain(String domainId)
	throws DomainNotFoundException, DaoException {
		try{
			return JsonicUtil.decode(getFile(domainId), Domain.class);
		} catch(FileNotFoundException e){
			throw new DomainNotFoundException(domainId);
		} catch(IOException e){
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isDomainExist(String domainId) throws DaoException {
		throw new UnsupportedOperationException();
	}

	private File getFile(String domainId){
		return new File(getContext().getDomainBaseDir(domainId), domainId + ".json");
	}
}
