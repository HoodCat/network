package chat;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatWindow {

	private Frame frame;
	private Panel pannel;
	private Button buttonSend;
	private TextField textField;
	private TextArea textArea;
	private ClientSocket clientSocket;
	private String name;
	public ChatWindow(String name) {
		this.name = name;
		frame = new Frame(name);
		pannel = new Panel();
		buttonSend = new Button("Send");
		textField = new TextField();
		textArea = new TextArea(30, 80);
	}

	public void show() {
		// Button
		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent actionEvent ) {
				sendMessage();
			}
		});
		

		// Textfield
		textField.setColumns(80);
		textField.addKeyListener( new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				char keyCode = e.getKeyChar();
				if (keyCode == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});

		// Pannel
		pannel.setBackground(Color.LIGHT_GRAY);
		pannel.add(textField);
		pannel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, pannel);

		// TextArea
		textArea.setEditable(false);
		frame.add(BorderLayout.CENTER, textArea);

		// Frame
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.pack();
		
		clientSocket = new ClientSocket();
	}
	
	private void sendMessage() {
		String message = textField.getText() + "\n";
		if("quit\n".equals(message) == true) {
			System.exit(0);
		}
		clientSocket.send(message);
		textField.setText("");
		textField.requestFocus();
	}
	
	private class ClientSocket {
		private Socket socket;
		
		public ClientSocket() {
			this.socket = new Socket();
			
			this.connect();
			this.send("protocol_join:"+name+"\n");
			this.receive();
		}
		
		private void connect() {
			InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 7777);
			try {
				// 1. connect
				socket.connect(serverAddress);
			} catch(IOException e) {
				textArea.setText(serverAddress.getAddress() + " 에 연결을 실패하였습니다.\n");
				System.out.println(e.getMessage());
			}	
		}
		
		public void send(String message) {
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						OutputStream os = socket.getOutputStream();
						os.write(message.getBytes("UTF-8"));
						os.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();
		}
		
		private void receive() {
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						InputStream is = socket.getInputStream();
						byte[] buffer = new byte[256];
						while(true) {
							int readLen = is.read(buffer);
							textArea.append(new String(buffer, 0, readLen, "UTF-8") + "\n");								
						}						
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			};
			thread.start();
		}
	}
}
