package jp.go.nict.langrid.management.logic;

public class QoSResult {
	public QoSResult() {
	}
	public QoSResult(QoSType type, int denominator, double value) {
		this.type = type;
		this.denominator = denominator;
		this.value = value;
	}

	public QoSType getType() {
		return type;
	}
	public void setType(QoSType type) {
		this.type = type;
	}
	public int getDenominator() {
		return denominator;
	}
	public void setDenominator(int denominator) {
		this.denominator = denominator;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}

	private QoSType type;
	private int denominator;
	private double value;
}
