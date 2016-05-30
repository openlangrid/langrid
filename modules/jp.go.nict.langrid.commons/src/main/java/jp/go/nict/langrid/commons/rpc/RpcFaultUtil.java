/*
 * This is a program for Language Grid Core Node. This combines multiple language resources and provides composite language services.
 * Copyright (C) NICT Language Grid Project.
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
package jp.go.nict.langrid.commons.rpc;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONException;

public class RpcFaultUtil {
	public static Exception rpcFaultToThrowable(RpcFault fault){
		String[] v = fault.getFaultString().split(":", 2);
		if(v.length == 1){
			return new RuntimeException(v[0]);
		}
		Class<?> clazz = null;
		try {
			clazz = Class.forName(v[0]);
		} catch (ClassNotFoundException e) {
			return (Exception)new RuntimeException(json.format(fault));
		}
		try{
			Exception e = (Exception)json.parse(v[1], clazz);
			e.fillInStackTrace();
			return e;
		} catch(JSONException e){
		}
		try {
			try{
				Exception e = (Exception)clazz.getConstructor(String.class).newInstance(v[1]);
				e.fillInStackTrace();
				return e;
			} catch(Exception e){
			}
			Exception e = (Exception)clazz.newInstance();
			e.fillInStackTrace();
			return e;
		} catch (IllegalArgumentException e) {
			return (Exception)new RuntimeException(json.format(fault));
		} catch (SecurityException e) {
			return (Exception)new RuntimeException(json.format(fault));
		} catch (InstantiationException e) {
			return (Exception)new RuntimeException(json.format(fault));
		} catch (IllegalAccessException e) {
			return (Exception)new RuntimeException(json.format(fault));
		}
	}

	public static RpcFault throwableToRpcFault(String faultCode, Throwable t){
		RpcFault f = new RpcFault();
		f.setFaultCode(faultCode);
		f.setFaultString(t.getClass().getName() + ":" + json.format(t));
		f.setDetail(ExceptionUtil.getMessageWithStackTrace(t));
		return f;
	}

	private static JSON json = new JSON(){
		protected boolean ignore(Context context, java.lang.Class<?> target, java.lang.reflect.Member member) {
			if (member.getDeclaringClass().equals(Throwable.class)) return true;
			return super.ignore(context, target, member);
		};
	};
}
