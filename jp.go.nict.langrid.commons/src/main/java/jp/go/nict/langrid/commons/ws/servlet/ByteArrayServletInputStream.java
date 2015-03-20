package jp.go.nict.langrid.commons.ws.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

public class ByteArrayServletInputStream extends ServletInputStream {
	public ByteArrayServletInputStream(byte[] bytes){
		this.is = new ByteArrayInputStream(bytes);
	}

	@Override
	public int read() throws IOException {
		return is.read();
	}

	private InputStream is;
}
