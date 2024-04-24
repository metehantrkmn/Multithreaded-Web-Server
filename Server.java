package Mypackage;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

	private Socket sc = null;
	private ServerSocket server = null;
	private ExecutorService pool = null;
	private InputStreamReader reader = null;
	//parserandbuilder static metodlar
	
	public Server(int port) {
		
		try {
			server = new ServerSocket(80);
			pool = Executors.newCachedThreadPool();
		}catch(Exception ex) {
			System.out.println("port is not available or thread pool may not be initialized!!!");
		}
		
		while(true) {
			try {
				System.out.println("listening for connections");
				sc = server.accept();
				pool.execute(new ClientHandler(sc));
			}catch(IOException exe) {
				System.out.println("server couldnt accept client!!!!");
			}catch(Exception general) {
				System.out.println("coultn conneted to client and create a socket");
			}
		}
		
		
	}
	
	public static void main(String[] args) {
		Server server = new Server(80);
	}
	
}
