package jp.go.nict.langrid.dao;

import java.io.File;

import jp.go.nict.langrid.dao.entity.ResourceType;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.initializer.DomainInitializer;

import org.junit.Test;

public class DomainInitializerTest {
	@Test
	public void test_clearServiceType() throws Exception{
		DaoFactory f = DaoFactory.createInstance();
		DaoContext dc = f.getDaoContext();
		dc.beginTransaction();
		try{
//			ServiceType st = f.createServiceTypeDao().getServiceType("nict.nlp", "AdjacencyPair");
//			System.out.println(st.getInterfaceDefinitions().size());
//			st.getInterfaceDefinitions().clear();
//			System.out.println(st.getInterfaceDefinitions().size());
			f.createServiceTypeDao().clear();
			dc.commitTransaction();
		} catch(Exception e){
			dc.rollbackTransaction();
			throw e;
		}
	}

	@Test
	public void test_initAll() throws Exception{
		DaoFactory f = DaoFactory.createInstance();
		DaoContext dc = f.getDaoContext();
		String gridId = "grid1";
		boolean dropAndCreate = true;
		DomainInitializer.init(f, dc, gridId, dropAndCreate, "init");
	}

	@Test
	public void test_serviceType() throws Exception{
		ServiceType st = DomainInitializer.getServiceType(new File(
				"init/domains/nict.nlp/serviceTypes/AdjacencyPair.json"), "nict.nlp"
				, DaoFactory.createInstance().createServiceTypeDao());
		System.out.println(st.getInterfaceDefinitions().size());
		System.out.println(st.getMetaAttributes().size());
	}
	
	@Test
	public void test_resourceType() throws Exception{
		ResourceType st = DomainInitializer.getResourceType(new File(
				"init/domains/nict.nlp/resourceTypes/BilingualDictionary.json"), "nict.nlp"
				, DaoFactory.createInstance().createResourceTypeDao());
		System.out.println(st.getMetaAttributes().size());
	}
}
