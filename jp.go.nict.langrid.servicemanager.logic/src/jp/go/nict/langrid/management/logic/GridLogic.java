/*
 * $Id: GridLogic.java 405 2011-08-25 01:43:27Z t-nakaguchi $
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
package jp.go.nict.langrid.management.logic;

import java.util.List;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.commons.lang.block.BlockPR;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.GridAlreadyExistsException;
import jp.go.nict.langrid.dao.GridNotFoundException;
import jp.go.nict.langrid.dao.entity.Grid;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class GridLogic extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public GridLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getAccessLogDao().clear();
		getAccessStateDao().clear();
		getAccessRightDao().clear();
		getAccessLimitDao().clear();
		getServiceDao().clear();
		getResourceDao().clear();
		getNodeDao().clear();
		getTemporaryUserDao().clear();
		getUserDao().clear();
		getFederationDao().clear();
		getOverUseLimitDao().clear();
		getNewsDao().clear();
		getGridDao().clear();
		getSystemPropertyDao().clear();
	}

	@DaoTransaction
	public List<Grid> listAllGrids() throws DaoException{
		return getGridDao().listAllGrids();
	}

	@DaoTransaction
	public Grid getGrid(String gridId) throws GridNotFoundException, DaoException{
		return getGridDao().getGrid(gridId);
	}

	@DaoTransaction
	public void addGrid(Grid grid) throws GridAlreadyExistsException, DaoException{
		getGridDao().addGrid(grid);
	}

	@DaoTransaction
	public void deleteGrid(String gridId)
	throws DaoException{
		getAccessLogDao().deleteAccessLogsOfGrid(gridId);
		getAccessStateDao().deleteAccessStatOfGrid(gridId);
		getAccessRightDao().deleteAccessRightsOfGrid(gridId);
		getAccessLimitDao().deleteAccessLimitsOfGrid(gridId);
		getServiceDao().deleteServicesOfGrid(gridId);
		getResourceDao().deleteResourcesOfGrid(gridId);
		getNodeDao().deleteNodesOfGrid(gridId);
		getTemporaryUserDao().deleteUsersOfGrid(gridId);
		getUserDao().deleteUsersOfGrid(gridId);
//		getFederationDao().deleteFederationsOf(gridId);
		getOverUseLimitDao().deleteOverUseLimitsOfGrid(gridId);
		getNewsDao().deleteAllNews(gridId);
		getGridDao().deleteGrid(gridId);
		getSystemPropertyDao().deletePropertiesOfGrid(gridId);
	}

	@DaoTransaction
	public <T> T transactRead(String gridId, BlockPR<Grid, T> gridBlock)
	throws GridNotFoundException, DaoException{
		return gridBlock.execute(getGridDao().getGrid(gridId));
	}

	@DaoTransaction
	public void transactUpdate(String gridId, BlockP<Grid> gridBlock)
	throws GridNotFoundException, DaoException{
		Grid g = getGridDao().getGrid(gridId);
		gridBlock.execute(g);
		g.touchUpdatedDateTime();
	}
}
