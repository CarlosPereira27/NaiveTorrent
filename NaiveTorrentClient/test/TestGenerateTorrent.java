import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import br.ufla.naivetorrent.domain.file.MetaTorrent;
import br.ufla.naivetorrent.domain.tracker.Tracker;
import br.ufla.naivetorrent.torrent.ExtractMetaInfo;
import br.ufla.naivetorrent.torrent.ReadTorrent;
import br.ufla.naivetorrent.torrent.CreateTorrent;

public class TestGenerateTorrent {
	
	public void showFile(File file) 
			throws IOException {
		System.out.println(file.length());
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getCanonicalPath());
		System.out.println(file.getParentFile());
		System.out.println(file.getName());
		System.out.println(file.getParent());
		System.out.println(file.getPath());
		System.out.println(file.getAbsoluteFile());
	}
	
	@Test
	public void test() 
			throws IOException {
		File shareFile = new File("/home/carlos/workspaceTorrent/NaiveTorrent/"
				+ "NaiveTorrentClient");
		File torrentFile = new File("/home/carlos/naiveTorrent.torrent");
		ExtractMetaInfo extractMetaInfo = new ExtractMetaInfo(shareFile);
		extractMetaInfo.setCreatedBy("Carlos Henrique Pereira");
		extractMetaInfo.setComment("hello world");
		extractMetaInfo.setEncoding("utf-8");
		Tracker tracker1 = new Tracker();
		tracker1.setSocketAddressListening(new InetSocketAddress("127.0.0.1", 8080));
		Tracker tracker2 = new Tracker();
		tracker2.setSocketAddressListening(new InetSocketAddress("192.168.0.1", 8080));
		Tracker tracker3 = new Tracker();
		tracker3.setSocketAddressListening(new InetSocketAddress("192.168.0.101", 8080));
		extractMetaInfo.setTrackers(Arrays.asList(tracker1, tracker2, tracker3));
		MetaTorrent metaTorrent = extractMetaInfo.generateMetaTorrent();
		CreateTorrent makeTorrent = new CreateTorrent(metaTorrent, torrentFile);
		makeTorrent.create();

	}
	
	public String bytesToString(ByteBuffer byteBuffer) {
		byte data[] = byteBuffer.array();
		int length = data.length;
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			char c = (char) (data[i] & 0xFF);
			
			System.out.println((int) c);
			sb.append(c);
		}
		return sb.toString();
	}
	
	public ByteBuffer stringToBytes(String string) {
		char data[] = string.toCharArray();
		int length = data.length;
		byte dataBytes[] = new byte[length];
		for (int i = 0; i < length; i++) {
			dataBytes[i] = (byte) data[i];
		}
		return ByteBuffer.wrap(dataBytes);
	}
	
	@Test
	public void readTest() 
			throws IOException {
		File shareFile = new File("/home/carlos/workspaceTorrent/NaiveTorrent/"
				+ "NaiveTorrentClient");
		File torrentFile = new File("/home/carlos/naiveTorrent.torrent");
		ExtractMetaInfo extractMetaInfo = new ExtractMetaInfo(shareFile);
		extractMetaInfo.setCreatedBy("Carlos Henrique Pereira");
		extractMetaInfo.setComment("hello world");
		extractMetaInfo.setEncoding("utf-8");
		Tracker tracker1 = new Tracker();
		tracker1.setSocketAddressListening(new InetSocketAddress("127.0.0.1", 8080));
		Tracker tracker2 = new Tracker();
		tracker2.setSocketAddressListening(new InetSocketAddress("192.168.0.1", 8080));
		Tracker tracker3 = new Tracker();
		tracker3.setSocketAddressListening(new InetSocketAddress("192.168.0.101", 8080));
		extractMetaInfo.setTrackers(Arrays.asList(tracker1, tracker2, tracker3));
		MetaTorrent metaTorrentCreated = extractMetaInfo.generateMetaTorrent();
		ReadTorrent readTorrent = new ReadTorrent(torrentFile);
		MetaTorrent metaTorrentRead = readTorrent.read();
		assertEquals(metaTorrentCreated.getCreatedBy(), metaTorrentRead.getCreatedBy());
		//assertEquals(metaTorrentCreated.getCreationDate(), metaTorrentRead.getCreationDate());
		assertEquals(metaTorrentCreated.getEncoding(), metaTorrentRead.getEncoding());
		assertEquals(metaTorrentCreated.getComment(), metaTorrentRead.getComment());
		byte b[] = new byte[256];
		for (int i = 0; i < 256; i++) {
			b[i] = (byte) i;
		}
		String str = bytesToString(ByteBuffer.wrap(b));
		String str2 = new String(b);
		System.out.println(Arrays.toString(b));
		System.out.println(str);
		System.out.println(Arrays.toString(stringToBytes(str).array()));
		assertEquals(metaTorrentCreated.getInfoHash(), metaTorrentRead.getInfoHash());
		assertEquals(metaTorrentCreated, metaTorrentRead);
	}

}
