import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import org.junit.Test;

public class TestHttpRequest {

	@Test
	public void test() throws IOException {
		StringBuilder sbResponse = new StringBuilder();
		String urlStr = "http://stackoverflow.com/about";
		
		URL url = new URL(urlStr);
		
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		con.setRequestMethod("GET");
		con.connect();
		
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			System.out.println(con.getResponseMessage());
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sbResponse.append(inputLine);
			}
			System.out.println("######");
			System.out.println(sbResponse.toString());
			in.close();
		} else {
			
		}
	}
}
