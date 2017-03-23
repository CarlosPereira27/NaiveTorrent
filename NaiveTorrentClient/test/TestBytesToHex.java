import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import br.ufla.naivetorrent.util.UtilHex;

public class TestBytesToHex {

	
	@Test
	public void testBytesToHex() throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		byte b[] = "hello world".getBytes();
		byte result[] = messageDigest.digest(b);
		
		String hexExpected = "2AAE6C35C94FCFB415DBE95F408B9CE91EE846ED";
		String hexResult = UtilHex.toHexString(ByteBuffer.wrap(result));

		// VERIFICAO
		assertEquals(hexExpected, hexResult);
		
	}
	
	@Test
	public void testHexToBytes() throws NoSuchAlgorithmException {
		String hexString = "2AAE6C35C94FCFB415DBE95F408B9CE91EE846ED";
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		byte b[] = "hello world".getBytes();
		byte expected[] = messageDigest.digest(b);
		
		byte result[] = UtilHex.toBytes(hexString).array();

		// VERIFICAO
		assertArrayEquals(expected, result);
		
	}

}
