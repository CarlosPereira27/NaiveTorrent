import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import gyurix.bencoder.BEncoder;

public class TestBEncoder {
	
	@Test
	public void testEncode() {
		BEncoder bEncoder = new BEncoder();
		Map<Object, Object> mapa = new LinkedHashMap<>();
		mapa.put("id", "0");
		mapa.put("ip", "127.0.0.1");
		mapa.put("port", 8080);
		Integer leng = 20;
		List<Object> lista = new ArrayList<>();
		lista.add(0);
		lista.add("olá mundo");
		lista.add(2);
		bEncoder.write(mapa);
		bEncoder.write(leng);
		bEncoder.write(lista);
		String expected = "d2:id1:02:ip9:127.0.0.14:porti8080eei20eli0e"
				+ "9:olá mundoi2ee";
		// VERIFICAÇÃO
		assertEquals(expected, bEncoder.toString());
	}
	
	@Test
	public void testDecode() {
		String strBecnded = "d2:id1:02:ip9:127.0.0.14:porti8080eei20e"
				+ "li0e9:olá mundoi2ee";
		BEncoder bEncoder = new BEncoder(strBecnded);
		@SuppressWarnings("unchecked")
		Map<Object, Object> mapResult =  (Map<Object, Object>) bEncoder.read();
		Map<Object, Object> mapExpected = new LinkedHashMap<>();
		mapExpected.put("id", "0");
		mapExpected.put("ip", "127.0.0.1");
		mapExpected.put("port", 8080l);
		Long longExpected = 20l;
		bEncoder.read();
		Long longResult = (Long) bEncoder.read();
		List<Object> listExpected = new ArrayList<>();
		listExpected.add(0l);
		listExpected.add("olá mundo");
		listExpected.add(2l);
		bEncoder.read();
		@SuppressWarnings("unchecked")
		List<Object> listResult = (List<Object>) bEncoder.read();
		
		// VERIFICACAO
		assertEquals(mapExpected, mapResult);
		assertEquals(longExpected, longResult);
		assertEquals(listExpected, listResult);
	}
	
	@Test
	public void testDecodeList() {
		String strBecnded = "d2:id1:02:ip9:127.0.0.14:porti8080ee"
				+ "i20eli0e9:olá mundoi2ee";
		BEncoder bEncoder = new BEncoder(strBecnded);
		List<Object> listResult = bEncoder.readAll();
		Map<String, Object> mapExpected = new LinkedHashMap<>();
		mapExpected.put("id", "0");
		mapExpected.put("ip", "127.0.0.1");
		mapExpected.put("port", 8080l);
		Long longExpected = 20l;
		List<Object> listExpected = new ArrayList<>();
		listExpected.add(0l);
		listExpected.add("olá mundo");
		listExpected.add(2l);
		
		// VERIFICAO
		assertEquals(mapExpected, listResult.get(0));
		assertEquals(longExpected, listResult.get(1));
		assertEquals(listExpected, listResult.get(2));
	}

}
