package jp.go.nict.langrid.p2pgridbasis.dao.langrid;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.management.logic.FederationLogic;

public class AbstractP2PGridBasisDao {
	protected boolean isReachableForwardOrBackward(String sourceGridId, String targetGridId)
	throws DaoException{
		FederationLogic fl = new FederationLogic();
		return fl.isReachable(sourceGridId, targetGridId)
				|| fl.isReachable(targetGridId, sourceGridId);
	}

	protected boolean isReachable(String sourceGridId, String targetGridId)
	throws DaoException{
		FederationLogic fl = new FederationLogic();
		return fl.isReachable(sourceGridId, targetGridId);
	}
}
