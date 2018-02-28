package test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class LocalHost {
	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
		
			String hostName = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			byte[] addresses = inetAddress.getAddress();
			
			System.out.println(hostName);
			System.out.println(hostAddress);
			
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< addresses.length ; i++) {
				
			}
						
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
