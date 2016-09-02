/*
 * $Id: FederationLogic.java 405 2011-08-25 01:43:27Z t-nakaguchi $
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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.FederationNotFoundException;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.federation.FederationGraph;
import jp.go.nict.langrid.management.logic.federation.FederationReverseGraph;

/**
 * 
 * 
 * @author Takao Nakaguchi
 * @author $Author:nakaguchi $
 * @version $Revision:4384 $
 */
public class FederationLogic extends AbstractLogic{
	/**
	 * 
	 * 
	 */
	public FederationLogic() throws DaoException{
	}

	@DaoTransaction
	public void clear() throws DaoException{
		getFederationDao().clear();
	}

	@DaoTransaction
	public List<Federation> listAllFederations() throws DaoException{
		return getFederationDao().list();
	}

	@DaoTransaction
	public Collection<String> listAllReachableGridIdsFrom(String sourceGridId) throws DaoException{
		Set<String> ret = new LinkedHashSet<>();

		FederationGraph fg = buildGraph();
		ret.add(sourceGridId);
		List<Federation> feds = new ArrayList<>(fg.listFederationsFrom(sourceGridId));
		Collections.sort(feds, (l, r) -> (int)(r.getCreatedDateTime().getTimeInMillis() - l.getCreatedDateTime().getTimeInMillis()));
		Collection<String> ids = new ArrayList<>();
		for(Federation f : feds){
			if(f.isRequesting() || !f.isConnected()) continue;
			String gid = f.getTargetGridId();
			ret.add(gid);
			ids.add(gid);
		}
		for(String gid : ids){
			listAllReachableGridIdsFrom(gid, fg, ret);
		}
		ret.remove(sourceGridId);
		return ret;
	}

	private void listAllReachableGridIdsFrom(String sourceGridId, FederationGraph fg, Set<String> ret){
		List<Federation> feds = new ArrayList<>(fg.listFederationsFrom(sourceGridId));
		Collections.sort(feds, (l, r) -> (int)(r.getCreatedDateTime().getTimeInMillis() - l.getCreatedDateTime().getTimeInMillis()));
		Collection<String> ids = new ArrayList<>();
		for(Federation f : feds){
			if(f.isRequesting() || !f.isConnected()) continue;
			String gid = f.getTargetGridId();
			if(ret.contains(gid)) continue;
			if(!fg.isTransitive(gid)) continue;
			ret.add(gid);
			ids.add(gid);
		}
		for(String gid : ids){
			listAllReachableGridIdsFrom(gid, fg, ret);
		}
	}

	@DaoTransaction
	public Collection<String> listAllReachableGridIdsTo(String targetGridId) throws DaoException{
		Set<String> ret = new LinkedHashSet<>();

		FederationReverseGraph fg = buildReverseGraph();
		ret.add(targetGridId);
		List<Federation> feds = new ArrayList<>(fg.listFederationsTo(targetGridId));
		Collections.sort(feds, (l, r) -> (int)(r.getCreatedDateTime().getTimeInMillis() - l.getCreatedDateTime().getTimeInMillis()));
		Collection<String> ids = new ArrayList<>();
		for(Federation f : feds){
			if(f.isRequesting() || !f.isConnected()) continue;
			String gid = f.getSourceGridId();
			ret.add(gid);
			ids.add(gid);
		}
		if(fg.isTransitive(targetGridId)){
			for(String gid : ids){
				listAllReachableGridIdsTo(gid, fg, ret);
			}
		}
		ret.remove(targetGridId);
		return ret;
	}

	private void listAllReachableGridIdsTo(String targetGridId, FederationReverseGraph fg, Set<String> ret){
		List<Federation> feds = new ArrayList<>(fg.listFederationsTo(targetGridId));
		Collections.sort(feds, (l, r) -> (int)(r.getCreatedDateTime().getTimeInMillis() - l.getCreatedDateTime().getTimeInMillis()));
		Collection<String> ids = new ArrayList<>();
		for(Federation f : feds){
			if(f.isRequesting() || !f.isConnected()) continue;
			String gid = f.getSourceGridId();
			if(ret.contains(gid)) continue;
			ret.add(gid);
			ids.add(gid);
		}
		if(fg.isTransitive(targetGridId)){
			for(String gid : ids){
				listAllReachableGridIdsTo(gid, fg, ret);
			}
		}
	}
	@DaoTransaction
	public void addFederation(String sourceGridId, String targetGridId)
	throws DaoException{
		getFederationDao().addFederation(sourceGridId, targetGridId);
	}

	@DaoTransaction
	public void addFederation(String sourceGridId, String sourceGridName
		, String targetGridId, String targetGridName, boolean requesting
		   , String targetGridUserId, String targetGridAccessToken, String targetGridOrganization
		   , URL targetGridHomepage, boolean disconnected)
	throws DaoException{
		getFederationDao().addFederation(new Federation(
				sourceGridId, targetGridId
				, sourceGridName, targetGridName
				, targetGridUserId, targetGridAccessToken
				, requesting
				, targetGridOrganization, targetGridHomepage
				, disconnected));
	}

	@DaoTransaction
	public void deleteFederation(String sourceGridId, String targetGridId)
	throws FederationNotFoundException, DaoException{
		getFederationDao().deleteFederation(sourceGridId, targetGridId);
	}
	
	@DaoTransaction
	public void setRequesting(String sourceGridId, String targetGridId, boolean isRequesting)
	throws DaoException{
	   getFederationDao().setRequesting(sourceGridId, targetGridId, isRequesting);
	}

	@DaoTransaction
	public void setConnection(String sourceGridId, String targetGridId, boolean isConnected)
	throws DaoException{
		getFederationDao().setConnected(sourceGridId, targetGridId, isConnected);
	}

	@DaoTransaction
	public FederationGraph buildGraph()
	throws DaoException{
		return new FederationGraph(getGridDao().listAllGrids(), getFederationDao().list());
	}

	@DaoTransaction
	public FederationReverseGraph buildReverseGraph()
	throws DaoException{
		return new FederationReverseGraph(getGridDao().listAllGrids(), getFederationDao().list());
	}

	@DaoTransaction
	public boolean isReachable(String sourceGridId, String targetGridId)
	throws DaoException{
		return getNearestFederation(sourceGridId, targetGridId) != null;
	}

	@DaoTransaction
	public Federation getNearestFederation(String sourceGridId, String targetGridId)
	throws DaoException{
		FederationDao fdao = getFederationDao();
		try{
			Federation f = fdao.getFederation(sourceGridId, targetGridId);
			if(f.isConnected() && !f.isRequesting()) return f;
		} catch(FederationNotFoundException e){
		}
		return buildGraph().getNearestFederation(sourceGridId, targetGridId);
	}
}
