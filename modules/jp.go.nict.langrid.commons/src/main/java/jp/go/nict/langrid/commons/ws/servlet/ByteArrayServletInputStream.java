package jp.go.nict.langrid.commons.ws.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class ByteArrayServletInputStream extends ServletInputStream {
	public ByteArrayServletInputStream(byte[] bytes){
		this.is = new ByteArrayInputStream(bytes);
	}

	@Override
	public int read() throws IOException {
		int ret = is.read();
		finished = ret == -1;
		return ret;
	}

	private InputStream is;

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		this.readListener = readListener;
		try {
			this.readListener.onDataAvailable();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	private ReadListener readListener;
	private boolean finished;
}
