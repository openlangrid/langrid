package jp.go.nict.langrid.commons.rpc.intf;

public class FileContent {
	public FileContent() {
	}
	public FileContent(String filename, String mimeType, byte[] body) {
		this.filename = filename;
		this.mimeType = mimeType;
		this.body = body;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}


	private String filename;
	private String mimeType;
	private byte[] body;
}
