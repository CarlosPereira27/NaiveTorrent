import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.rmi.CORBA.Util;
import javax.xml.ws.handler.MessageContext;

import org.junit.Test;

import br.ufla.naivetorrent.domain.tracker.Tracker;
import br.ufla.naivetorrent.tracker.protocol.RequestEvent;
import br.ufla.naivetorrent.tracker.protocol.RequestMessage;
import br.ufla.naivetorrent.tracker.request.HttpRequest;
import br.ufla.naivetorrent.util.UtilHex;

public class TestHttpRequest {

	@Test
	public void test() throws IOException {
		StringBuilder sbResponse = new StringBuilder();
		String urlStr = "http://stackoverflow.com/about";
		
		URL url = new URL(urlStr);
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		con.setRequestMethod("GET");
		con.connect();
		
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			System.out.println(con.getResponseMessage());
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sbResponse.append(inputLine);
			}
			System.out.println("######");
			System.out.println(sbResponse.toString());
			in.close();
		} else {
			
		}
	}
	
	@Test
	public void test2() 
			throws IOException, NoSuchAlgorithmException {
		MessageDigest messageDigestMD5 = MessageDigest.getInstance("MD5");
		MessageDigest messageDigestSHA1 = MessageDigest.getInstance("SHA1");
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			messageDigestMD5.update((byte) random.nextInt(512));
			messageDigestSHA1.update((byte) random.nextInt(512));
		}
		RequestMessage message = new RequestMessage();
		message.setDownloaded(1000l);
		message.setPort(6070);
		message.setIp("127.0.0.1");
		message.setLeft(120323l);
		message.setUploaded(1556l);
		message.setPeerId(UtilHex.toHexString(
				ByteBuffer.wrap(messageDigestSHA1.digest())));
		message.setEvent(RequestEvent.STARTED);
		message.setInfoHash(UtilHex.toHexString(
				ByteBuffer.wrap(messageDigestMD5.digest())));
		Tracker tracker = new Tracker();
		tracker.setSocketAddressListening(new InetSocketAddress(8080));
		HttpRequest httpRequest = new HttpRequest(tracker, message);
		InetSocketAddress i = new InetSocketAddress("127.0.0.1", 6067);
		System.out.println(i.getAddress().getHostAddress());
		
		
	}
}
