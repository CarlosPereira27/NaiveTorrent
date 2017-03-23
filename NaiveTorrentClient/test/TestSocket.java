import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;


import br.ufla.naivetorrent.util.UtilByteString;

@SuppressWarnings("unused")
public class TestSocket {
	
	public static String generateString() {
		StringBuilder sb = new StringBuilder();
		int n = 1024 * 1024;
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			char c = (char) random.nextInt(255);
			byte b = (byte) c;
			if (b == -1) {
				System.out.println("'" + c + "'");
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	public static byte data[];
	public static void main(String[] args) throws InterruptedException {
		Socket s1;
		try {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					ServerSocket serverSocket = null;
					try {
						serverSocket = new ServerSocket(6067);
						System.out.println(serverSocket.getInetAddress().getHostAddress());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						Socket s = serverSocket.accept();
						byte data2[] = new byte[1024 * 1024];
						int v;
						int index = 0;
						InputStream in = s.getInputStream();
						System.out.println(in.read(data2));
//						while ((v = in.read()) != -1) {
//							data2[index++] = (byte) v;
//						}
						//System.out.println(index);
							
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}).start();
			Thread.sleep(1000);
			System.out.println(InetAddress.getByName("localhost").getHostName());
			s1 = new Socket("localhost", 6067);
			//Socket s2 = new Socket("localhost", 6878);
			System.out.println("s1-" + s1.getInetAddress().getHostAddress() + ":" + s1.getPort());
			//System.out.println("s2-" + s2.getInetAddress().getHostAddress() + ":" + s2.getPort());
			String strExpected = generateString();
			//s1.connect(new InetSocketAddress(s2.getInetAddress().getHostAddress(), 
			//		s2.getPort()));
			data = UtilByteString.stringToBytes(strExpected).array();
			for (int i = 0; i < 1024 * 1024; i++) {
				if (data[i] == -1) {
					System.out.println("'" + data[i] + "'");
				}
			}
			s1.getOutputStream().write(data);
			System.out.println(data.length);
			s1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

}
