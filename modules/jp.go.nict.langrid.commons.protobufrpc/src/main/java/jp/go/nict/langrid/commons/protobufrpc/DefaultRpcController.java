/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) 2010 NICT Language Grid Project.
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation, either version 2.1 of the License, or (at 
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package jp.go.nict.langrid.commons.protobufrpc;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

/**
 * Default implementation of RpcController.
 * @author Takao Nakaguchi
 */
public class DefaultRpcController implements RpcController {
	@Override
	public String errorText(){
		return failedMessage;
	}

	/**
	 * Returns the exception that caused operation failed.
	 * @return exception
	 */
	public Throwable errorException(){
		return exception;
	}

	@Override
	public boolean failed() {
		return failedMessage != null || exception != null;
	}

	@Override
	public boolean isCanceled() {
		return false;
	}

	@Override
	public void notifyOnCancel(RpcCallback<Object> callback) {
	}

	@Override
	public void reset() {
	}

	@Override
	public void setFailed(String reason) {
		failedMessage = reason;
	}

	/**
	 * Sets the exception. Then filed() will return true;
	 * @param exception
	 */
	public void setException(Throwable exception){
		this.exception = exception;
	}

	@Override
	public void startCancel() {
		
	}
	
	private String failedMessage;
	private Throwable exception;
}
