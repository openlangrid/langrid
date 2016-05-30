package jp.go.nict.langrid.management.web.view.component.form;

import java.io.Serializable;

import jp.go.nict.langrid.management.web.model.exception.ServiceManagerException;
import jp.go.nict.langrid.management.web.view.page.BasePage;
import jp.go.nict.langrid.management.web.view.session.ServiceManagerSession;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;

/**
 * 
 * 
 * @author Masaaki Kamiya
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public abstract class AbstractForm<T extends Serializable> extends Form<T> {
	/**
	 * 
	 * 
	 */
	public AbstractForm(String id, T... params){
		super(id);
		setMultiPart(true);
		try{
			if(params != null && params.length != 0){
				addComponents(params[0]);
			}else{
				addComponents(null);
			}
		}catch(ServiceManagerException e){
			doErrorProcess(e);
		}
	}

	/**
	 * 
	 * 
	 */
	public T getFormModelObject(){
		return getModelObject();
	}

	/**
	 * 
	 * 
	 */
	protected abstract void addComponents(T initialParameter)
	throws ServiceManagerException;
	
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
					T resultParameter = getResultParameter();
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
	
	/**
	 * 
	 * 
	 */
	protected void doErrorProcess(ServiceManagerException e){
		((BasePage)getPage()).doErrorProcess(e, "'" + getLogMessage() +"'");
	}

	/**
	 * 
	 * 
	 */
	protected String getLogMessage(){
		return "";
	}
	
	protected T getResultParameter(){
		return resultParameter;
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
	
	/**
	 * 
	 * 
	 */
	protected void setIsValidateError(boolean isValidateError){
		this.isValidateError = isValidateError;
	}
	
	/**
	 * 
	 * 
	 */
	protected void setValidateErrorMessage(String message){
		errorMessage = message;
	}
	
	/**
	 * 
	 * 
	 */
	protected void setIsCancel(boolean isCancel){
		this.isCancel = isCancel;
	}

	/**
	 * 
	 * 
	 * @throws ServiceManagerException 
	 */
	protected abstract void setResultPage(T resultParameter) throws ServiceManagerException;

   private String errorMessage = "";
	private boolean isCancel = false;
	private boolean isValidateError = false;
	protected ServiceManagerException raisedException;
	protected T resultParameter;

	private static final long serialVersionUID = 1L;
}
