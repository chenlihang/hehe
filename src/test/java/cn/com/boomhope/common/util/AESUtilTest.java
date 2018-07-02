package cn.com.boomhope.common.util;

public class AESUtilTest
{
	public static void main(String[] args) throws Exception
	{
		String key = "test";
		String passwd = "bioauth123";
		String encrypt = AESUtil.aesEncrypt(passwd, key);
		System.out.println(encrypt);
		String decrypt = AESUtil.aesDecrypt(encrypt, key);
		System.out.println(decrypt);
	}
}
