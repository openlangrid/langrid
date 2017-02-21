package jp.go.nict.langrid.servlet.langrid;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import jp.go.nict.langrid.commons.ws.ServletServiceContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.servlet.ReverseProxyServlet;

public class AuthenticatingReverseProxyServlet extends ReverseProxyServlet{
	private static final long serialVersionUID = -3055949289906630530L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			df = DaoFactory.createInstance();
		} catch (DaoException e) {
			throw new ServletException(e);
		}
		super.init(config);
	}

	@Override
	protected String getRemoteUserName(HttpServletRequest req) {
		try {
			return df.createUserDao().getUser(new ServletServiceContext(req).getSelfGridId(), req.getRemoteUser())
				.getEmailAddress();
		} catch (DaoException e) {
			return req.getRemoteUser();
		}
	}
	
	private DaoFactory df;
}
