package cn.com.boomhope.common.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * �ļ�������
 * 
 * @author ֣����
 *
 */
public class FileUtils
{
	/**
	 * ���ָ���ļ���byte����
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytes(String filePath) throws Exception
	{
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try
		{
			int len = 1024;
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream(len);
			byte[] b = new byte[len];
			int n;
			while ((n = fis.read(b)) != -1)
			{
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
			c(buffer, "e:/data/gk/"+file.getParentFile().getName()+"/"+file.getName()+"dat");
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if (fis != null)
			{
				fis.close();
			}
			if (bos != null)
			{
				bos.close();
			}
		}
		return buffer;
	}

	/**
	 * ���byte���飬����ļ�
	 * 
	 * @param bytes
	 * @param filePath
	 * @throws Exception
	 */
	public static void createFile(byte[] bytes, String filePath) throws Exception
	{
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try
		{
			File dir = new File(filePath);
			if (!dir.exists())
			{
				dir.getParentFile().mkdirs();
			}
			file = new File(filePath);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if (bos != null)
			{
				bos.close();
			}
			if (fos != null)
			{
				fos.close();
			}
		}
	}

	public static void c(byte[] buffer,String dest)
	{
		FileOutputStream fs = null;
		try
		{
			File newFile = new File(dest);
			if (!newFile.getParentFile().exists())
			{
				newFile.getParentFile().mkdirs();
			}
			fs = new FileOutputStream(dest);
			fs.write(buffer);
		}
		catch (Exception e)
		{
		}
		finally
		{
			if (fs != null)
			{
				try
				{
					fs.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
}