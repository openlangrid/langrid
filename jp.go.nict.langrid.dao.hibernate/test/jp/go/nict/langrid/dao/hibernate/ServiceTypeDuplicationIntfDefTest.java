package jp.go.nict.langrid.dao.hibernate;

import java.util.List;
import java.util.Map;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoException;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.DomainDao;
import jp.go.nict.langrid.dao.ServiceTypeDao;
import jp.go.nict.langrid.dao.entity.Domain;
import jp.go.nict.langrid.dao.entity.ServiceInterfaceDefinition;
import jp.go.nict.langrid.dao.entity.ServiceMetaAttribute;
import jp.go.nict.langrid.dao.entity.ServiceType;
import jp.go.nict.langrid.dao.entity.ServiceTypePK;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServiceTypeDuplicationIntfDefTest {
	
	@Before
	public void setUp() throws Throwable{
		DaoFactory df = DaoFactory.createInstance();
		dc = df.getDaoContext();
		ddao = df.createDomainDao();
		stdao = df.createServiceTypeDao();
		dc.beginTransaction();
		try{
			stdao.clear();
			ddao.clear();
		} finally{
			dc.commitTransaction();
		}
	}

	@Test
	public void test_mergeEntity_for_servicetype() throws Throwable{
		try{
			dc.mergeEntity(new ServiceType("testdomain", "testservicetype"));
			Assert.fail();
		} catch(DaoException e){}
	}

	@Test
	public void test_mergeEntity_by_hand() throws Throwable{
		String domainId = "testdomain";
		String serviceTypeId = "testservicetype";
		String protocolId = "testproto";
		
		dc.beginTransaction();
		try{
			// first
			ddao.addDomain(new Domain(domainId));
			ServiceType st = new ServiceType(domainId, serviceTypeId);
			st.getInterfaceDefinitions().put(protocolId, new ServiceInterfaceDefinition(st, protocolId));
			stdao.addServiceType(st);
		} finally{
			dc.commitTransaction();
		}

		dc.beginTransaction();
		try{
			// second
			ServiceType st = new ServiceType(domainId, serviceTypeId);
			st.setServiceTypeName("serviceTypeName");
			st.setDescription("description");
			st.getInterfaceDefinitions().put(protocolId, new ServiceInterfaceDefinition(st, protocolId));
			// merge
			ServiceType tgt = stdao.getServiceType(domainId, serviceTypeId);
			tgt.setServiceTypeName(st.getServiceTypeName());
			tgt.setDescription(st.getDescription());
			for(Map.Entry<String, ServiceMetaAttribute> e : st.getMetaAttributes().entrySet()){
				tgt.getMetaAttributes().put(e.getKey(), e.getValue());
			}
			for(Map.Entry<String, ServiceInterfaceDefinition> e : st.getInterfaceDefinitions().entrySet()){
				tgt.getInterfaceDefinitions().put(e.getKey(), e.getValue());
			}
			tgt.setCreatedDateTime(st.getCreatedDateTime());
			tgt.setUpdatedDateTime(st.getUpdatedDateTime());
		} finally{
			dc.commitTransaction();
		}

		dc.beginTransaction();
		try{
			List<ServiceInterfaceDefinition> list = ((HibernateDaoContext)dc).listEntity(ServiceInterfaceDefinition.class);
			Assert.assertEquals(1, list.size());
			ServiceType st = dc.loadEntity(ServiceType.class, new ServiceTypePK(domainId, serviceTypeId));
			Assert.assertEquals("serviceTypeName", st.getServiceTypeName());
			Assert.assertEquals("description", st.getDescription());
		} finally{
			dc.commitTransaction();
		}
	}

	@Test
	public void test_serviceintf_duplication() throws Throwable{
		String domainId = "testdomain";
		String serviceTypeId = "testservicetype";
		String protocolId = "testproto";
		
		dc.beginTransaction();
		try{
			// first
			ddao.addDomain(new Domain(domainId));
			ServiceType st = new ServiceType(domainId, serviceTypeId);
			st.getInterfaceDefinitions().put(protocolId, new ServiceInterfaceDefinition(st, protocolId));
			stdao.addServiceType(st);
		} finally{
			dc.commitTransaction();
		}

		dc.beginTransaction();
		try{
			// second
			ServiceType st = new ServiceType(domainId, serviceTypeId);
			st.getInterfaceDefinitions().put(protocolId, new ServiceInterfaceDefinition(st, protocolId));
			((HibernateDaoContext)dc).getSession().saveOrUpdate(st);
		} finally{
			dc.commitTransaction();
		}
		
		Assert.assertEquals(2, ((HibernateDaoContext)dc).listEntity(ServiceInterfaceDefinition.class).size());
	}

	private DaoContext dc;
	private DomainDao ddao;
	private ServiceTypeDao stdao;
}
