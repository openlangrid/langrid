package jp.go.nict.langrid.service_1_3.foundation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NewerAndOlderKeys {
	public NewerAndOlderKeys() {
	}
	public NewerAndOlderKeys(Set<Serializable> newerKeys, Set<Serializable> olderKeys) {
		this.newerKeys = newerKeys;
		this.olderKeys = olderKeys;
	}
	public NewerAndOlderKeys(Serializable[] newerIds, Serializable[] olderIds) {
		this.newerKeys = new HashSet<>(Arrays.asList(newerIds));
		this.olderKeys = new HashSet<>(Arrays.asList(olderIds));
	}
	public Set<Serializable> getNewerKeys() {
		return newerKeys;
	}
	public void setNewerKeys(Set<Serializable> newerKeys) {
		this.newerKeys = newerKeys;
	}
	public Set<Serializable> getOlderKeys() {
		return olderKeys;
	}
	public void setOlderKeys(Set<Serializable> olderKeys) {
		this.olderKeys = olderKeys;
	}

	private Set<Serializable> newerKeys;;
	private Set<Serializable> olderKeys;
}
