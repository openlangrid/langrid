package jp.go.nict.langrid.commons.ws.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;

public class AlternateOutputHttpServletResponseWrapper extends HttpServletResponseWrapper{
	public AlternateOutputHttpServletResponseWrapper(
			HttpServletResponse response, ServletOutputStream alternateOutput){
		super(response);
		this.sos = alternateOutput;
	}

	public int getStatus(){
		return status;
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
	private ServletOutputStream sos;
	private PrintWriter writer;
}
