package jp.go.nict.langrid.service_1_3.foundation;

import java.util.Calendar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class KeyAndUpdate {
	public KeyAndUpdate() {
	}
	public KeyAndUpdate(Object key, Calendar update) {
		this.key = key;
		this.update = update;
	}
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public Object getKey() {
		return key;
	}
	public void setKey(Object key) {
		this.key = key;
	}
	public Calendar getUpdate() {
		return update;
	}
	public void setUpdate(Calendar update) {
		this.update = update;
	}

	private Object key;
	private Calendar update;
}
