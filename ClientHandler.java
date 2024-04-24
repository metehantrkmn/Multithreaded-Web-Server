package Mypackage;

import java.io.*;
import java.util.*;
import java.net.*;

public class ClientHandler implements Runnable{
	
	private BufferedInputStream in = null;
	private BufferedOutputStream out = null;
	private Socket sc = null;
	private byte[] buffer;
	private int id;
	public static int counter = 0;
	int count = 0;
	private String ThreadName;
	
	public ClientHandler(Socket sc) throws IOException {
		this.sc = sc;
		this.in = new BufferedInputStream(sc.getInputStream());
		this.out = new BufferedOutputStream(sc.getOutputStream());
		this.id = counter;
		counter++;
		Thread.currentThread().setName("Thread " + counter);
		this.ThreadName = Thread.currentThread().getName();
	}
	
	public void run() {
		System.out.println(ThreadName);
		try {
			System.out.println(ThreadName + " connected to client reading the request...");
			//buffer = in.readAllBytes(); 
			//buffer = in.readNBytes(100); solution
			Request request = readStream();
			
			//System.out.println(ThreadName + " read all bytes!!!");
			//System.out.println(ThreadName + " calling for parser");
			//Request request = ParserandBuilder.getPartitions(buffer);
			//System.out.println(request.getRequestLine());
			//String body = new String(request.getBody());
			System.out.println(ThreadName + " writing requuest line");
			System.out.println(request.getRequestType());
			System.out.println(request.getUrl());
			System.out.println(request.getHttpProtocol());
			System.out.println(request.getHeaders().get("Content-Type:"));
			System.out.println(request.getHeaders().get("Content-Length:"));
			System.out.println(new String(request.getBody()));
			byte[]	responseBody =  fileHandler(request.getUrl());
			Response response = new Response("HTTP/1.1 200 OK");
			out.write(response.getResponseLine().getBytes());
			//out.write(request.getHeaders().get("Content-Type:").getBytes());
			//out.write(request.getHeaders().get("Content-Length:").getBytes());
			out.write("\r\n".getBytes());
			System.out.println("before out.write content Type = " + request.getRequestedContentType());
			out.write(("Content-Type: " + request.getRequestedContentType() + "\r\n").getBytes());
			out.write(("Content-Length: " + responseBody.length + "\r\n").getBytes());
			out.write("\r\n".getBytes());
			//out.write(ThreadName.getBytes());
			//out.write("hello from the other side\n".getBytes());
			out.write(responseBody);
			//out.write(request.getBody());
			//out.write("\r\n".getBytes());
			//out.write(response.getBody());
			out.close();
			in.close();
		}catch(FileNotFoundException ex) {
			try {				
				out.write("HTTP/1.1 404 NOTfOUND\r\n\r\n".getBytes());
				System.out.println(ThreadName + " file not found exception");
				ex.printStackTrace();
				out.close();
			}catch(Exception inner) {
				System.out.println("silly");
			}
		}catch (Exception exe) {
			System.out.println(ThreadName + " an exception occured while reading all bytes");
			exe.printStackTrace();			
		}
			
	}
	
	public Request readStream() throws IOException, InterruptedException {
		StringBuilder builder = new StringBuilder();
		Request req ;
		String str = ""; 
		byte[] buffer = new byte[1];
		int contentLength;

		this.in.read(buffer);
		builder.append(new String(buffer));
		str = builder.toString();
		
		/*
		 * aslında bu  thread data alış verişi gerçekleşmesi durumunda favicon dosyasının çekilmesi için bekletilmekte
		System.out.println(this.ThreadName + " " + str.codePointAt(0)); 		//eğer dumy request ise ve bağlantı kurulmasına rağmen istek gönderilmiyorsa null okur
																				//eğer null okunan durumu handle etmezsen thread sonsuza kadar null okumaya devam eder	
		*/
		while(str.endsWith("\r\n\r\n") != true && str.codePointAt(0) != 0) {	
			this.in.read(buffer);
			builder.append(new String(buffer));
			str = builder.toString();
			//System.out.println(this.ThreadName + " " + str + "\n");
			//Thread.sleep(10);
		}
		System.out.println(this.ThreadName + " end of function");
		System.out.println(str);
		
		req = parseRequest(str);
		
		if(req.getRequestType().equals("POST") == true) {
			contentLength = Integer.parseInt(req.getHeaders().get("Content-Length:"));
			buffer = new byte[contentLength];
			in.read(buffer);
		}
		
		req.setBody(buffer);
		
		return req;
	}
	
	public Request parseRequest(String str) {
		String requestType;
		String url;
		String httpProtocol;
		HashMap<String,String> map = new HashMap<String,String>();
		Request request;
		String[] firstLine;		//ilk satırdaki str leri boşluğa göre ayrılmış halini tutar
		String[] lines;
		
		lines = str.split("\r\n");
		firstLine = lines[0].split(" ");
		requestType = firstLine[0];
		url = firstLine[1];
		httpProtocol = firstLine[2];
		
		for(int i = 1; i<lines.length;i++) {
			String[] line = lines[i].split(" ");
			map.put(line[0], line[1]);
		}
		
		request = new Request(requestType,url,httpProtocol,map);
		String[] temp = url.split(".");
		System.out.println(temp.length);
		String contentType = url.split("\\.")[1];
		
		System.out.println("before adding string content type = "+ contentType);
		
		if(contentType.equals("html") == true || contentType.equals("css") == true || contentType.equals("csv") == true || contentType.equals("xml") == true || contentType.equals("plain") == true) {
			contentType = "text/" + contentType;
		}else if(contentType.equals("jpeg") == true || contentType.equals("png") == true ) {
			contentType = "image/" + contentType;
		}else if(contentType.equals("javascript") == true) {
			contentType = "application/" + contentType;
		}else if(contentType.equals("svg") == true || contentType.equals("xml+svg") == true) {
			contentType = "image/svg+xml";
		}
		
		System.out.println("content type = " + contentType);
		
		request.setRequestedContentType(contentType);
		
		return request;
		
	}
	
	public byte[] fileHandler(String url) throws IOException{
		url = url.replace("/", "\\");
		url = "." + url;
		System.out.println(url);
		File file = new File(url);
		FileInputStream in = new FileInputStream(file);
		byte[] array;
		array = in.readAllBytes();
		return array;
	}
	
	
}
