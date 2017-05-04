package jp.go.nict.langrid.commons.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import jp.go.nict.langrid.commons.util.function.Consumer;

public class CollectionFixture<T> {
	public CollectionFixture(Collection<T> collection){
		this.collection = collection;
		this.it = collection.iterator();
	}

	public CollectionFixture<T> assertHasNext(){
		assertTrue(it.hasNext());
		return this;
	}

	public CollectionFixture<T> assertNotHasNext(){
		assertFalse(it.hasNext());
		return this;
	}

	public CollectionFixture<T> assertSize(int expected){
		assertEquals(expected, collection.size());
		return this;
	}

	public CollectionFixture<T> assertNextEquals(T expected){
		assertEquals(expected, it.next());
		return this;
	}

	@SuppressWarnings("unchecked")
	public <U extends T> CollectionFixture<T> next(Consumer<U> c){
		c.accept((U)it.next());
		return this;
	}

	private Collection<T> collection;
	private Iterator<T> it;
}
