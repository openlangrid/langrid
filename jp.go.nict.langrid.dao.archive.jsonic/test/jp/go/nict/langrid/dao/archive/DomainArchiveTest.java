package jp.go.nict.langrid.dao.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.Protocol;
import jp.go.nict.langrid.dao.entity.ResourceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;

import org.junit.Test;

public class DomainArchiveTest {
	@Test
	public void test() throws Exception{
		System.setProperty(
				"jp.go.nict.langrid.dao.hibernate.LangridSessionFactory.hibernate.cfg"
				, "hibernate.cfg.test2.0.xml");
		DomainArchive arc = new DomainArchive();
		arc.read1pass(new ZipInputStream(new FileInputStream("langrid-domains.zip")));
		arc.read2pass(
				new ZipInputStream(new FileInputStream("langrid-domains.zip"))
				, DaoFactory.createInstance());

		{
			List<Protocol> values = arc.getProtocols();
			assertNotNull(values);
			assertEquals(1, values.size());
			assertEquals("SOAP_RPC_ENCODED", values.get(0).getProtocolId());
		}

		String domainId = null;
		{
			List<Domain> values = arc.getDomains();
			assertEquals(1, values.size());
			domainId = values.get(0).getDomainId();
			assertEquals("nict.nlp", domainId);
		}

		{
			List<ResourceMetaAttribute> values = arc.getResourceMetaAttributes(domainId);
			assertNotNull(values);
			assertEquals(8, values.size());
		}

		{
			List<ResourceType> values = arc.getResourceTypes(domainId);
			assertNotNull(values);
			assertEquals(13, values.size());
		}

		{
			List<ServiceMetaAttribute> values = arc.getServiceMetaAttributes(domainId);
			assertNotNull(values);
			assertEquals(9, values.size());
		}

		{
			List<ServiceType> values = arc.getServiceTypes(domainId);
			assertNotNull(values);
			assertEquals(16, values.size());
		}
	}
}
