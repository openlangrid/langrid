package jp.go.nict.langrid.p2pgridbasis.dao.langrid;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.entity.UpdateManagedEntity;
import jp.go.nict.langrid.dao.util.EntityUtil;
import jp.go.nict.langrid.p2pgridbasis.dao.DataDaoException;
import jp.go.nict.langrid.p2pgridbasis.data.Data;

public abstract class AbstractP2PGridBasisUpdateManagedEntityDao<T extends UpdateManagedEntity> extends AbstractP2PGridBasisDao<T>{
	public AbstractP2PGridBasisUpdateManagedEntityDao(DaoContext daoContext) {
		super(daoContext);
	}

	protected boolean handleDataDeletion(Data data, T entity)
	throws DataDaoException{
		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
			try {
				removeEntityListener();
				try{
					getDaoContext().removeEntity(
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

	protected boolean beforeUpdateEntity(T entity){
		return true;
	}
	protected boolean beforeSaveEntity(T entity){
		return true;
	}
	protected boolean handleData(Data data, T entity)
	throws DataDaoException{
		if(data.getAttributes().getKeys().contains("IsDeleted") &&
				data.getAttributes().getValue("IsDeleted").equals("true")) {
			try {
				removeEntityListener();
				try{
					return getDaoContext().removeEntity(
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
				getDaoContext().beginTransaction();
				try{
					UpdateManagedEntity existing = getDaoContext().loadEntity(
							entity.getClass(), EntityUtil.getId(entity));
					if(existing != null){
						if(entity.getUpdatedDateTime().after(existing.getUpdatedDateTime())){
							if(beforeUpdateEntity(entity)) getDaoContext().mergeEntity(entity);
						}
					} else{
						if(beforeSaveEntity(entity)) getDaoContext().saveEntity(entity);
					}
				} finally{
					getDaoContext().commitTransaction();
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
}
