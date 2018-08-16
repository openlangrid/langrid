package jp.go.nict.langrid.client;

public class StandardUserParam {
	public StandardUserParam() {
	}
	public StandardUserParam(LatLng latlng, long callTime, int numberOfServices, int numberOfUsers) {
		this.latlng = latlng;
		this.callTime = callTime;
		this.numberOfServices = numberOfServices;
		this.numberOfUsers = numberOfUsers;
	}
	public LatLng getLatlng() {
		return latlng;
	}
	public void setLatlng(LatLng latlng) {
		this.latlng = latlng;
	}
	public long getCallTime() {
		return callTime;
	}
	public void setCallTime(long callTime) {
		this.callTime = callTime;
	}
	public int getNumberOfServices() {
		return numberOfServices;
	}
	public void setNumberOfServices(int numberOfServices) {
		this.numberOfServices = numberOfServices;
	}
	public int getNumberOfUsers() {
		return numberOfUsers;
	}
	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}


	private LatLng latlng;
	private long callTime;
	private int numberOfServices;
	private int numberOfUsers;
}
