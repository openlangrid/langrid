package jp.go.nict.langrid.p2pgridbasis.dao.langrid;

import org.apache.log4j.Logger;

import jp.go.nict.langrid.commons.lang.reflect.GenericsUtil;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GenericHandler;
import jp.go.nict.langrid.management.logic.FederationLogic;
import jp.go.nict.langrid.management.logic.federation.FederationGraph;
import jp.go.nict.langrid.p2pgridbasis.controller.ControllerException;
import jp.go.nict.langrid.p2pgridbasis.controller.P2PGridController;
import jp.go.nict.langrid.p2pgridbasis.controller.jxta.JXTAController;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDao;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;

public abstract class AbstractP2PGridBasisDao<T>
implements DataDao{
	@SuppressWarnings("unchecked")
	public AbstractP2PGridBasisDao(DaoContext daoContext){
		this.dc = daoContext;
		Class<?>[] classes = GenericsUtil.getTypeArgumentClasses(this.getClass(), AbstractP2PGridBasisDao.class);
		if(classes.length == 0) throw new RuntimeException(
				"Failed to determine acctual class of type argument T.");
		this.clazz = (Class<T>)classes[0];
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

	private static FederationGraph graph;
	private static long graphTime;

	private P2PGridController controller;
	private DaoContext dc;
	private Class<T> clazz;
	private GenericHandler<T> handler;
	private static Logger logger = Logger.getLogger(AbstractP2PGridBasisDao.class.getName());
}
