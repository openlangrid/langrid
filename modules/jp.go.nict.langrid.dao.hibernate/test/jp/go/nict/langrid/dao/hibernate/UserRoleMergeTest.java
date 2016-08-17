package jp.go.nict.langrid.dao.hibernate;

import java.util.Arrays;

import org.junit.Test;

import jp.go.nict.langrid.dao.DaoContext;
import jp.go.nict.langrid.dao.DaoFactory;
import jp.go.nict.langrid.dao.entity.User;
import jp.go.nict.langrid.dao.entity.UserAttribute;
import jp.go.nict.langrid.dao.entity.UserRole;

public class UserRoleMergeTest {
	@Test
	public void test() throws Throwable{
		DaoFactory df = DaoFactory.createInstance();
		DaoContext dc = df.getDaoContext();
		df.createUserDao().clear();

		User u1 = new User("grid1", "user1", "user1");
		u1.getRoles().add(new UserRole("grid1", "user1", "role1"));
		u1.setAttribute(new UserAttribute("grid1", "user1", "attr1", "value1"));
		dc.beginTransaction();
		try{
			dc.saveEntity(u1);
			dc.commitTransaction();
		} finally{
		}

		User u2 = new User("grid1", "user1", "user1");
		u2.getRoles().add(new UserRole("grid1", "user1", "role1"));
		u2.setAttributes(Arrays.asList(new UserAttribute("grid1", "user1", "attr1", "value1")));
		dc.beginTransaction();
		try{
			dc.mergeEntity(u2);
			dc.commitTransaction();
		} finally{
		}
	}
}
