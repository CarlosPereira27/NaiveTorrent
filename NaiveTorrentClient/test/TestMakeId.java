import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import br.ufla.naivetorrent.domain.peer.Peer;
import br.ufla.naivetorrent.util.UtilGenerateId;

public class TestMakeId {
	
	@Test
	public void test() throws NoSuchAlgorithmException {
		System.out.println(UtilGenerateId.generateIdHex("127.0.0.1", 8080));
		// Dados
		String id = "AGAJADSA1231ADS12";
		String ip = "127.0.0.1";
		Integer port = 8080;
		// Criação de objeto peer
		Peer peer = new Peer();
		peer.setIdHex(id);
		peer.setSocketAddressListening(new InetSocketAddress(ip, port));
	}

}
