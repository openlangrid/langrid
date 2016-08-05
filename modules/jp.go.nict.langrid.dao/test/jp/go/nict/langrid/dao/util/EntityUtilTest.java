package jp.go.nict.langrid.dao.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Test;

public class EntityUtilTest {
	@Entity
	static class Entity1{
		@Id
		private String id;
	}
	@Entity
	@IdClass(Entity2PK.class)
	static class Entity2{
		@Id
		private String id1;
		@Id
		private String id2;
		@Id
		private String id3;
	}
	public static class Entity2PK{
		public Entity2PK() {
		}
		public Entity2PK(String id1, String id2) {
			this.id1 = id1;
			this.id2 = id2;
		}

		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}
		public String getId1() {
			return id1;
		}
		public void setId1(String id1) {
			this.id1 = id1;
		}
		public String getId2() {
			return id2;
		}
		public void setId2(String id2) {
			this.id2 = id2;
		}
		private String id1;
		private String id2;
	}
	
	@Test
	public void test_getFieldNames() throws Throwable{
		List<String> names = new ArrayList<>();
		EntityUtil.getFieldNames(Entity2.class, names);
		Assert.assertArrayEquals(
				new String[]{"id1", "id2", "id3"},
				names.toArray(new String[]{})
				);
	}
	
	@Test
	public void test_getIdFieldName() throws Throwable{
		Assert.assertEquals("id", EntityUtil.getIdFieldName(Entity1.class));
	}

	@Test
	public void test_getIdFieldNames_1() throws Throwable{
		Assert.assertArrayEquals(
				new String[]{"id"},
				EntityUtil.getIdFieldNames(Entity1.class));
	}

	@Test
	public void test_getIdFieldNames_2() throws Throwable{
		Assert.assertArrayEquals(
				new String[]{"id1", "id2"},
				EntityUtil.getIdFieldNames(Entity2.class));
	}

	@Test
	public void test_getId_1() throws Throwable{
		Assert.assertEquals(
				"hello",
				EntityUtil.getId(Entity1.class, new String[]{"hello"})
				);
	}

	@Test
	public void test_getId_2() throws Throwable{
		Assert.assertEquals(
				new Entity2PK("hello", "world"),
				EntityUtil.getId(Entity2.class, new Object[]{"hello", "world"})
				);
	}
}
