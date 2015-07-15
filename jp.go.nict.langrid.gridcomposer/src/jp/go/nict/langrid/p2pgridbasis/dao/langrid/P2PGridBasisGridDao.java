/*
 * $Id: P2PGridBasisGridDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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
import jp.go.nict.langrid.dao.EntityAlreadyExistsException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.GridNotFoundException;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.PeerSummaryAdvertisement;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.GridData;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisGridDao implements DataDao, GridDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisGridDao(GridDao dao, DaoContext context) {
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
		logger.debug("### Grid : setEntityListener ###");
		daoContext.addEntityListener(Grid.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### Grid : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(Grid.class, handler);
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
		logger.debug("[Grid] : " + data.getId());
		if(data.getClass().equals(GridData.class) == false) {
			throw new UnmatchedDataTypeException(GridData.class.toString(), data.getClass().toString());
		}

		Grid grid = null;
		try{
			grid = ((GridData)data).getGrid();
			if(getController().getSelfGridId().equals(grid.getGridId())) return false;
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
 			boolean updated = false;
			try {
				logger.info("Delete");
				removeEntityListener();
				dao.deleteGrid(grid.getGridId());
				updated = true;
				setEntityListener();
				getController().baseSummaryAdd(data);
			} catch (GridNotFoundException e) {
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
			daoContext.beginTransaction();
			removeEntityListener();
			DaoException exp = null;
			try{
				if(!getController().getSelfGridId().equals(grid.getGridId())){
					grid.setHosted(false);
					daoContext.mergeEntity(grid);
				}
			} catch(DaoException ex){
				exp = ex;
			} finally{
				setEntityListener();
			}
			if(exp != null){
				daoContext.commitTransaction();
			} else{
				daoContext.rollbackTransaction();
			}
			getController().baseSummaryAdd(data);
			return true;
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
	}

	@Override
	public void addGrid(Grid grid) throws EntityAlreadyExistsException,
			DaoException {
		dao.addGrid(grid);
	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public void deleteGrid(String gridId) throws GridNotFoundException,
			DaoException {
		dao.deleteGrid(gridId);
	}

	@Override
	public Grid getGrid(String gridId) throws GridNotFoundException,
			DaoException {
		return dao.getGrid(gridId);
	}

	@Override
	public boolean isGridExist(String gridId) throws DaoException {
		return dao.isGridExist(gridId);
	}

	@Override
	public List<Grid> listAllGrids() throws DaoException {
		return dao.listAllGrids();
	}

	private GridDao dao;
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<Grid> handler = new GenericHandler<Grid>(){
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
				getController().publish(new GridData(
						daoContext.loadEntity(Grid.class, id)
						));

				if(modifiedProperties.contains("hosted")){
					Grid grid = (Grid)id;
					if(grid.isHosted()){
						if(getController().hostSummaryCreate(grid.getGridId())){
							getController().summaryPublish(PeerSummaryAdvertisement._hostedSummary);
							getController().summaryPublish(PeerSummaryAdvertisement._stateSummary);
							getController().summaryPublish(PeerSummaryAdvertisement._logSummary);
						}
					}
				}

				logger.info("published[Node(id=" + id + ")]");
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
				getController().revoke(GridData.getDataID((String)id));
				logger.info("revoked[Node(id=" + id + ")]");
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

	static private Logger logger = Logger.getLogger(P2PGridBasisGridDao.class);
}
