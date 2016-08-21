package jp.go.nict.langrid.dao.hibernate;

import org.junit.Assert;
import org.junit.Test;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.entity.News;

public class HibernateNewsDaoTest {
	@Test
	public void test_autoGenerateId() throws Throwable{
		DaoFactory df = DaoFactory.createInstance();
		DaoContext dc = df.getDaoContext();
		df.createNewsDao().clear();
		News n = new News("grid1", "hello", "1");
		dc.beginTransaction();
		try{
//			df.createNewsDao().addNews(n);
			dc.saveEntity(n);
			dc.commitTransaction();
		} finally{
			
		}
		Assert.assertTrue(n.getId() != 0);
	}

	@Test
	public void test_ignoreGenerateId() throws Throwable{
		DaoFactory df = DaoFactory.createInstance();
		DaoContext dc = df.getDaoContext();
		df.createNewsDao().clear();
		News n = new News("grid1", "hello", "1");
		n.setId(1);
		dc.beginTransaction();
		try{
			dc.saveEntity(n);
			dc.commitTransaction();
		} finally{
			
		}
		Assert.assertTrue(n.getId() == 1);
	}
}
