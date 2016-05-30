package jp.go.nict.langrid.dao.archive;

import java.io.FileInputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.ServiceType;

import org.junit.Assert;
import org.junit.Test;

public class ImporterTest {
	@Test
	public void importDomainArchive() throws Exception{
		System.setProperty(
				"jp.go.nict.langrid.dao.hibernate.LangridSessionFactory.hibernate.cfg"
				, "hibernate.cfg.test2.0.xml");
		DaoFactory f = DaoFactory.createInstance();
		DaoContext c = f.getDaoContext();
		c.beginTransaction();
		try{
			DomainArchive arc = new DomainArchive();
			arc.read1pass(new ZipInputStream(new FileInputStream("langrid-domains.dar")));
			arc.read2pass(
					new ZipInputStream(new FileInputStream("langrid-domains.dar"))
					, DaoFactory.createInstance());
			Importer i = new Importer(f);
			i.clearDomains();
			i.importDomains(arc);
		} finally{
			c.commitTransaction();
		}

		c.beginTransaction();
		try{
			List<Domain> domains = f.createDomainDao().listAllDomains();
			Assert.assertEquals(1, domains.size());
			String did = domains.get(0).getDomainId();
			Assert.assertEquals(8, f.createResourceTypeDao().listAllResourceMetaAttributes(did).size());
			Assert.assertEquals(13, f.createResourceTypeDao().listAllResourceTypes(did).size());
			Assert.assertEquals(9, f.createServiceTypeDao().listAllServiceMetaAttributes(did).size());
			List<ServiceType> stypes = f.createServiceTypeDao().listAllServiceTypes(did);
			Assert.assertEquals(16, stypes.size());
			ServiceType st = null;
			for(ServiceType s : stypes){
				if(s.getServiceTypeId().equals("Translation")){
					st = s;
					break;
				}
			}
			Assert.assertNotNull(st);
			Assert.assertEquals(1, st.getInterfaceDefinitions().values().size());
			Assert.assertNotNull(st.getMetaAttributes().get("supportedLanguagePairs_PairList"));
			Assert.assertNotNull(st.getMetaAttributes().get("supportedLanguagePairs_AnyCombination"));
		} finally{
			c.commitTransaction();
		}
	}
}
