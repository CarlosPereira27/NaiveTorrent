import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.InternalFrameFocusTraversalPolicy;

import org.junit.Test;

import br.ufla.naivetorrent.domain.file.MetaFileTorrent;
import br.ufla.naivetorrent.domain.file.MetaInfoTorrent;
import br.ufla.naivetorrent.domain.file.MetaTorrent;
import br.ufla.naivetorrent.domain.file.ShareTorrent;
import br.ufla.naivetorrent.torrent.CreateShareTorrent;

public class TestFile {

	/*@Test
	public void test() throws IOException{
		File file = new File("/home/alcance/workspace/TESTE/teste/aux.txt");
		String path = file.getPath();
		File dir = new File(path.substring(0, path.lastIndexOf('/')));
		dir.mkdirs();
		//System.out.println(file.createNewFile());
	}*/
	
	private int index = 0;
	

	@Test
	public void test2() {
		byte b[] = new byte[4];
		b[2] = 39;
		b[3] = 18;
		System.out.println(10002);
		byte c[] = BigInteger.valueOf(10002).toByteArray();
		System.out.println(Arrays.toString(c));
		System.out.println(Arrays.toString(b));
		System.out.println(new BigInteger(b).intValue());
	}
	

	
	@Test
	public void test1() throws Exception {
		String command = "create-torrent \"/home/carlos/download/teste\" "
				+ "\"/home/carlos/download/teste.torrent\" "
				+ "{ 192.168.0.1:8067, 192.168.0.1:8080, 192.168.0.1:7070 } "
				+ "\"Carlos Pereira\" \"Comentários do torrent\" "
				+ "\"codificação UTF-8\"";
		System.out.println(command);
		ExtractCommand extractCommand = new ExtractCommand(command);
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
		System.out.println(createdBy);
		System.out.println(Arrays.toString(trackers.toArray()));
		System.out.println(comment);
		System.out.println(encoding);
	}
	
	@Test
	public void empty() throws IOException{
		ShareTorrent st = new ShareTorrent();
		st.setSharePathString("/home/alcance/");
		
		MetaTorrent mt = new MetaTorrent();
		
		MetaInfoTorrent info = new MetaInfoTorrent();
		
		MetaFileTorrent mtf = new MetaFileTorrent();
		mtf.setPathFile("/a.txt");
		mtf.setLength(50l);
		MetaFileTorrent mtfb = new MetaFileTorrent();		
		mtfb.setPathFile("/B/b.txt");
		mtfb.setLength(1024l);
		
		ArrayList<MetaFileTorrent> a = new ArrayList<MetaFileTorrent>();
		a.add(mtf);
		a.add(mtfb);
		
		info.setMetaFiles(a);
		mt.setInfo(info);
		st.setMetaTorrent(mt);
		
		CreateShareTorrent cst = new CreateShareTorrent(st);
		
		System.out.println(cst.createFiles());
		//System.out.println(file.createNewFile());
	}
	
	
}
