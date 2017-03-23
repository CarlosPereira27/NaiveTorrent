import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestExtractCommand {
	
	@Test
	public void test() {
		String command = "create-torrent \"/home/carlos/download/teste\" "
				+ "\"/home/carlos/download/teste.torrent\" "
				+ "{ 192.168.0.1:8067, 192.168.0.1:8080, 192.168.0.1:7070 } "
				+ "\"Carlos Pereira\" \"Comentários do torrent\" "
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
			System.out.println(cmd);
			System.out.println(sharePath);
			System.out.println(torrentPath);
			System.out.println(Arrays.toString(trackers.toArray()));
			System.out.println(createdBy);
			System.out.println(comment);
			System.out.println(encoding);
		} catch (Exception e) {
			System.out.println("Comando errado!");
			System.out.println(e.getMessage());
		}
	}

}
