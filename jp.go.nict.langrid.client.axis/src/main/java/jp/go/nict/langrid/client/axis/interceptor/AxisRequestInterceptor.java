package jp.go.nict.langrid.client.axis.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.Socket;
import java.util.concurrent.Callable;

import jp.go.nict.langrid.commons.io.NullOutputStream;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.components.net.BooleanHolder;
import org.apache.axis.transport.http.HTTPSender;
import org.apache.axis.transport.http.SocketHolder;

public class AxisRequestInterceptor extends HTTPSender{
	public static <T> void invoke(OutputStream os, Callable<T> c){
		try{
			AxisRequestInterceptor.setOutputStreamForCurrentThread(os);
			c.call();
		} catch(UndeclaredThrowableException e){
			if(e.getUndeclaredThrowable() instanceof AxisFault){
				AxisFault af = (AxisFault)e.getUndeclaredThrowable();
				if(af.getCause() instanceof ProcessTakenRuntimeException){
					return;
				}
			}
			throw e;
		} catch(Exception e){
			throw new RuntimeException(e);
		} finally{
			AxisRequestInterceptor.removeOutputStreamForCurrentThread();
		}
	}

	@Override
	protected void getSocket(SocketHolder sockHolder,
			MessageContext msgContext, String protocol, String host,
			int port, int timeout, StringBuffer otherHeaders,
			BooleanHolder useFullURL) throws Exception
	{
		sockHolder.setSocket(new Socket(){
			@Override
			public OutputStream getOutputStream() throws IOException {
				OutputStream os = tlsOs.get();
				if(os != null) return os;
				return new NullOutputStream();
			}
			@Override
			public InputStream getInputStream() throws IOException {
				throw new ProcessTakenRuntimeException();
			}
		});
	}

	public static void setOutputStreamForCurrentThread(OutputStream os){
		tlsOs.set(os);
	}
	public static void removeOutputStreamForCurrentThread(){
		tlsOs.remove();
	}

	private static ThreadLocal<OutputStream> tlsOs = new ThreadLocal<OutputStream>();
	private static final long serialVersionUID = -5101031502381518215L;
}
