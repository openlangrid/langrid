package jp.go.nict.langrid.management.logic;


public class ServiceNotInactiveException extends ServiceLogicException{
	public ServiceNotInactiveException(String serviceGridId, String serviceId, String message){
		super(serviceGridId, serviceId, message);
	}

	private static final long serialVersionUID = -5636071122209588829L;
}
