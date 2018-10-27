package jp.go.nict.langrid.cosee.binding;

public class NoBindingFoundException extends RuntimeException{
	public NoBindingFoundException(String invocationName){
		this.invocationName = invocationName;
	}

	public String getInvocationName() {
		return invocationName;
	}

	@Override
	public String toString() {
		return "no binding found for " + invocationName;
	}

	private String invocationName;
	private static final long serialVersionUID = -2663863024176287124L;
}
