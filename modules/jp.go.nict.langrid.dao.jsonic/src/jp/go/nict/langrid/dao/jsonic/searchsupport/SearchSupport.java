package jp.go.nict.langrid.dao.jsonic.searchsupport;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.dao.MatchingCondition;
import jp.go.nict.langrid.dao.Order;
import jp.go.nict.langrid.dao.OrderDirection;

public abstract class SearchSupport<T> {
	private MatchingCondition[] conditions;
	private Order[] orders;

	SearchSupport(MatchingCondition[] conditions, Order[] orders) {
		this.conditions = conditions != null ? conditions : new MatchingCondition[] {};
		this.orders = orders != null ? orders : new Order[] {};
	}

	@SuppressWarnings("unchecked")
	public final Pair<T[], Integer> filter(T[] target, int from, int count) {
		if (target.length < 1) {
			return new Pair<T[], Integer>(emptyArray(), 0);
		}

		Set<T> out = new TreeSet<T>(getComparator(orders));
		for (T obj : target) {
			boolean f = true;
			for (MatchingCondition cond : this.conditions) {
				if (! match(cond, obj)) {
					f = false;
					break;
				}
			}
			if (f) {
				out.add(obj);
			}
		}

		int total = out.size();
		int partSize = Math.min(total - from, count);
		if (partSize < 1 || from < 0) {
			return new Pair<T[], Integer>(emptyArray(), total);
		}

		T[] ar = (T[]) Array.newInstance(getComponentType(), partSize);
		Iterator<T> it = out.iterator();
		for (int i = 0; i < from; i++) {
			it.next();
		}
		for (int i = 0; i < partSize; i++) {
			ar[i] = it.next();
		}

		return new Pair<T[], Integer>(ar, total);
	}

	@SuppressWarnings("unchecked")
	protected T[] emptyArray() {
		return (T[]) Array.newInstance(getComponentType(), 0);
	}

	protected Boolean match(MatchingCondition cond, T obj) {
		return getDefinition(cond.getFieldName()).match(cond, obj);
	}

	protected Comparator<T> getComparator(final Order[] orders) {
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				for (Order ord: orders) {
					int con = getDefinition(ord.getFieldName()).compare(o1, o2);
					if (con != 0) {
						return con * (ord.getDirection() == OrderDirection.ASCENDANT ? 1 : -1);
					}
				}
				return o1.hashCode() - o2.hashCode();
			}
		};
	}

	protected abstract Class<T> getComponentType();

	protected abstract FieldDefs<T> getDefinition(String fieldName);

	protected interface FieldDefs<T> {
		boolean match(MatchingCondition cond, T obj);
		int compare(T left, T right);
	}
}
