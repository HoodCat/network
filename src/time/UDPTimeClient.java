package time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPTimeClient {
	private static final String SERVER_IP = "192.168.1.23";
	private static final int PORT = 5000;
	private static final int BUFFER_SIZE = 1024;
	
	public static void main(String[] args) {
		DatagramSocket socket = null;
		String message = "";
		int count = 0;
		
		try {
			socket = new DatagramSocket();
			while(count != 10) {
				Thread.sleep(3000);
				DatagramPacket sendPacket =
						new DatagramPacket(message.getBytes("UTF-8"),
								message.length(),
								new InetSocketAddress(SERVER_IP, PORT));
				socket.send(sendPacket);
				
				DatagramPacket receivePacket = 
						new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				
				socket.receive(receivePacket);
				
				String receiveStr = new String(receivePacket.getData(),
						0, receivePacket.getLength(), "UTF-8"); 
				System.out.println(receiveStr);
				count++;
			}
			DatagramPacket sendPacket =
					new DatagramPacket("quit".getBytes("UTF-8"),
							"quit".length(),
							new InetSocketAddress(SERVER_IP, PORT));
			socket.send(sendPacket);
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		}
	}
}
