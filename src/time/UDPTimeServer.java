package time;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UDPTimeServer {
	private static final int BUFFER_SIZE = 1024;
	private static final int PORT = 5000;
	
	public static void main(String[] args) {
		DatagramSocket socket = null;
		
		try {
			socket = new DatagramSocket(PORT);
			while(true) {
				
				DatagramPacket receivePacket = new DatagramPacket(
						new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket);
				
				String message = new String(receivePacket.getData(),
						0, receivePacket.getLength(), "UTF-8");
				
				if("quit".equals(message)) {
					System.out.println("서버 종료");
					break;
				}
				
				if(!"".equals(message)) {
					continue;
				}
				
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss a");
				
				byte[] sendData = format.format(calendar.getTime())
						.getBytes("UTF-8");
				
				DatagramPacket sendPacket = new DatagramPacket(
						sendData,
						sendData.length,
						receivePacket.getAddress(),
						receivePacket.getPort());
				socket.send(sendPacket);
			}			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		}
	}
}
