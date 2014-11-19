package setup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.ProtocolDao;
import jp.go.nict.langrid.dao.ResourceDao;
import jp.go.nict.langrid.dao.ResourceTypeDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.archive.LangridJSON;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.Resource;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.Service;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.User;

public class DumpDB {
	public static void main(String[] args) throws Exception{
		System.setProperty(
				"jp.go.nict.langrid.dao.hibernate.LangridSessionFactory.hibernate.cfg"
				, "hibernate.cfg.xml");
		File baseDir = new File("dump");
		File domainBaseDir = new File(baseDir, "domains");
		File protocolBaseDir = new File(baseDir, "protocols");
		File gridBaseDir = new File(baseDir, "grids");
		DaoFactory f = DaoFactory.createInstance();
		DaoContext c = f.getDaoContext();
		c.beginTransaction();
		try{
			domainBaseDir.mkdirs();
			protocolBaseDir.mkdirs();
			gridBaseDir.mkdirs();
			dumpAllProtocols(f, protocolBaseDir);

			List<Domain> domains = dumpAllDomains(f, domainBaseDir);
			for(Domain d : domains){
				dumpAllResourceMetaAttributes(f, domainBaseDir, d.getDomainId());
				dumpAllResourceTypes(f, domainBaseDir, d.getDomainId());
				dumpAllServiceMetaAttributes(f, domainBaseDir, d.getDomainId());
				dumpAllServiceTypes(f, domainBaseDir, d.getDomainId());
			}
			List<Grid> grids = dumpAllGrids(f, gridBaseDir);
			for(Grid g : grids){
				String gid = g.getGridId();
				dumpAllUsers(f, gridBaseDir, gid);
				dumpAllResources(f, gridBaseDir, gid);
				dumpAllServices(f, gridBaseDir, gid);
			}
		} finally{
			c.rollbackTransaction();
		}
	}

	private static List<Domain> dumpAllDomains(DaoFactory factory, File baseDir) throws Exception{
		DomainDao dao = factory.createDomainDao();
		List<Domain> domains = dao.listAllDomains();
		for(Domain d : domains){
			FileOutputStream os = new FileOutputStream(new File(baseDir, d.getDomainId() + ".json"));
			try{
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				new LangridJSON(baseDir, d.getDomainId()).format(d, w);
				w.close();
			} finally{
				os.close();
			}
		}
		return domains;
	}

	private static List<Grid> dumpAllGrids(DaoFactory factory, File baseDir) throws Exception{
		GridDao dao = factory.createGridDao();
		List<Grid> grids = dao.listAllGrids();
		for(Grid g : grids){
			FileOutputStream os = new FileOutputStream(new File(baseDir, g.getGridId() + ".json"));
			try{
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				new LangridJSON(baseDir, g.getGridId()).format(g, w);
				w.close();
			} finally{
				os.close();
			}
		}
		return grids;
	}

	private static void dumpAllProtocols(DaoFactory factory, File baseDir) throws Exception{
		ProtocolDao dao = factory.createProtocolDao();
		for(Protocol p : dao.listAllProtocols()){
			FileOutputStream os = new FileOutputStream(new File(
					baseDir, p.getProtocolId() + ".json"));
			try{
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				new LangridJSON(baseDir, p.getProtocolId()).format(p, w);
				w.close();
			} finally{
				os.close();
			}
		}
	}

	private static void dumpAllResourceMetaAttributes(
			DaoFactory factory, File bd, String domainId) throws Exception{
		File baseDir = new File(new File(bd, domainId), "resourceMetaAttributes");
		baseDir.mkdirs();
		ResourceTypeDao dao = factory.createResourceTypeDao();
		for(ResourceMetaAttribute rt : dao.listAllResourceMetaAttributes(domainId)){
			FileOutputStream os = new FileOutputStream(new File(
					baseDir, "" + rt.getAttributeId() + ".json"));
			try{
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				new LangridJSON(baseDir, rt.getAttributeId()).format(rt, w);
				w.close();
			} finally{
				os.close();
			}
		}
	}

