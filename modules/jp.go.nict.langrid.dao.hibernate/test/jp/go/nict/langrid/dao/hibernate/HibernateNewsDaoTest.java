package jp.go.nict.langrid.dao.hibernate;

import org.junit.Test;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.entity.News;

public class HibernateNewsDaoTest {
	@Test
	public void test() throws Throwable{
		DaoFactory df = DaoFactory.createInstance();
		DaoContext dc = df.getDaoContext();
		News n = new News("grid1", "hello", "1");
		dc.beginTransaction();
		try{
//			df.createNewsDao().addNews(n);
			dc.saveEntity(n);
			dc.commitTransaction();
		} finally{
			
		}
	}
}
