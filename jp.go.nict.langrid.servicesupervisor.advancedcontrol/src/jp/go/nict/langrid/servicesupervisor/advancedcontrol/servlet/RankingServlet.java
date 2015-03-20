package jp.go.nict.langrid.servicesupervisor.advancedcontrol.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.parameter.ParameterRequiredException;
import jp.go.nict.langrid.commons.parameter.annotation.Parameter;
import jp.go.nict.langrid.commons.parameter.annotation.ParameterConfig;
import jp.go.nict.langrid.commons.ws.param.HttpServletRequestParameterContext;
import jp.go.nict.langrid.commons.ws.param.ServletContextParameterContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceStatRanking;
import jp.go.nict.langrid.dao.ServiceStatRankingSearchResult;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.servicesupervisor.advancedcontrol.filter.SelectionLogic;
import jp.go.nict.langrid.servicesupervisor.advancedcontrol.filter.SelectionLogic.Mode;

public class RankingServlet extends HttpServlet{
	@ParameterConfig(loadAllFields=true, prefix="langrid.node.")
	public static class ConfigParams{
		@Parameter(required=true)
		private String gridId;
		@Parameter(required=true)
		private String nodeId;
	}
	@ParameterConfig(loadAllFields=true)
	public static class RequestParams{
		@Parameter(defaultValue="3")
		private int sinceDays;
		@Parameter(defaultValue="FASTEST")
		private Mode mode;
		private String domainId;
		private String serviceTypeId;
		private String langAttr;
		private String langValue;
	}
	public static class Result{
		public int getRank() {
			return rank;
		}
		public void setRank(int rank) {
			this.rank = rank;
		}
		public String getServiceId() {
			return serviceId;
		}
		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}
		public long getRequestBytes() {
			return requestBytes;
		}
		public void setRequestBytes(long requestBytes) {
			this.requestBytes = requestBytes;
		}
		public long getResponseBytes() {
			return responseBytes;
		}
		public void setResponseBytes(long responseBytes) {
			this.responseBytes = responseBytes;
		}
		public long getResponseMillis() {
			return responseMillis;
		}
		public void setResponseMillis(long responseMillis) {
			this.responseMillis = responseMillis;
		}
		public long getAccessCount() {
			return accessCount;
		}
		public void setAccessCount(long accessCount) {
			this.accessCount = accessCount;
		}
		public double getResponseMillisAve() {
			return responseMillisAve;
		}
		public void setResponseMillisAve(double responseMillisAve) {
			this.responseMillisAve = responseMillisAve;
		}
		int rank;
		String serviceId;
		long requestBytes;
		long responseBytes;
		long responseMillis;
		long accessCount;
		double responseMillisAve;
	}
	@Override 
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			ConfigParams cp = new ConfigParams();
			new ServletContextParameterContext(
					getServletConfig().getServletContext()
					).load(cp);
			RequestParams rp = new RequestParams();
			new HttpServletRequestParameterContext(req).load(rp);

			DaoFactory f = DaoFactory.createInstance();
			DaoContext c = f.getDaoContext();
			ServiceDao s = f.createServiceDao();
			c.beginTransaction();
			try{
				ServiceStatRankingSearchResult r = s.searchServiceStatRanking(
						0, 20, cp.gridId, cp.nodeId, false
						, SelectionLogic.buildConditions(
								rp.domainId, rp.serviceTypeId, rp.langAttr, rp.langValue
								)
						, rp.sinceDays
						, SelectionLogic.buildOrders(rp.mode)
						);
				int rank = 1;
				List<Result> res = new ArrayList<RankingServlet.Result>();
				for(ServiceStatRanking ssr : r.getElements()){
					Result result = new Result();
					result.rank = rank++;
					result.serviceId = ssr.getServiceId();
					result.requestBytes = ssr.getRequstBytes();
					result.responseBytes = ssr.getResponseBytes();
					result.responseMillis = ssr.getResponseMillis();
					result.accessCount = ssr.getAccessCount();
					result.responseMillisAve = ssr.getResponseMillisAve();
					res.add(result);
				}
				resp.setContentType("application/json");
				resp.getWriter().write(JSON.encode(res));
			} finally{
				c.commitTransaction();
			}
		} catch (DaoException e) {
			throw new ServletException(e);
		} catch (ParameterRequiredException e) {
			throw new ServletException(e);
		}
	}

	private static final long serialVersionUID = 3760431715133990727L;
}
