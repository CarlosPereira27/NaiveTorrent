import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
