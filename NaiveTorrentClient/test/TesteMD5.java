import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

import org.junit.Test;

import br.ufla.naivetorrent.tracker.protocol.RequestEvent;
import br.ufla.naivetorrent.util.UtilHex;;


public class TesteMD5 {
	
	private String toString(byte bytes[]) {
		byte bytesCp[] = Arrays.copyOf(bytes, bytes.length);
		StringBuilder sb = new StringBuilder();
		int n = bytes.length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 8; j++) {
				if ((int) (bytesCp[i] & -128) == -128) {
					sb.append('1');
				} else {
					sb.append('0');
				}
				bytesCp[i] = (byte) (bytesCp[i] << 1);
			}
		}
		return sb.toString();
	}

	@Test
	public void test() throws NoSuchAlgorithmException {
		System.out.println(new InetSocketAddress(6669).getAddress().getHostAddress());
		System.out.println(RequestEvent.STOPPED.ordinal());
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		byte b[] = "hello world".getBytes();
		byte resultExpected[] = messageDigest.digest(b);
		System.out.println(resultExpected.length);
		System.out.println(toString(resultExpected));
		String hexString = UtilHex.toHexString(ByteBuffer.wrap(resultExpected));
		System.out.println(hexString);
		byte result[] = UtilHex.toBytes(hexString).array();
		messageDigest.reset();
		messageDigest.update("o".getBytes());
		messageDigest.update("l".getBytes());
		messageDigest.update("a".getBytes());
		byte result2[] = messageDigest.digest();
		assertArrayEquals(resultExpected, result);
		messageDigest = MessageDigest.getInstance("MD5");
		System.out.println(messageDigest.digest().length);
		Enumeration<NetworkInterface> e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements()) {
			    NetworkInterface n = e.nextElement();
			    Enumeration<InetAddress> ee = n.getInetAddresses();
			    
			    while (ee.hasMoreElements()) {
			        InetAddress i = ee.nextElement();
			        System.out.println(i.getHostAddress());
			        System.out.println(toString(
			        		i.getAddress()));
			        System.out.println(getType(i));
			    }
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private String getType(InetAddress address) {
		String type = "";
		if (address.isAnyLocalAddress()) {
			type += "AnyLocalAddress";
		}
		if (address.isLinkLocalAddress()) {
			type += "LinkLocalAddress";
		}
		if (address.isLoopbackAddress()) {
			type += "LoopbackAddress";
		}
		if (address.isMCGlobal()) {
			type += "MCGlobal";
		}
		if (address.isMCLinkLocal()) {
			type += "MCLinkLocal";
		}
		if (address.isMCNodeLocal()) {
			type += "MCNodeLocal";
		}
		if (address.isMCOrgLocal()) {
			type += "MCOrgLocal";
		}
		if (address.isMCSiteLocal()) {
			type += "MCSiteLocal";
		}
		if (address.isMulticastAddress()) {
			type += "MulticastAddress";
		}
		if (address.isSiteLocalAddress()) {
			type += "SiteLocalAddress";
		}
		
		return type;
	}

}
