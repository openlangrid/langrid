package jp.go.nict.langrid.commons.ws.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;

public class ByteArrayOutputHttpServletResponseWrapper extends HttpServletResponseWrapper{
	public ByteArrayOutputHttpServletResponseWrapper(
			HttpServletResponse response){
		super(response);
	}

	public int getStatus(){
		return status;
	}

	public byte[] getOutput(){
		return dup.toByteArray();
	}

	@Override
	public void setStatus(int sc) {
		super.setStatus(sc);
		status = sc;
	}

	@Override
	public ServletOutputStream getOutputStream()
			throws IOException {
		return sos;
	}

	@Override
	public synchronized PrintWriter getWriter() throws IOException {
		if(writer == null){
			writer = new PrintWriter(new OutputStreamWriter(
					getOutputStream(), getCharset().newEncoder()
					));
		}
		return writer;
	}

	private Charset getCharset(){
		String enc = getResponse().getCharacterEncoding();
		if(enc == null) return CharsetUtil.UTF_8;
		try{
			return Charset.forName(enc);
		} catch(UnsupportedCharsetException e){
			return CharsetUtil.UTF_8;
		}
	}

	private int status = 200;
	private ByteArrayOutputStream dup = new ByteArrayOutputStream();
	private ServletOutputStream sos = new OutputStreamServletOutputStream(dup);
	private PrintWriter writer;
}
