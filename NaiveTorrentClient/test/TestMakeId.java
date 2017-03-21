import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class TestMakeId {
	
	@Test
	public void test() throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		byte b[] = "hello world".getBytes();
		byte expected[] = messageDigest.digest(b);
	}

}
