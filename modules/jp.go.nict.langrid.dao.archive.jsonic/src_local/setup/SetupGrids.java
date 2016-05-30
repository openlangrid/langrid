package setup;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.commons.io.RegexFileNameFilter;
import jp.go.nict.langrid.commons.security.MessageDigestUtil;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.ResourceDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.archive.FilenameUtil;
import jp.go.nict.langrid.dao.archive.LangridJSON;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourceAttribute;
import jp.go.nict.langrid.dao.entity.ServiceAttribute;
import jp.go.nict.langrid.dao.entity.ServiceEndpoint;
import jp.go.nict.langrid.dao.entity.User;

public class SetupGrids {
	public static void main(String[] args) throws Exception{
		System.setProperty(
				"jp.go.nict.langrid.dao.hibernate.LangridSessionFactory.hibernate.cfg"
				, "hibernate.cfg.xml");
		File baseDir = new File("contents_grids");
		DaoFactory f = DaoFactory.createInstance();
		DaoContext c = f.getDaoContext();
		c.beginTransaction();
		try{
			clearAll(f);
			List<String> gridIds = setupGrids(f, baseDir);
			for(String gid : gridIds){
				setupUsers(f, gid, baseDir);
				setupResources(f, gid, baseDir);
				setupExternalServices(f, gid, baseDir);
			}
		} finally{
			c.commitTransaction();
		}
	}

	private static void clearAll(DaoFactory factory) throws Exception{
		factory.createServiceDao().clear();
		factory.createResourceDao().clear();
		factory.createUserDao().clear();
		factory.createGridDao().clear();
	}

	private static List<String>  setupGrids(DaoFactory factory, File baseDir) throws Exception{
		baseDir = new File(baseDir, "grids");
		GridDao gdao = factory.createGridDao();
		DomainDao ddao = factory.createDomainDao();
		List<String> gridIds = new ArrayList<String>();
		for(File f : baseDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			Grid g = new LangridJSON(baseDir, FilenameUtil.getIdFromFileName(f))
					.parse(new FileInputStream(f), Grid.class);
			List<Domain> domains = new ArrayList<Domain>();
			for(Domain d : g.getSupportedDomains()){
				domains.add(ddao.getDomain(d.getDomainId()));
			}
			g.getSupportedDomains().clear();
			g.getSupportedDomains().addAll(domains);
			gdao.addGrid(g);
			gridIds.add(g.getGridId());
		}
		return gridIds;
	}

	private static void setupUsers(DaoFactory factory, String gridId, File baseDir) throws Exception{
		baseDir = new File(new File(new File(baseDir, "grids"), gridId), "users");
		UserDao dao = factory.createUserDao();
		for(File f : baseDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			User u = new LangridJSON(baseDir, FilenameUtil.getIdFromFileName(f))
					.parse(new FileInputStream(f), User.class);
			u.setPassword(MessageDigestUtil.digestBySHA512(u.getPassword()));
			dao.addUser(u);
		}
	}

	private static void setupResources(DaoFactory factory, String gridId, File baseDir)
	throws Exception{
		baseDir = new File(new File(new File(baseDir, "grids"), gridId), "resources");
		ResourceDao dao = factory.createResourceDao();
		for(File f : baseDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			Resource r = new LangridJSON(baseDir, FilenameUtil.getIdFromFileName(f))
					.parse(new FileInputStream(f), Resource.class);
			for(ResourceAttribute a : r.getAttributes()){
				a.setGridId(r.getGridId());
				a.setResourceId(r.getResourceId());
			}
			dao.addResource(r);
		}
	}

	private static void setupExternalServices(DaoFactory factory, String gridId, File baseDir)
	throws Exception{
		baseDir = new File(new File(new File(baseDir, "grids"), gridId), "services");
		ServiceDao dao = factory.createServiceDao();
		for(File f : baseDir.listFiles(new RegexFileNameFilter("es_.*\\.json$"))){
			ExternalService s = new LangridJSON(baseDir, FilenameUtil.getIdFromServiceFileName(f))
					.parse(new FileInputStream(f), ExternalService.class);
			for(ServiceAttribute a : s.getAttributes()){
				a.setGridId(gridId);
				a.setServiceId(s.getServiceId());
			}
			for(ServiceEndpoint a : s.getServiceEndpoints()){
				a.setGridId(gridId);
				a.setServiceId(s.getServiceId());
			}
			s.setGridId(gridId);
			dao.addService(s);
		}
	}
}
