package jp.go.nict.langrid.p2pgridbasis.dao.langrid;

import java.lang.reflect.Type;

import org.apache.log4j.Logger;

import jp.go.nict.langrid.commons.lang.reflect.GenericsUtil;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.dao.entity.UpdateManagedEntity;
import jp.go.nict.langrid.dao.util.EntityUtil;
import jp.go.nict.langrid.management.logic.FederationLogic;
import jp.go.nict.langrid.management.logic.federation.FederationGraph;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;

public abstract class AbstractP2PGridBasisDao<T>
implements DataDao{
	@SuppressWarnings("unchecked")
	public AbstractP2PGridBasisDao(DaoContext daoContext){
		this.dc = daoContext;
		Type type = GenericsUtil.getActualTypeArgumentTypes(this.getClass(), AbstractP2PGridBasisDao.class)[0];
		if(!(type instanceof Class)) throw new RuntimeException(
				"The parameter for T of AbstractP2PGridBasisDao must not be parameterized type.");
		this.clazz = (Class<T>)type;
	}

	protected DaoContext getDaoContext() {
		return dc;
	}

	protected String getSelfGridId() throws ControllerException{
		return getController().getSelfGridId();
	}

	protected P2PGridController getController() throws ControllerException{
		if (controller == null) {
			controller = JXTAController.getInstance();
		}

		return controller;
	}
	
	public void setHandler(GenericHandler<T> handler) {
		this.handler = handler;
	}

	public void setEntityListener() {
		logger.debug("### " + clazz.getSimpleName() + " : setEntityListener ###");
		getDaoContext().addEntityListener(clazz, handler);
		getDaoContext().addTransactionListener(handler);
	}

	public void removeEntityListener() {
		logger.debug("### " + clazz.getSimpleName() + " : removeEntityListener ###");
		getDaoContext().removeTransactionListener(handler);
		getDaoContext().removeEntityListener(clazz, handler);
	}

	protected boolean handleDataDeletion(Data data, UpdateManagedEntity entity)
	throws DataDaoException{
		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
			try {
				removeEntityListener();
				try{
					dc.removeEntity(
							entity.getClass(), EntityUtil.getId(entity));
					return true;
				} finally{
					setEntityListener();
					getController().baseSummaryAdd(data);
				}
			} catch(Exception e) {
				throw new DataDaoException(e);
			}
		}
		return false;
	}

	protected boolean handleData(Data data, UpdateManagedEntity entity)
	throws DataDaoException{
		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
			try {
				removeEntityListener();
				try{
					return dc.removeEntity(
							entity.getClass(), EntityUtil.getId(entity));
				} finally{
					setEntityListener();
					getController().baseSummaryAdd(data);
				}
			} catch(Exception e) {
				throw new DataDaoException(e);
			}
		}

		try {
			removeEntityListener();
			try{
				dc.beginTransaction();
				try{
					UpdateManagedEntity existing = dc.loadEntity(
							entity.getClass(), EntityUtil.getId(entity));
					if(existing != null){
						if(entity.getUpdatedDateTime().after(existing.getUpdatedDateTime())){
							dc.mergeEntity(entity);
						}
					} else{
						dc.saveEntity(entity);
					}
				} finally{
					dc.commitTransaction();
				}
				return true;
			} finally{
				setEntityListener();
				getController().baseSummaryAdd(data);
			}
		} catch(Exception e) {
			throw new DataDaoException(e);
		}
	}

	protected boolean isReachableTo(String gridId)
	throws DataDaoException{
		try{
			String self = this.getController().getSelfGridId();
			FederationGraph g = getGraph();
			return g.isReachable(self, gridId);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		} catch (DaoException e) {
			throw new DataDaoException(e);
		}
	}

	protected boolean isReachableToOrFrom(String gridId)
	throws DataDaoException{
		try{
			String self = this.getController().getSelfGridId();
			FederationGraph g = getGraph();
			return g.isReachable(self, gridId)
					|| g.isReachable(gridId, self);
		} catch (ControllerException e) {
			throw new DataDaoException(e);
		} catch (DaoException e) {
			throw new DataDaoException(e);
		}
	}

	private static synchronized FederationGraph getGraph()
	throws DaoException{
		if(graph == null || (System.currentTimeMillis() - graphTime) > 1000 * 60 * 30){
			graph = new FederationLogic().buildGraph();
			graphTime = System.currentTimeMillis();
		}
		return graph;
	}

	private P2PGridController controller;
	private DaoContext dc;
	private Class<T> clazz;
	private GenericHandler<T> handler;
	private static FederationGraph graph;
	private static long graphTime;
	private static Logger logger = Logger.getLogger(AbstractP2PGridBasisDao.class.getName());
}
