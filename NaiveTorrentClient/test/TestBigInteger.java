import static org.junit.Assert.*;


import java.math.BigInteger;

import org.junit.Test;

public class TestBigInteger {
	
	@Test
	public void test() {
		int valExpected = 10002;
		byte b[] = new byte[4];
		b[2] = 39;
		b[3] = 18;
		byte bExpected[] = new byte[2];
		bExpected[0] = 39;
		bExpected[1] = 18;
		int valResult = new BigInteger(b).intValue();
		byte bResult[] = BigInteger.valueOf(valExpected).toByteArray();
		
		// VERIFICAO
		assertEquals(valExpected, valResult);
		assertArrayEquals(bExpected, bResult);
	}

}
