import static org.junit.Assert.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;


public class TestSecurity {
	
	
	@Test
	public void testSHA1() throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		byte b[] = "hello world".getBytes();
		byte expected[] = messageDigest.digest(b);
		
		messageDigest.reset();
		messageDigest.update("h".getBytes());
		messageDigest.update("e".getBytes());
		messageDigest.update("l".getBytes());
		messageDigest.update("l".getBytes());
		messageDigest.update("o".getBytes());
		messageDigest.update(" ".getBytes());
		messageDigest.update("w".getBytes());
		messageDigest.update("o".getBytes());
		messageDigest.update("r".getBytes());
		messageDigest.update("l".getBytes());
		messageDigest.update("d".getBytes());
		byte result[] = messageDigest.digest();
		
		// VERIFICAO
		assertArrayEquals(expected, result);
	}
	
	@Test
	public void testMD5() throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte b[] = "hello world".getBytes();
		byte expected[] = messageDigest.digest(b);
		
		messageDigest.reset();
		messageDigest.update("h".getBytes());
		messageDigest.update("e".getBytes());
		messageDigest.update("l".getBytes());
		messageDigest.update("l".getBytes());
		messageDigest.update("o".getBytes());
		messageDigest.update(" ".getBytes());
		messageDigest.update("w".getBytes());
		messageDigest.update("o".getBytes());
		messageDigest.update("r".getBytes());
		messageDigest.update("l".getBytes());
		messageDigest.update("d".getBytes());
		byte result[] = messageDigest.digest();
		
		// VERIFICAO
		assertArrayEquals(expected, result);
	}

}
