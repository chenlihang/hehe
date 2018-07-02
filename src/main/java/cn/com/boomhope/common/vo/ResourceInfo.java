package cn.com.boomhope.common.vo;

import java.util.ArrayList;
import java.util.List;

public class ResourceInfo extends AbstractVo
{
	private String resCode;
	private String resName;
	private String resUrl;
	private String resType;
	private String resBioType;
	private List<ResourceInfo> resourcesList = new ArrayList<ResourceInfo>();

	public ResourceInfo()
	{
	}

	public ResourceInfo(ResourceInfo resourceInfo)
	{
		this.resCode = resourceInfo.getResCode();
		this.resName = resourceInfo.getResName();
		this.resUrl = resourceInfo.getResUrl();
		this.resType = resourceInfo.getResType();
		this.resBioType = resourceInfo.getResBioType();
	}

	public void add(ResourceInfo resourceInfo)
	{
		if (resourceInfo != null)
		{
			this.resourcesList.add(resourceInfo);
		}
	}

	public String getResCode()
	{
		return resCode;
	}

	public void setResCode(String resCode)
	{
		this.resCode = resCode;
	}

	public String getResName()
	{
		return resName;
	}

	public void setResName(String resName)
	{
		this.resName = resName;
	}

	public String getResUrl()
	{
		return resUrl;
	}

	public void setResUrl(String resUrl)
	{
		this.resUrl = resUrl;
	}

	public String getResType()
	{
		return resType;
	}

	public void setResType(String resType)
	{
		this.resType = resType;
	}

	public List<ResourceInfo> getResourcesList()
	{
		return resourcesList;
	}

	public void setResourcesList(List<ResourceInfo> resourcesList)
	{
		this.resourcesList = resourcesList;
	}

	public String getResBioType()
	{
		return resBioType;
	}

	public void setResBioType(String resBioType)
	{
		this.resBioType = resBioType;
	}
}
