package setup;

import java.io.File;
import java.io.FileInputStream;

import jp.go.nict.langrid.commons.io.RegexFileNameFilter;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.NodeDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.entity.BPELService;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Node;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class SetupDB {
	public static void main(String[] args) throws Exception{
		File baseDir = new File("contents");
		DaoFactory f = DaoFactory.createInstance();
		DaoContext c = f.getDaoContext();
		c.beginTransaction();

		try{
			setupUsers(f, baseDir);
			setupServices(f, baseDir);
			setupNodes(f, baseDir);
		} finally{
			c.commitTransaction();
		} 
	}

	private static void setupUsers(DaoFactory factory, File baseDir) throws Exception{
		UserDao dao = factory.createUserDao();
		dao.clear();
		for(File f : baseDir.listFiles(new RegexFileNameFilter("^user_.*\\.json$"))){
			dao.addUser(JSON.decode(
					new FileInputStream(f)
					, User.class
					));
		}
	}

	private static void setupServices(DaoFactory factory, File baseDir) throws Exception{
		ServiceDao dao = factory.createServiceDao();
		dao.clear();
		for(File f : baseDir.listFiles(new RegexFileNameFilter("^es_.*\\.json$"))){
			dao.addService(JSON.decode(new FileInputStream(f), ExternalService.class));
		}
		for(File f : baseDir.listFiles(new RegexFileNameFilter("^bs_.*\\.json$"))){
			dao.addService(JSON.decode(new FileInputStream(f), BPELService.class));
		}
	}

	private static void setupNodes(DaoFactory factory, File baseDir) throws Exception{
		NodeDao dao = factory.createNodeDao();
		dao.clear();
		for(File f : baseDir.listFiles(new RegexFileNameFilter("^node_.*\\.json$"))){
			dao.addNode(JSON.decode(new FileInputStream(f), Node.class));
		}
	}
}
