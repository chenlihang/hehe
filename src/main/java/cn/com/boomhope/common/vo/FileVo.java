package cn.com.boomhope.common.vo;

import java.io.File;

public class FileVo
{
	private String group;
	private String type;
	private String path;
	private String suffix;

	public String getGroup()
	{
		return group;
	}

	public void setGroup(String group)
	{
		this.group = group;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getSuffix()
	{
		return suffix;
	}

	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}

	public String getAbsultePath(String fileRoot, String fileName)
	{
		return fileRoot + File.separator + path + File.separator + fileName + "." + suffix;
	}
}
