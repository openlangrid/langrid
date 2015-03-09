package jp.go.nict.langrid.management.web.model.service.impl;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GridAlreadyExistsException;
import jp.go.nict.langrid.dao.GridNotFoundException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.SystemPropertyDao;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.management.logic.GridLogic;
import jp.go.nict.langrid.management.logic.Scope;
import jp.go.nict.langrid.management.logic.SystemPropertyLogic;
import jp.go.nict.langrid.management.web.model.GridModel;
import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.model.service.GridService;
import jp.go.nict.langrid.management.web.model.service.LangridList;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 1506 $
 */
public class GridServiceImpl implements GridService {
	public GridServiceImpl() throws ServiceManagerException {
	}

	@Override
	public void add(GridModel obj) throws ServiceManagerException {
		Grid grid = new Grid();
		try {
			new GridLogic().addGrid(setProperty(obj, grid));
		} catch(GridAlreadyExistsException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void delete(GridModel condition) throws ServiceManagerException {
		try {
			new GridLogic().deleteGrid(condition.getGridId());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void edit(final GridModel obj) throws ServiceManagerException {
		try {
			new GridLogic().transactUpdate(selfGridId, new BlockP<Grid>() {
				@Override
				public void execute(Grid arg0) {
					setProperty(obj, arg0);
				}
			});
		} catch(GridNotFoundException e) {
			throw new ServiceManagerException(e, this.getClass());
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public GridModel get(String id) throws ServiceManagerException {
		try {
			Grid g = new GridLogic().getGrid(id);
			return makeModel(g);
		} catch(GridNotFoundException e) {
			return null;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public LangridList<GridModel> getList(int index, int count,
		MatchingCondition[] conditions, Order[] orders, Scope scope)
	throws ServiceManagerException {
		try {
			LangridList<GridModel> list = new LangridList<GridModel>();
			for(Grid g : new GridLogic().listAllGrids()) {
				list.add(makeModel(g));
			}
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	/**
	 * @see jp.go.nict.langrid.management.new GridLogic().SystemPropertyLogic
	 */
	@Override
	public int getPasswordExpiredDay() throws ServiceManagerException {
		try {
			SystemPropertyLogic logic = new SystemPropertyLogic();
			String expiredDate = logic.getProperty(selfGridId,
				SystemPropertyDao.PASSWORD_EXPIRATION_DAYS);
			if(expiredDate == null) {
				throw new ServiceManagerException("Password expired date is not found.");
			}
			return Integer.valueOf(expiredDate);
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	public String getSelfGridId() throws ServiceManagerException {
		return selfGridId;
	}

	@Override
	public void setSelfGridId(String gridId) {
		this.selfGridId = gridId;
	}

	@Override
	public int getTotalCount(MatchingCondition[] conditions, Scope scope)
	throws ServiceManagerException {
		try {
			return new GridLogic().listAllGrids().size();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public boolean isAutoApproveEnabled() throws ServiceManagerException {
		try {
			return new GridLogic().getGrid(selfGridId).isAutoApproveEnabled();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public boolean isCommercialUse() throws ServiceManagerException {
		try {
			return new GridLogic().getGrid(selfGridId).isCommercialUseAllowed();
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void setAutoApproveEnabled(final boolean autoApproveEnabled)
	throws ServiceManagerException {
		try {
			new GridLogic().transactUpdate(selfGridId, new BlockP<Grid>() {
				@Override
				public void execute(Grid grid) {
					grid.setAutoApproveEnabled(autoApproveEnabled);
				}
			});
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
		this.autoApproveEnabled = autoApproveEnabled;
	}

	@Override
	public void setCommercialUse(final boolean isCommercialUse)
	throws ServiceManagerException {
		try {
			new GridLogic().transactUpdate(selfGridId, new BlockP<Grid>() {
				@Override
				public void execute(Grid grid) {
					grid.setCommercialUseAllowed(isCommercialUse);
				}
			});
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}

	@Override
	public void setPasswordExpiredDay(int day) throws ServiceManagerException {
		try {
			SystemPropertyLogic logic = new SystemPropertyLogic();
			logic.setProperty(selfGridId, SystemPropertyDao.PASSWORD_EXPIRATION_DAYS,
				String.valueOf(day));
		} catch(DaoException e) {
			throw new ServiceManagerException(e, this.getClass());
		}
	}
	
	@Override
	public LangridList<GridModel> getAll() throws ServiceManagerException {
		try {
			LangridList<GridModel> list = new LangridList<GridModel>();
			LangridList<GridModel> temp = new LangridList<GridModel>();
			for(Grid g : new GridLogic().listAllGrids()) {
				if(g.getGridId().equals(getSelfGridId())){
					list.add(makeModel(g));
					continue;
				}
				temp.add(makeModel(g));
			}
			list.addAll(temp);
			return list;
		} catch(DaoException e) {
			throw new ServiceManagerException(e);
		}
	}

	@Override
	public void setScopeParameter(String serviceGridId, String userGridId, String userId) {
//		this.serviceGridId = serviceGridId;
//		this.userGridId = userGridId;
		this.userId = userId;
	}
	
	@Override
	public boolean isExist(String dataId) throws ServiceManagerException {
		return get(dataId) != null;
	}

	private GridModel makeModel(Grid entity) {
		GridModel model = new GridModel();
		model.setAutoApproveEnabled(entity.isAutoApproveEnabled());
		model.setCommercialUseAllowed(entity.isCommercialUseAllowed());
		model.setCreatedDateTime(entity.getCreatedDateTime());
		model.setGridId(entity.getGridId());
		model.setGridName(entity.getGridName());
		model.setHosted(entity.isHosted());
		model.setOperatorUserId(entity.getOperatorUserId());
		model.setUpdatedDateTime(entity.getUpdatedDateTime());
		model.setUrl(entity.getUrl());
		return model;
	}

	private Grid setProperty(GridModel model, Grid entity) {
		entity.setAutoApproveEnabled(model.isAutoApproveEnabled());
		entity.setCommercialUseAllowed(model.isCommercialUseAllowed());
		entity.setGridId(model.getGridId());
		entity.setGridName(model.getGridName());
		entity.setHosted(model.isHosted());
		entity.setOperatorUserId(model.getOperatorUserId());
		entity.setUrl(model.getUrl());
		return entity;
	}

	private String selfGridId;
//	private String serviceGridId;
//	private String userGridId;
	private String userId;
	private boolean autoApproveEnabled;
	private static final long serialVersionUID = 1L;
}
