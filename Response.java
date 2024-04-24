package Mypackage;

import java.util.*;

public class Response {

	private String responseLine;
	private HashMap<String, String> myMap;
	private byte[] body;
	
	public Response(String responseLine) {
		this.responseLine = responseLine;
	}
	
	public Response(String responseLine, HashMap<String,String> map) {
		this(responseLine);
		this.myMap = map;
	}
	
	public Response(String responseLine, HashMap<String,String> map, byte[] body) {
		this(responseLine, map);
		this.body = body;
	}
	
	public Response(String responseLine, byte[] body) {
		this.responseLine = responseLine;
		this.body = body;
	}

	public String getResponseLine() {
		return responseLine;
	}

	public void setResponseLine(String responseLine) {
		this.responseLine = responseLine;
	}

	public HashMap<String, String> getMyMap() {
		return myMap;
	}

	public void setMyMap(HashMap<String, String> myMap) {
		this.myMap = myMap;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
	
	
	
	
}
