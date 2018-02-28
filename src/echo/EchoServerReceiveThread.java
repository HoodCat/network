package echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServerReceiveThread extends Thread {
	
	private Socket socket;
	
	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// 4. 연결 성공
		InetSocketAddress remoteSocketAddress =
				(InetSocketAddress)socket.getRemoteSocketAddress();
		int remoteHostPort = remoteSocketAddress.getPort();
		String remoteHostAddress = remoteSocketAddress.
				getAddress().
				getHostAddress();
		
		consoleLog(
				String.format("connected from %s:%d",
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
					consoleLog("disconnected by client");
					break;
				}
				
				String data = new String(buffer, 0, readByteCount, "UTF-8");
				consoleLog("received: " + data);
				
				// 7. 데이터 쓰기
				os.write(data.getBytes());
			}
		} catch(SocketException e) {
			// 상대편이 정상적으로 소켓을 닫지 않고 종료한 경우
			consoleLog("sudden closed by client");
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if( socket != null && !socket.isClosed()) {
					socket.close();
				} 
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void consoleLog(String log) {
		System.out.println("[server:" + getId() + "] " + log);
	}
}
