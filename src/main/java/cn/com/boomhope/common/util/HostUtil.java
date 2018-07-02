package cn.com.boomhope.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

public class HostUtil {
	
	protected static Logger log = Logger.getLogger(HostUtil.class);
	
	public static String getIPByCMD() {
		StringBuilder sb = new StringBuilder();
		String command = "cmd.exe /c ipconfig | findstr IPv4";
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				line = line.substring(line.lastIndexOf(":") + 2, line.length());
				sb.append(line);
				break;
			}
			br.close();
			p.destroy();
		} catch (IOException e) {
			log.error(e);
		}
		return sb.toString();
	}

	public static String getIPByJava() {
		List<String> ips = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface intf = (NetworkInterface) en.nextElement();
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
				while (enumIpAddr.hasMoreElements()) {
					InetAddress inetAddress = (InetAddress) enumIpAddr
							.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						ips.add(inetAddress.getHostAddress().toString());
					}
				}
			}
		} catch (SocketException e) {
		}
		return ips.get(0);
	}
	
	public static void main(String[] args) {
		System.out.println(getIPByJava());
	}
}
