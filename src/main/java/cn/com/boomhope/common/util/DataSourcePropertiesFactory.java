package cn.com.boomhope.common.util;

import java.util.Properties;

import org.apache.log4j.Logger;

public class DataSourcePropertiesFactory
{
	private static Logger log = Logger.getLogger(DataSourcePropertiesFactory.class);
	private static final String PROP_PASSWORD = "password";

	public static Properties getProperties(String pwd, String key, Boolean production) throws Exception
	{
		Properties p = new Properties();
		String newPwd = pwd;
		if (production)
		{
			if (noNull(pwd) && noNull(key))
			{
				newPwd = decrypt(pwd, key);
			}
		}
		p.setProperty(PROP_PASSWORD, newPwd);
		return p;
	}

	public static String encrpty(String content, String key) throws Exception
	{
		if (!noNull(content))
		{
			log.warn("加密文本为空");
			return content;
		}
		if (!noNull(key))
		{
			log.warn("秘钥为空");
			return content;
		}
		// 取收尾两位
		char f = content.charAt(0);
		char l = content.charAt(content.length() - 1);
		String fl = "" + f + l;
		//
		content = Base64Util.encodeToString((fl + content).getBytes(), 2);
		//
		String encrypt = AESUtil.aesEncrypt(content, key);
		return encrypt;
	}

	public static String decrypt(String content, String key) throws Exception
	{
		if (!noNull(content))
		{
			log.warn("加密文本为空");
			return content;
		}
		if (!noNull(key))
		{
			log.warn("秘钥为空");
			return content;
		}
		//
		String decrypt = AESUtil.aesDecrypt(content, key);
		byte[] bytes = Base64Util.decode(decrypt.getBytes(), 2);
		decrypt = new String(bytes).substring(2);
		return decrypt;
	}

	public static boolean noNull(String str)
	{
		return str != null && str.length() > 0;
	}
}
