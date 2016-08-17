package jp.go.nict.langrid.service_1_3.foundation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NewerAndOlderKeys {
	public NewerAndOlderKeys() {
	}
	public NewerAndOlderKeys(Set<Object> newerKeys, Set<Object> olderKeys) {
		this.newerKeys = newerKeys;
		this.olderKeys = olderKeys;
	}
	public NewerAndOlderKeys(Object[] newerIds, Object[] olderIds) {
		this.newerKeys = new HashSet<>(Arrays.asList(newerIds));
		this.olderKeys = new HashSet<>(Arrays.asList(olderIds));
	}
	public Set<Object> getNewerKeys() {
		return newerKeys;
	}
	public void setNewerKeys(Set<Object> newerKeys) {
		this.newerKeys = newerKeys;
	}
	public Set<Object> getOlderKeys() {
		return olderKeys;
	}
	public void setOlderKeys(Set<Object> olderKeys) {
		this.olderKeys = olderKeys;
	}

	private Set<Object> newerKeys;;
	private Set<Object> olderKeys;
}
