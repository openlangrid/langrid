package jp.go.nict.langrid.commons.cs.binding;

public class Condition {
	public Condition() {
	}
	public Condition(String param, String op, String value) {
		this.param = param;
		this.op = op;
		this.value = value;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	private String param;
	private String op;
	private String value;
}
