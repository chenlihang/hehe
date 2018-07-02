package cn.com.boomhope.common.file;

import java.util.Map;

public class FileProcesserFactory
{
	public static FileProcesser getFileProcesser(Map<String,Object> settingCxt)
	{
		String fileMode = (String) settingCxt.get("fileMode");
		if("hd".equals(fileMode))
		{
			return new HdFileProcesser(settingCxt);
		}
		else if("mg".equals(fileMode))
		{
			return null;
		}
		else if("fs".equals(fileMode))
		{
			return new FsFileProcesser(settingCxt);
		}
		return null;
	}
}
