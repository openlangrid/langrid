package jp.go.nict.langrid.foundation.v1_3;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import jp.go.nict.langrid.commons.ws.LocalServiceContext;
import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.GridDao;
import jp.go.nict.langrid.dao.UserDao;
import jp.go.nict.langrid.dao.entity.Grid;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.service_1_3.foundation.GridEntry;

public class GridManagementTest {
	@Before
	public void setup() throws Throwable{
		DaoFactory df = DaoFactory.createInstance();
		DaoContext dc = df.getDaoContext();
		GridDao gd = df.createGridDao();
		UserDao ud = df.createUserDao();
		dc.transact(() -> {
			gd.clear();
			ud.clear();
		});
		dc.transact(() ->{
			Grid g = new Grid();
			g.setGridId("langrid-1.2");
			g.setOperatorUserId("operator1");
			gd.addGrid(g);
			User u = new User();
			u.setGridId(g.getGridId());
			u.setUserId("operator1");
			ud.addUser(u);
		});
	}

	@Test
	public void test() throws Throwable{
		GridEntry ge = new GridManagement(new LocalServiceContext()).getGridEntry();
		Assert.assertEquals("langrid-1.2", ge.getGridId());
		Assert.assertEquals("operator1", ge.getOperator().getUserId());
//		System.out.println(JSON.encode(ge));
	}
}
