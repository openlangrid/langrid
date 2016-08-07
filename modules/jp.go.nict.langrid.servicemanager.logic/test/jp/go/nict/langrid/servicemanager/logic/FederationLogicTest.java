package jp.go.nict.langrid.servicemanager.logic;

import org.junit.Before;
import org.junit.Test;

import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.FederationDao;
import jp.go.nict.langrid.dao.entity.Federation;
import jp.go.nict.langrid.management.logic.FederationLogic;
import org.junit.Assert;

public class FederationLogicTest {
	@Before
	public void setUp() throws Throwable{
		fdao = DaoFactory.createInstance().createFederationDao();
		fdao.clear();
	}

	@Test
	public void test_isReachable() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(new Federation("grid1", "grid2"));
		Assert.assertTrue(fl.isReachable("grid1", "grid2"));
	}

	@Test
	public void test_getNearestFederation_1hop() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(new Federation("grid1", "grid2"));
		Assert.assertEquals("grid2", fl.getNearestFederation("grid1", "grid2").getTargetGridId());
	}

	@Test
	public void test_idReachable_2hop() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(new Federation("grid1", "grid2"));
		fdao.addFederation(new Federation("grid2", "grid3"));
		Assert.assertTrue(fl.isReachable("grid1", "grid3"));
	}

	@Test
	public void test_getNearestFederation() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(new Federation("grid1", "grid2"));
		fdao.addFederation(new Federation("grid2", "grid3"));
		fdao.addFederation(new Federation("grid3", "grid4"));
		fdao.addFederation(new Federation("grid1", "grid3"));
		Federation f = fl.getNearestFederation("grid1", "grid4");
		Assert.assertEquals("grid3", f.getTargetGridId());
	}

	@Test
	public void test_getNearestFederation_2() throws Throwable{
		FederationLogic fl = new FederationLogic();
		fdao.addFederation(new Federation("grid1", "grid2"));
		fdao.addFederation(new Federation("grid2", "grid3"));
		fdao.addFederation(new Federation("grid3", "grid4"));
		fdao.addFederation(new Federation("grid4", "grid5"));
		fdao.addFederation(new Federation("grid5", "grid6"));
		fdao.addFederation(new Federation("grid6", "grid11"));
		fdao.addFederation(new Federation("grid1", "grid7"));
		fdao.addFederation(new Federation("grid7", "grid8"));
		fdao.addFederation(new Federation("grid8", "grid4"));
		fdao.addFederation(new Federation("grid8", "grid9"));
		fdao.addFederation(new Federation("grid8", "grid10"));
		fdao.addFederation(new Federation("grid9", "grid11"));
		fdao.addFederation(new Federation("grid10", "grid11"));
		Federation f = fl.getNearestFederation("grid1", "grid11");
		Assert.assertEquals("grid7", f.getTargetGridId());
	}

	private FederationDao fdao;
}
