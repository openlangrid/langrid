package jp.go.nict.langrid.dao;

import jp.go.nict.langrid.commons.util.function.Functions.RunnableWithException;

public abstract class AbstractDaoContext implements DaoContext{
	@Override
	public void transact(RunnableWithException<DaoException> c)
	throws DaoException{
		beginTransaction();
		try{
			c.run();
		} catch(DaoException e){
			rollbackTransaction();
			throw e;
		} catch(RuntimeException e){
			rollbackTransaction();
			throw e;
		}
		commitTransaction();
	}
}
