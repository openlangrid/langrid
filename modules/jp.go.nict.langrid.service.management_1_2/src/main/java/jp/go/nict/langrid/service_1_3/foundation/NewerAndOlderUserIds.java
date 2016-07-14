package jp.go.nict.langrid.service_1_3.foundation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NewerAndOlderUserIds {
	public NewerAndOlderUserIds() {
	}
	public NewerAndOlderUserIds(Set<String> newerIds, Set<String> olderIds) {
		this.newerIds = newerIds;
		this.olderIds = olderIds;
	}
	public NewerAndOlderUserIds(String[] newerIds, String[] olderIds) {
		this.newerIds = new HashSet<>(Arrays.asList(newerIds));
		this.olderIds = new HashSet<>(Arrays.asList(olderIds));
	}
	public Set<String> getNewerIds() {
		return newerIds;
	}
	public void setNewerIds(Set<String> newerIds) {
		this.newerIds = newerIds;
	}
	public Set<String> getOlderIds() {
		return olderIds;
	}
	public void setOlderIds(Set<String> olderIds) {
		this.olderIds = olderIds;
	}

	private Set<String> newerIds;;
	private Set<String> olderIds;
}
