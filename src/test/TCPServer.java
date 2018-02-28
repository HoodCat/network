package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
	private static final int SERVER_PORT = 5000;
	public static void main(String[] args) {
		ExecutorService pool = Executors
				.newFixedThreadPool(
						Runtime.
						getRuntime().
						availableProcessors());
		
		ServerSocket serverSocket = null;
		try {
			// 1. 서버 소켓 생성
			serverSocket = new ServerSocket();
			
			// 2. 바인딩(Binding)
			String localhostAddress = InetAddress
					.getLocalHost()
					.getHostAddress();
			
			serverSocket.bind(
					new InetSocketAddress(localhostAddress, SERVER_PORT));
			System.out.println("[server] binding " + localhostAddress + ":"
					+ SERVER_PORT);
			
			// 3. 연결 요청 기다림(accept)
			Socket socket = serverSocket.accept();	// blocking
			
			// 4. 연결 성공
			InetSocketAddress remoteSocketAddress =
					(InetSocketAddress)socket.getRemoteSocketAddress();
			int remoteHostPort = remoteSocketAddress.getPort();
			String remoteHostAddress = remoteSocketAddress.
					getAddress().
					getHostAddress();
			
			System.out.println(
					String.format("[server] connected from %s:%d",
							remoteHostAddress, remoteHostPort));
			try {
				// 5. I/OStream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				
				while(true) {
					// 6. 데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); //Blocking
					
	
					if(readByteCount <= -1) {	// 정상 종료
						System.out.println("[server] disconnected by client");
						break;
					}
					
					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[server] received: " + data);
					
					// 7. 데이터 쓰기
					os.write(data.getBytes());
				}
			} catch(SocketException e) {
				// 상대편이 정상적으로 소켓을 닫지 않고 종료한 경우
				System.out.println("[server] sudden closed by client");
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				if( socket != null && !socket.isClosed()) {
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(serverSocket != null && !serverSocket.isClosed()) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
