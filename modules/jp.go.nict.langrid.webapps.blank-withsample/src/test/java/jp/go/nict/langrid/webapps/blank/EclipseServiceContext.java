package jp.go.nict.langrid.webapps.blank;

import jp.go.nict.langrid.commons.ws.LocalServiceContext;

public class EclipseServiceContext extends LocalServiceContext{
	@Override
	public String getRealPath(String path) {
		return "WebContent/" + path;
	}
}
