package cn.com.boomhope.common.file;

import java.io.File;
import java.util.Calendar;
import java.util.Map;

import cn.com.boomhope.common.exception.LogicException;
import cn.com.boomhope.common.util.FileBase64Util;
import cn.com.boomhope.common.util.FileUtil;
import cn.com.boomhope.common.vo.FileVo;

public class HdFileProcesser extends BaseFileProcesser implements FileProcesser
{
	private final String fileRoot;

	public HdFileProcesser(Map<String, Object> settingCxt)
	{
		super(settingCxt);
		fileRoot = (String) settingCxt.get("fileRoot");
	}

	/**
	 * 保存文件
	 * @param type
	 * @param distributePath
	 * @param fileName
	 * @param content
	 * @return
	 *
	 * 年/月/日/hash/faceid.jpg<br>
	 * /bioauth/face/2016/09/30/3/2.jpg
	 */
	@Override
	public String saveFile(String type, String distributePath, String fileName, byte[] content){
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return null;
		}
		try
		{
			(new File(fileRoot + File.separator + filePathVo.getPath() + File.separator + distributePath)).mkdirs();// 则建立新文件夹
			String actualName = distributePath + File.separator + fileName;
			FileUtil.createFile(content, filePathVo.getAbsultePath(fileRoot, actualName));
			return actualName;
		}
		catch (Exception e)
		{
			log.error(e);
			return null;
		}
	}

	@Override
	public String saveFile(String type, String fileName, byte[] content)
	{
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return null;
		}
		try
		{
			(new File(fileRoot + File.separator + filePathVo.getPath())).mkdirs();// 则建立新文件夹
			FileUtil.createFile(content, filePathVo.getAbsultePath(fileRoot, fileName));
			return fileName;
		}
		catch (Exception e)
		{
			log.error(e);
			return null;
		}
	}

	@Override
	public String saveFile(String type, String fileName, String content)
	{
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return null;
		}
		try
		{
			(new File(fileRoot + File.separator + filePathVo.getPath())).mkdirs();// 则建立新文件夹
			FileUtil.createFile(content, filePathVo.getAbsultePath(fileRoot, fileName));
			return fileName;
		}
		catch (Exception e)
		{
			log.error(e);
			return null;
		}
	}

	@Override
	public boolean removeFile(String type, String fileName)
	{
		boolean ok = false;
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return ok;
		}
		try
		{
			File f = new File(filePathVo.getAbsultePath(fileRoot, fileName));
			if(f.exists())
			{
				FileUtil.deleteFile(f.getAbsolutePath());
				ok = true;
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return ok;
	}

	@Override
	public String updateFile(String type, String fileName, byte[] content)
	{
		boolean dok = false;
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return null;
		}
		try
		{
			File f = new File(filePathVo.getAbsultePath(fileRoot, fileName));
			if(f.exists())
			{
				FileUtil.deleteFile(f.getAbsolutePath());
				dok = true;
			}
			if(dok)
			{
				(new File(fileRoot + File.separator + filePathVo.getPath())).mkdirs(); // 如果文件夹不存在 则建立新文件夹
				FileUtil.createFile(content, filePathVo.getAbsultePath(fileRoot, fileName));
				return fileName;
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	@Override
	public String updateFile(String type, String fileName, String content)
	{
		boolean dok = false;
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return null;
		}
		try
		{
			File f = new File(filePathVo.getAbsultePath(fileRoot, fileName));
			if(f.exists())
			{
				FileUtil.deleteFile(f.getAbsolutePath());
				dok = true;
			}
			if(dok)
			{
				(new File(fileRoot + File.separator + filePathVo.getPath())).mkdirs(); // 如果文件夹不存在 则建立新文件夹
				FileUtil.createFile(content, filePathVo.getAbsultePath(fileRoot, fileName));
				return fileName;
			}
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return null;
	}

	@Override
	public byte[] getFile(String type, String fileName)
	{
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return null;
		}
		try {
			File f = new File(filePathVo.getAbsultePath(fileRoot, fileName));
			if(!f.exists()){
				throw new LogicException("文件不存在");
			}
			return FileUtil.getBytes(f.getAbsolutePath());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public String getFileStr(String type, String fileName)
	{
		FileVo filePathVo = this.pathMap.get(type);
		if (filePathVo == null)
		{
			return null;
		}
		try {
			File f = new File(filePathVo.getAbsultePath(fileRoot, fileName));
			if(!f.exists()){
				throw new LogicException("文件不存在");
			}
			return FileUtil.getString(f.getAbsolutePath());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * copy照片到日志目录
	 * @param type
	 * @param fileName
	 * @param content
	 * @return
	 */
	@Override
	public String copyToLogFile(String type, String fileName, String newType, String newFileName){
		byte[] content = getFile(type, fileName);
		String distributePath = logDistributePath(newFileName);
		return saveFile(newType, distributePath, newFileName, content);
	}

	/**
	 *日志文件的路径日期部分
	 * @return
	 */
	private String logDistributePath(String fileName){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hashCode = Math.abs(fileName.hashCode());
		int hashNum = hashCode % 50;
		String distributePath = year + File.separator + month + File.separator + day + File.separator + hashNum;
		return distributePath;
	}

	@Override
	public String saveLogFile(String type, String fileName, byte[] content) {
		String distributePath = logDistributePath(fileName);
		return saveFile(type, distributePath, fileName, content);
	}

	/**
	 * 保存按照时间分目录的图片
	 * @param fileKey
	 * @param type
	 * @param count
	 * @param base64
	 * @return
	 */
	@Override
	public String saveDatePathFile(String fileKey, String type, int count, String base64){
		return saveDatePathFile(fileKey, type, count, FileBase64Util.base64ToByte(base64));
	}

	/**
	 *	保存按照时间分目录的图片
	 * @param fileKey
	 * @param type
	 * @param count
	 * @param bytes
	 * @return
	 */
	@Override
	public String saveDatePathFile(String fileKey, String type, int count, byte[] bytes){
		String retFilename = "";
		try
		{
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH)+1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int hashCode = Math.abs(fileKey.hashCode());
			int hashNum = hashCode % count;
			String hashPath = year + File.separator + month + File.separator + day + File.separator + hashNum;
			retFilename = saveFile(type, hashPath, fileKey, bytes);
		}
		catch (Exception e)
		{
			log.error(e);
		}
		return retFilename;
	}
}
