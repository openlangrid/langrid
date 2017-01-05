package jp.go.nict.langrid.management.web.servlet;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.entity.Federation;

public class FederationGraphServletTest {
	@Test
	public void test_graph1() throws Throwable{
		System.setProperty("jp.go.nict.langrid.dao.hibernate.LangridSessionFactory.hibernate.cfg",
				"testcfg/id.langrid.org.kyotooplg.hibernate.cfg.xml");
		Process p = new ProcessBuilder("/usr/local/bin/dot", "-Tpng").start();
		try(OutputStream os = p.getOutputStream()){
			FederationGraphServlet.doGenerateGraph(
					DaoFactory.createInstance().createFederationDao().list(),
					"kyotooplg", os);
		}
		try(InputStream is = p.getInputStream();
				OutputStream os = new FileOutputStream("out.png")){
			StreamUtil.transfer(is, os);
		}
	}

	@Test
	public void test_graph2() throws Throwable{
		List<Federation> fs = new ArrayList<>();
		fs.add(newFederation("grid1", "grid2"));
		fs.add(newFederation("grid2", "grid3"));
		fs.add(newFederation("grid3", "grid4"));
		fs.add(newFederation("grid4", "grid5"));
		fs.add(newRequestingFederation("grid5", "grid6"));
		fs.add(newFederation("grid6", "grid11"));
		fs.add(newFederation("grid1", "grid7"));
		fs.add(newFederation("grid7", "grid8"));
		fs.add(newFederation("grid8", "grid4"));
		fs.add(newFederation("grid8", "grid9"));
		fs.add(newFederation("grid8", "grid10"));
		fs.add(newFederation("grid9", "grid11"));
		fs.add(newFederation("grid10", "grid11"));
		Process p = new ProcessBuilder("/usr/local/bin/dot", "-Tpng").start();
		try(OutputStream os = p.getOutputStream()){
			FederationGraphServlet.doGenerateGraph(fs, "grid1", os);
		}
		try(InputStream is = p.getInputStream();
				OutputStream os = new FileOutputStream("out.png")){
			StreamUtil.transfer(is, os);
		}
	}

	private Federation newFederation(String sgid, String tgid){
		return newFederation(sgid, tgid, false);
	}

	private Federation newFederation(String sgid, String tgid, boolean symmetric){
		return newFederation(sgid, tgid, symmetric, true);
	}

	private Federation newFederation(String sgid, String tgid, boolean symmetric, boolean transitive){
		Federation f = new Federation(sgid, tgid);
		f.setConnected(true);
		f.setRequesting(false);
		f.setSymmetric(symmetric);
		f.setForwardTransitive(transitive);
		if(symmetric) f.setBackwardTransitive(transitive);
		return f;
	}

	private Federation newRequestingFederation(String sgid, String tgid){
		Federation f = new Federation(sgid, tgid);
		f.setConnected(true);
		f.setRequesting(true);
		f.setSymmetric(true);
		f.setForwardTransitive(true);
		f.setBackwardTransitive(true);
		return f;
	}
}
