import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.junit.Test;

import br.ufla.naivetorrent.util.UtilHex;

public class TestBytesToHex {
	
	@SuppressWarnings("unused")
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
	public void testBytesToHex() throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		byte b[] = "hello world".getBytes();
		byte result[] = messageDigest.digest(b);
		
		String hexExpected = "2AAE6C35C94FCFB415DBE95F408B9CE91EE846ED";
		String hexResult = UtilHex.toHexString(ByteBuffer.wrap(result));

		assertEquals(hexExpected, hexResult);
		
	}
	
	@Test
	public void testHexToBytes() throws NoSuchAlgorithmException {
		String hexString = "2AAE6C35C94FCFB415DBE95F408B9CE91EE846ED";
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		byte b[] = "hello world".getBytes();
		byte expected[] = messageDigest.digest(b);
		
		byte result[] = UtilHex.toBytes(hexString).array();

		assertArrayEquals(expected, result);
		
	}

}
