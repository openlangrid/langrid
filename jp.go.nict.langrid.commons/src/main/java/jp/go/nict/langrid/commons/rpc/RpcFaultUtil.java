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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.go.nict.langrid.commons.lang.ExceptionUtil;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSONException;
import jp.go.nict.langrid.repackaged.net.arnx.jsonic.util.PropertyInfo;

public class RpcFaultUtil {
	public static Exception rpcFaultToThrowable(RpcFault fault){
		String[] v = fault.getFaultString().split(":", 2);
		try {
			Class<?> clazz = Class.forName(v[0]);
			return (Exception)JSON.decode(v[1], clazz);
		} catch (ClassCastException e) {
			return (Exception)new RuntimeException(JSON.encode(fault));
		} catch (ClassNotFoundException e) {
			return (Exception)new RuntimeException(JSON.encode(fault));
		} catch(JSONException e){
			return (Exception)new RuntimeException(v[1]);
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
		public Appendable format(Context context, Object source, Appendable ap) throws java.io.IOException {
			super.format(new JSON.Context(){
				@Override
				protected List<PropertyInfo> getGetProperties(Class<?> c) {
					List<PropertyInfo> props = super.getGetProperties(c);
					if(Exception.class.isAssignableFrom(c)){
						Iterator<PropertyInfo> it = props.iterator();
						while(it.hasNext()){
							if(ignores.contains(it.next().getName())) it.remove();
						}
					}
					return props;
				}
			}, source, ap);
			return ap;
		};
	};

	private static Set<String> ignores = new HashSet<String>();
	static{
		ignores.add("cause");
		ignores.add("stackTrace");
		ignores.add("localizedMessage");
		ignores.add("message");
	}
}
