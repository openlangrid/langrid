/*
 * $Id: P2PGridBasisProtocolDao.java 1043 2014-01-09 13:27:07Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.ProtocolAlreadyExistsException;
import jp.go.nict.langrid.dao.ProtocolDao;
import jp.go.nict.langrid.dao.ProtocolNotFoundException;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ProtocolData;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1043 $
 */
public class P2PGridBasisProtocolDao implements DataDao, ProtocolDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisProtocolDao(ProtocolDao dao, DaoContext context) {
		this.dao = dao;
		this.daoContext = context;
	}

	private P2PGridController getController() throws ControllerException{
		if (controller == null) {
			controller = JXTAController.getInstance();
		}

		return controller;
	}

	public void setEntityListener() {
		logger.debug("### Protocol : setEntityListener ###");
		daoContext.addEntityListener(Protocol.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### Protocol : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(Protocol.class, handler);
	}

	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.dao#updateDataSource(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public boolean updateDataSource(Data data) throws DataDaoException, UnmatchedDataTypeException {
		return updateDataTarget(data);
	}
	/*
	 * (non-Javadoc)
	 * @see jp.go.nict.langrid.p2pgridbasis.dao#updateData(jp.go.nict.langrid.p2pgridbasis.data.Data)
	 */
	synchronized public boolean updateDataTarget(Data data) throws UnmatchedDataTypeException, DataDaoException {
		logger.debug("[Protocol] : " + data.getId());
		if(data.getClass().equals(ProtocolData.class) == false) {
			throw new UnmatchedDataTypeException(ProtocolData.class.toString(), data.getClass().toString());
		}

		ProtocolData protocolData = (ProtocolData) data;
		Protocol protocol = null;
		try{
			protocol = protocolData.getProtocol();
			if(protocol.getOwnerUserGridId().equals(getController().getSelfGridId())){
				return false;
			}
		} catch(ControllerException e){
			return false;
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
 			boolean updated = false;
			try {
				logger.info("Delete");
				removeEntityListener();
				dao.deleteProtocol(protocol.getProtocolId());
				updated = true;
				setEntityListener();
				getController().baseSummaryAdd(data);
			} catch (ProtocolNotFoundException e) {
				// 
				// 
				try {
					getController().baseSummaryAdd(data);
				} catch (ControllerException e1) {
					e1.printStackTrace();
				}
			} catch (DaoException e) {
				throw new DataDaoException(e);
			} catch (ControllerException e) {
				throw new DataDaoException(e);
			}
			return updated;
		}

		try {
			logger.debug("New or UpDate");
			removeEntityListener();
			daoContext.beginTransaction();
			daoContext.mergeEntity(protocol);
			daoContext.commitTransaction();
			setEntityListener();
			getController().baseSummaryAdd(data);
			return true;
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
	}


	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public List<Protocol> listAllProtocols() throws DaoException {
		return dao.listAllProtocols();
	}

	@Override
	public void addProtocol(Protocol protocol) throws ProtocolAlreadyExistsException,
			DaoException {
		dao.addProtocol(protocol);
	}

	@Override
	public void deleteProtocol(String protocolId) throws ProtocolNotFoundException,
			DaoException {
		dao.deleteProtocol(protocolId);
	}

	@Override
	public Protocol getProtocol(String protocolId) throws ProtocolNotFoundException,
			DaoException {
		return dao.getProtocol(protocolId);
	}

	@Override
	public boolean isProtocolExist(String protocolId) throws DaoException {
		return dao.isProtocolExist(protocolId);
	}
	
	@Override
	public List<Protocol> listAllProtocols(String gridId) throws DaoException {
		return dao.listAllProtocols(gridId);
	}

	private ProtocolDao dao;
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<Protocol> handler = new GenericHandler<Protocol>(){
		protected boolean onNotificationStart() {
			try{
				daoContext.beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
				return false;
			}
		}

		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			try{
				getController().publish(new ProtocolData(
						daoContext.loadEntity(Protocol.class, id)
						));
				logger.info("published[Protocol(id=" + id + ")]");
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
				getController().revoke(ProtocolData.getDataID(null, id.toString()));
				logger.info("revoked[Protocol(id=" + id + ")]");
			} catch(ControllerException e){
				logger.error("failed to revoke instance.", e);
			} catch(DataNotFoundException e){
				logger.error("failed to find data.", e);
			}
		}

		protected void onNotificationEnd(){
			try{
				daoContext.commitTransaction();
			} catch (DaoException e) {
				logger.error("failed to access dao.", e);
			}
		}
	};

	static private Logger logger = Logger.getLogger(P2PGridBasisProtocolDao.class);
}
