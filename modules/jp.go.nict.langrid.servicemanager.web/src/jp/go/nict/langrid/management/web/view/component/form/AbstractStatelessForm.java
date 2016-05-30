package jp.go.nict.langrid.management.web.view.component.form;

import java.io.Serializable;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.BasePage;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;

import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.markup.html.form.StatelessForm;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 303 $
 */
public abstract class AbstractStatelessForm<T extends Serializable>
extends StatelessForm<T>
{
	/**
	 * 
	 * 
	 */
	public AbstractStatelessForm(String id, T... params){
		super(id);
		if(params != null && params.length != 0){
			addComponents(params[0]);
		}else{
			addComponents(null);
		}
	}
	
	/**
	 * 
	 * 
	 */
	public T getFormModelObject(){
		return getModelObject();
	}
	
	protected abstract void addComponents(T initialParameter);
	
	@Override
	protected void delegateSubmit(IFormSubmittingComponent submittingComponent){
		try{
			raisedException = null;
			if(submittingComponent == null){
				onSubmit();
			}else{
				submittingComponent.onSubmit();
			}
			if(!isCancel){
				if(isValidateError){
					error(errorMessage);
					isValidateError = false;
				}else{
					raiseException();
					resultParameter = getResultParameter();
					if(resultParameter == null){
						resultParameter = getModelObject();
					}
					setResultPage(resultParameter);
				}
			}
			isCancel = false;
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}
	}
	
	protected void doErrorProcess(ServiceManagerException e){
		((BasePage)getPage()).doErrorProcess(e, "'" + getLogMessage() +"'");
	}

	protected String getLogMessage(){
		return "";
	}

	protected T getResultParameter(){
		return null;
	}
	
	protected String getSessionPassword(){
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		return session.getPassword();
	}
	
	protected String getSessionUserId(){
		ServiceManagerSession session = (ServiceManagerSession)getSession();
		return session.getUserId();
	}
	
	protected void raiseException() throws ServiceManagerException{
		if(raisedException != null){
			throw raisedException;
		}
	}

	protected abstract void setResultPage(T resultParameter);
	
	protected String errorMessage = "";
	protected boolean isCancel = false;
	protected boolean isValidateError = false;
	protected ServiceManagerException raisedException;
	protected T resultParameter;
	private static final long serialVersionUID = 1L;
}
