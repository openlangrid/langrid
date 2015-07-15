/*
 * $Id: P2PGridBasisFederationDao.java 1043 2014-01-09 13:27:07Z t-nakaguchi $
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.FederationAlreadyExistsException;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.FederationNotFoundException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.dao.entity.FederationPK;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.FederationData;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1043 $
 */
public class P2PGridBasisFederationDao implements DataDao, FederationDao {
	/**
	 * The constructor.
	 * @param dao
	 */
	public P2PGridBasisFederationDao(FederationDao dao, DaoContext context) {
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
		logger.debug("### Federation : setEntityListener ###");
		daoContext.addEntityListener(Federation.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### Federation : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(Federation.class, handler);
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
	synchronized public boolean updateDataTarget(Data data) throws DataDaoException, UnmatchedDataTypeException {
		// ignore federation sharing message
		return true;
/*
		logger.debug("[Federation] : " + data.getId());
		if(data.getClass().equals(FederationData.class) == false) {
			throw new UnmatchedDataTypeException(FederationData.class.toString(), data.getClass().toString());
		}
		
		String selfGridId = null;
		Federation federation = null;
		try{
			federation = ((FederationData)data).getFederation();
			selfGridId = getController().getSerlfGridId();
		} catch (ParseException e) {
			throw new DataDaoException(e);
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
		if(federation.getSourceGridId().equals(selfGridId)
				|| federation.getTargetGridId().equals(selfGridId)){
			return false;
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
 			boolean updated = false;
			try {
				logger.debug("Delete");
				removeEntityListener();
				dao.deleteFederation(federation.getSourceGridId()
									, federation.getTargetGridId());
				updated = true;
				setEntityListener();
				getController().createFederation();
				getController().baseSummaryAdd(data);
			} catch (FederationNotFoundException e) {
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
			if(dao.isFederationExist(federation.getSourceGridId(), federation.getTargetGridId())){
				daoContext.updateEntity(federation);
			} else{
				dao.addFederation(federation);
			}
			setEntityListener();
			getController().createFederation();
			getController().baseSummaryAdd(data);
			return true;
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
*/	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public boolean isFederationExist(String sourceGridId, String targetGridId)
			throws DaoException {
		return dao.isFederationExist(sourceGridId, targetGridId);
	}

	@Override
	public Federation getFederation(String sourceGridId, String targetGridId)
	throws FederationNotFoundException, DaoException {
		return dao.getFederation(sourceGridId, targetGridId);
	}

	@Override
	public void addFederation(Federation federation) throws FederationAlreadyExistsException,
			DaoException {
		dao.addFederation(federation);
	}

	@Override
	public void addFederation(String sourceGridId, String targetGridId)
	throws FederationAlreadyExistsException, DaoException {
		dao.addFederation(sourceGridId, targetGridId);
	}

	@Override
	public void deleteFederation(String sourceGridId, String targetGridId)
			throws FederationNotFoundException, DaoException {
		dao.deleteFederation(sourceGridId, targetGridId);
	}

	@Override
	public void deleteFederationsOf(String gridId) throws DaoException {
		dao.deleteFederationsOf(gridId);
	}

	@Override
	public List<String> listSourceGridIds(String targetGridId) throws DaoException {
		return dao.listSourceGridIds(targetGridId);
	}

	@Override
	public List<String> listTargetGridIds(String sourceGridId) throws DaoException {
		return dao.listTargetGridIds(sourceGridId);
	}

	@Override
	public List<Federation> listFederationsFrom(String sourceGridId)
			throws DaoException {
		return dao.listFederationsFrom(sourceGridId);
	}

	@Override
	public List<Federation> listFederationsToward(String targetGridId)
			throws DaoException {
		return dao.listFederationsToward(targetGridId);
	}

	@Override
	public List<Federation> list() throws DaoException {
		return dao.list();
	}

	@Override
	public void setRequesting(String sourceGridId, String targetGridId,
			boolean isRequesting) throws DaoException {
		dao.setRequesting(sourceGridId, targetGridId, isRequesting);
	}

	static private Logger logger = Logger.getLogger(P2PGridBasisFederationDao.class);

	private FederationDao dao;
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<Federation> handler = new GenericHandler<Federation>(){
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
				P2PGridController c = getController();
				c.createFederation();
				String selfGridId = c.getSelfGridId();
				Federation f = daoContext.loadEntity(Federation.class, id);
				Grid tg = daoContext.loadEntity(Grid.class, f.getTargetGridId());
				c.publish(new FederationData(f));
				if(f.getSourceGridId().equals(selfGridId)
						&& !f.isRequesting() && !tg.isHosted()
						&& (modifiedProperties.isEmpty() || modifiedProperties.contains("requesting"))){
					String tgUrl = tg.getUrl();
					try{
						URL gurl = new URL(tgUrl);
						String url = 
								gurl.getProtocol() + "://"
								+ f.getTargetGridUserId() + ":" + f.getTargetGridAccessToken()
								+ "@" + gurl.getHost()
								+ ((gurl.getPort() != -1) ? ":" + gurl.getPort() : "")
								+ gurl.getFile()
								+ (gurl.getFile().endsWith("/") ? "" : "/")
								+ "RdvPeer#"
								+ selfGridId
								;
						c.addSeedUri(new URI(url));
					} catch(MalformedURLException e){
						logger.warn("failed to add seed URI for: " + tgUrl, e);
					} catch(URISyntaxException e){
						logger.warn("failed to add seed URI for: " + tgUrl, e);
					}
				}
				logger.info("published[Federation(id=" + id + ")]");
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
				FederationPK pk = (FederationPK)id;
				getController().createFederation();
				getController().revoke(FederationData.getDataID(null, pk));
				logger.info("revoked[Federation(id=" + pk + ")]");
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

	@Override
	public void setConnected(String sourceGridId, String targetGridId,	boolean isRequesting)
	throws DaoException {
		dao.setConnected(sourceGridId, targetGridId, isRequesting);
	}
}
