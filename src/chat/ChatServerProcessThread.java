package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServerProcessThread extends Thread{
	private static List<ChatServerProcessThread> processList = new ArrayList<>();
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	private String nickname;
	
	public ChatServerProcessThread(Socket socket) {
		this.socket = socket;
		this.nickname = null;
		try {
			this.br = new BufferedReader(
					new InputStreamReader(socket.getInputStream(), "UTF-8"));
			this.pw = new PrintWriter(
					new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		synchronized (processList) {
			ChatServerProcessThread.processList.add(this);			
		}
		
		InetSocketAddress clientAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		ChatServer.consoleLog(clientAddress.getAddress() + "님과 연결 되었습니다.");
	}

	@Override
	public void run() {
		while( true ) {
			String message = receive();
			if(message == null || "quit".equals(message)) {
				broadcast(this.nickname + "님이 연결을 끊었습니다.");
				ChatServer.consoleLog("연결이 끊겼습니다");
				break;
			}else if(message.startsWith("protocol_join:")) {
				this.nickname = message.substring(14);
				message = "'" + this.nickname + "'님이 참여하셨습니다.";
			} else {
				message = this.nickname + ":" + message;
			}
			
			ChatServer.consoleLog("'"+message+"'"+"를 받았습니다.");
			broadcast(message);
		}
		removeThread(this);
	}
	
	private void broadcast(String message) {
		synchronized (processList) {
			for(ChatServerProcessThread process: processList) {
				process.send(message);
			}
		}
	}
	
	private String receive() {
		String message = null;
		try {
			message = this.br.readLine();
		} catch(IOException e) {
			removeThread(this);
		}
		return message;
	}
	
	private void send(String message) {
		this.pw.write(message);
		this.pw.flush();
	}
	
	private static void removeThread(ChatServerProcessThread process) {
		process.shutdown();
		processList.remove(process);
	}
	
	private void shutdown() {
		try {
			if(this.socket != null || !this.socket.isClosed()){
				socket.close();
			}
			if(this.br != null) {
				br.close();
			}
			
			if(this.pw != null) {
				pw.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((br == null) ? 0 : br.hashCode());
		result = prime * result + ((pw == null) ? 0 : pw.hashCode());
		result = prime * result + ((socket == null) ? 0 : socket.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatServerProcessThread other = (ChatServerProcessThread) obj;
		if (br == null) {
			if (other.br != null)
				return false;
		} else if (!br.equals(other.br))
			return false;
		if (pw == null) {
			if (other.pw != null)
				return false;
		} else if (!pw.equals(other.pw))
			return false;
		if (socket == null) {
			if (other.socket != null)
				return false;
		} else if (!socket.equals(other.socket))
			return false;
		return true;
	}
}
