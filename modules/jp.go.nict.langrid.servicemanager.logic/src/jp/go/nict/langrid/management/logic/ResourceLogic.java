/*
 * $Id: ResourceLogic.java 405 2011-08-25 01:43:27Z t-nakaguchi $
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

import java.util.logging.Logger;

import jp.go.nict.langrid.commons.lang.block.BlockP;
import jp.go.nict.langrid.commons.lang.block.BlockPR;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.ResourceAlreadyExistsException;
import jp.go.nict.langrid.dao.ResourceDao;
import jp.go.nict.langrid.dao.ResourceNotFoundException;
import jp.go.nict.langrid.dao.ResourceSearchResult;
import jp.go.nict.langrid.dao.entity.Resource;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class ResourceLogic
extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public ResourceLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getResourceDao().clear();
	}

	@DaoTransaction
	public ResourceSearchResult searchResources(
			int startIndex , int maxCount
			, String gridId, String userId
			, MatchingCondition[] conditions, Order[] orders
			, Scope scope
			) throws DaoException
	{
	   if(scope.equals(Scope.ALL)){
	      return searchAllResources(
	         startIndex, maxCount, gridId
	         , conditions, orders
	      );
      } else if(scope.equals(Scope.MINE)){
         return searchMyResources(
               startIndex, maxCount, gridId, userId
               , conditions, orders);
      }
      return new ResourceSearchResult(new Resource[]{}, 0, true);
	}

	@DaoTransaction
	public void addResource(Resource resource, boolean allowAllAccess)
		throws ResourceAlreadyExistsException, DaoException, ResourceLogicException
	{
		resource.setVisible(true);
		getResourceDao().addResource(resource);
	}


	@DaoTransaction
	public void deleteResource(String serviceGridId, String resourceId)
	throws DaoException, ResourceLogicException{
		Resource resource = getResourceDao().getResource(serviceGridId, resourceId);
		if(resource.isActive()){
			throw new ResourceLogicException(
					serviceGridId, resourceId, "resource is active");
		}
		getResourceDao().deleteResource(serviceGridId, resourceId);
	}

	@DaoTransaction
	public <T> T transactRead(String serviceGridId, String resourceId, BlockPR<Resource, T> block)
	throws ResourceNotFoundException, DaoException{
		return block.execute(getResourceDao().getResource(serviceGridId, resourceId));
	}

	@DaoTransaction
	public void transactUpdate(String serviceGridId, String resourceId, BlockP<Resource> block)
	throws ResourceNotFoundException, DaoException{
		Resource r = getResourceDao().getResource(serviceGridId, resourceId);
		block.execute(r);
		r.touchUpdatedDateTime();
	}

	@DaoTransaction
	public boolean isResourceVisible(
			String serviceGridId, String resourceId
			)
	throws ResourceNotFoundException, DaoException {
		ResourceDao dao = getResourceDao();
		return dao.getResource(serviceGridId, resourceId).isVisible();
	}

	@DaoTransaction
	public void setResourceVisible(
			String serviceGridId, String resourceId, boolean visible)
	throws ResourceNotFoundException, DaoException {
		ResourceDao dao = getResourceDao();
		Resource r = dao.getResource(serviceGridId, resourceId);
		r.setVisible(visible);
		r.touchUpdatedDateTime();
	}
	
  @DaoTransaction
   public void activateResource(
         String gridId, String resourceId
         )
  throws ResourceNotFoundException, DaoException {
     ResourceDao dao = getResourceDao();
     Resource resource = dao.getResource(gridId, resourceId);
     if(resource.isActive()) return;
     resource.setActive(true);
     resource.touchUpdatedDateTime();
   }

   @DaoTransaction
   public void deactivateResource(
         String gridId, String resourceId
         )
   throws ResourceNotFoundException, DaoException{
      ResourceDao dao = getResourceDao();
      Resource resource = dao.getResource(gridId, resourceId);
      if( ! resource.isActive()) return;
      resource.setActive(false);
      resource.touchUpdatedDateTime();
   }

	private ResourceSearchResult searchAllResources(
      int startIndex, int maxCount, String serviceGridId
      , MatchingCondition[] conditions, Order[] orders
      )
   throws DaoException
   {
      return getResourceDao().searchResources(
               startIndex, maxCount, serviceGridId
               , conditions, orders
               );
   }
   
   private ResourceSearchResult searchMyResources(
         int startIndex, int maxCount
         , String serviceAndUserGridId, String userId
         , MatchingCondition[] conditions, Order[] orders
         )
      throws DaoException
   {
      conditions = ArrayUtil.append(
            conditions
            , new MatchingCondition("gridId", serviceAndUserGridId)
            , new MatchingCondition("ownerUserId", userId)
            );
      return getResourceDao().searchResources(
            startIndex, maxCount, serviceAndUserGridId, conditions, orders
            );
   }

	private static Logger logger = Logger.getLogger(
			ResourceLogic.class.getName());
	
}
