package jp.go.nict.langrid.service_1_3.foundation;

import java.util.Calendar;

public class UpdateManagedEntry {
	public UpdateManagedEntry() {
	}
	public UpdateManagedEntry(Calendar createdDateTime, Calendar updatedDateTime) {
		this.createdDateTime = createdDateTime;
		this.updatedDateTime = updatedDateTime;
	}

	public Calendar getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Calendar createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public Calendar getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(Calendar updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	private Calendar createdDateTime;
	private Calendar updatedDateTime;

}
