/*
 * $Id: ServiceManagerException.java 406 2011-08-25 02:12:29Z t-nakaguchi $
 * 
 * This is a program for Language Grid Core Node. This combines multiple language resources and
 * provides composite language services. Copyright (C) 2005-2008 NICT Language Grid Project.
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.management.web.model.exception;

import java.util.Calendar;
import java.util.Date;


/**
 * Service managerの例外クラス
 * @author $Author: t-nakaguchi $
 * @version $Revision: 406 $
 */
public class ServiceManagerException extends Exception{
	/**
	 * 
	 * 
	 */
	public ServiceManagerException(Exception e){
		this(e, null, "");
	}

	/**
	 * 
	 * 
	 */
	public ServiceManagerException(Exception e, Class errorClass){
		this(e, errorClass, "");
	}

	/**
	 * 
	 * 
	 */
	public ServiceManagerException(
			Exception e, Class errorClass, String message, String... params)
	{
		super((Exception)e);
		this.e = e;
		this.errorClass = errorClass == null ? e.getClass() : errorClass;
		StringBuffer sb = new StringBuffer();
		if(!message.equals("")){
			sb.append("\"");
			sb.append(message);
			sb.append("\" ");
		}
		if(params.length != 0){
			sb.append(" params[");
			for(String param : params){
				sb.append(param);
				sb.append(",");
			}
			sb.append("]");
		}
		this.params = sb.toString();
		this.message = message;
		this.errorCode = makeErrorCode(e);
	}
	
	/**
	 * 
	 * 
	 */
	public ServiceManagerException(String message){
		super(message);
		errorCode = "ES000";
		this.message = message;
	}

	/**
	 * 
	 * 
	 */
	public Class getErrorClass(){
		return errorClass;
	}
	
	/**
	 * 
	 * 
	 */
	public String getErrorCode(){
		return errorCode;
	}
	
	/**
	 * 
	 * 
	 */
	public String getMessage(){
		return isMask ? "" : message;
	}
	
	/**
	 * 
	 * 
	 */
	public String getNoMaskMessage(){
		return message;
	}
	
	/**
	 * 
	 * 
	 */
	public Exception getParentException(){
		return e;
	}
	
	/**
	 * 
	 * 
	 */
	public Date getRaisedDate(){
		return raisedDate.getTime();
	}
	
	/**
	 * 
	 * 
	 */
	public void setMaskMessage(boolean isMask){
		this.isMask = isMask;
	}
	
	protected String makeErrorCode(Exception e){
         return "ES000";
	}

	protected Exception e;
	protected Class errorClass;
	protected String message;
	protected String params;
	private String errorCode;
	private boolean isMask = false;
	private Calendar raisedDate = Calendar.getInstance();
   private static final long serialVersionUID = 1L;
}
