package cn.com.boomhope.common.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.log4j.Logger;

/**
 * 文件工具�?
 * 
 * @author 郑铭�?
 *
 */
public class FileUtil
{
	
	protected static Logger log = Logger.getLogger(FileUtil.class);
	/**
	 * 获得指定文件的byte数组
	 * 
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
	 * 根据byte数组，生成文�?
	 * 
	 * @param bytes
	 * @param filePath
	 * @throws Exception
	 */
	public static void createFile_io(byte[] bytes, String filePath) throws Exception
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

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 * @add 杨金水添加
	 */
	public static boolean deleteFile(String sPath)
	{
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists())
		{
			try
			{
				file.delete();
				flag = true;
			}
			catch (Exception ex)
			{
				System.out.println("删除文件时出现错误");
			}
		}
		return flag;
	}

	/**
	 * 删除文件夹(包括文件夹中的文件内容，文件夹)
	 * 
	 * @param strFolderPath
	 * @return
	 */
	public static boolean removeFolder(String strFolderPath)
	{
		boolean bFlag = false;
		try
		{
			if (strFolderPath == null || "".equals(strFolderPath))
			{
				return bFlag;
			}
			File file = new File(strFolderPath.toString());
			bFlag = file.delete();
			if (bFlag == Boolean.TRUE)
			{
				System.out.println("[REMOE_FOLDER:" + file.getPath() + "删除成功!]");
			}
			else
			{
				System.out.println("[REMOE_FOLDER:" + file.getPath() + "删除失败]");
			}
		}
		catch (Exception e)
		{
			log.error("FLOADER_PATH:" + strFolderPath + "删除文件夹失败!");
		}
		return bFlag;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 *
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean DeleteFolder(String sPath)
	{
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists())
		{ // 不存在返回 false
			return flag;
		}
		else
		{
			// 判断是否为文件
			if (file.isFile())
			{ // 为文件时调用删除文件方法
				return deleteFile(sPath);
			}
			else
			{ // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath)
	{
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator))
		{
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory())
		{
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			// 删除子文件
			if (files[i].isFile())
			{
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) break;
			} // 删除子目录
			else
			{
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) break;
			}
		}
		if (!flag) return false;
		// 删除当前目录
		if (dirFile.delete())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 利用NIO将内容输出到文件中
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void createFile(byte[] bytes, String filePath) throws Exception
	{
		FileOutputStream fos = null;
		FileChannel fc_out = null;
		try
		{
			fos = new FileOutputStream(filePath, true);
			fc_out = fos.getChannel();
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			buf.put(bytes);
			buf.flip();
			fc_out.write(buf);
		}
		catch (IOException e)
		{
			log.error(e);
		}
		finally
		{
			if (null != fc_out)
			{
				fc_out.close();
			}
			if (null != fos)
			{
				fos.close();
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		String filePath = "E://test";
		File dir = new File(filePath);
		if (!dir.exists())
		{
			dir.mkdir();
		}
		long start = System.currentTimeMillis();
		System.out.println("测试IO性能");
		for (int i = 0; i < 1; i++)
		{
			String path = filePath + "/io" + i + ".txt";
			FileUtil.createFile("这是人间", path);
		}
		System.out.println("IO耗时：" + (System.currentTimeMillis() - start) + "ms");
	}

	public static void createFile(String content, String filePath) throws Exception
	{
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try
		{
			fos = new FileOutputStream(filePath, true);
			osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(content);
			osw.flush();
		}
		catch (IOException e)
		{
			log.error(e);
		}
		finally
		{
			if (null != osw)
			{
				osw.close();
			}
			if (null != fos)
			{
				fos.close();
			}
		}
	}

	public static String getString(String absolutePath) throws Exception
	{
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuilder fileContent; // 文件很长的话建议使用StringBuffer
		try
		{
			fis = new FileInputStream(absolutePath);
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			String line = null;
			fileContent = new StringBuilder();
			while ((line = br.readLine()) != null)
			{
				fileContent.append(line).append("\r\n");
			}
			return fileContent.toString();
		}
		catch (Exception e)
		{
			log.error(e);
		}
		finally
		{
			if(br!=null)
			{
				br.close();
			}
			if(isr!=null)
			{
				isr.close();
			}
			if(fis!=null)
			{
				fis.close();
			}
		}
		return null;
	}
}