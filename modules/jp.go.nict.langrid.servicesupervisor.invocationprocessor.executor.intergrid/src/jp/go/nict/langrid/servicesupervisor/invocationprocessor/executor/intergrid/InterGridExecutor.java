/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
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
package jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.intergrid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.lang.StringUtil;
import jp.go.nict.langrid.commons.util.ArrayUtil;
import jp.go.nict.langrid.commons.ws.LangridConstants;
import jp.go.nict.langrid.commons.ws.ServiceContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.FederationNotFoundException;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.management.logic.FederationLogic;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.AbstractExecutor;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.Executor;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ExecutorParams;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.GridTrackUtil;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.GridTrackUtil.GridTrack;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.GridTrackUtil.Invocation;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.NoValidEndpointsException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ProcessFailedException;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.ServiceInvoker;
import jp.go.nict.langrid.servicesupervisor.invocationprocessor.executor.TooManyCallNestException;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public class InterGridExecutor extends AbstractExecutor implements Executor {
	public InterGridExecutor(DaoFactory daoFactory, ExecutorParams params)
	throws DaoException{
		this.gridDao = daoFactory.createGridDao();
		this.federationDao = daoFactory.createFederationDao();
		this.federationLogic = new FederationLogic();
		this.serviceDao = daoFactory.createServiceDao();
		this.userDao = daoFactory.createUserDao();
		this.connectionTimeout = params.interGridCallConnectionTimeout;
		this.readTimeout = params.interGridCallReadTimeout;
		this.maxCallNest = params.maxCallNest;
	}

	@Override
	public void execute(
			ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response,
			ServiceContext serviceContext, DaoContext daoContext,
			String sourceGridId, String sourceUserId,
			String targetGridId, String targetServiceId,
			Map<String, String> headers,
			String query, String protocol, byte[] input
			)
	throws DaoException, TooManyCallNestException, NoValidEndpointsException, ProcessFailedException, IOException{
		String selfGridId = serviceContext.getSelfGridId();
		String prevGridId = selfGridId;
		URL url = null;
		String authId = null;
		String authPasswd = null;
		String[] visited = serviceContext.getRequestMimeHeaders().getHeader(LangridConstants.HTTPHEADER_FEDERATEDCALL_VISITED);
		if(visited == null) visited = new String[]{};
		visited = StringUtil.join(visited, ",").split(",");
		daoContext.beginTransaction();
		try{
			boolean forward = true;
			Federation f = null;
			Grid nextGrid = null;
			String[] route = serviceContext.getRequestMimeHeaders().getHeader(LangridConstants.HTTPHEADER_FEDERATEDCALL_ROUTE);
			if(route != null && route.length == 1 && route[0].length() > 0){
				// パス指定があればそれに従う．無ければショートカット含め接続を検索/探索
				String[] r = StringUtil.join(route, ",").split(",", 2);
				if(r.length == 0) throw new ProcessFailedException("invalid route spec: " + StringUtil.join(route, ","));
				String nextGid = r[0];
				String rest = r.length > 1 ? r[1] : null;
				nextGrid = gridDao.getGrid(nextGid);
				f = federationLogic.getReachableTransitiveFederation(selfGridId, nextGid);
				if(f == null){
					throw new ProcessFailedException("no reachable federation from " + selfGridId + " to " + nextGid);
				}
				forward = f.getSourceGridId().equals(selfGridId);
				if(rest != null) headers.put(LangridConstants.HTTPHEADER_FEDERATEDCALL_ROUTE, rest);
				else headers.put(LangridConstants.HTTPHEADER_FEDERATEDCALL_ROUTE, "");
			} else{
				List<Federation> path = federationLogic.getShortestPath(selfGridId, targetGridId, new HashSet<>(Arrays.asList(visited)));
				if(path.size() > 0){
					if(serviceContext.getRequestMimeHeaders().getHeader(
							LangridConstants.HTTPHEADER_FEDERATEDCALL_BYPASSINGINVOCATION) != null
							&&
							gridDao.getGrid(targetGridId).isBypassExecutionAllowed()){
						// get farthest
						f = path.get(path.size() - 1);
						forward = f.getTargetGridId().equals(targetGridId);
						prevGridId = forward ? f.getSourceGridId() : f.getTargetGridId();
					} else{
						// get nearest
						f = path.get(0);
						forward = f.getSourceGridId().equals(selfGridId);
					}
				}
				if(f == null){
					throw new ProcessFailedException("no route to target grid: " + targetGridId + " from grid:" + selfGridId);
				}
				nextGrid = gridDao.getGrid(forward ? f.getTargetGridId() : f.getSourceGridId());
			}
			String gurl = nextGrid.getUrl();
			if(!gurl.endsWith("/")) gurl += "/";
			url = new URL(gurl + "invoker/" + targetGridId + ":" + targetServiceId
					+ ((query != null) ? query : ""));
			authId = forward ? f.getTargetGridUserId() : f.getSourceGridUserId();
			authPasswd = f.getTargetGridAccessToken();
		} catch(MalformedURLException e){
			throw new ProcessFailedException(e);
		} finally{
			daoContext.commitTransaction();
		}
		headers.put(LangridConstants.HTTPHEADER_FEDERATEDCALL_VISITED, StringUtil.join(ArrayUtil.append(visited, selfGridId), ","));
		headers.put(LangridConstants.HTTPHEADER_FEDERATEDCALL_SOURCEGRIDID, prevGridId);
		headers.putIfAbsent(
				LangridConstants.HTTPHEADER_FEDERATEDCALL_CALLERUSER
				, serviceContext.getAuthUserGridId() + ":" + serviceContext.getAuthUser()
				);

		String nestCountString = headers.get(LangridConstants.HTTPHEADER_CALLNEST);
		if(nestCountString != null){
			int nest = Integer.parseInt(nestCountString) + 1;
			if(nest > maxCallNest){
				throw new TooManyCallNestException(maxCallNest, nest);
			} else{
				headers.put(
						LangridConstants.HTTPHEADER_CALLNEST
						, Integer.toString(nest));
			}
		}

		ServiceInvoker.invoke(
				selfGridId, url, authId, authPasswd, headers
				, new ByteArrayInputStream(input), response, response.getOutputStream()
				, connectionTimeout, readTimeout
				);

		// affect gridTrack to Federation
		String gridTrack = response.getHeader(LangridConstants.HTTPHEADER_GRIDTRACK);
		if(gridTrack != null){
			daoContext.beginTransaction();
			try{
				updateFederationDelay(selfGridId, GridTrackUtil.decode(gridTrack));
			} finally{
				try{
					daoContext.commitTransaction();
				} catch(DaoException e){}
			}
		}

		// remove shortcut if removed at destination grid.
		if(selfGridId.equals(sourceGridId) &&
					serviceContext.getRequestMimeHeaders().getHeader(
							LangridConstants.HTTPHEADER_FEDERATEDCALL_REMOVESHORTCUT
					) != null){
			String r = response.getHeader(
							LangridConstants.HTTPHEADER_FEDERATEDCALL_SHORTCUTRESULT);
			if(r != null && r.equals("removed")){
				daoContext.beginTransaction();
				try{
					Federation f = federationDao.getFederation(sourceGridId, targetGridId);
					if(f.isShortcut()){
						federationDao.deleteFederation(sourceGridId, targetGridId);
					}
				} catch(FederationNotFoundException e){
				} finally{
					daoContext.commitTransaction();
				}
			}
		}
		// create shortcut if created at destination grid.
		do{
			if(!serviceContext.getSelfGridId().equals(sourceGridId) ||
				serviceContext.getRequestMimeHeaders().getHeader(
						LangridConstants.HTTPHEADER_FEDERATEDCALL_CREATESHORTCUT
				) == null) break;
			String sr = response.getHeader(
					LangridConstants.HTTPHEADER_FEDERATEDCALL_SHORTCUTRESULT);
			if(sr == null) break;
			String[] symAndToken = sr.split(";", 2);
			if(symAndToken.length != 2) break;
			Grid source = gridDao.getGrid(sourceGridId);
			Grid target = gridDao.getGrid(targetGridId);
			boolean ff = federationDao.isFederationExist(sourceGridId, targetGridId);
			boolean bf = federationDao.isFederationExist(targetGridId, sourceGridId);
			if(ff || bf) break;
			User sourceUser = userDao.getUser(source.getGridId(), source.getOperatorUserId());
			User targetUser = userDao.getUser(target.getGridId(), target.getOperatorUserId());
			if(sourceUser == null || targetUser == null) break;
			boolean sym = symAndToken[0].equals("symm");
			String token = symAndToken[1];
			federationDao.addFederation(new Federation(
					sourceGridId, source.getGridName(), source.getOperatorUserId(), sourceUser.getOrganization(),
					targetGridId, target.getGridName(), target.getOperatorUserId(), targetUser.getOrganization(),
					targetUser.getHomepageUrl(), token,
					false, true, true, sym, sym, true));
		} while(false);

		if(selfGridId.equals(sourceGridId)){
			// remove unnecessary headers for client
//			response.setHeader(LangridConstants.HTTPHEADER_FEDERATEDCALL_SHORTCUTRESULT, null);
		}
	}

	private void updateFederationDelay(String selfGridId, Collection<GridTrack> gts)
	throws DaoException{
		Iterator<GridTrack> it = gts.iterator();
		updateFederationDelay(selfGridId, it);
	}
	private GridTrack updateFederationDelay(String selfGridId, Iterator<GridTrack> it)
	throws DaoException{
		if(!it.hasNext()) return null;
		GridTrack cur = it.next();
		GridTrack next = updateFederationDelay(selfGridId, it);
		if(next == null) return cur;
		long d = cur.getProcessMillis() - next.getProcessMillis();
		Federation f = federationLogic.getReachableFederation(cur.getGridId(), next.getGridId());
		if(f != null){
			long c = f.getInvocationCount();
			double o = f.getAveOverhead();
			long nc = c + 1;
			double no = (o * c + d) / nc;
			f.setInvocationCount(nc);
			f.setAveOverhead(no);
			if(f.getSourceGridId().equals(selfGridId)){
				f.setUpdatedDateTime(Calendar.getInstance());
			}
		}
		for(Invocation i : cur.getInvocations()){
			updateFederationDelay(selfGridId, i.getGridTrack());
		}
		return cur;
	}

	private FederationLogic federationLogic;
	private GridDao gridDao;
	private FederationDao federationDao;
	private ServiceDao serviceDao;
	private UserDao userDao;
	private int connectionTimeout;
	private int readTimeout;
	private int maxCallNest;
	
	//private static Logger logger = Logger.getLogger(IntraGridExecutor.class.getName());
}
