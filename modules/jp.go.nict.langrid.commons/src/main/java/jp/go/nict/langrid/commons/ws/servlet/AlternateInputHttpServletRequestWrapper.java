package jp.go.nict.langrid.commons.ws.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import jp.go.nict.langrid.commons.nio.charset.CharsetUtil;

public class AlternateInputHttpServletRequestWrapper extends HttpServletRequestWrapper{
	public AlternateInputHttpServletRequestWrapper(
			HttpServletRequest request, ServletInputStream alternateInput){
		super(request);
		this.sis = alternateInput;
	}

	@Override
	public ServletInputStream getInputStream()
			throws IOException {
		return sis;
	}

	@Override
	public synchronized BufferedReader getReader() throws IOException {
		if(reader == null){
			reader = new BufferedReader(new InputStreamReader(
					getInputStream(), getCharset().newDecoder()
					));
		}
		return reader;
	}

	private Charset getCharset(){
		String enc = getRequest().getCharacterEncoding();
		if(enc == null) return CharsetUtil.UTF_8;
		try{
			return Charset.forName(enc);
		} catch(UnsupportedCharsetException e){
			return CharsetUtil.UTF_8;
		}
	}

	private ServletInputStream sis;
	private BufferedReader reader;
}
