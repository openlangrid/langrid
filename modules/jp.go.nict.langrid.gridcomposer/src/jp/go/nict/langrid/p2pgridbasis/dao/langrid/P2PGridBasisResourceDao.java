/*
 * $Id: P2PGridBasisResourceDao.java 401 2011-08-25 01:11:16Z t-nakaguchi $
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

import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ResourceAlreadyExistsException;
import jp.go.nict.langrid.dao.ResourceDao;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.ResourceNotFoundException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.ResourceSearchResult;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourcePK;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.dao.DataNotFoundException;
import jp.go.nict.langrid.p2pgridbasis.dao.UnmatchedDataTypeException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.ResourceData;
import jp.go.nict.langrid.p2pgridbasis.data.langrid.DataConvertException;

import org.apache.log4j.Logger;

/**
 * 
 * 
 * @author $Author: t-nakaguchi $
 * @version $Revision: 401 $
 */
public class P2PGridBasisResourceDao implements DataDao, ResourceDao {
	/**
	 * The constructor.
	 * @param dao
	 */
	public P2PGridBasisResourceDao(ResourceDao dao, DaoContext context) {
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
		logger.debug("### Resource : setEntityListener ###");
		daoContext.addEntityListener(Resource.class, handler);
		daoContext.addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### Resource : removeEntityListener ###");
		daoContext.removeTransactionListener(handler);
		daoContext.removeEntityListener(Resource.class, handler);
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
		logger.debug("[Resource] " + data.getGridId() + ":" + data.getId() + " updated");
		if(data.getClass().equals(ResourceData.class) == false) {
			throw new UnmatchedDataTypeException(ResourceData.class.toString(), data.getClass().toString());
		}

		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
 			boolean updated = false;
			try {
				logger.debug("Delete");
				ResourceData resourceData = (ResourceData)data;
				Resource resource = resourceData.getResource();
				removeEntityListener();
				dao.deleteResource(resource.getGridId(), resource.getResourceId());
				updated = true;
				setEntityListener();
				getController().baseSummaryAdd(data);
			} catch (DataConvertException e) {
				throw new DataDaoException(e);
			} catch (ResourceNotFoundException e) {
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

		Resource resource= null;
		try {
			ResourceData resourceData = (ResourceData)data;
			resource = resourceData.getResource();
			logger.debug("New or UpDate");
			removeEntityListener();
			if(dao.isResourceExist(resource.getGridId(), resource.getResourceId())){
				logger.debug("UpDate");
				daoContext.updateEntity(resource);
			}else{
				logger.debug("New");
				dao.addResource(resource);
			}
			setEntityListener();
			getController().baseSummaryAdd(data);
			return true;
		} catch (DaoException e) {
			throw new DataDaoException(e);
		} catch (DataConvertException e) {
			throw new DataDaoException(e);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		}
	}

	@Override
	public void addResource(Resource resource) throws DaoException,
			ResourceAlreadyExistsException {
		dao.addResource(resource);
	}

	@Override
	public void clear() throws DaoException {
		dao.clear();
	}

	@Override
	public void deleteResource(String resourceGridId, String resourceId)
			throws ResourceNotFoundException, DaoException {
		dao.deleteResource(resourceGridId, resourceId);
	}

	@Override
	public void deleteResourcesOfGrid(String gridId) throws DaoException {
		dao.deleteResourcesOfGrid(gridId);
	}

	@Override
	public void deleteResourcesOfUser(String userGridId, String userId)
			throws DaoException {
		dao.deleteResourcesOfUser(userGridId, userId);
	}

	@Override
	public Resource getResource(String resourceGridId, String resourceId)
			throws ResourceNotFoundException, DaoException {
		return dao.getResource(resourceGridId, resourceId);
	}

	@Override
	public boolean isResourceExist(String resourceGridId, String resourceId)
			throws DaoException {
		return dao.isResourceExist(resourceGridId, resourceId);
	}

	@Override
	public List<Resource> listAllResources(String resourceGridId)
			throws DaoException {
		return dao.listAllResources(resourceGridId);
	}

	@Override
	public List<Resource> listResourcesOfUser(String userGridId, String userId)
			throws DaoException {
		return dao.listResourcesOfUser(userGridId, userId);
	}

	@Override
	public ResourceSearchResult searchResources(int startIndex, int maxCount,
			String resourceGridId, MatchingCondition[] conditions,
			Order[] orders) throws DaoException {
		return dao.searchResources(startIndex, maxCount, resourceGridId, conditions, orders);
	}

	static private Logger logger = Logger.getLogger(P2PGridBasisResourceDao.class);

	private ResourceDao dao;
	private DaoContext daoContext;
	private P2PGridController controller;
	private GenericHandler<Resource> handler = new GenericHandler<Resource>(){
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
				getController().publish(new ResourceData(
						daoContext.loadEntity(Resource.class, id)
						));
				logger.debug("published[Resource(id=" + id + ")]");
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
				ResourcePK pk = (ResourcePK)id;
				getController().revoke(ResourceData.getDataID(null, pk));
				logger.info("revoked[Resource(id=" + pk + ")]");
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
}
