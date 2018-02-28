package util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookUp {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String domain = null;
		while(true) {
			domain = scanner.nextLine();
			if(domain == null || "exit".equals(domain)) {
				break;
			}
			try {
				InetAddress[] addresses = Inet6Address.getAllByName(domain);
	
				for(InetAddress address: addresses) {
					System.out.println(address.getHostName() + " : " + address.getHostAddress());
				}
			} catch (UnknownHostException e) {
				System.out.println("해당 도메인을 가진 주소를 찾을 수 없습니다.");
//				e.printStackTrace();
			} 
		}
		
		scanner.close();
	}
}