	private static void dumpAllResourceTypes(
			DaoFactory factory, File bd, String domainId) throws Exception{
		File baseDir = new File(new File(bd, domainId), "resourceTypes");
		baseDir.mkdirs();
		ResourceTypeDao dao = factory.createResourceTypeDao();
		for(ResourceType rt : dao.listAllResourceTypes(domainId)){
			FileOutputStream os = new FileOutputStream(new File(
					baseDir, "" + rt.getResourceTypeId() + ".json"));
			try{
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				new LangridJSON(baseDir, rt.getResourceTypeId()).format(rt, w);
				w.close();
			} finally{
				os.close();
			}
		}
	}

	private static void dumpAllServiceMetaAttributes(
			DaoFactory factory, File bd, String domainId) throws Exception{
		File baseDir = new File(new File(bd, domainId), "serviceMetaAttributes");
		baseDir.mkdirs();
		ServiceTypeDao dao = factory.createServiceTypeDao();
		for(ServiceMetaAttribute rt : dao.listAllServiceMetaAttributes(domainId)){
			FileOutputStream os = new FileOutputStream(new File(
					baseDir, "" + rt.getAttributeId() + ".json"));
			try{
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				new LangridJSON(baseDir, rt.getAttributeId()).format(rt, w);
				w.close();
			} finally{
				os.close();
			}
		}
	}

	private static void dumpAllServiceTypes(
			DaoFactory factory, File bd, String domainId) throws Exception{
		File baseDir = new File(new File(bd, domainId), "serviceTypes");
		baseDir.mkdirs();
		ServiceTypeDao dao = factory.createServiceTypeDao();
		for(ServiceType st : dao.listAllServiceTypes(domainId)){
			String stid = st.getServiceTypeId();
			FileOutputStream os = new FileOutputStream(new File(
					baseDir, stid + ".json"));
			try{
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				new LangridJSON(baseDir, stid).format(st, w);
				w.close();
			} finally{
				os.close();
			}
		}
	}


	private static void dumpAllUsers(DaoFactory factory, File baseDir, String gridId) throws Exception{
		baseDir = new File(new File(baseDir, gridId), "users");
		baseDir.mkdirs();
		UserDao dao = factory.createUserDao();
		for(User u : dao.listAllUsers(gridId)){
			File f = new File(baseDir, u.getUserId() + ".json");
			FileOutputStream os = new FileOutputStream(f);
			try{
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				new LangridJSON(baseDir, u.getUserId()).format(u, w);
				w.close();
			} finally{
				os.close();
			}
		}
	}

	private static void dumpAllResources(DaoFactory factory, File baseDir, String gridId) throws Exception{
		baseDir = new File(new File(baseDir, gridId), "resources");
		baseDir.mkdirs();
		ResourceDao dao = factory.createResourceDao();
		for(Resource r : dao.listAllResources(gridId)){
			File f = new File(baseDir, r.getResourceId() + ".json");
			FileOutputStream os = new FileOutputStream(f);
			try{
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				new LangridJSON(baseDir, r.getResourceId()).format(r, w);
				w.close();
			} finally{
				os.close();
			}
		}
	}

	private static void dumpAllServices(
			DaoFactory factory, File baseDir, String gridId) throws Exception{
		baseDir = new File(new File(baseDir, gridId), "services");
		baseDir.mkdirs();
		ServiceDao dao = factory.createServiceDao();
		for(Service s : dao.listAllServices(gridId)){
			String sid = s.getServiceId();
			FileOutputStream os = new FileOutputStream(new File(baseDir, sid + ".json"));
			try{
				OutputStreamWriter w = new OutputStreamWriter(os, "UTF-8");
				new LangridJSON(baseDir, sid).format(s, w);
				w.close();
			} finally{
				os.close();
			}
		}
	}
}
