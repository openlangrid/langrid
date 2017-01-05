package jp.go.nict.langrid.servicemanager.web;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
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
		try(OutputStream os = p.getOutputStream();
				Writer w = new OutputStreamWriter(os, "UTF-8");
				PrintWriter pw = new PrintWriter(w)){
			pw.println("digraph g{");
			pw.println("\tgraph[layout=neato,overlap=false];");
			for(Federation f : DaoFactory.createInstance().createFederationDao().list()){
				pw.format("\t\"%s\"->\"%s\" [", f.getSourceGridId(), f.getTargetGridId());
				boolean first = true;
				if(f.isForwardTransitive()){
					pw.format("arrowhead=normalnormal");
					first = false;
				}
				if(f.isSymmetric()){
					if(!first) pw.format(",");
					pw.format("dir=both");
					first = false;
					if(f.isBackwardTransitive()){
						pw.format("arrowtail=normalnormal");
					}
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
		fs.add(newFederation("grid5", "grid6"));
		fs.add(newFederation("grid6", "grid11"));
		fs.add(newFederation("grid1", "grid7"));
		fs.add(newFederation("grid7", "grid8"));
		fs.add(newFederation("grid8", "grid4"));
		fs.add(newFederation("grid8", "grid9"));
		fs.add(newFederation("grid8", "grid10"));
		fs.add(newFederation("grid9", "grid11"));
		fs.add(newFederation("grid10", "grid11"));
		Process p = new ProcessBuilder("/usr/local/bin/dot", "-Tpng").start();
		try(OutputStream os = p.getOutputStream();
				Writer w = new OutputStreamWriter(os, "UTF-8");
				PrintWriter pw = new PrintWriter(w)){
			pw.println("digraph g{");
			pw.println("\tgraph[layout=neato,overlap=false];");
			for(Federation f : fs){
				pw.format("\t\"%s\"->\"%s\" [", f.getSourceGridId(), f.getTargetGridId());
				boolean first = true;
				if(f.isSymmetric()){
					pw.format("dir=both");
					first = false;
				}
				if(f.isRequesting() || !f.isConnected()){
					if(!first) pw.format(",");
					pw.format("style=dotted");
					first = false;
				}
				pw.format("];%n");
			}
			pw.println("}");
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

	private Federation newRequestingFederation(String sgid, String tgid, boolean symmetric, boolean transitive){
		Federation f = new Federation(sgid, tgid);
		f.setConnected(true);
		f.setRequesting(true);
		f.setSymmetric(symmetric);
		f.setForwardTransitive(transitive);
		if(symmetric) f.setBackwardTransitive(transitive);
		return f;
	}
}
