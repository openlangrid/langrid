package jp.go.nict.langrid.management.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.util.file.File;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.ws.param.HttpServletRequestParameterContext;
import jp.go.nict.langrid.commons.ws.param.ServletConfigParameterContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.entity.Federation;

@WebServlet("/federationGraph")
public class FederationGraphServlet extends HttpServlet{
	@Override
	public void init(ServletConfig config) throws ServletException {
		ParameterContext pc = new ServletConfigParameterContext(config);
		this.dotPath = pc.getString("dotPath", "/usr/bin/dot");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		File f = new File(dotPath);
		if(!f.exists()){
			resp.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
					"Graphviz not found. Please install it and set its path to \"dotPath\" config or context parameter.");
			return;
		}
		ParameterContext pc = new HttpServletRequestParameterContext(req);
		String format = pc.getString("format", "png");
		resp.setContentType("image/" + format);
		try {
			generateGraph(dotPath, format, resp.getOutputStream());
		} catch (DaoException | InterruptedException e) {
			throw new ServletException(e);
		}
	}

	private void generateGraph(String dotPath, String format, OutputStream os)
	throws IOException, DaoException, InterruptedException{
		Process p = new ProcessBuilder(dotPath, "-T" + format).start();
		try{
			try(OutputStream dot = p.getOutputStream();
					Writer w = new OutputStreamWriter(dot, "UTF-8");
					PrintWriter pw = new PrintWriter(w)){
				pw.println("digraph g{");
				pw.println("\tgraph[layout=neato,overlap=false];");
				for(Federation f : DaoFactory.createInstance().createFederationDao().list()){
					pw.format("\t\"%s\"->\"%s\" [", f.getSourceGridId(), f.getTargetGridId());
					boolean first = true;
					if(f.isTransitive()){
						pw.format("arrowhead=normalnormal");
						first = false;
					}
					if(f.isSymmetric()){
						if(!first) pw.format(",");
						pw.format("dir=both");
						first = false;
					}
					if(f.isRequesting() || !f.isConnected()){
						if(!first) pw.format(",");
						pw.format("style=dashed");
						first = false;
					}
					pw.format("]");
					pw.format(";%n");
				}
				pw.println("}");
			}
			try(InputStream is = p.getInputStream()){
				StreamUtil.transfer(is, os);
			}
		} finally{
			p.waitFor(1000, TimeUnit.MILLISECONDS);
		}
	}

	private String dotPath;
	private static final long serialVersionUID = 419107746222407497L;
}
