package cn.com.boomhope.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 * MD5算法
 * @author 郑铭生
 *
 */
public class Md5Util
{
	protected static Logger log = Logger.getLogger(Md5Util.class);
	static String str;

	public static String md5(String plainText)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++)
			{
				i = b[offset];
				if (i < 0) i += 256;
				if (i < 16) buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
			return str;
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error(e);
		}
		return null;
	}
}
