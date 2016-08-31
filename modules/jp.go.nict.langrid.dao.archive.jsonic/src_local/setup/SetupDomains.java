package setup;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.go.nict.langrid.commons.io.RegexFileNameFilter;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.ProtocolDao;
import jp.go.nict.langrid.dao.ResourceTypeDao;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.archive.FilenameUtil;
import jp.go.nict.langrid.dao.archive.LangridJSON;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ServiceInterfaceDefinition;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;

public class SetupDomains {
	public static void main(String[] args) throws Exception{
		System.setProperty(
				"jp.go.nict.langrid.dao.hibernate.LangridSessionFactory.hibernate.cfg"
				, "hibernate.cfg.xml");
		File baseDir = new File("contents_domains");
		DaoFactory f = DaoFactory.createInstance();
		DaoContext c = f.getDaoContext();
		c.beginTransaction();
		try{
			clearAll(f);
			c.commitTransaction();
			c.beginTransaction();
			List<String> domainIds = setupDomains(f, baseDir);
			c.commitTransaction();
			c.beginTransaction();
			setupProtocols(f, baseDir);
			for(String did : domainIds){
				setupResourceMetaAttributes(f, did, baseDir);
				setupResourceTypes(f, did, baseDir);
				setupServiceMetaAttributes(f, did, baseDir);
				setupServiceTypes(f, did, baseDir);
			}
		} finally{
			c.commitTransaction();
		} 
	}

	private static void clearAll(DaoFactory factory) throws Exception{
		factory.createServiceTypeDao().clear();
		factory.createResourceTypeDao().clear();
		factory.createProtocolDao().clear();
		factory.createDomainDao().clear();
	}

	private static List<String>  setupDomains(DaoFactory factory, File baseDir) throws Exception{
		baseDir = new File(baseDir, "domains");
		DomainDao dao = factory.createDomainDao();
		List<String> domainIds = new ArrayList<String>();
		for(File f : baseDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			Domain d = new LangridJSON(baseDir, FilenameUtil.getIdFromFileName(f))
					.parse(new FileInputStream(f), Domain.class);
			dao.addDomain(d);
			d.setUpdatedDateTime(Calendar.getInstance());
			domainIds.add(d.getDomainId());
		}
		return domainIds;
	}

	private static void setupProtocols(DaoFactory factory, File baseDir) throws Exception{
		baseDir = new File(baseDir, "protocols");
		ProtocolDao dao = factory.createProtocolDao();
		for(File f : baseDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			Protocol p = new LangridJSON(baseDir, FilenameUtil.getIdFromFileName(f))
					.parse(new FileInputStream(f), Protocol.class);
			p.setUpdatedDateTime(Calendar.getInstance());
			dao.addProtocol(p);
		}
	}

	private static void setupResourceMetaAttributes(DaoFactory factory, String domainId, File baseDir)
	throws Exception{
		baseDir = new File(new File(new File(baseDir, "domains"), domainId), "resourceMetaAttributes");
		ResourceTypeDao dao = factory.createResourceTypeDao();
		for(File f : baseDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			ResourceMetaAttribute metaAttribute = new LangridJSON(baseDir, FilenameUtil.getIdFromFileName(f))
					.parse(new FileInputStream(f), ResourceMetaAttribute.class);
			metaAttribute.setDomainId(domainId);
			dao.addResourceMetaAttribute(metaAttribute);
		}
	}

	private static void setupResourceTypes(DaoFactory factory, String domainId, File baseDir)
	throws Exception{
		baseDir = new File(new File(new File(baseDir, "domains"), domainId), "resourceTypes");
		ResourceTypeDao dao = factory.createResourceTypeDao();
		for(File f : baseDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			ResourceType resourceType = new LangridJSON(baseDir, FilenameUtil.getIdFromFileName(f))
					.parse(new FileInputStream(f), ResourceType.class);
			for(ResourceMetaAttribute m : resourceType.getMetaAttributes().values()){
				m.setDomainId(domainId);
			}
			resourceType.setUpdatedDateTime(Calendar.getInstance());
			resourceType.setDomainId(domainId);
			dao.addResourceType(resourceType);
		}
	}

	private static void setupServiceMetaAttributes(DaoFactory factory, String domainId, File baseDir)
	throws Exception{
		baseDir = new File(new File(new File(baseDir, "domains"), domainId), "serviceMetaAttributes");
		ServiceTypeDao dao = factory.createServiceTypeDao();
		for(File f : baseDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			ServiceMetaAttribute metaAttribute = new LangridJSON(baseDir, FilenameUtil.getIdFromFileName(f))
					.parse(new FileInputStream(f), ServiceMetaAttribute.class);
			metaAttribute.setDomainId(domainId);
			dao.addServiceMetaAttribute(metaAttribute);
		}
	}

	private static void setupServiceTypes(DaoFactory factory, String domainId, File baseDir)
	throws Exception{
		baseDir = new File(new File(new File(baseDir, "domains"), domainId), "serviceTypes");
		ServiceTypeDao dao = factory.createServiceTypeDao();
		for(File f : baseDir.listFiles(new RegexFileNameFilter(".*\\.json$"))){
			ServiceType serviceType = new LangridJSON(
					baseDir, FilenameUtil.getIdFromFileName(f)
					).parse(new FileInputStream(f), ServiceType.class);
			for(ServiceMetaAttribute ma : serviceType.getMetaAttributes().values()){
				ma.setDomainId(domainId);
			}
			for(ServiceInterfaceDefinition def : serviceType.getInterfaceDefinitions().values()){
				def.setServiceType(serviceType);
			}
			serviceType.setDomainId(domainId);
			serviceType.setServiceTypeName(serviceType.getServiceTypeId());
			dao.addServiceType(serviceType);
		}
	}
}
