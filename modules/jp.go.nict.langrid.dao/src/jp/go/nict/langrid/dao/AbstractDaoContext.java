package jp.go.nict.langrid.dao;

import jp.go.nict.langrid.commons.util.function.Functions.ConsumerWithException;

public abstract class AbstractDaoContext implements DaoContext{
	@Override
	public void transact(ConsumerWithException<DaoContext, DaoException> c)
	throws DaoException{
		beginTransaction();
		try{
			c.accept(this);
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
