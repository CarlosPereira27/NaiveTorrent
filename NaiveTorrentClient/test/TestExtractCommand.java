import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import br.ufla.naivetorrent.cli.ExtractCommand;

public class TestExtractCommand {
	
	@Test
	public void test2() {
		String str = "/home/carlos/Documents/teste/";
		String str2 = "ubuntu-14.04.3-desktop-amd64.iso";
		File file = new File(str);
		System.out.println(file.getPath());
		File file2 = new File(file.getPath() + "/" + str2);
		System.out.println(file2.getPath());
		System.out.println(file.exists());
		
	}
	
	@Test
	public void test3() {
		List<Integer> list = new ArrayList<>();
		list.add(10);
		list.add(20);
		list.add(3);
		list.add(2);
		Integer i = 20;
		list.remove(i);
		System.out.println(Arrays.toString(list.toArray()));
		
	}
	
	@Test
	public void test() {
		String command = 
				"create-torrent \"/home/carlos/workspaceTorrent/NaiveTorrent/NaiveTorrentClient\" "
				+ "\"/home/carlos/Documents/naiveTorrent.torrent\" "
				+ "{ 192.168.0.1:8067, 192.168.0.1:8080, 192.168.0.1:7070 } "
				+ "\"Carlos Pereira\" "
				+ "\"Comentários do torrent\" "
				+ "\"codificação UTF-8\"";
		System.out.println(command);
		ExtractCommand extractCommand = new ExtractCommand(command);
		try {
			String cmd = extractCommand.readCmd();
			String sharePath = extractCommand.readParameter();
			String torrentPath = extractCommand.readParameter();
			List<String> trackers = extractCommand.readList();
			String createdBy = extractCommand.readParameter();
			String comment = extractCommand.readParameter();
			String encoding = extractCommand.readParameter();

			// VERIFICAO
			assertEquals("create-torrent", cmd);
			assertEquals("/home/carlos/workspaceTorrent/NaiveTorrent/NaiveTorrentClient", 
					sharePath);
			assertEquals("/home/carlos/Documents/naiveTorrent.torrent", 
					torrentPath);
			List<String> trackersExpected = Arrays.asList(
					"192.168.0.1:8067", 
					"192.168.0.1:8080", 
					"192.168.0.1:7070");
			assertEquals(trackersExpected, trackers);
			assertEquals("Carlos Pereira", createdBy);
			assertEquals("Comentários do torrent", comment);
			assertEquals("codificação UTF-8", encoding);
		} catch (Exception e) {
			fail();
			System.out.println("Comando errado!");
			System.out.println(e.getMessage());
		}
	}

}
