import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import br.ufla.naivetorrent.domain.file.HashPiece;
import br.ufla.naivetorrent.domain.file.MetaFileTorrent;
import br.ufla.naivetorrent.domain.file.MetaTorrent;
import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.domain.tracker.Tracker;
import br.ufla.naivetorrent.torrent.ExtractMetaInfo;
import br.ufla.naivetorrent.torrent.ReadTorrent;
import br.ufla.naivetorrent.torrent.CreateShareTorrent;
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
	public void testCreateFile() 
			throws Exception {
		File shareFile = new File("/home/carlos/workspaceTorrent/NaiveTorrent/"
				+ "NaiveTorrentClient");
		File torrentFile = new File("/home/carlos/naiveTorrent.torrent");
		torrentFile.createNewFile();
		ExtractMetaInfo extractMetaInfo = new ExtractMetaInfo(shareFile);
		extractMetaInfo.setCreatedBy("Carlos Henrique Pereira");
		extractMetaInfo.setComment("hello world");
		extractMetaInfo.setEncoding("utf-8");
		Tracker tracker1 = new Tracker();
		tracker1.setAddressListening(new InetSocketAddress("127.0.0.1", 8080));
		Tracker tracker2 = new Tracker();
		tracker2.setAddressListening(new InetSocketAddress("192.168.0.1", 8080));
		Tracker tracker3 = new Tracker();
		tracker3.setAddressListening(new InetSocketAddress("192.168.0.101", 8080));
		extractMetaInfo.setTrackers(Arrays.asList(tracker1, tracker2, tracker3));
		MetaTorrent metaTorrent = extractMetaInfo.generateMetaTorrent();
		System.out.println(metaTorrent);
		CreateTorrent makeTorrent = new CreateTorrent(metaTorrent, torrentFile);
		makeTorrent.create();

	}
	
	@Test
	public void readFileTest() 
			throws Exception {
		File shareFile = new File("/home/carlos/workspaceTorrent/NaiveTorrent/"
				+ "NaiveTorrentClient");
		File torrentFile = new File("/home/carlos/naiveTorrent.torrent");
		ExtractMetaInfo extractMetaInfo = new ExtractMetaInfo(shareFile);
		extractMetaInfo.setCreatedBy("Carlos Henrique Pereira");
		extractMetaInfo.setComment("hello world");
		extractMetaInfo.setEncoding("utf-8");
		Tracker tracker1 = new Tracker();
		tracker1.setAddressListening(new InetSocketAddress("127.0.0.1", 8080));
		Tracker tracker2 = new Tracker();
		tracker2.setAddressListening(new InetSocketAddress("192.168.0.1", 8080));
		Tracker tracker3 = new Tracker();
		tracker3.setAddressListening(new InetSocketAddress("192.168.0.101", 8080));
		extractMetaInfo.setTrackers(Arrays.asList(tracker1, tracker2, tracker3));
		MetaTorrent metaTorrentCreated = extractMetaInfo.generateMetaTorrent();
		CreateTorrent createTorrent = new CreateTorrent(metaTorrentCreated,
				torrentFile);
		createTorrent.create();
		ReadTorrent readTorrent = new ReadTorrent(torrentFile);
		MetaTorrent metaTorrentRead = readTorrent.read();
		
		// VERIFICAO
		assertEquals(metaTorrentCreated, metaTorrentRead);
	}
	
	@Test
	public void readFileTest2() 
			throws Exception {
		File shareFile = new File("/home/carlos/Documents/teste/"
				+ "2015apostilalfa-150311090027-conversion-gate01.pdf");
		File torrentFile = new File("/home/carlos/apostila.torrent");
		ExtractMetaInfo extractMetaInfo = new ExtractMetaInfo(shareFile);
		extractMetaInfo.setCreatedBy("Carlos Henrique Pereira");
		extractMetaInfo.setComment("hello world");
		extractMetaInfo.setEncoding("utf-8");
		Tracker tracker1 = new Tracker();
		tracker1.setAddressListening(new InetSocketAddress("127.0.0.1", 8084));
		extractMetaInfo.setTrackers(Arrays.asList(tracker1));
		MetaTorrent metaTorrentCreated = extractMetaInfo.generateMetaTorrent();
		CreateTorrent createTorrent = new CreateTorrent(metaTorrentCreated,
				torrentFile);
		createTorrent.create();
		ReadTorrent readTorrent = new ReadTorrent(torrentFile);
		MetaTorrent metaTorrentRead = readTorrent.read();
		shareFile.renameTo(new File("/home/carlos/Documents/teste/"
				+ "2015apostilalfa-150311090027-conversion-gate01.pdf.part"));
		// VERIFICAO
		assertEquals(metaTorrentCreated, metaTorrentRead);
		int n = (int) (metaTorrentRead.getLenghtTorrent()
				/ metaTorrentRead.getPiecesLength());
		ShareTorrent shareTorrent = new ShareTorrent();
		shareTorrent.setSharePath(new File("/home/carlos/Documents/teste/"));
		shareTorrent.setMetaTorrent(metaTorrentRead);
//		List<MetaFileTorrent> metaFiles = 
//				metaTorrentCreated.getInfo().getMetaFiles();
//		for (MetaFileTorrent metaFile : metaFiles) {
//			shareTorrent.setMetaFileCompleted(metaFile);
//		}
		for (int i = 0; i < n; i++) {
			HashPiece hashPiece = new HashPiece(shareTorrent, i);
			assertEquals(true, hashPiece.check());
		}
		CreateShareTorrent createShareTorrent = new CreateShareTorrent(shareTorrent);
		createShareTorrent.createFiles();
	}
	
	@Test
	public void readAndWriteMetaTorrent() throws IOException{
		File torrentFile = new File("/home/carlos/naiveTorrent.torrent");
		ReadTorrent readTorrent = new ReadTorrent(torrentFile);
		MetaTorrent metaTorrentRead = readTorrent.read();
		
		System.out.println(metaTorrentRead.getInfo());
		
		ShareTorrent share = new ShareTorrent();
		share.setMetaTorrent(metaTorrentRead);
		share.setSharePath(new File("/home/carlos/Documentos/"));
		
		CreateShareTorrent cs = new CreateShareTorrent(share);
		cs.createFiles();
	}


}
