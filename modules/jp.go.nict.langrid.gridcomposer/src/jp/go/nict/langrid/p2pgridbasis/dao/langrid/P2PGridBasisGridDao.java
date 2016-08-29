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
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.EntityAlreadyExistsException;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.GridNotFoundException;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.adv.PeerSummaryAdvertisement;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.GridData;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisGridDao
extends AbstractP2PGridBasisDao<Grid>
implements DataDao, GridDao {
	/**
	 * 
	 * 
	 */
	public P2PGridBasisGridDao(GridDao dao, FederationDao fdao, DaoContext context) {
		super(context);
		setHandler(handler);
		this.dao = dao;
		this.fdao = fdao;
	}

	@Override
	synchronized public boolean updateData(Data data) throws UnmatchedDataTypeException, DataDaoException {
		if(data.getClass().equals(GridData.class) == false) {
			throw new UnmatchedDataTypeException(GridData.class.toString(), data.getClass().toString());
		}

		Grid grid = null;
		try{
			grid = ((GridData)data).getGrid();
			if(getSelfGridId().equals(grid.getGridId())) return false;
		} catch(Exception e) {
			throw new DataDaoException(e);
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
			try {
				logger.info("Delete");
				removeEntityListener();
				try{
					dao.deleteGrid(grid.getGridId());
					return true;
				} finally{
					setEntityListener();
					getController().baseSummaryAdd(data);
				}
			} catch(Exception e) {
				throw new DataDaoException(e);
			}
		}

		removeEntityListener();
		try {
			DaoException exp = null;
			getDaoContext().beginTransaction();
			try{
				if(dao.isGridExist(grid.getGridId())){
					grid.setHosted(false);
					getDaoContext().mergeEntity(grid);
				} else if(
						fdao.listFederationsFrom(grid.getGridId()).size() > 0 ||
						fdao.listFederationsToward(grid.getGridId()).size() > 0
						){
					getDaoContext().saveEntity(grid);
				}
			} catch(DaoException ex){
				exp = ex;
			} finally{
				if(exp == null){
					getDaoContext().commitTransaction();
				} else{
					getDaoContext().rollbackTransaction();
				}
			}
			getController().baseSummaryAdd(data);
			return true;
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		} finally{
			setEntityListener();
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
	private FederationDao fdao;
	private GenericHandler<Grid> handler = new GenericHandler<Grid>(){
		protected boolean onNotificationStart() {
			try{
				getDaoContext().beginTransaction();
				return true;
			} catch (DaoException e) {
				logger.log(Level.SEVERE, "failed to access dao.", e);
				return false;
			}
		}

		protected void doUpdate(Serializable id, Set<String> modifiedProperties){
			try{
				getController().publish(new GridData(
						getDaoContext().loadEntity(Grid.class, id)
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
			} catch(ControllerException e){
				logger.log(Level.SEVERE, "failed to publish instance.", e);
			} catch(DaoException e){
				logger.log(Level.SEVERE, "failed to access dao.", e);
			} catch(DataConvertException e){
				logger.log(Level.SEVERE, "failed to convert data.", e);
			}
		}

		protected void doRemove(Serializable id){
			try{
				getController().revoke(GridData.getDataID((String)id));
				logger.info("revoked[Node(id=" + id + ")]");
			} catch(ControllerException e){
				logger.log(Level.SEVERE, "failed to revoke instance.", e);
			} catch(DataNotFoundException e){
				logger.log(Level.SEVERE, "failed to find data.", e);
			}
		}

		protected void onNotificationEnd(){
			try{
				getDaoContext().commitTransaction();
			} catch (DaoException e) {
				logger.log(Level.SEVERE, "failed to access dao.", e);
			}
		}
	};

	static private Logger logger = Logger.getLogger(P2PGridBasisGridDao.class.getName());
}
