package jp.go.nict.langrid.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.go.nict.langrid.commons.io.StreamUtil;
import jp.go.nict.langrid.commons.net.URLUtil;
import jp.go.nict.langrid.commons.parameter.ParameterContext;
import jp.go.nict.langrid.commons.util.Pair;
import jp.go.nict.langrid.commons.ws.param.ServletConfigParameterContext;

public class ReverseProxyServlet extends HttpServlet{
	@Override
	public void init(ServletConfig config) throws ServletException {
		ParameterContext pc = new ServletConfigParameterContext(config);
		mappings = pc.getWithTransformer("mappings", s -> {
			List<Pair<String, String>> ret = new ArrayList<>();
			String[] v = s.split(",");
			for(int i = 0; i < (v.length / 2); i++){
				try {
					ret.add(Pair.create(
							URLDecoder.decode(v[i * 2].trim(), "UTF-8"),
							URLDecoder.decode(v[i * 2 + 1].trim(), "UTF-8")
							));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return ret;
		});
		additionalHeaders = pc.getWithTransformer("additionalHeaders", s -> {
			List<Pair<String, String>> ret = new ArrayList<>();
			String[] v = s.split(",");
			for(int i = 0; i < v.length; i++){
				String[] v2 = v[i].split(":");
				ret.add(Pair.create(v2[0].trim(), v2[1].trim()));
			}
			return ret;
		});
		passRemoteUser = pc.getBoolean("passRemoteUser", false);
		super.init(config);
	}

	protected String getRemoteUserName(HttpServletRequest req){
		return req.getRemoteUser();
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//		String requestline = req.getMethod() + " " + req.getRequestURI();
		String local = "/" + URLUtil.getLocalUriString(req.getRequestURI());
		for(Pair<String, String> map : mappings){
			if(!local.startsWith(map.getFirst())) continue;
			String url = map.getSecond() + local.substring(map.getFirst().length());
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
			try{
				if(passRemoteUser){
					String ru = getRemoteUserName(req);
					System.out.println(ru);
					con.setRequestProperty("REMOTE_USER", ru);
				}
				// copy request header
				Enumeration<?> hns = req.getHeaderNames();
				while(hns.hasMoreElements()){
					String name = hns.nextElement().toString();
					String value = req.getHeader(name);
					con.addRequestProperty(name, value);
				}
				for(Pair<String, String> h : additionalHeaders){
					con.addRequestProperty(h.getFirst(), h.getSecond());
				}
				if(req.getMethod().equalsIgnoreCase("POST")){
					con.setDoOutput(true);
					// transfer
					StreamUtil.transfer(req.getInputStream(), con.getOutputStream());
/*				try{
						es.execute(Functions.soften(() -> {
							StreamUtil.transfer(req.getInputStream(), con.getOutputStream());
						}));
					} catch(SoftenedException e){
						e.printStackTrace();
					}
*/
				}
				// copy response header
				int rc = con.getResponseCode();
				res.setStatus(rc);
				for(Map.Entry<String, List<String>> entry : con.getHeaderFields().entrySet()){
					String h = entry.getKey();
					if(h == null) continue;
					for(String v : entry.getValue()){
						if(h.equalsIgnoreCase("Content-Length")) continue;
						if(h.equalsIgnoreCase("Transfer-Encoding")) continue;
						res.addHeader(h, v);
					}
				}
				// transfer
				if(200 <= rc && rc < 400){
					StreamUtil.transfer(con.getInputStream(), res.getOutputStream());
				} else{
					StreamUtil.transfer(con.getErrorStream(), res.getOutputStream());
				}
			} catch(FileNotFoundException e){
				res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} catch(IOException e){
				
			} finally{
//				System.out.println(requestline + "  ->  " + con.getResponseCode() + " " + con.getResponseMessage());
				con.disconnect();
			}
			return;
		}
		super.service(req, res);
	}

	private List<Pair<String, String>> mappings = new ArrayList<>();
	private List<Pair<String, String>> additionalHeaders = new ArrayList<>();
	private boolean passRemoteUser;
	private static final long serialVersionUID = -3586649747617765152L;
}
