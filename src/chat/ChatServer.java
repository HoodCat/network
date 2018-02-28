package chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		InetSocketAddress serverAddress = new InetSocketAddress("localhost", 7777);
		
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(serverAddress);
			consoleLog(serverAddress.getAddress() + "에 바인딩 되었습니다.");
			while( true ) {
				Socket socket = serverSocket.accept();
				new ChatServerProcessThread(socket).start();
			}
		} catch(IOException e) {
			
		} finally {
			try {
				if(serverSocket != null || !serverSocket.isClosed()) {
					serverSocket.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void consoleLog(String message) {
		System.out.println("[server: " + message + "]");
	}
}