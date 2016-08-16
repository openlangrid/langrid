package jp.go.nict.langrid.foundation.v1_3;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.ServiceDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.entity.ExternalService;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.ServicePK;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;

public class EntityManagementTest {
	@Before
	public void setup() throws Throwable{
		DaoFactory df = DaoFactory.createInstance();
		DaoContext dc = df.getDaoContext();
		GridDao gd = df.createGridDao();
		UserDao ud = df.createUserDao();
		ServiceDao sd = df.createServiceDao();
		dc.transact(() -> {
			gd.clear();
			ud.clear();
			sd.clear();
		});
		dc.transact(() ->{
			Grid g = new Grid();
			g.setGridId("grid1");
			g.setOperatorUserId("operator1");
			gd.addGrid(g);
			User u = new User();
			u.setGridId(g.getGridId());
			u.setUserId("operator1");
			ud.addUser(u);
		});
		dc.transact(() ->{
			ExternalService s = new ExternalService();
			s.setGridId("grid1");
			s.setServiceId("service1");
			sd.addService(s);
		});
	}

	@Test
	public void test_listservice() throws Throwable{
		System.out.println(JSON.encode(
				new EntityManagement().getNewerAndOlderKeys("Service", Calendar.getInstance())
				, true));
	}

	@Test
	public void test_getservice() throws Throwable{
		System.out.println(JSON.encode(
				new EntityManagement().getEntity("Service", new ServicePK("grid1", "service1"))
				, true));
	}

	@Test
	public void test_listresourcemetaattr() throws Throwable{
		System.out.println(JSON.encode(
				new EntityManagement().getNewerAndOlderKeys("ResourceMetaAttribute", Calendar.getInstance())
				, true));
	}

}
