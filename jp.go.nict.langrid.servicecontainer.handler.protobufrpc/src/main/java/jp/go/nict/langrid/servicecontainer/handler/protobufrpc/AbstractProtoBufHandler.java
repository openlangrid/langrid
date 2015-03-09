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
package jp.go.nict.langrid.servicecontainer.handler.protobufrpc;

import jp.go.nict.langrid.commons.rpc.RpcFault;
import jp.go.nict.langrid.commons.rpc.RpcFaultUtil;

/**
 * 
 * 
 * @author Takao Nakaguchi
 */
public abstract class AbstractProtoBufHandler{
/*
	protected Fault createFault(Throwable exception){
		if(exception instanceof InvocationTargetException){
			exception = ((InvocationTargetException)exception).getCause();
		}
		Fault.Builder b = Fault.newBuilder();
		if(exception instanceof InvalidParameterException){
			b.setFaultCode("parameterException");
		} else{
			b.setFaultCode("Server.userException");
		}
		return b.setFaultString(exception.toString())
				.setFaultDetail(ExceptionUtil.getMessageWithStackTrace(exception))
				.build();
	}
*/
	protected RpcFault createRpcFault(Throwable exception){
		return RpcFaultUtil.throwableToRpcFault("Server.userException", exception);
/*
		if(exception instanceof InvocationTargetException){
			exception = ((InvocationTargetException)exception).getCause();
		}
		RpcFault f = new RpcFault();
		if(exception instanceof InvalidParameterException){
			f.setFaultCode("parameterException");
		} else{
			f.setFaultCode("Server.userException");
			if(!(exception instanceof LangridException)){
				exception = new ProcessFailedException(exception);
			}
		}
		f.setFaultString(exception.toString());
		f.setDetail(ExceptionUtil.getMessageWithStackTrace(exception));
		return f;
*/
	}
}
