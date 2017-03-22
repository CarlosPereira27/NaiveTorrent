import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class TestFile {

	@Test
	public void test() throws IOException{
		File file = new File("/home/alcance/workspace/TESTE/teste/aux.txt");
		String path = file.getPath();
		File dir = new File(path.substring(0, path.lastIndexOf('/')));
		dir.mkdirs();
		//System.out.println(file.createNewFile());
	}
}
