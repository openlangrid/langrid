package jp.go.nict.langrid.servicesupervisor.responder;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.dao.GridNotFoundException;
import jp.go.nict.langrid.dao.NodeNotFoundException;
import jp.go.nict.langrid.dao.UserNotFoundException;
import jp.go.nict.langrid.servicesupervisor.ServiceNotFoundException;
import jp.go.nict.langrid.servicesupervisor.ServiceNotActiveException;
import jp.go.nict.langrid.servicesupervisor.frontend.AccessLimitExceededException;
import jp.go.nict.langrid.servicesupervisor.frontend.NoAccessPermissionException;

public abstract class FaultResponder {
	public abstract void setResponse(HttpServletResponse response);
	public final void respond(Throwable e) throws IOException{
		if(e instanceof AccessLimitExceededException){
			doRespond((AccessLimitExceededException)e);
		} else if(e instanceof NoAccessPermissionException){
			doRespond((NoAccessPermissionException)e);
		} else if(e instanceof ServiceNotActiveException){
			doRespond((ServiceNotActiveException)e);
		} else if(e instanceof ServiceNotFoundException){
			doRespond((ServiceNotFoundException)e);
		} else if(e instanceof jp.go.nict.langrid.dao.ServiceNotFoundException){
			doRespond((jp.go.nict.langrid.dao.ServiceNotFoundException)e);
		} else if(e instanceof UserNotFoundException){
			doRespond((UserNotFoundException)e);
		} else if(e instanceof GridNotFoundException){
			doRespond((GridNotFoundException)e);
		} else if(e instanceof NodeNotFoundException){
			doRespond((NodeNotFoundException)e);
		} else{
			fallback(e);
		}
	}

	protected void doRespond(AccessLimitExceededException e) throws IOException{
		fallback(e);
	}
	protected void doRespond(NoAccessPermissionException e) throws IOException{
		fallback(e);
	}
	protected void doRespond(ServiceNotActiveException e) throws IOException{
		fallback(e);
	}
	protected void doRespond(ServiceNotFoundException e) throws IOException{
		fallback(e);
	}
	protected void doRespond(jp.go.nict.langrid.dao.ServiceNotFoundException e) throws IOException{
		fallback(e);
	}
	protected void doRespond(UserNotFoundException e) throws IOException{
		fallback(e);
	}
	protected void doRespond(GridNotFoundException e) throws IOException{
		fallback(e);
	}
	protected void doRespond(NodeNotFoundException e) throws IOException{
		fallback(e);
	}
	protected abstract void doRespond(Throwable e) throws IOException;

	private void fallback(Throwable e) throws IOException{
		logger.log(Level.SEVERE, "unexpected exception occurred in invoke process.", e);
		doRespond(e);
	}

	private static Logger logger = Logger.getLogger(FaultResponder.class.getName());
}
