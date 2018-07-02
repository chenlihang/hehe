package cn.com.boomhope.common.file;

import java.util.Map;

import org.apache.log4j.Logger;

import cn.com.boomhope.common.vo.FileVo;

public abstract class BaseFileProcesser implements FileProcesser
{
	protected static Logger log = Logger.getLogger(BaseFileProcesser.class);
	protected Map<String,FileVo> pathMap;

	@SuppressWarnings("unchecked")
	public BaseFileProcesser(Map<String,Object> settingCxt)
	{
		pathMap = (Map<String, FileVo>) settingCxt.get("pathMap");
	}

	@Override
	public String copyFile(String type, String fileName, String newType, String newFileName)
	{
		byte[] content = getFile(type, fileName);
		return saveFile(newType, newFileName, content);
	}
}
