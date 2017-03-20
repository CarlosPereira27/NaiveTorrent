import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

import org.junit.Test;

public class TestNetworkInterface {
	
	@Test
	public void test() throws NoSuchAlgorithmException {
		Enumeration<NetworkInterface> e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements()) {
			    NetworkInterface n = e.nextElement();
			    Enumeration<InetAddress> ee = n.getInetAddresses();
			    
			    while (ee.hasMoreElements()) {
			        InetAddress i = ee.nextElement();
			        System.out.println(i.getHostAddress());
			        System.out.println(getType(i));
			    }
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private String getType(InetAddress address) {
		String type = "";
		if (address.isAnyLocalAddress()) {
			type += "AnyLocalAddress";
		}
		if (address.isLinkLocalAddress()) {
			type += "LinkLocalAddress";
		}
		if (address.isLoopbackAddress()) {
			type += "LoopbackAddress";
		}
		if (address.isMCGlobal()) {
			type += "MCGlobal";
		}
		if (address.isMCLinkLocal()) {
			type += "MCLinkLocal";
		}
		if (address.isMCNodeLocal()) {
			type += "MCNodeLocal";
		}
		if (address.isMCOrgLocal()) {
			type += "MCOrgLocal";
		}
		if (address.isMCSiteLocal()) {
			type += "MCSiteLocal";
		}
		if (address.isMulticastAddress()) {
			type += "MulticastAddress";
		}
		if (address.isSiteLocalAddress()) {
			type += "SiteLocalAddress";
		}
		return type;
	}

}
