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
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.util.Quartet;
import jp.go.nict.langrid.commons.util.function.Functions;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.FederationNotFoundException;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.federation.FederationGraph;
import jp.go.nict.langrid.management.logic.federation.GenericBackwardFederationGraph;
import jp.go.nict.langrid.management.logic.federation.GenericForwardFederationGraph;
import jp.go.nict.langrid.management.logic.federation.graph.DijkstraSearch;

/**
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

	public static String newToken(){
		return StringUtil.randomString(25);
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
		FederationGraph fg = forwardGraph.get();
		return fg.listAllReachableGridIds(sourceGridId);
	}

	@DaoTransaction
	public Collection<String> listAllReachableGridIdsTo(String targetGridId) throws DaoException{
		FederationGraph fg = backwardGraph.get();
		return fg.listAllReachableGridIds(targetGridId);
	}

	@DaoTransaction
	public void addFederation(String sourceGridId, String targetGridId)
	throws DaoException{
		getFederationDao().addFederation(sourceGridId, targetGridId);
	}

	@DaoTransaction
	public void addFederation(
			String sourceGridId, String sourceGridName, String sourceGridUserId, String sourceGridOrganization,
			String targetGridId, String targetGridName, String targetGridUserId, String targetGridOrganization,
			URL targetGridHomepage, String targetGridAccessToken,
			boolean requesting, boolean disconnected,
			boolean targetTransitive, boolean symmetric, boolean sourceTransitive)
	throws DaoException{
		getFederationDao().addFederation(new Federation(
				sourceGridId, sourceGridName, sourceGridUserId, sourceGridOrganization,
				targetGridId, targetGridName, targetGridUserId, targetGridOrganization, targetGridHomepage, 
				targetGridAccessToken,
				requesting, disconnected, targetTransitive, symmetric, sourceTransitive, false));
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

	FederationGraph buildGraph()
	throws DaoException{
		return new GenericForwardFederationGraph(getFederationDao().listFromOldest(),
				new DijkstraSearch<>(costFunc));
	}

	FederationGraph buildReverseGraph()
	throws DaoException{
		return new GenericBackwardFederationGraph(getFederationDao().listFromOldest(),
				new DijkstraSearch<>(costFunc));
	}

	private Function<Quartet<String, Integer, String, Federation>, Integer> costFunc = 
			q -> (int)(q.getSecond() + q.getFourth().getAveOverhead() + 1);

	@DaoTransaction
	public boolean isReachable(String sourceGridId, String targetGridId)
	throws DaoException{
		Federation f = getReachableFederation(sourceGridId, targetGridId);
		if(f != null) return true;
		return forwardGraph.get().isReachable(sourceGridId, targetGridId);
	}

	@DaoTransaction
	public Federation getReachableFederation(String sourceGridId, String targetGridId)
	throws DaoException{
		FederationDao fdao = getFederationDao();
		try{
			Federation f = fdao.getFederation(sourceGridId, targetGridId);
			if(f.isConnected() && !f.isRequesting()) return f;
		} catch(FederationNotFoundException e){
		}
		try{
			Federation f = fdao.getFederation(targetGridId, sourceGridId);
			if(f.isConnected() && !f.isRequesting() && f.isSymmetric()) return f;
		} catch(FederationNotFoundException e){
		}
		return null;
	}

	@DaoTransaction
	public Federation getReachableTransitiveFederation(String sourceGridId, String targetGridId)
	throws DaoException{
		FederationDao fdao = getFederationDao();
		try{
			Federation f = fdao.getFederation(sourceGridId, targetGridId);
			if(f.isConnected() && !f.isRequesting() && f.isForwardTransitive()) return f;
		} catch(FederationNotFoundException e){
		}
		try{
			Federation f = fdao.getFederation(targetGridId, sourceGridId);
			if(f.isConnected() && !f.isRequesting() && f.isSymmetric() && f.isBackwardTransitive()) return f;
		} catch(FederationNotFoundException e){
		}
		return null;
	}

	@DaoTransaction
	public Collection<Federation> getShortestPath(String sourceGridId, String targetGridId)
	throws DaoException{
		return getShortestPath(sourceGridId, targetGridId, Collections.emptySet());
	}

	@DaoTransaction
	public List<Federation> getShortestPath(String sourceGridId, String targetGridId, Set<String> visited)
	throws DaoException{
		List<Federation> ret = new ArrayList<>();
		{	Federation f = getReachableFederation(sourceGridId, targetGridId);
			if(f != null){
				ret.add(f);
				return ret;
			}
		}
		return forwardGraph.get().getShortestPath(sourceGridId, targetGridId, visited);
	}

	@DaoTransaction
	public void update(String sourceGridId, String targetGridId, Consumer<Federation> cons)
	throws DaoException{
		Federation f = getFederationDao().getFederation(sourceGridId, targetGridId);
		cons.accept(f);
	}

	private Cache<FederationGraph> forwardGraph = new Cache<>(
			Functions.soften(() -> buildGraph()), 30*60*1000);
	private Cache<FederationGraph> backwardGraph = new Cache<>(
			Functions.soften(() -> buildReverseGraph()), 30*60*1000);

	private static class Cache<T>{
		public Cache(Supplier<T> supplier, long lifeMillis){
			this.supplier = supplier;
			this.lifeMillis = lifeMillis;
		}
		public synchronized T get(){
			if(instance == null || (System.currentTimeMillis() - birth) > lifeMillis){
				instance = supplier.get();
				birth = System.currentTimeMillis();
			}
			return instance;
		}
		private Supplier<T> supplier;
		private long lifeMillis;
		private T instance;
		private long birth;
	}
}
