package Mypackage;

import java.util.*;

public class Request {

	private String requestType;
	private String url;
	private String RequestedContentType;
	private String httpProtocol;
	private HashMap<String,String> headers;
	private byte[] body;
	
	public Request(String requestType, String url, String httpVersion) {
		this.requestType = requestType;
		this.url = url;
		this.httpProtocol = httpVersion;
	}
	
	public Request(String requestType,String url,String httpProtocol, HashMap<String, String> headers) {
		this(requestType,url,httpProtocol);
		this.headers = headers;
	}
	
	public Request(String requestType,String url, String httpVersion, HashMap<String,String> headers, byte[] body) {
		this(requestType,url,httpVersion, headers);
		this.body = body;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHttpProtocol() {
		return httpProtocol;
	}

	public void setHttpProtocol(String httpVersion) {
		this.httpProtocol = httpProtocol;
	}

	public HashMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
	
	public void setRequestedContentType(String str) {
		this.RequestedContentType = str;
	}
	
	public String getRequestedContentType() {
		return this.RequestedContentType;
	}
	
}
