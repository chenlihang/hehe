package cn.com.boomhope.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class IpUtil
{

	protected static Logger log = Logger.getLogger(IpUtil.class);
	private static boolean first = true;

	/**
	 * 自动获取IP的方法
	 * 
	 * @return
	 */
	public static String getIpAddresses()
	{
		try
		{
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements())
			{
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				Enumeration<InetAddress> enInetAddress = networkInterface.getInetAddresses();
				while (enInetAddress.hasMoreElements())
				{
					InetAddress inetAddress = enInetAddress.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress())
					{
						return inetAddress.getHostAddress();
					}
				}
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	/**
	 * 是否自动获取IP
	 * 
	 * @param flag
	 *            : 值true为自动获取IP,false为手动获取IP
	 * @param host
	 *            :
	 * @return
	 */
	public static String getIp(boolean flag, String host)
	{
		String ip = null;
		if (flag)
		{
			ip = IpUtil.getIpAddresses();
		}
		else
		{
			ip = host;
		}
		if (first)
		{
			log.info(String.format("IP获取方式:%s,IP:%s", flag ? "自动" : "手动", ip));
			first = false;
		}
		return ip;
	}
}
