/*
 * $Id: P2PGridBasisDomainDao.java 1522 2015-03-11 02:20:42Z t-nakaguchi $
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
import jp.go.nict.langrid.dao.DomainAlreadyExistsException;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.DomainNotFoundException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.DomainPK;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DomainData;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1522 $
 */
public class P2PGridBasisDomainDao
extends AbstractP2PGridBasisUpdateManagedEntityDao<Domain>
implements DataDao, DomainDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisDomainDao(DomainDao dao, DaoContext context) {
		super(context);
		setHandler(handler);
		this.dao = dao;
	}

	@Override
	synchronized public boolean updateData(Data data) throws UnmatchedDataTypeException, DataDaoException {
		logger.debug("[Domain] : " + data.getId());
		if(data.getClass().equals(DomainData.class) == false) {
			throw new UnmatchedDataTypeException(DomainData.class.toString(), data.getClass().toString());
		}
		Domain entity = null;
		try {
			entity = ((DomainData)data).getDomain();
			if(entity.getOwnerUserGridId().equals(getSelfGridId())) return false;
			if(!isReachableTo(entity.getOwnerUserGridId())) return false;
			try {
				Domain domainInDb = dao.getDomain(entity.getDomainId());
				if(domainInDb.getOwnerUserGridId().equals(getSelfGridId())){
					return false;
				}
			} catch (DomainNotFoundException e) {
			}
		} catch(Exception e) {
			throw new DataDaoException(e);
		}
		return handleData(data, entity);
	}


	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public List<Domain> listAllDomains() throws DaoException {
		return dao.listAllDomains();
	}

	@Override
	public void addDomain(Domain domain) throws DomainAlreadyExistsException,
			DaoException {
		dao.addDomain(domain);
	}

	@Override
	public void deleteDomain(String domainId) throws DomainNotFoundException,
			DaoException {
		dao.deleteDomain(domainId);
	}

	@Override
	public Domain getDomain(String domainId) throws DomainNotFoundException,
			DaoException {
		return dao.getDomain(domainId);
	}

	@Override
	public boolean isDomainExist(String domainId) throws DaoException {
		return dao.isDomainExist(domainId);
	}

	@Override
	public List<Domain> listAllDomains(String gridId) throws DaoException {
		return dao.listAllDomains(gridId);
	}

	private DomainDao dao;
	private GenericHandler<Domain> handler = new GenericHandler<Domain>(){
		protected boolean onNotificationStart() {
			try{
				getDaoContext().beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
				return false;
			}
		}

		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			try{
				getController().publish(new DomainData(
						getDaoContext().loadEntity(Domain.class, id)
						));
				logger.info("published[Domain(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to publish instance.", e);
			} catch(DaoException e){
				logger.error("failed to access dao.", e);
			} catch(DataConvertException e){
				logger.error("failed to convert data.", e);
			}
		}

		protected void doRemove(Serializable id){
			try{
				DomainPK pk = new DomainPK(id.toString());
				getController().revoke(DomainData.getDataID(null, pk));
				logger.info("revoked[Domain(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to revoke instance.", e);
			} catch(DataNotFoundException e){
				logger.error("failed to find data.", e);
			}
		}

		protected void onNotificationEnd(){
			try{
				getDaoContext().commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};

	static private Logger logger = Logger.getLogger(P2PGridBasisDomainDao.class);
}
