package cn.com.boomhope.common.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class FileBase64Util
{
	/**
	 * ���ļ�ת��base64 �ַ�
	 * 
	 * @param path �ļ�·��
	 * @return
	 * @throws Exception
	 */
	public static String encodeFile(String path) throws Exception
	{
		FileInputStream inputFile = null;
		try
		{
			byte[] buffer = FileUtil.getBytes(path);
			return new BASE64Encoder().encode(buffer);
		}
		finally
		{
			if (inputFile != null)
			{
				inputFile.close();
			}
		}
	}

	/**
	 * ��base64�ַ���뱣���ļ�
	 * 
	 * @param base64Code
	 * @param targetPath
	 * @throws Exception
	 */
	public static void decodeFile(String base64Code, String targetPath) throws Exception
	{
		FileOutputStream out = null;
		try
		{
			byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
			FileUtil.createFile(buffer, targetPath);
		}
		finally
		{
			if (out != null)
			{
				out.close();
			}
		}
	}
	
	public static byte[] toByteArray(String base64Code) throws IOException
	{
		byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
		return buffer;
	}

	public static String byteToBase64(byte[] bytes)
	{
		BASE64Encoder encode = new BASE64Encoder();
		String base64 = encode.encode(bytes);
		return base64;
	}
	
	public static byte[] base64ToByte(String base64)
	{
//		try
//		{
//			String tmpPath = new File("").getAbsolutePath() + File.separator + System.currentTimeMillis();
//			decodeFile(base64, tmpPath);
//			File f = new File(tmpPath);
//			if (f.exists())
//			{
//				Path path = Paths.get(f.getAbsolutePath());
//				byte[] bytes = Files.readAllBytes(path);
//				if (bytes != null && bytes.length != 0)
//				{
//					FileUtil.deleteFile(tmpPath);
//					return bytes;
//				}
//			}
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return null;
		return Base64Util.decode(base64, 2);
	}
}
