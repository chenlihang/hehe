package org.csource.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.nio.channels.FileChannel;

import org.apache.log4j.Logger;

/**
 * 文件工具类
 * 
 * @author 郑铭生
 *
 */
public class FileUtil {
	
	protected static Logger log = Logger.getLogger(FileUtil.class);
	/**
	 * 获得指定文件的byte数组
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytes(String filePath) throws Exception {
		byte[] buffer = null;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			int len = 1024;
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bos = new ByteArrayOutputStream(len);
			byte[] b = new byte[len];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			if (fis != null) {
				fis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		return buffer;
	}

	/**
	 * 根据byte数组，生成文件
	 * 
	 * @param bytes
	 * @param filePath
	 * @throws Exception
	 */
	public static void createFile(byte[] bytes, String filePath)
			throws Exception {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists()) {
				dir.getParentFile().mkdirs();
			}
			file = new File(filePath);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);
		} catch (Exception e) {
			throw e;
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
	}

	public static void copyFile(String oldPath, String newPath) {
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			@SuppressWarnings("unused")
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				inStream = new FileInputStream(oldPath); // 读入原文件
				fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				System.out.println("[COPY_FILE:" + oldfile.getPath()
						+ "复制文件成功!]");
			}
		} catch (Exception e) {
			System.out.println("拷贝文件时出错");
		} finally {
			if (fs != null) {
				try {
					fs.close();
				} catch (Exception e) {
					System.out.println("关闭流出错");
				}
			}
			if (inStream != null) {
				try {
					inStream.close();
				} catch (Exception e) {
					System.out.println("关闭流出错");
				}
			}
		}
	}

	public static void copyFolder(String oldPath, String newPath) {
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					try {
						input = new FileInputStream(temp);
						output = new FileOutputStream(newPath + File.separator
								+ (temp.getName()).toString());
						byte[] b = new byte[1024 * 5];
						int len;
						while ((len = input.read(b)) != -1) {
							output.write(b, 0, len);
						}
						output.flush();
					} catch (Exception e) {
						System.out.println("复制文件夹过程中出现异常");
					} finally {
						if (output != null) {
							output.close();
						}
						if (input != null) {
							input.close();
						}
					}
				}
				if (temp.isDirectory()) {
					copyFolder(oldPath + "/ " + file[i], newPath + "/ "
							+ file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错 ");
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					System.out.println("关闭流的过程中出现异常");
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					System.out.println("关闭流的过程中出现异常");
				}
			}
		}
	}

	/**
	 * 删除文件夹(包括文件夹中的文件内容，文件夹)
	 * 
	 * @param strFolderPath
	 * @return
	 */
	public static boolean removeFolder(String strFolderPath) {
		boolean bFlag = false;
		try {
			if (strFolderPath == null || "".equals(strFolderPath)) {
				return bFlag;
			}
			File file = new File(strFolderPath.toString());
			bFlag = file.delete();
			if (bFlag == Boolean.TRUE) {
				System.out
						.println("[REMOE_FOLDER:" + file.getPath() + "删除成功!]");
			} else {
				System.out.println("[REMOE_FOLDER:" + file.getPath() + "删除失败]");
			}
		} catch (Exception e) {
			log.error("FLOADER_PATH:" + strFolderPath + "删除文件夹失败!");
		}
		return bFlag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 * @add 杨金水添加
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		boolean flag = false;
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
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
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			try {
				file.delete();
				flag = true;
			} catch (Exception ex) {
				System.out.println("删除文件时出现错误");
			}
		}
		return flag;
	}

	/**
	 * 使用文件通道的方式复制文件
	 */
	public static void fileChannelCopy(String src, String tar) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;

		try {
			File s = new File(src);
			File t = new File(tar);
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			log.error(e);
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
	}
	
	/**
	 * 置换两个文件
	 */
	public static void changeFile(File srcFolder, File tarFolder){
		try{
			File srcFile = null;
			File tarFile = null;
			if(srcFolder.isDirectory()){
				File[] srcFileList = srcFolder.listFiles();
				if(srcFileList.length>0){
					srcFile = srcFileList[0];
				}
			}
			if(tarFolder.isDirectory()){
				File[] tarFileList = tarFolder.listFiles();
				if(tarFileList.length>0){
					tarFile = tarFileList[0];
				}
			}
			//将rand1 copy到rand2里的
			FileUtil.fileChannelCopy(srcFile.getAbsolutePath(), tarFile.getParentFile().getAbsolutePath()+File.separator+srcFile.getName());
			//将rand2 copy到rand1里的
			FileUtil.fileChannelCopy(tarFile.getAbsolutePath(), srcFile.getParentFile().getAbsolutePath()+File.separator+tarFile.getName());
			//rand2移动到rand1里后，删除原来rand2下的rand2文件
			FileUtil.deleteFile(srcFile.getAbsolutePath());
			//rand2移动到rand1里后，删除原来rand2下的rand2文件
			FileUtil.deleteFile(tarFile.getAbsolutePath());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	 // 读取文件指定行。   
	public static void readAppointedLineNumber(File sourceFile, int lineNumber) throws IOException {   
		   FileReader in = new FileReader(sourceFile);   
		   LineNumberReader reader = new LineNumberReader(in);   
		   String s = "";   
//		   if (lineNumber <= 0 || lineNumber > getTotalLines(sourceFile)) {   
//			   System.out.println("不在文件的行数范围(1至总行数)之内。");   
//			   System.exit(0);   
//		   }   
		   int lines = 0;   
		   while (s != null) {   
			   lines++;   
			   s = reader.readLine();   
			   if((lines - lineNumber) == 0) {   
				   System.out.println("11"+s+"22");   
				   System.exit(0);
			   }   
		   }   
		   if(reader!=null){
			   reader.close();
		   }
	}  
	
	/**
	 * 从一堆文件中找出一个文件名称
	 * @param inputFile
	 * @param sourceFolder
	 * @return
	 */
	public static String findFileName(String inputFile, String sourceFolder){
		try{
			if(inputFile!=null && inputFile.trim().length()!=0){
				File folder = new File(sourceFolder);
				if(folder.exists()){
					File[] files = folder.listFiles();
					for(File file : files){
						if(inputFile.equals(file.getName())){
							return file.getAbsolutePath();
						}
					}
					System.out.println("找不到文件："+inputFile);
				}else{
					System.out.println("文件不存在");
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}