package jp.go.nict.langrid.service_1_2.foundation.servicemanagement;

public class QoS {
	public QoS() {
	}
	public QoS(String qosType, long denominator, double value) {
		this.qosType = qosType;
		this.denominator = denominator;
		this.value = value;
	}
	public String getQosType() {
		return qosType;
	}
	public void setQosType(String qosType) {
		this.qosType = qosType;
	}
	public long getDenominator() {
		return denominator;
	}
	public void setDenominator(long denominator) {
		this.denominator = denominator;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

	private String qosType;
	private long denominator;
	private double value;
}
